package com.example.backend.Tests.TagTests;

import com.example.backend.dto.request.TagRequestDTO;
import com.example.backend.dto.response.NoteResponseDTO;
import com.example.backend.dto.response.TagResponseDTO;
import com.example.backend.entity.Note;
import com.example.backend.entity.Tag;
import com.example.backend.exception.MyException;
import com.example.backend.mapper.NoteMapper;
import com.example.backend.mapper.TagMapper;
import com.example.backend.repository.TagRepository;
import com.example.backend.service.impl.TagServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @Mock
    private NoteMapper noteMapper;

    @Mock
    private TagMapper tagMapper;


    @InjectMocks
    private TagServiceImpl tagService;

    @Captor
    private ArgumentCaptor <Tag> tagCaptor;

    private Long id_note1;
    private Long id_note2;
    private Long id_tag1;
    private Long id_tag2;
    private Note note1;
    private Note note2;
    private Tag tag1;
    private Tag tag2;
    private TagResponseDTO tagResponseDTO1;
    private TagResponseDTO tagResponseDTO2;
    private NoteResponseDTO noteResponseDTO1;
    private NoteResponseDTO noteResponseDTO2;
    private List<Tag> tags;
    private List<Note> notes;
    private List<TagResponseDTO> tagResponseListDTO;
    private List<NoteResponseDTO> noteResponseListDTO;

    @BeforeEach
    void setUp() {

        id_note1 = 1L;
        id_note2 = 2L;

        id_tag1 = 1L;
        id_tag2 = 2L;

        note1 = new Note();
        note2 = new Note();

        tag1 = new Tag();
        tag2 = new Tag();

        tagResponseDTO1 = new TagResponseDTO();
        tagResponseDTO2 = new TagResponseDTO();

        noteResponseDTO1 = new NoteResponseDTO();
        noteResponseDTO2 = new NoteResponseDTO();

        tags = new ArrayList<>();
        notes = new ArrayList<>();

        tagResponseListDTO = new ArrayList<>();
        noteResponseListDTO = new ArrayList<>();

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
        tags.add(tag1);

        tag2.setId_tag(id_tag2);
        tag2.setTagName("Entertainment");
        tags.add(tag2);

        noteResponseDTO1.setTitle(note1.getTitle());
        noteResponseDTO1.setDescription(note1.getDescription());
        noteResponseDTO1.setId_note(note1.getId_note());
        noteResponseListDTO.add(noteResponseDTO1);

        noteResponseDTO2.setTitle(note2.getTitle());
        noteResponseDTO2.setDescription(note2.getDescription());
        noteResponseDTO2.setId_note(note2.getId_note());
        noteResponseListDTO.add(noteResponseDTO2);

        tagResponseDTO1.setTagName(tag1.getTagName());
        tagResponseDTO1.setId_tag(id_tag1);
         tagResponseListDTO.add(tagResponseDTO1);

        tagResponseDTO2.setTagName(tag2.getTagName());
        tagResponseDTO2.setId_tag(id_tag2);
         tagResponseListDTO.add(tagResponseDTO2);


        tag1.setNotes(notes);
        note1.setTags(tags);
    }

    @Test
    void testConstructor() {
        TagServiceImpl tagService = new TagServiceImpl();
        assertNotNull(tagService);
    }
    
    @Test
    void createTagTest() throws MyException {

        when(tagRepository.save(any(Tag.class))).thenReturn(new Tag());

        when(tagMapper.tagToTagResponseDTO(any(Tag.class))).thenReturn(tagResponseDTO1);
        
        TagResponseDTO createdTagResponseDTO = tagService.createTag(tag1.getTagName());

        assertNotNull(createdTagResponseDTO);
        
        verify(tagRepository).save(tagCaptor.capture());
        System.out.println(tagCaptor.getValue().getTagName());
        assertEquals(tagCaptor.getValue().getTagName(), tag1.getTagName());
    }

    @Test
    void getAllTagsTest() {

        when(tagRepository.findAll()).thenReturn(tags);
        when(tagMapper.toTagResponseListDTO(tags)).thenReturn( tagResponseListDTO);

        List<TagResponseDTO> collectedTagResponseListDTO = tagService.getAllTags();

        assertEquals(tagResponseListDTO, collectedTagResponseListDTO);
    }

    @Test
    void findTagByIdTest() {

        when(tagRepository.findById(id_tag1)).thenReturn(Optional.of(tag1));
        when(tagMapper.tagToTagResponseDTO(tag1)).thenReturn(tagResponseDTO1);

        TagResponseDTO collectedTagResponseDTO = tagService.findTagById(id_tag1);

        assertEquals(id_tag1, collectedTagResponseDTO.getId_tag());
    }

    @Test
    void findTagByIdTest_WhenTagDoesNotExist() {

        when(tagRepository.findById(id_tag1)).thenReturn(Optional.empty());

        TagResponseDTO collectedTagResponseDto = tagService.findTagById(id_tag1);

        verify(tagRepository).findById(id_tag1);
        verifyNoInteractions(tagMapper);

        assertNull(collectedTagResponseDto);
    }

    @Test
    void getOrCreateTagsTest() throws MyException {

        List<String> tagNames = Arrays.asList("Music", "Entertainment");

        Tag existingTag = tag1;

        TagResponseDTO existingTagResponseDTO = tagResponseDTO1;

        TagResponseDTO newTagResponseDTO = new TagResponseDTO();
        newTagResponseDTO.setTagName("Entertainment");

        when(tagRepository.findTagByTagName("Music")).thenReturn(Optional.of(tag1));
        when(tagRepository.findTagByTagName("Entertainment")).thenReturn(Optional.empty());
        when(tagMapper.tagToTagResponseDTO(existingTag)).thenReturn(existingTagResponseDTO);
        when(tagService.createTag("Entertainment")).thenReturn(newTagResponseDTO);

        List<TagResponseDTO> resultTagResponseListDTO = tagService.getOrCreateTags(tagNames);

        verify(tagRepository).findTagByTagName("Music");
        verify(tagRepository).findTagByTagName("Entertainment");
        verify(tagMapper).tagToTagResponseDTO(existingTag);
//        verify(tagService).createTag("Entertainment");

        assertEquals(resultTagResponseListDTO.size(), 2);
        assertEquals(existingTagResponseDTO, resultTagResponseListDTO.get(0));
        assertEquals(newTagResponseDTO, resultTagResponseListDTO.get(1));
    }

    @Test
    void getOrCreateTagsTest_WithTagNameNull() throws MyException {

        List<String> tagNames = List.of();

        when(tagRepository.findTagByTagName(any())).thenReturn(Optional.empty());

        List<TagResponseDTO> resultTagResponseListDTO = tagService.getOrCreateTags(tagNames);

        assertThrows(MyException.class, () -> tagService.createTag(null));
    }

    @Test
    void getNotesTest() {

        when(tagRepository.findById(id_tag1)).thenReturn(Optional.of(tag1));
        when(noteMapper.toNoteResponseDTOList(notes)).thenReturn(noteResponseListDTO);

        List<NoteResponseDTO> collectedNoteResponseListDTO = tagService.getNotes(id_tag1);

        assertEquals(collectedNoteResponseListDTO, noteResponseListDTO);
    }

    @Test
    void validTagDTO() {
        TagRequestDTO tagDTO = new TagRequestDTO();

        tagDTO.setTagName("Music");

        assertDoesNotThrow(() -> tagService.validate(tagDTO.getTagName()));
    }

    @Test
    void validateTagDTO_NullTagName() {
        TagRequestDTO tagDTO = new TagRequestDTO();

        MyException exception = assertThrows(MyException.class, () -> tagService.validate(tagDTO.getTagName()));
        assertEquals("Tag's name can't be null or empty.", exception.getMessage());
    }

    @Test
    void validateTagDTO_OnlySpaces() {
        TagRequestDTO tagDTO = new TagRequestDTO();

        tagDTO.setTagName("  ");

        MyException exception = assertThrows(MyException.class, () -> tagService.validate(tagDTO.getTagName()));
        assertEquals("Tag's name can't be null or empty.", exception.getMessage());
    }

    @Test
    void updateTagTest() throws MyException {

        String newTagName = "Fantasy";

        when(tagRepository.findById(id_tag1)).thenReturn(Optional.of(tag1));

        when(tagRepository.save(any(Tag.class))).thenReturn(tag1);
        when(tagMapper.tagToTagResponseDTO(any())).thenReturn(new TagResponseDTO());

        TagResponseDTO resultTagResponseDTO = tagService.updateTag(id_tag1, newTagName);

        verify(tagRepository).save(tag1);

        assertEquals(tag1.getTagName(), newTagName);
    }

    @Test
    void updateTagTest_WhenTagDoesNotExist() throws MyException {

        String newTagName = "Fantasy";

        when(tagRepository.findById(id_tag1)).thenReturn(Optional.empty());

        TagResponseDTO resultTagResponseDTO = tagService.updateTag(id_tag1, newTagName);

        assertNull(resultTagResponseDTO);
        verify(tagRepository).findById(id_tag1);
        verifyNoInteractions(tagMapper);
        verifyNoMoreInteractions(tagRepository);
    }

    @Test
    void deleteTagTest() {

        when(tagRepository.findById(id_tag1)).thenReturn(Optional.ofNullable(tag1));

        when(tagMapper.tagToTagResponseDTO(tag1)).thenReturn(new TagResponseDTO());

        TagResponseDTO deletedTagResponseDTO = tagService.deleteTag(id_tag1);

        verify(tagRepository).delete(tag1);

        assertFalse(tag1.getNotes().contains(note1));
        assertFalse(note1.getTags().contains(tag1));
    }

    @Test
    void deleteTagTest_WhenTagDoesNotExist() {

        when(tagRepository.findById(id_tag1)).thenReturn(Optional.empty());

        TagResponseDTO deletedTagResponseDTO = tagService.deleteTag(id_tag1);

        verify(tagRepository).findById(id_tag1);
        verifyNoMoreInteractions(tagRepository);
        verifyNoInteractions(tagMapper);

        assertNull(deletedTagResponseDTO);
    }
}