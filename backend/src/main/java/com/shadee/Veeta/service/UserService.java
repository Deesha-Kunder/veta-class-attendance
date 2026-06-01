package com.shadee.Veeta.service;

import com.shadee.Veeta.dto.UserInfo;
import com.shadee.Veeta.modelclass.Student;
import com.shadee.Veeta.modelclass.Users;
import com.shadee.Veeta.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserInfo getAdminDataById(String id){
        Users user = userRepository.findById(id).orElseThrow(()->new RuntimeException("User not found"));

        return new UserInfo(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }
    public Student getStudentFromId(String userId){
        Users users = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("user not found"));

        if(users.getStudent() == null){
            throw new RuntimeException("This user is not a student");
        }
        return users.getStudent();
    }
}
