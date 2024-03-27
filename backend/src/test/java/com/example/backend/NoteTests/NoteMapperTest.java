package com.example.backend.NoteTests;


import com.example.backend.dto.request.NoteRequestDTO;
import com.example.backend.dto.request.UpdatedNoteRequestDTO;
import com.example.backend.dto.response.NoteResponseDTO;
import com.example.backend.entity.Note;
import com.example.backend.entity.Tag;
import com.example.backend.mapper.NoteMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class NoteMapperTest {

    @Autowired
    NoteMapper noteMapper;

    private Tag tag1;
    private Tag tag2;
    private List<Tag> tags;
    private Note note1;
    private Note note2;
    private List<Note> notes;
    private NoteResponseDTO noteResponseDTO;
    private NoteRequestDTO noteRequestDTO;

    @BeforeEach
    void setUp() {

        tag1 = new Tag();
        tag2 = new Tag();
        note1 = new Note();
        note2 = new Note();
        notes = new ArrayList<>();
        tags = new ArrayList<>();
        noteResponseDTO = new NoteResponseDTO();
        noteRequestDTO = new NoteRequestDTO();

        tag1.setTagName("Music");
        tag2.setTagName("Entertainment");

        tags.add(tag1);
        tags.add(tag2);

        note1.setTitle("Valid title");
        note1.setDescription("Valid description");
        note1.setId_note(1L);
        note1.setEnabled(true);
        note1.setTags(tags);

        note2.setTitle("Valid title 2");
        note2.setDescription("Valid description 2");
        note2.setId_note(2L);
        note2.setEnabled(true);

        noteResponseDTO.setTitle("Valid title");
        noteResponseDTO.setDescription("Valid description");
        noteResponseDTO.setId_note(1L);
        noteResponseDTO.setEnabled(true);
        noteResponseDTO.setTags(tags);

        noteRequestDTO.setTitle("Valid title");
        noteRequestDTO.setDescription("Valid description");

        notes.add(note1);
        notes.add(note2);
    }

    @Test
    void noteToNoteResponseDTOTest() {

        NoteResponseDTO noteResponseDTO1 = noteMapper.noteToNoteResponseDTO(note1);

        assertEquals(note1.getId_note(), noteResponseDTO1.getId_note());
        assertEquals(note1.getTitle(), noteResponseDTO1.getTitle());
        assertEquals(note1.getDescription(), noteResponseDTO1.getDescription());
        assertEquals(note1.isEnabled(), noteResponseDTO1.isEnabled());
        assertEquals(note1.getTags(), noteResponseDTO1.getTags());
    }

    @Test
    void noteResponseDTOToNoteTest() {

        Note note = noteMapper.noteResponseDTOToNote(noteResponseDTO);

        assertEquals(note.getId_note(), noteResponseDTO.getId_note());
        assertEquals(note.getTitle(), noteResponseDTO.getTitle());
        assertEquals(note.getDescription(), noteResponseDTO.getDescription());
        assertEquals(note.isEnabled(), noteResponseDTO.isEnabled());
        assertEquals(note.getTags(), noteResponseDTO.getTags());
    }
    @Test
    void noteRequestDTOToNoteResponseDTOTest() {

        NoteResponseDTO newNoteResponseDTO = noteMapper.noteRequestDTOToNoteResponseDTO(noteRequestDTO);

        assertEquals(newNoteResponseDTO.getTitle(), noteRequestDTO.getTitle());
        assertEquals(newNoteResponseDTO.getDescription(), noteRequestDTO.getDescription());
    }

    @Test
    void toNoteResponseDTOList() {

        List<NoteResponseDTO> noteResponseListDTO = noteMapper.toNoteResponseDTOList(notes);

        assertEquals(noteResponseListDTO.size(), notes.size());
        assertEquals(noteResponseListDTO.get(0).getId_note(), notes.get(0).getId_note());
        assertEquals(noteResponseListDTO.get(1).getTags(), notes.get(1).getTags());
    }
}
