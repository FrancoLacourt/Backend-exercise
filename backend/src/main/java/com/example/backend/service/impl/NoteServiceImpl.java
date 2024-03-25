package com.example.backend.service.impl;

import com.example.backend.dto.request.NoteRequestDTO;
import com.example.backend.dto.request.TagRequestDTO;
import com.example.backend.entity.Note;
import com.example.backend.entity.Tag;
import com.example.backend.exception.ExceptionMethods;
import com.example.backend.exception.MyException;
import com.example.backend.mapper.NoteMapper;
import com.example.backend.mapper.TagMapper;
import com.example.backend.repository.NoteRepository;
import com.example.backend.repository.TagRepository;
import com.example.backend.service.NoteService;
import com.example.backend.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteServiceImpl implements NoteService {

    private NoteRepository noteRepository;
    private TagRepository tagRepository;
    private NoteMapper noteMapper;
    private TagService tagService;
    private TagMapper tagMapper;

    @Autowired
    public NoteServiceImpl(NoteRepository noteRepository, NoteMapper noteMapper, TagService tagService, TagMapper tagMapper, TagRepository tagRepository) {
        this.noteRepository = noteRepository;
        this.noteMapper = noteMapper;
        this.tagService = tagService;
        this.tagMapper = tagMapper;
        this.tagRepository = tagRepository;
    }

    public NoteServiceImpl() {

    }


    //Validates input, associates tags, create note and save all to the repository.
    @Override
    @Transactional
    public NoteRequestDTO createNote(NoteRequestDTO noteDTO) throws MyException {
        validate(noteDTO);

        List<TagRequestDTO> tagsDTO = tagService.getOrCreateTags(noteDTO.getTagNames());
        List<Tag> tags = tagMapper.toTagList(tagsDTO);

        noteDTO.setEnabled(true);
        noteDTO.setTags(tags);

        Note note = noteMapper.noteDTOToNote(noteDTO);

        Note createdNote = noteRepository.save(note);

        for (Tag tag : tags) {
            tag.getNotes().add(createdNote);
        }

        tagRepository.saveAll(tags);


        return noteMapper.noteToNoteDTO(createdNote);
    }


    @Override
    public List<NoteRequestDTO> getAllNotes() {

        List<Note> notes = noteRepository.findAll();

        return noteMapper.toNoteDTOList(notes);
    }


    // Retrieve the notes that are enabled.
    @Override
    public List<NoteRequestDTO> getEnabledNotes() {
        List<NoteRequestDTO> enabledNotesDTO = getAllNotes();

        return enabledNotesDTO.stream().filter(NoteRequestDTO::isEnabled)
                .collect(Collectors.toList());
    }

    // Retrieve the notes that are disabled.
    @Override
    public List<NoteRequestDTO> getDisabledNotes() {
        List<NoteRequestDTO> disabledNotesDTO = getAllNotes();

        return disabledNotesDTO.stream()
                .filter(task -> !task.isEnabled())
                .collect(Collectors.toList());
    }

    @Override
    public NoteRequestDTO findNoteById(Long id_note) {

        Note note = noteRepository.findById(id_note).orElse(null);

        if (note != null) {
            return noteMapper.noteToNoteDTO(note);
        } else {
            System.out.println("It wasn't possible to find a note with the ID: " + id_note);
            return null;
        }
    }


    //Updates title and description of a note.
    @Override
    public NoteRequestDTO updateNote(Long id_note, NoteRequestDTO updatedNoteDTO) throws MyException {
        validate(updatedNoteDTO);

        Note note = noteRepository.findById(id_note).orElse(null);

        if (note != null) {
            note.setTitle(updatedNoteDTO.getTitle());
            note.setDescription(updatedNoteDTO.getDescription());
            Note savedNote = noteRepository.save(note);
            return noteMapper.noteToNoteDTO(savedNote);
        } else {
            return null;
        }
    }

    @Override
    public NoteRequestDTO addTagToNote(Long id_note, Long id_tag) {
        Note note = noteRepository.findById(id_note).orElse(null);
        Tag tag = tagRepository.findById(id_tag).orElse(null);

        if (note != null && tag != null) {

            note.getTags().add(tag);
            tag.getNotes().add(note);

            noteRepository.save(note);
            tagRepository.save(tag);

            return noteMapper.noteToNoteDTO(note);
        } else {
            return null;
        }
    }

    @Override
    public NoteRequestDTO removeTagFromNote(Long id_note, Long id_tag) {
        Note note = noteRepository.findById(id_note).orElse(null);
        Tag tag = tagRepository.findById(id_tag).orElse(null);

        if (note != null && tag != null) {
            note.getTags().remove(tag);
            tag.getNotes().remove(note);

            noteRepository.save(note);
            tagRepository.save(tag);

            return noteMapper.noteToNoteDTO(note);
        } else {
            return null;
        }
    }

    @Override
    public NoteRequestDTO disableNote(Long id_note) {

        Note note = noteRepository.findById(id_note).orElse(null);

        if (note != null) {
            note.setEnabled(false);
            Note disabledNote = noteRepository.save(note);
            return noteMapper.noteToNoteDTO(disabledNote);
        } else {
            System.out.println("It wasn't possible to find a note with the ID: " + id_note);
            return null;
        }
    }

    @Override
    public NoteRequestDTO enableNote(Long id_note) {
        Note note = noteRepository.findById(id_note).orElse(null);

        if (note != null) {
            note.setEnabled(true);
            Note enabledNote = noteRepository.save(note);
            return noteMapper.noteToNoteDTO(enabledNote);
        } else {
            System.out.println("It wasn't possible to find a note with the ID: " + id_note);
            return null;
        }
    }


    //Deletes a note, removing associations with tags.
    @Override
    public NoteRequestDTO deleteNote(Long id_note) {
        Note note = noteRepository.findById(id_note).orElse(null);

        if (note != null) {
            for (Tag tag : note.getTags()) {
                tag.getNotes().remove(note);
            }
            note.getTags().clear();

            noteRepository.delete(note);

            return noteMapper.noteToNoteDTO(note);
        } else {
            System.out.println("It wasn't possible to find a note with the ID: " + id_note);
            return null;
        }
    }

    public void validate(NoteRequestDTO noteDTO) throws MyException {
        if (noteDTO.getTitle() == null || ExceptionMethods.onlySpaces(noteDTO.getTitle())
                || noteDTO.getDescription() == null || ExceptionMethods.onlySpaces(noteDTO.getDescription())) {
            throw new MyException("Note's title or description can't be null or empty.");
        }
    }
}
