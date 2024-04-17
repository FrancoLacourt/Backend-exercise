package com.example.backend.Tests.UserTests;

import com.example.backend.dto.response.UserResponseDTO;
import com.example.backend.entity.Note;
import com.example.backend.entity.Tag;
import com.example.backend.entity.UserEntity;
import com.example.backend.exception.MyException;
import com.example.backend.mapper.UserMapper;
import com.example.backend.repository.NoteRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    NoteRepository noteRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserServiceImpl userService;

    private Long id_note1;
    private Long id_note2;
    private Long id_tag1;
    private Long id_tag2;
    private Long id_user;
    private Long id_user2;
    private Note note1;
    private Note note2;
    private Tag tag1;
    private Tag tag2;
    private List<Tag> tags;
    private List<Note> notes;
    private List<UserEntity> users;
    private List<UserResponseDTO> userResponseListDTO;
    private UserEntity user;
    private UserEntity user2;
    private UserResponseDTO userResponseDTO;
    private UserResponseDTO userResponseDTO2;
    private String userPassword = "franco123";
    private String user2password= "federico123";

    @BeforeEach
    void setUp() {

        id_note1 = 1L;
        id_note2 = 2L;

        id_tag1 = 1L;
        id_tag2 = 2L;

        id_user = 1L;
        id_user2 = 2L;

        note1 = new Note();
        note2 = new Note();

        tag1 = new Tag();
        tag2 = new Tag();

        tags = new ArrayList<>();
        notes = new ArrayList<>();
        users = new ArrayList<>();
        userResponseListDTO = new ArrayList<>();

        user = new UserEntity();
        user2 = new UserEntity();

        userResponseDTO = new UserResponseDTO();
        userResponseDTO2 = new UserResponseDTO();

        note1.setId_note(id_note1);
        note1.setTitle("Valid title");
        note1.setDescription("Valid description");
        notes.add(note1);

        note2.setId_note(id_note2);
        note2.setTitle("Valid title 2");
        note2.setDescription("Valid description 2");
        notes.add(note2);

        tag1.setId_tag(id_tag1);
        tag1.setTagName("Music");
        tag1.setNotes(notes);
        tags.add(tag1);

        tag2.setId_tag(id_tag2);
        tag2.setTagName("Entertainment");
        tag2.setNotes(notes);
        tags.add(tag2);

        note1.setTags(tags);
        note2.setTags(tags);


        user.setId(id_user);
        user.setName("Franco");
        user.setLastName("Lacourt");
        user.setNotes(notes);
        user.setPassword(passwordEncoder.encode(userPassword));

        userResponseDTO.setId(id_user);
        userResponseDTO.setName("Franco");
        userResponseDTO.setLastName("Lacourt");

        user2.setId(id_user2);
        user2.setName("Federico");
        user2.setLastName("Lacourt");
        user2.setNotes(notes);
        user2.setPassword(passwordEncoder.encode(user2password));

        userResponseDTO2.setId(id_user2);
        userResponseDTO2.setName("Federico");
        userResponseDTO2.setLastName("Lacourt");

        users.add(user);
        users.add(user2);

        userResponseListDTO.add(userResponseDTO);
        userResponseListDTO.add(userResponseDTO2);
    }

    @Test
    void testConstructor() {
        UserServiceImpl userService = new UserServiceImpl();
        assertNotNull(userService);
    }

    @Test
    void changeUserPasswordTest() throws MyException {

        String newPassword = "franco";

        when(userRepository.findById(id_user)).thenReturn(Optional.ofNullable(user));
        when(passwordEncoder.matches(newPassword, user.getPassword())).thenReturn(false);
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.userToUserResponseDTO(any(UserEntity.class))).thenReturn(new UserResponseDTO());

        UserResponseDTO resultResponseDTO = userService.changeUserPassword(id_user, newPassword);

        assertEquals(passwordEncoder.encode(newPassword), user.getPassword());

        verify(userRepository).findById(id_user);
        verify(userRepository).save(user);
        verify(userMapper).userToUserResponseDTO(any(UserEntity.class));
    }

    @Test
    void changeUserPassword_WhenUserDoesNotExist() throws MyException {

        String newPassword = "franco";

        when(userRepository.findById(id_user)).thenReturn(Optional.empty());

        UserResponseDTO resultResponseDTO = userService.changeUserPassword(id_user, newPassword);

        assertNull(resultResponseDTO);
        verify(userRepository).findById(id_user);
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(userMapper);
    }

    @Test
    void changeUserPassword_WhenPasswordIsTheSameAsTheOldOne() throws MyException {

        String newPassword = "franco123";

        when(userRepository.findById(id_user)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(newPassword, user.getPassword())).thenReturn(true);

        UserResponseDTO resultResponseDTO = userService.changeUserPassword(id_user, newPassword);

        assertNull(resultResponseDTO);
        verify(userRepository).findById(id_user);
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(userMapper);
    }

    @Test
    void getAllUsersTest() {

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toUserResponseListDTO(users)).thenReturn(userResponseListDTO);

        List<UserResponseDTO> collectedUsersResponseDTO = userService.getAllUsers();

        assertEquals(userResponseListDTO, collectedUsersResponseDTO);
    }

    @Test
    void findUserByIdTest() {

        when(userRepository.findById(id_user)).thenReturn(Optional.ofNullable(user));
        when(userMapper.userToUserResponseDTO(user)).thenReturn(userResponseDTO);

        UserResponseDTO collectedUserResponseDTO = userService.findUserById(id_user);

        assertEquals(id_user, collectedUserResponseDTO.getId());
    }

    @Test
    void findUserByIdTest_WhenUserDoesNotExist() {

        when(userRepository.findById(id_user)).thenReturn(Optional.empty());

        UserResponseDTO collectedUserResponseDTO = userService.findUserById(id_user);

        assertNull(collectedUserResponseDTO);
        verify(userRepository).findById(id_user);
        verifyNoInteractions(userMapper);
    }

    @Test
    void deleteUserTest() {

        when(userRepository.findById(id_user)).thenReturn(Optional.ofNullable(user));
        when(userMapper.userToUserResponseDTO(user)).thenReturn(new UserResponseDTO());

        UserResponseDTO deletedUserResponseDTO = userService.deleteUser(id_user);

        verify(noteRepository).deleteAll(anyList());
        verify(userRepository).delete(user);

        assertFalse(user.getNotes().contains(note1));
        assertFalse(note1.getTags().contains(tag1));
        assertEquals(user.getNotes().size(), 0);
    }

    @Test
    void deleteUserTest_WhenUserDoesNotExist() {

        when(userRepository.findById(id_user)).thenReturn(Optional.empty());

        UserResponseDTO deletedUserDTO = userService.deleteUser(id_user);

        assertNull(deletedUserDTO);
        verify(userRepository).findById(id_user);
        verifyNoInteractions(noteRepository);
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(userMapper);
    }

    @Test
    void validatePassword_NullPassword() {

        String password = "";

        MyException exception = assertThrows(MyException.class, () -> userService.validatePassword(password));
        assertEquals("Password can't be null or empty.", exception.getMessage());
    }

    @Test
    void validatePassword_OnlySpacesPassword() {

        String password = "          ";

        MyException exception = assertThrows(MyException.class, () -> userService.validatePassword(password));
        assertEquals("Password can't be null or empty.", exception.getMessage());
    }
}