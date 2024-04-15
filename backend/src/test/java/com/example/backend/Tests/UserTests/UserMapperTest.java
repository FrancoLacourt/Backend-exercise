package com.example.backend.Tests.UserTests;

import com.example.backend.dto.response.UserResponseDTO;
import com.example.backend.entity.UserEntity;
import com.example.backend.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UserMapperTest {

    @Autowired
    UserMapper userMapper;

    private UserEntity user;
    private UserEntity user2;
    private List<UserEntity> users;

    @BeforeEach
    void setUp() {

        user = new UserEntity();
        user2 = new UserEntity();
        users = new ArrayList<>();

        user.setId(1L);
        user.setName("Franco");
        user.setLastName("Lacourt");
        user.setEmail("francolaco99@gmail.com");
        user.setDisplayName("francolaco99");
        user.setRegistrationDate(LocalDate.now());

        user2.setId(2L);
        user2.setName("Federico");
        user2.setLastName("Lacourt");
        user2.setEmail("fedelaco2000@gmail.com");
        user2.setDisplayName("fedelaco2000");
        user2.setRegistrationDate(LocalDate.now());

        users.add(user);
        users.add(user2);
    }

    @Test
    void userToUserResponseDTO() {

        UserResponseDTO newUserResponseDTO = userMapper.userToUserResponseDTO(user);

        assertEquals(newUserResponseDTO.getId(), user.getId());
        assertEquals(newUserResponseDTO.getName(), user.getName());
        assertEquals(newUserResponseDTO.getLastName(), user.getLastName());
        assertEquals(newUserResponseDTO.getEmail(), user.getEmail());
        assertEquals(newUserResponseDTO.getDisplayName(), user.getDisplayName());
        assertEquals(newUserResponseDTO.getRegistrationDate(), user.getRegistrationDate());
    }

    @Test
    void toUserResponseListDTO() {

        List<UserResponseDTO> newUserResponseListDTO = userMapper.toUserResponseListDTO(users);

        assertEquals(newUserResponseListDTO.size(), users.size());
        assertEquals(newUserResponseListDTO.get(0).getId(), users.get(0).getId());
        assertEquals(newUserResponseListDTO.get(1).getId(), users.get(1).getId());
        assertEquals(2, newUserResponseListDTO.size());
    }

}
