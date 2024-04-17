package com.example.backend.service;

import com.example.backend.dto.request.AuthLoginRequestDTO;
import com.example.backend.dto.request.AuthRegisterRequestDTO;
import com.example.backend.dto.response.AuthResponseDTO;
import com.example.backend.exception.MyException;

public interface AuthService {

    AuthResponseDTO register(AuthRegisterRequestDTO request) throws MyException;

    AuthResponseDTO login(AuthLoginRequestDTO request);
}