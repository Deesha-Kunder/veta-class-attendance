package com.shadee.Veeta.service;

import com.shadee.Veeta.dto.*;
import com.shadee.Veeta.modelclass.Role;
import com.shadee.Veeta.modelclass.Student;
import com.shadee.Veeta.modelclass.StudentCourse;
import com.shadee.Veeta.modelclass.Users;
import com.shadee.Veeta.repository.StudentCourseRepository;
import com.shadee.Veeta.repository.StudentRegisterRepository;
import com.shadee.Veeta.repository.UserRepository;
import com.shadee.Veeta.security.CustomUserDetails;
import com.shadee.Veeta.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    CustomUserDetailsService userDetailsService;

    @Autowired
    StudentRegisterRepository studentRegisterRepository;

    @Autowired
    private StudentCourseRepository studentCourseRepository;

    public ResponseEntity<?> login(LoginRequest loginRequest) {
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String accessToken = jwtUtils.generateJwtToken(authentication);
            String refreshToken = jwtUtils.generateRefreshToken(authentication);

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            UserInfo userInfo = new UserInfo(
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    Role.valueOf(userDetails.getAuthorities().iterator().next().getAuthority())
            );
            System.out.println(userInfo);
            return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken, userInfo));
        }catch (BadCredentialsException e){
            boolean emailExists = userRepository.existsByEmail(loginRequest.getEmail());
            if(!emailExists){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Email Not Found"));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Incorrect Password"));
        }

    }

    public ResponseEntity<SignUpResponse> signUp(SignUpRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new SignUpResponse("email already exist",null));
        }
        Users user = new Users();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        user.setRole(signUpRequest.getRole());

        Student studentData = null;

        if("STUDENT".equalsIgnoreCase(signUpRequest.getRole().name())){
            Optional<Student> student = studentRegisterRepository.findByEmail(signUpRequest.getEmail());

            if(student.isEmpty()){
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new SignUpResponse("Student email is not found in Student records",null));
            }
            studentData = student.get();
            user.setStudent(studentData);
        }
        Users savedUser = userRepository.save(user);

        if(studentData != null){
            StudentCourse studentCourse = new StudentCourse();
            studentCourse.setStudentId(savedUser.getUserId());
            studentCourse.setCourseId(1);
            studentCourse.setEmailId(savedUser.getEmail());
            studentCourse.setRequiredMinutes(studentData.getCourseHour() * 60L);
            studentCourse.setTotalCompletedMinutes(0L);

            studentCourseRepository.save(studentCourse);
        }

        UserInfo userInfo = new UserInfo(
                savedUser.getUserId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
        System.out.println(userInfo);
        return ResponseEntity.ok(new SignUpResponse("Registered Successfully",userInfo));

    }
    public RefreshTokenResponse refreshToken(RefreshTokenRequest refreshTokenResponse){
        String refreshToken = refreshTokenResponse.getRefreshToken();
        String email = jwtUtils.getEmailFromToken(refreshToken);

        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(email);
        if(jwtUtils.validateJwtToken(refreshToken,userDetails)){
            String newAccessToken = jwtUtils.generateJwtToken(
                    new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities())
            );
            return new RefreshTokenResponse(newAccessToken,refreshToken);
        }
        else {
            throw new RuntimeException("Invalid refresh token");
        }

    }
}
