package com.example.eCommerce.service;

import com.example.eCommerce.model.User;
import com.example.eCommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User registerUser(String email,String password){
        String encodedPassword=passwordEncoder.encode(password);
        User user = new User(null,email,encodedPassword);
        return userRepository.save(user);
    }

    public User loginUser(String email, String password){
        return userRepository.findByEmail(email)
                .filter(user -> passwordEncoder.matches(password,user.getPassword()))
                .orElse(null);
    }
}
