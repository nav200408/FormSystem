package com.example.FormSystem.controller;

import com.example.FormSystem.constant.MessageConstant;
import com.example.FormSystem.dto.request.LoginRequest;
import com.example.FormSystem.dto.request.RegisterRequest;
import com.example.FormSystem.dto.response.AuthResponse;
import com.example.FormSystem.dto.response.ResponseData;
import com.example.FormSystem.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseData<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);

        ResponseData<AuthResponse> res = new ResponseData<>(
                MessageConstant.LOGIN_SUCCESS,
                response,
                HttpStatus.OK.value(),
                true);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseData<Void>> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);

        ResponseData<Void> res = new ResponseData<>(
                MessageConstant.REGISTER_SUCCESS,
                null,
                HttpStatus.CREATED.value(),
                true);

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }
}
