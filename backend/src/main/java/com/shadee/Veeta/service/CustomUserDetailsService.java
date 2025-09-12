package com.shadee.Veeta.service;

import com.shadee.Veeta.modelclass.Users;
import com.shadee.Veeta.repository.UserRepository;
import com.shadee.Veeta.security.CustomUserDetails;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = repository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User Not Found with Email"+email));
        return CustomUserDetails.build(user);
    }
}
