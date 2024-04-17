package com.example.backend.service.impl;

import com.example.backend.dto.request.AuthLoginRequestDTO;
import com.example.backend.dto.request.AuthRegisterRequestDTO;
import com.example.backend.dto.response.AuthResponseDTO;
import com.example.backend.entity.UserEntity;
import com.example.backend.exception.InvalidPasswordException;
import com.example.backend.exception.MyException;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.AuthService;
import com.example.backend.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public AuthResponseDTO register(AuthRegisterRequestDTO request) throws MyException{

        validatePassword(request);

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("There is already an account registered with this email.");
        }

        var user = UserEntity.builder()
                .email(request.getEmail())
                .displayName(request.getDisplayName())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .lastName(request.getLastName())
                .isActive(true)
                .registrationDate(LocalDate.now())
                .build();

        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);

        Long id = user.getId();
        String email = user.getEmail();
        String lastName = user.getLastName();
        String name = user.getName();
        String displayName = user.getDisplayName();
        boolean isActive = user.isActive();
        LocalDate registrationDate = user.getRegistrationDate();

        return AuthResponseDTO.builder()
                .id(id)
                .lastName(lastName)
                .displayName(displayName)
                .name(name)
                .token(jwtToken)
                .email(email)
                .isActive(isActive)
                .registrationDate(registrationDate)
                .build();
    }

    @Override
    public AuthResponseDTO login(AuthLoginRequestDTO request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findUserByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);

        Long id = user.getId();
        String name = user.getName();
        String lastName = user.getLastName();
        String email = user.getEmail();
        String displayName = user.getDisplayName();
        boolean isActive = user.isActive();
        LocalDate registrationDate = user.getRegistrationDate();

        return AuthResponseDTO.builder()
                .id(id)
                .token(jwtToken)
                .displayName(displayName)
                .name(name)
                .lastName(lastName)
                .email(email)
                .isActive(isActive)
                .registrationDate(registrationDate)
                .build();
    }

    public void validatePassword(AuthRegisterRequestDTO request) throws InvalidPasswordException {

        if (!StringUtils.hasText(request.getPassword()) || !StringUtils.hasText(request.getRepeatedPassword())) {
            throw new InvalidPasswordException("One or both passwords are empty.");
        }

        if (!request.getPassword().equals(request.getRepeatedPassword())) {
            throw new InvalidPasswordException("Passwords don't match.");
        }
    }
}