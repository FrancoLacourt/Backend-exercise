package com.example.backend.NoteTests;

import com.example.backend.dto.request.NoteDTO;
import com.example.backend.dto.request.TagDTO;
import com.example.backend.entity.Note;
import com.example.backend.entity.Tag;
import com.example.backend.mapper.TagMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TagMapperTest {

    @Autowired
    TagMapper tagMapper;

    private Tag tag1;
    private Tag tag2;
    private List<Tag> tags;
    private Note note1;
    private Note note2;
    private List<Note> notes;
    private NoteDTO noteDTO;
    private TagDTO tagDTO1;
    private TagDTO tagDTO2;
    private List<TagDTO> tagDTOS;

    @BeforeEach
    void setUp() {

        tag1 = new Tag();
        tag2 = new Tag();
        note1 = new Note();
        note2 = new Note();
        notes = new ArrayList<>();
        tags = new ArrayList<>();
        noteDTO = new NoteDTO();
        tagDTO1 = new TagDTO();
        tagDTO2 = new TagDTO();
        tagDTOS = new ArrayList<>();

        tag1.setTagName("Music");
        tag1.setId_tag(1L);
        tag2.setTagName("Entertainment");
        tag2.setId_tag(2L);

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

        tag1.setNotes(notes);

        tagDTO1.setTagName("Music");
        tagDTO1.setId_tag(1L);
        tagDTO1.setNotes(notes);

        tagDTO2.setTagName("Entertainment");
        tagDTO2.setId_tag(2L);
        tagDTO2.setNotes(notes);

        tagDTOS.add(tagDTO1);
        tagDTOS.add(tagDTO2);
    }

    @Test
    void tagToTagDTOTest() {

        TagDTO tagDTO = tagMapper.tagToTagDTO(tag1);

        assertEquals(tagDTO.getTagName(), tag1.getTagName());
        assertEquals(tagDTO.getId_tag(), tag1.getId_tag());
        assertEquals(tagDTO.getNotes(), tag1.getNotes());
    }

    @Test
    void tagDTOtoTagTest() {

        Tag tag = tagMapper.tagDTOToTag(tagDTO1);

        assertEquals(tag.getTagName(), tagDTO1.getTagName());
        assertEquals(tag.getId_tag(), tagDTO1.getId_tag());
        assertEquals(tag.getNotes(), tagDTO1.getNotes());
    }

    @Test
    void toTagDTOListTest() {

        List<TagDTO> newTagDTOS = tagMapper.toTagDTOList(tags);

        assertEquals(newTagDTOS.size(), tags.size());
        assertEquals(newTagDTOS.get(0).getId_tag(), tags.get(0).getId_tag());
        assertEquals(newTagDTOS.get(1).getNotes(), tags.get(1).getNotes());
    }

    @Test
    void toTagListTest() {

        List<Tag> newTags = tagMapper.toTagList(tagDTOS);

        assertEquals(newTags.size(), tagDTOS.size());
        assertEquals(newTags.get(0).getId_tag(), tagDTOS.get(0).getId_tag());
        assertEquals(newTags.get(1).getNotes(), tagDTOS.get(1).getNotes());
    }
}