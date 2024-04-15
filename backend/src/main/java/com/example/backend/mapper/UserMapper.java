package com.example.backend.mapper;

import com.example.backend.dto.response.AuthResponseDTO;
import com.example.backend.dto.response.TagResponseDTO;
import com.example.backend.dto.response.UserResponseDTO;
import com.example.backend.entity.Tag;
import com.example.backend.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserResponseDTO userToUserResponseDTO(UserEntity user);
    List<UserResponseDTO> toUserResponseListDTO(List <UserEntity> users);
}
