package com.bank.cash_crunch_helper.service;

import com.bank.cash_crunch_helper.config.security.JwtService;
import com.bank.cash_crunch_helper.constant.Role;
import com.bank.cash_crunch_helper.dataaccess.UserRepository;
import com.bank.cash_crunch_helper.dbmodel.User;
import com.bank.cash_crunch_helper.dto.request.LoginRequestDTO;
import com.bank.cash_crunch_helper.dto.request.RegisterRequestDTO;
import com.bank.cash_crunch_helper.dto.response.AuthResponseDTO;
import com.bank.cash_crunch_helper.exception.BusinessException;
import com.bank.cash_crunch_helper.exception.ExceptionSeverity;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    static final String EMAIL_ALREADY_EXISTS_MESSAGE= "The email address you provided is already registered.";

    @Transactional
    public AuthResponseDTO register(RegisterRequestDTO request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isPresent())
            throw new BusinessException(ExceptionSeverity.ERROR, EMAIL_ALREADY_EXISTS_MESSAGE);
        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        User savedUser = userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        return AuthResponseDTO.builder()
                .accessToken(jwtToken)
                .build();
    }

    @Transactional
    public AuthResponseDTO authenticate(LoginRequestDTO request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        return AuthResponseDTO.builder()
                .accessToken(jwtToken)
                .build();
    }


    public void refreshToken(HttpServletRequest request,HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            User user = this.userRepository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                String accessToken = jwtService.generateToken(user);
                AuthResponseDTO authResponse = AuthResponseDTO.builder()
                        .accessToken(accessToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
