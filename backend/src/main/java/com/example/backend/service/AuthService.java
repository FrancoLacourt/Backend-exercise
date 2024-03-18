package com.example.backend.service;

import com.example.backend.dto.request.AuthLoginRequestDTO;
import com.example.backend.dto.request.AuthRegisterRequestDTO;
import com.example.backend.dto.response.AuthResponseDTO;

public interface AuthService {

    AuthResponseDTO register(AuthRegisterRequestDTO request);

    AuthResponseDTO login(AuthLoginRequestDTO request);
}
