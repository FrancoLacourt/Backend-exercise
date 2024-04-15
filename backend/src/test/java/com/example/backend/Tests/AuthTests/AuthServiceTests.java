package com.example.backend.Tests.AuthTests;

import com.example.backend.dto.request.AuthLoginRequestDTO;
import com.example.backend.dto.request.AuthRegisterRequestDTO;
import com.example.backend.dto.response.AuthResponseDTO;
import com.example.backend.entity.UserEntity;
import com.example.backend.exception.InvalidPasswordException;
import com.example.backend.exception.MyException;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.JwtService;
import com.example.backend.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;


    @Test
    void registerTest() throws MyException {

        AuthRegisterRequestDTO requestDTO = new AuthRegisterRequestDTO();
        requestDTO.setEmail("francolaco99@gmail.com");
        requestDTO.setDisplayName("francolaco99");
        requestDTO.setPassword("franco123");
        requestDTO.setRepeatedPassword("franco123");
        requestDTO.setName("Franco");
        requestDTO.setLastName("Lacourt");

        when(userRepository.existsByEmail(requestDTO.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(requestDTO.getPassword())).thenReturn("encodedPassword");

        String mockToken = "mockToken";
        when(jwtService.generateToken(any())).thenReturn(mockToken);

        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        when(userRepository.save(userCaptor.capture())).thenAnswer(invocation -> {
            UserEntity savedUser = invocation.getArgument(0);
            savedUser.setId(1L);
            return savedUser;
        });

        AuthResponseDTO responseDTO = authService.register(requestDTO);

        assertNotNull(responseDTO);
        assertEquals(requestDTO.getEmail(), responseDTO.getEmail());
        assertEquals(requestDTO.getDisplayName(), responseDTO.getDisplayName());
        assertEquals(requestDTO.getName(), responseDTO.getName());
        assertEquals(requestDTO.getLastName(), responseDTO.getLastName());
        assertTrue(responseDTO.isActive());
        assertNotNull(responseDTO.getToken());
        assertNotNull(responseDTO.getRegistrationDate());
    }

    @Test
    void loginTest() {

        AuthLoginRequestDTO requestDTO = new AuthLoginRequestDTO();
        requestDTO.setEmail("francolaco99@gmail.com");
        requestDTO.setPassword("franco123");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(requestDTO.getEmail(), requestDTO.getPassword()));

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setName("Franco");
        user.setLastName("Lacourt");
        user.setEmail("francolaco99@gmail.com");
        user.setDisplayName("francolaco99");
        user.setActive(true);
        user.setRegistrationDate(LocalDate.now());

        when(userRepository.findUserByEmail(requestDTO.getEmail())).thenReturn(Optional.of(user));

        String mockToken = "mockToken";
        when(jwtService.generateToken(user)).thenReturn(mockToken);

        AuthResponseDTO responseDTO = authService.login(requestDTO);

        assertNotNull(responseDTO);
        assertEquals(user.getEmail(), responseDTO.getEmail());
        assertEquals(user.getDisplayName(), responseDTO.getDisplayName());
        assertEquals(user.getName(), responseDTO.getName());
        assertEquals(user.getLastName(), responseDTO.getLastName());
        assertEquals(user.getRegistrationDate(), responseDTO.getRegistrationDate());
        assertTrue(responseDTO.isActive());
        assertNotNull(responseDTO.getToken());
        assertNotNull(responseDTO.getRegistrationDate());

        verify(userRepository).findUserByEmail(requestDTO.getEmail());
    }

    @Test
    void validatePasswordTest() throws InvalidPasswordException{

        AuthRegisterRequestDTO requestDTO = new AuthRegisterRequestDTO();

        requestDTO.setPassword("franco123");
        requestDTO.setRepeatedPassword("franco123");

        authService.validatePassword(requestDTO);

        assertDoesNotThrow(() -> authService.validatePassword(requestDTO));
    }

    @Test
    void validatePasswordTest_WhenPasswordsDontMatch() throws InvalidPasswordException{

        AuthRegisterRequestDTO requestDTO = new AuthRegisterRequestDTO();

        requestDTO.setPassword("franco123");
        requestDTO.setRepeatedPassword("franco");

        assertThrows(InvalidPasswordException.class, () -> authService.validatePassword(requestDTO));
    }

    @Test
    void validatePasswordTest_WhenPasswordsAreEmpty() throws InvalidPasswordException {

        AuthRegisterRequestDTO requestDTO = new AuthRegisterRequestDTO();

        requestDTO.setPassword("     ");
        requestDTO.setRepeatedPassword("franco");

        assertThrows(InvalidPasswordException.class, () -> authService.validatePassword(requestDTO));
    }

    @Test
    void validatePasswordsTest_WhenPasswordsAreNull() throws InvalidPasswordException {

        AuthRegisterRequestDTO requestDTO = new AuthRegisterRequestDTO();

        requestDTO.setPassword(null);
        requestDTO.setRepeatedPassword("franco");

        assertThrows(InvalidPasswordException.class, () -> authService.validatePassword(requestDTO));

    }



}
