package com.example.backend.NoteTests;

import com.example.backend.dto.request.NoteRequestDTO;
import com.example.backend.dto.request.TagRequestDTO;
import com.example.backend.dto.response.NoteResponseDTO;
import com.example.backend.dto.response.TagResponseDTO;
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
    private NoteResponseDTO noteResponseDTO;
    private TagResponseDTO tagResponseDTO1;
    private TagResponseDTO tagResponseDTO2;
    private List<TagResponseDTO> tagResponseListDTO;

    @BeforeEach
    void setUp() {

        tag1 = new Tag();
        tag2 = new Tag();
        note1 = new Note();
        note2 = new Note();
        notes = new ArrayList<>();
        tags = new ArrayList<>();
        noteResponseDTO = new NoteResponseDTO();
        tagResponseDTO1 = new TagResponseDTO();
        tagResponseDTO2 = new TagResponseDTO();
        tagResponseListDTO = new ArrayList<>();

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

        noteResponseDTO.setTitle("Valid title");
        noteResponseDTO.setDescription("Valid description");
        noteResponseDTO.setId_note(1L);
        noteResponseDTO.setEnabled(true);
        noteResponseDTO.setTags(tags);

        notes.add(note1);
        notes.add(note2);

        tag1.setNotes(notes);

        tagResponseDTO1.setTagName("Music");
        tagResponseDTO1.setId_tag(1L);
        tagResponseDTO1.setNotes(notes);

        tagResponseDTO2.setTagName("Entertainment");
        tagResponseDTO2.setId_tag(2L);
        tagResponseDTO2.setNotes(notes);

        tagResponseListDTO.add(tagResponseDTO1);
        tagResponseListDTO.add(tagResponseDTO2);
    }

    @Test
    void tagToTagResponseDTOTest() {

        TagResponseDTO tagResponseDTO = tagMapper.tagToTagResponseDTO(tag1);

        assertEquals(tagResponseDTO.getTagName(), tag1.getTagName());
        assertEquals(tagResponseDTO.getId_tag(), tag1.getId_tag());
        assertEquals(tagResponseDTO.getNotes(), tag1.getNotes());
    }

    @Test
    void tagResponseDTOToTagTest() {

        Tag tag = tagMapper.tagResponseDTOToTag(tagResponseDTO1);

        assertEquals(tag.getTagName(), tagResponseDTO1.getTagName());
        assertEquals(tag.getId_tag(), tagResponseDTO1.getId_tag());
        assertEquals(tag.getNotes(), tagResponseDTO1.getNotes());
    }

    @Test
    void toTagResponseListDTO() {

        List<TagResponseDTO> newTagResponseListDTO = tagMapper.toTagResponseListDTO(tags);

        assertEquals(newTagResponseListDTO.size(), tags.size());
        assertEquals(newTagResponseListDTO.get(0).getId_tag(), tags.get(0).getId_tag());
        assertEquals(newTagResponseListDTO.get(1).getNotes(), tags.get(1).getNotes());
    }

    @Test
    void tagResponseListDTOToTagList() {

        List<Tag> newTags = tagMapper.tagResponseListDTOToTagList(tagResponseListDTO);

        assertEquals(newTags.size(), tagResponseListDTO.size());
        assertEquals(newTags.get(0).getId_tag(), tagResponseListDTO.get(0).getId_tag());
        assertEquals(newTags.get(1).getNotes(), tagResponseListDTO.get(1).getNotes());
    }
}