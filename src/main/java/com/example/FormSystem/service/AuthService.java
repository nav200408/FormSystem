package com.example.FormSystem.service;

import com.example.FormSystem.dto.request.LoginRequest;
import com.example.FormSystem.dto.request.RegisterRequest;
import com.example.FormSystem.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    void register(RegisterRequest request);
}
