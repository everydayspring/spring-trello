package com.sparta.springtrello.domain.auth.controller;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.springtrello.domain.auth.dto.request.SigninRequest;
import com.sparta.springtrello.domain.auth.dto.request.SignupRequest;
import com.sparta.springtrello.domain.auth.dto.response.SigninResponse;
import com.sparta.springtrello.domain.auth.dto.response.SignupResponse;
import com.sparta.springtrello.domain.auth.service.AuthService;
import com.sparta.springtrello.domain.auth.service.TestService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final TestService testService;

    @PostMapping("/auth/signup")
    public SignupResponse signup(@Valid @RequestBody SignupRequest signupRequest) {
        return authService.signup(signupRequest);
    }

    @PostMapping("/auth/signin")
    public SigninResponse signin(@Valid @RequestBody SigninRequest signinRequest) {
        return authService.signin(signinRequest);
    }

    @GetMapping("/auth/signin")
    public void test() {
        testService.test();
    }
}
