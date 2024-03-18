package com.example.backend.NoteTests;

import com.example.backend.entity.Note;
import com.example.backend.entity.Tag;
import com.example.backend.repository.NoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class NoteRepositoryTest {

    @Autowired
    NoteRepository noteRepository;

    private Tag tag1;
    private Tag tag2;
    private List<Tag> tags;
    private Note note1;
    private Note note2;
    private List<Note> notes;

    @BeforeEach
    void setUp() {

        tag1 = new Tag();
        tag2 = new Tag();
        note1 = new Note();
        note2 = new Note();
        notes = new ArrayList<>();
        tags = new ArrayList<>();

        tag1.setTagName("Music");
        tag2.setTagName("Entertainment");

        tags.add(tag1);
        tags.add(tag2);

        note1.setTitle("Valid title");
        note1.setDescription("Valid description");
        note1.setTags(tags);

        note2.setTitle("Valid title 2");
        note2.setDescription("Valid description 2");

        notes.add(note1);
        notes.add(note2);
    }

    @Test
    void saveNoteTest() {

        noteRepository.save(note1);

        Optional<Note> savedNote = noteRepository.findById(note1.getId_note());

        assertTrue(noteRepository.existsById(note1.getId_note()));
        assertEquals("Valid title", savedNote.get().getTitle());
        assertEquals("Valid description", savedNote.get().getDescription());
        assertEquals(tags, savedNote.get().getTags());
    }

    @Test
    void deleteNoteTest() {

        noteRepository.save(note1);

        assertTrue(noteRepository.existsById(note1.getId_note()));

        noteRepository.delete(note1);

        assertFalse(noteRepository.existsById(note1.getId_note()));
        assertEquals((noteRepository.findById(note1.getId_note())),Optional.empty());

    }

    @Test
    void findNoteByIdTest() {

        noteRepository.save(note1);

        Optional<Note> foundNote = noteRepository.findById(note1.getId_note());

        assertTrue(foundNote.isPresent());

        assertEquals("Valid title", foundNote.get().getTitle());
        assertEquals("Valid description", foundNote.get().getDescription());
    }

    @Test
    void findAllNotesTest() {

        noteRepository.save(note1);
        noteRepository.save(note2);

        List<Note> foundNotes = noteRepository.findAll();

        assertEquals(notes, foundNotes);
    }
}