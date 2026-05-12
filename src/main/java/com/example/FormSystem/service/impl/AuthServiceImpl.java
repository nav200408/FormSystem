package com.example.FormSystem.service.impl;

import com.example.FormSystem.constant.MessageConstant;
import com.example.FormSystem.dto.request.LoginRequest;
import com.example.FormSystem.dto.request.RegisterRequest;
import com.example.FormSystem.dto.response.AuthResponse;
import com.example.FormSystem.entity.User;
import com.example.FormSystem.repository.UserRepository;
import com.example.FormSystem.service.AuthService;
import com.example.FormSystem.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    public AuthServiceImpl(AuthenticationManager authenticationManager, 
                           UserRepository userRepository, 
                           PasswordEncoder encoder, 
                           JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken((User) authentication.getPrincipal());

        User userDetails = (User) authentication.getPrincipal();
        return new AuthResponse(jwt, userDetails.getUsername(), userDetails.getRole().name());
    }

    @Override
    public void register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException(MessageConstant.USERNAME_ALREADY_TAKEN);
        }

        User user = new User(request.getUsername(), 
                             encoder.encode(request.getPassword()), 
                             request.getRole());

        userRepository.save(user);
    }
}
