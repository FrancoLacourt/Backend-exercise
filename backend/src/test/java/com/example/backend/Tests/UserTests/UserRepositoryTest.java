package com.example.backend.Tests.UserTests;

import com.example.backend.entity.UserEntity;
import com.example.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

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
    void saveUserTest() {

        userRepository.save(user);

        Optional<UserEntity> savedUser = userRepository.findById(user.getId());

        assertTrue(userRepository.existsById(user.getId()));
        assertEquals("Franco", savedUser.get().getName());
    }

    @Test
    void deleteUserTest() {

        userRepository.save(user);

        assertTrue(userRepository.existsById(user.getId()));

        userRepository.delete(user);

        assertFalse(userRepository.existsById(user.getId()));
        assertEquals(userRepository.findById(user.getId()), Optional.empty());
    }

    @Test
    void findUserByIdTest() {

        userRepository.save(user);

        Optional<UserEntity> foundUser = userRepository.findById(user.getId());

        assertTrue(foundUser.isPresent());
        assertEquals("Lacourt", foundUser.get().getLastName());
    }

    @Test
    void findAllUsersTest() {

        userRepository.save(user);
        userRepository.save(user2);

        List<UserEntity> foundUsers = userRepository.findAll();

        assertEquals(users, foundUsers);
    }

    @Test
    void findUserByEmailTest() {

        userRepository.save(user);

        Optional<UserEntity> foundUser = userRepository.findUserByEmail("francolaco99@gmail.com");

        assertTrue(foundUser.isPresent());
        assertEquals(foundUser.get().getDisplayName(), user.getDisplayName());
    }

    @Test
    void existsByEmailTest() {

        userRepository.save(user);

        assertTrue(userRepository.existsByEmail(user.getEmail()));
    }
}