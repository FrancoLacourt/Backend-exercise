package com.example.backend.service.impl;

import com.example.backend.dto.request.NoteRequestDTO;
import com.example.backend.dto.request.UpdatedNoteRequestDTO;
import com.example.backend.dto.response.NoteResponseDTO;
import com.example.backend.dto.response.TagResponseDTO;
import com.example.backend.entity.Note;
import com.example.backend.entity.Tag;
import com.example.backend.entity.UserEntity;
import com.example.backend.exception.ExceptionMethods;
import com.example.backend.exception.MyException;
import com.example.backend.mapper.NoteMapper;
import com.example.backend.mapper.TagMapper;
import com.example.backend.repository.NoteRepository;
import com.example.backend.repository.TagRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.NoteService;
import com.example.backend.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteServiceImpl implements NoteService {

    private NoteRepository noteRepository;
    private TagRepository tagRepository;
    private NoteMapper noteMapper;
    private TagService tagService;
    private TagMapper tagMapper;
    private UserRepository userRepository;

    @Autowired
    public NoteServiceImpl(NoteRepository noteRepository, NoteMapper noteMapper, TagService tagService, TagMapper tagMapper, TagRepository tagRepository, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.noteMapper = noteMapper;
        this.tagService = tagService;
        this.tagMapper = tagMapper;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
    }

    public NoteServiceImpl() {

    }


    //Validates input, associates tags, create note and save all to the repository.
    @Override
    @Transactional
    public NoteResponseDTO createNote(NoteRequestDTO noteRequestDTO, Long id_user) throws MyException {
        validate(noteRequestDTO);

        List<TagResponseDTO> tagResponseListDTO = tagService.getOrCreateTags(noteRequestDTO.getTagNames());
        List<Tag> tags = tagMapper.tagResponseListDTOToTagList(tagResponseListDTO);

        NoteResponseDTO noteResponseDTO = noteMapper.noteRequestDTOToNoteResponseDTO(noteRequestDTO);

        noteResponseDTO.setEnabled(true);
        noteResponseDTO.setTags(tags);

        Note note = noteMapper.noteResponseDTOToNote(noteResponseDTO);

        Note createdNote = noteRepository.save(note);

        for (Tag tag : tags) {
            tag.getNotes().add(createdNote);
        }

        tagRepository.saveAll(tags);

        UserEntity user = userRepository.findById(id_user).orElse(null);

        if (user != null) {

            if (user.getNotes() == null) {
                // Si es null, inicializar la lista
                user.setNotes(new ArrayList<>());
            }

            user.getNotes().add(createdNote);
        } else {
            System.out.println("It wasn't possible to find an user with the ID: " + id_user);
            return null;
        }
        return noteMapper.noteToNoteResponseDTO(createdNote);
    }


    @Override
    public List<NoteResponseDTO> getAllNotes() {

        List<Note> notes = noteRepository.findAll();

        return noteMapper.toNoteResponseDTOList(notes);
    }


    // Retrieve the notes that are enabled.
    @Override
    public List<NoteResponseDTO> getEnabledNotes() {
        List<NoteResponseDTO> enabledNoteResponseDTOList = getAllNotes();

        return enabledNoteResponseDTOList.stream().filter(NoteResponseDTO::isEnabled)
                .collect(Collectors.toList());
    }

    // Retrieve the notes that are disabled.
    @Override
    public List<NoteResponseDTO> getDisabledNotes() {
        List<NoteResponseDTO> disabledNoteResponseDTOList = getAllNotes();

        return disabledNoteResponseDTOList.stream()
                .filter(task -> !task.isEnabled())
                .collect(Collectors.toList());
    }

    @Override
    public List<NoteResponseDTO> getAllNotesByUser(Long id_user) {

        UserEntity user = userRepository.findById(id_user).orElse(null);

        if (user != null) {
            List<Note> noteListByUser = user.getNotes();
            return noteMapper.toNoteResponseDTOList(noteListByUser);
        } else {
            System.out.println("It wasn't possible to find a user with the ID: " + id_user);
            return null;
        }
    }

    @Override
    public List<TagResponseDTO> getAllTagsByUserNotes(Long id_user) {
        List<TagResponseDTO> tags = new ArrayList<>();
        UserEntity user = userRepository.findById(id_user).orElse(null);

        if (user != null) {
            List<Note> noteListByUser = user.getNotes();
            for (Note note : noteListByUser) {
                for (Tag tag : note.getTags()) {

                    if (tags.isEmpty()) {
                        tags.add(tagMapper.tagToTagResponseDTO(tag));
                    } else {
                        if (!tags.contains(tagMapper.tagToTagResponseDTO(tag))) {
                            tags.add(tagMapper.tagToTagResponseDTO(tag));
                        }
                    }
                }
            }
        } else {
            System.out.println("It wasn't possible to find a user with the ID: " + id_user);
            return null;
        }
        return tags;
    }

    @Override
    public List<NoteResponseDTO> getAllEnabledNotesByUser(Long id_user) {

        UserEntity user = userRepository.findById(id_user).orElse(null);

        if (user != null) {

            List<Note> noteListByUser = user.getNotes();
            List<NoteResponseDTO> noteResponseDTOListByUser = new ArrayList<>();
            noteListByUser.forEach(note -> {
                if (note.isEnabled()) {
                    noteResponseDTOListByUser.add(noteMapper.noteToNoteResponseDTO(note));
                }
            });
            return noteResponseDTOListByUser;
        } else {
            System.out.println("It wasn't possible to find a user with the ID: " + id_user);
            return null;
        }
    }

    @Override
    public List<NoteResponseDTO> getAllDisabledNotesByUser(Long id_user) {

        UserEntity user = userRepository.findById(id_user).orElse(null);

        if (user != null) {

            List<Note> noteListByUser = user.getNotes();
            List<NoteResponseDTO> noteResponseDTOListByUser = new ArrayList<>();
            noteListByUser.forEach(note -> {
                if (!note.isEnabled()) {
                    noteResponseDTOListByUser.add(noteMapper.noteToNoteResponseDTO(note));
                }
            });
            return noteResponseDTOListByUser;
        } else {
            System.out.println("It wasn't possible to find a user with the ID: " + id_user);
            return null;
        }
    }

    @Override
    public NoteResponseDTO findNoteById(Long id_note) {

        Note note = noteRepository.findById(id_note).orElse(null);

        if (note != null) {
            return noteMapper.noteToNoteResponseDTO(note);
        } else {
            System.out.println("It wasn't possible to find a note with the ID: " + id_note);
            return null;
        }
    }


    //Updates title and description of a note.
    @Override
    public NoteResponseDTO updateNote(Long id_note, UpdatedNoteRequestDTO updatedNoteRequestDTO) throws MyException {

        NoteRequestDTO validateRequest = new NoteRequestDTO();

        validateRequest.setTitle(updatedNoteRequestDTO.getTitle());
        validateRequest.setDescription(updatedNoteRequestDTO.getDescription());

        validate(validateRequest);

        Note note = noteRepository.findById(id_note).orElse(null);

        if (note != null) {
            note.setTitle(updatedNoteRequestDTO.getTitle());
            note.setDescription(updatedNoteRequestDTO.getDescription());
            Note savedNote = noteRepository.save(note);
            return noteMapper.noteToNoteResponseDTO(savedNote);
        } else {
            return null;
        }
    }

    @Override
    public NoteResponseDTO addTagToNote(Long id_note, Long id_tag) {
        Note note = noteRepository.findById(id_note).orElse(null);
        Tag tag = tagRepository.findById(id_tag).orElse(null);

        if (note != null && tag != null) {

            note.getTags().add(tag);
            tag.getNotes().add(note);

            noteRepository.save(note);
            tagRepository.save(tag);

            return noteMapper.noteToNoteResponseDTO(note);
        } else {
            return null;
        }
    }

    @Override
    public NoteResponseDTO removeTagFromNote(Long id_note, Long id_tag) {
        Note note = noteRepository.findById(id_note).orElse(null);
        Tag tag = tagRepository.findById(id_tag).orElse(null);

        if (note != null && tag != null) {
            note.getTags().remove(tag);
            tag.getNotes().remove(note);

            noteRepository.save(note);
            tagRepository.save(tag);

            return noteMapper.noteToNoteResponseDTO(note);
        } else {
            return null;
        }
    }

    @Override
    public NoteResponseDTO disableNote(Long id_note) {

        Note note = noteRepository.findById(id_note).orElse(null);

        if (note != null) {
            note.setEnabled(false);
            Note disabledNote = noteRepository.save(note);
            return noteMapper.noteToNoteResponseDTO(disabledNote);
        } else {
            System.out.println("It wasn't possible to find a note with the ID: " + id_note);
            return null;
        }
    }

    @Override
    public NoteResponseDTO enableNote(Long id_note) {
        Note note = noteRepository.findById(id_note).orElse(null);

        if (note != null) {
            note.setEnabled(true);
            Note enabledNote = noteRepository.save(note);
            return noteMapper.noteToNoteResponseDTO(enabledNote);
        } else {
            System.out.println("It wasn't possible to find a note with the ID: " + id_note);
            return null;
        }
    }


    //Deletes a note, removing associations with tags.
    @Override
    public NoteResponseDTO deleteNote(Long id_note, Long id_user) {
        Note note = noteRepository.findById(id_note).orElse(null);
        UserEntity user = userRepository.findById(id_user).orElse(null);

        if (note != null && user != null) {
            for (Tag tag : note.getTags()) {
                tag.getNotes().remove(note);
            }
            note.getTags().clear();

            if (user.getNotes() == null) {
                // Si es null, inicializar la lista
                user.setNotes(new ArrayList<>());
            }

            user.getNotes().remove(note);

            noteRepository.delete(note);

            return noteMapper.noteToNoteResponseDTO(note);
        } else {
            System.out.println("The user and/or note does not exist");
            return null;
        }
    }

    public void validate(NoteRequestDTO noteRequestDTO) throws MyException {
        if (noteRequestDTO.getTitle() == null || ExceptionMethods.onlySpaces(noteRequestDTO.getTitle())
                || noteRequestDTO.getDescription() == null || ExceptionMethods.onlySpaces(noteRequestDTO.getDescription())) {
            throw new MyException("Note's title or description can't be null or empty.");
        }
    }
}
