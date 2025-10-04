package com.ecommerce.security.service;

import com.ecommerce.payload.AuthenticationResult;
import com.ecommerce.security.request.LoginRequest;
import com.ecommerce.security.request.SignupRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface AuthService {
    ResponseEntity<?> register(@Valid SignupRequest signUpRequest);
    AuthenticationResult login(LoginRequest loginRequest);
    Object getCurrentUserDetails(Authentication authentication);
    ResponseCookie logoutUser();
    Object getAllSellers(Pageable pageDetails);
}
