package com.example.backend.service;

import com.example.backend.dto.response.AuthResponseDTO;
import com.example.backend.dto.response.UserResponseDTO;
import com.example.backend.exception.MyException;

import java.util.List;

public interface UserService {

    UserResponseDTO changeUserPassword(Long id_user, String password) throws MyException;
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO findUserById(Long id_user);
    UserResponseDTO deleteUser(Long id_user);
}
