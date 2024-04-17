package com.example.backend.service.impl;

import com.example.backend.dto.response.UserResponseDTO;
import com.example.backend.entity.Note;
import com.example.backend.entity.Tag;
import com.example.backend.entity.UserEntity;
import com.example.backend.exception.MyException;
import com.example.backend.mapper.UserMapper;
import com.example.backend.repository.NoteRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private UserMapper userMapper;
    private NoteRepository noteRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, NoteRepository noteRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.noteRepository = noteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserServiceImpl() {
    }

    @Override
    public UserResponseDTO changeUserPassword(Long id_user, String password) throws MyException {

        validatePassword(password);

        UserEntity user = userRepository.findById(id_user).orElse(null);

        if (user != null) {
            if (passwordEncoder.matches(password, user.getPassword())) {
                System.out.println("New password must be different from the old one.");
                return null;
            }

            String newPasswordEncoded = passwordEncoder.encode(password);
            user.setPassword(newPasswordEncoded); // Asigna la nueva contraseña encriptada

            UserEntity savedUser = userRepository.save(user);

            return userMapper.userToUserResponseDTO(savedUser);
        } else {
            return null;
        }
    }
    @Override
    public List<UserResponseDTO> getAllUsers() {

        List<UserEntity> users = userRepository.findAll();

        return userMapper.toUserResponseListDTO(users);
    }

    @Override
    public UserResponseDTO findUserById(Long id_user) {

        UserEntity user = userRepository.findById(id_user).orElse(null);

        if (user != null) {
            return userMapper.userToUserResponseDTO(user);
        } else {
            System.out.println("It wasn't possible to find a user with the ID: " + id_user);
            return null;
        }

    }

    @Override
    public UserResponseDTO deleteUser(Long id_user) {
        UserEntity user = userRepository.findById(id_user).orElse(null);
        List<Note> userNotes = new ArrayList<>();

        if (user != null) {
            for (Note note : user.getNotes()) {
                for (Tag tag : note.getTags()) {
                    tag.getNotes().remove(note);
                }
                note.getTags().clear();
                userNotes.add(note);
            }

            user.getNotes().clear();
            noteRepository.deleteAll(userNotes);

            userRepository.delete(user);
            return userMapper.userToUserResponseDTO(user);
        } else {
            System.out.println("The user does not exist");
            return null;
        }
    }

    public void validatePassword(CharSequence password) throws MyException {
        if (password == null || password.toString().trim().isEmpty()) {
            throw new MyException("Password can't be null or empty.");
        }
    }
}