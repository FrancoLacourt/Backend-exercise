package com.example.backend.NoteTests;

import com.example.backend.entity.Note;
import com.example.backend.entity.Tag;
import com.example.backend.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class TagRepositoryTest {
    
    @Autowired
    TagRepository tagRepository;
    
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
    void saveTagTest() {

        tagRepository.save(tag1);

        Optional<Tag> savedTag = tagRepository.findById(tag1.getId_tag());

        assertTrue(tagRepository.existsById(tag1.getId_tag()));
        assertEquals("Music", savedTag.get().getTagName());
    }

    @Test
    void deleteTagTest() {

        tagRepository.save(tag1);

        assertTrue(tagRepository.existsById(tag1.getId_tag()));

        tagRepository.delete(tag1);

        assertFalse(tagRepository.existsById(tag1.getId_tag()));
        assertEquals(tagRepository.findById(tag1.getId_tag()), Optional.empty());
    }

    @Test
    void findTagByIdTest() {

        tagRepository.save(tag1);

        Optional<Tag> foundTag = tagRepository.findById(tag1.getId_tag());

        assertTrue(foundTag.isPresent());
        assertEquals("Music", foundTag.get().getTagName());
    }

    @Test
    void findAllTagsTest() {
        tagRepository.save(tag1);
        tagRepository.save(tag2);

        List<Tag> foundTags = tagRepository.findAll();

        assertEquals(tags, foundTags);
    }
    
}
