package com.example.backend.NoteTests;


import com.example.backend.dto.request.NoteRequestDTO;
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
    private NoteRequestDTO noteDTO;

    @BeforeEach
    void setUp() {

        tag1 = new Tag();
        tag2 = new Tag();
        note1 = new Note();
        note2 = new Note();
        notes = new ArrayList<>();
        tags = new ArrayList<>();
        noteDTO = new NoteRequestDTO();

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

        noteDTO.setTitle("Valid title");
        noteDTO.setDescription("Valid description");
        noteDTO.setId_note(1L);
        noteDTO.setEnabled(true);
        noteDTO.setTags(tags);

        notes.add(note1);
        notes.add(note2);
    }

    @Test
    void noteToNoteDTOTest() {

        NoteRequestDTO noteDTO1 = noteMapper.noteToNoteDTO(note1);

        assertEquals(note1.getId_note(), noteDTO1.getId_note());
        assertEquals(note1.getTitle(), noteDTO1.getTitle());
        assertEquals(note1.getDescription(), noteDTO1.getDescription());
        assertEquals(note1.isEnabled(), noteDTO1.isEnabled());
        assertEquals(note1.getTags(), noteDTO1.getTags());
    }

    @Test
    void noteDTOToNoteTest() {

        Note note = noteMapper.noteDTOToNote(noteDTO);

        assertEquals(note.getId_note(), noteDTO.getId_note());
        assertEquals(note.getTitle(), noteDTO.getTitle());
        assertEquals(note.getDescription(), noteDTO.getDescription());
        assertEquals(note.isEnabled(), noteDTO.isEnabled());
        assertEquals(note.getTags(), noteDTO.getTags());
    }

    @Test
    void toNoteDTOList() {

        List<NoteRequestDTO> noteDTOS = noteMapper.toNoteDTOList(notes);

        assertEquals(noteDTOS.size(), notes.size());
        assertEquals(noteDTOS.get(0).getId_note(), notes.get(0).getId_note());
        assertEquals(noteDTOS.get(1).getTags(), notes.get(1).getTags());
    }
}
