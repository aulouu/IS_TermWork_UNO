package com.is.uno.service;

import com.is.uno.dao.*;
import com.is.uno.dto.*;
import com.is.uno.exception.*;
import com.is.uno.model.*;
import com.is.uno.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDTO register(RegisterUserDTO registerUserDto) {
        if (userRepository.existsByUsername(registerUserDto.getUsername()))
            throw new UserAlreadyExistException(
                    String.format("Username %s already exists", registerUserDto.getUsername())
            );

        User user = User
                .builder()
                .username(registerUserDto.getUsername())
                .password(passwordEncoder.encode(registerUserDto.getPassword()))
                .registrationDate(LocalDateTime.now())
                .build();

        user = userRepository.save(user);

        String token = jwtUtils.generateJwtToken(user.getUsername());
        return new AuthResponseDTO(
                user.getUsername(),
                user.getRegistrationDate(),
                token
        );
    }

    public AuthResponseDTO login(LoginUserDTO loginUserDto) {
        User user = userRepository.findByUsername(loginUserDto.getUsername())
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("Username %s not found", loginUserDto.getUsername())
                ));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUserDto.getUsername(), loginUserDto.getPassword())
        );

        String token = jwtUtils.generateJwtToken(user.getUsername());
        return new AuthResponseDTO(
                user.getUsername(),
                user.getRegistrationDate(),
                token
        );
    }
}
