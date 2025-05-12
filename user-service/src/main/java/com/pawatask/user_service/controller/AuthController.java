package com.pawatask.user_service.controller;

import com.pawatask.user_service.dto.AuthResponse;
import com.pawatask.user_service.dto.LoginRequest;
import com.pawatask.user_service.dto.RegisterRequest;
import com.pawatask.user_service.dto.UserDto;
import com.pawatask.user_service.mapper.UserMapper;
import com.pawatask.user_service.repository.UserRepository;
import com.pawatask.user_service.security.JWTUtil;
import com.pawatask.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;
    private final UserService userService;

    @PostMapping("/register")
    public UserDto register(@RequestBody RegisterRequest request) {
        return UserMapper.toDto(userService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return userRepository.findByUsername(request.getUsername())
                .filter(user -> passwordEncoder.matches(request.getPassword(), user.getPassword()))
                .map(user -> {
                    String token = jwtUtil.generateToken(user.getUsername());
                    return ResponseEntity.ok(new AuthResponse(token));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}
