package com.tcognition.userservice.serviceImpl;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tcognition.userservice.entity.UserEntity;
import com.tcognition.userservice.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userContact ) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmailOrContactIgnoreCase(userContact)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with : " + userContact));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.emptyList()
        );
    }
}
