package com.example.backend.NoteTests;

import com.example.backend.dto.NoteDTO;
import com.example.backend.dto.TagDTO;
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
import org.mockito.internal.verification.NoMoreInteractions;
import org.mockito.stubbing.OngoingStubbing;
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
    private TagDTO tagDTO1;
    private TagDTO tagDTO2;
    private NoteDTO noteDTO1;
    private NoteDTO noteDTO2;
    private List<Tag> tags;
    private List<Note> notes;
    private List<TagDTO> tagsDTO;
    private List<NoteDTO> noteDTOS;

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

        tagDTO1 = new TagDTO();
        tagDTO2 = new TagDTO();

        noteDTO1 = new NoteDTO();
        noteDTO2 = new NoteDTO();

        tags = new ArrayList<>();
        notes = new ArrayList<>();

        tagsDTO = new ArrayList<>();
        noteDTOS = new ArrayList<>();

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

        noteDTO1.setTitle(note1.getTitle());
        noteDTO1.setDescription(note1.getDescription());
        noteDTO1.setId_note(note1.getId_note());
        noteDTOS.add(noteDTO1);

        noteDTO2.setTitle(note2.getTitle());
        noteDTO2.setDescription(note2.getDescription());
        noteDTO2.setId_note(note2.getId_note());
        noteDTOS.add(noteDTO2);

        tagDTO1.setTagName(tag1.getTagName());
        tagDTO1.setId_tag(id_tag1);
        tagsDTO.add(tagDTO1);

        tagDTO2.setTagName(tag2.getTagName());
        tagDTO2.setId_tag(id_tag2);
        tagsDTO.add(tagDTO2);


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

        when(tagMapper.tagToTagDTO(any(Tag.class))).thenReturn(tagDTO1);
        
        TagDTO createdTagDTO = tagService.createTag(tag1.getTagName());

        assertNotNull(createdTagDTO);
        
        verify(tagRepository).save(tagCaptor.capture());
        System.out.println(tagCaptor.getValue().getTagName());
        assertEquals(tagCaptor.getValue().getTagName(), tag1.getTagName());
    }

    @Test
    void getAllTagsTest() {

        when(tagRepository.findAll()).thenReturn(tags);
        when(tagMapper.toTagDTOList(tags)).thenReturn(tagsDTO);

        List<TagDTO> collectedTagsDTO = tagService.getAllTags();

        assertEquals(tagsDTO, collectedTagsDTO);
    }

    @Test
    void findTagByIdTest() {

        when(tagRepository.findById(id_tag1)).thenReturn(Optional.of(tag1));
        when(tagMapper.tagToTagDTO(tag1)).thenReturn(tagDTO1);

        TagDTO collectedTagDTO = tagService.findTagById(id_tag1);

        assertEquals(id_tag1, collectedTagDTO.getId_tag());
    }

    @Test
    void findTagByIdTest_WhenTagDoesNotExist() {

        when(tagRepository.findById(id_tag1)).thenReturn(Optional.empty());

        TagDTO collectedTagDto = tagService.findTagById(id_tag1);

        verify(tagRepository).findById(id_tag1);
        verifyNoInteractions(tagMapper);

        assertNull(collectedTagDto);
    }

    @Test
    void getOrCreateTagsTest() throws MyException {

        List<String> tagNames = Arrays.asList("Music", "Entertainment");

        Tag existingTag = tag1;

        TagDTO existingTagDTO = tagDTO1;

        TagDTO newTagDTO = new TagDTO();
        newTagDTO.setTagName("Entertainment");

        when(tagRepository.findTagByTagName("Music")).thenReturn(Optional.of(tag1));
        when(tagRepository.findTagByTagName("Entertainment")).thenReturn(Optional.empty());
        when(tagMapper.tagToTagDTO(existingTag)).thenReturn(existingTagDTO);
        when(tagService.createTag("Entertainment")).thenReturn(newTagDTO);

        List<TagDTO> resultTagsDTO = tagService.getOrCreateTags(tagNames);

        verify(tagRepository).findTagByTagName("Music");
        verify(tagRepository).findTagByTagName("Entertainment");
        verify(tagMapper).tagToTagDTO(existingTag);
//        verify(tagService).createTag("Entertainment");

        assertEquals(resultTagsDTO.size(), 2);
        assertEquals(existingTagDTO, resultTagsDTO.get(0));
        assertEquals(newTagDTO, resultTagsDTO.get(1));
    }

    @Test
    void getOrCreateTagsTest_WithTagNameNull() throws MyException {

        List<String> tagNames = List.of();

        when(tagRepository.findTagByTagName(any())).thenReturn(Optional.empty());

        List<TagDTO> resultTagsDTO = tagService.getOrCreateTags(tagNames);

        assertThrows(MyException.class, () -> tagService.createTag(null));
    }

    @Test
    void getNotesTest() {

        when(tagRepository.findById(id_tag1)).thenReturn(Optional.of(tag1));
        when(noteMapper.toNoteDTOList(notes)).thenReturn(noteDTOS);

        List<NoteDTO> collectedNotesDTO = tagService.getNotes(id_tag1);

        assertEquals(collectedNotesDTO, noteDTOS);
    }

    @Test
    void validTagDTO() {
        TagDTO tagDTO = new TagDTO();

        tagDTO.setTagName("Music");

        assertDoesNotThrow(() -> tagService.validate(tagDTO.getTagName()));
    }

    @Test
    void validateTagDTO_NullTagName() {
        TagDTO tagDTO = new TagDTO();

        MyException exception = assertThrows(MyException.class, () -> tagService.validate(tagDTO.getTagName()));
        assertEquals("Tag's name can't be null or empty.", exception.getMessage());
    }

    @Test
    void validateTagDTO_OnlySpaces() {
        TagDTO tagDTO = new TagDTO();

        tagDTO.setTagName("  ");

        MyException exception = assertThrows(MyException.class, () -> tagService.validate(tagDTO.getTagName()));
        assertEquals("Tag's name can't be null or empty.", exception.getMessage());
    }

    @Test
    void updateTagTest() throws MyException {

        String newTagName = "Fantasy";

        when(tagRepository.findById(id_tag1)).thenReturn(Optional.of(tag1));

        when(tagRepository.save(any(Tag.class))).thenReturn(tag1);
        when(tagMapper.tagToTagDTO(any())).thenReturn(new TagDTO());

        TagDTO resultTagDTO = tagService.updateTag(id_tag1, newTagName);

        verify(tagRepository).save(tag1);

        assertEquals(tag1.getTagName(), newTagName);
    }

    @Test
    void updateTagTest_WhenTagDoesNotExist() throws MyException {

        String newTagName = "Fantasy";

        when(tagRepository.findById(id_tag1)).thenReturn(Optional.empty());

        TagDTO resultTagDTO = tagService.updateTag(id_tag1, newTagName);

        assertNull(resultTagDTO);
        verify(tagRepository).findById(id_tag1);
        verifyNoInteractions(tagMapper);
        verifyNoMoreInteractions(tagRepository);
    }

    @Test
    void deleteTagTest() {

        when(tagRepository.findById(id_tag1)).thenReturn(Optional.ofNullable(tag1));

        when(tagMapper.tagToTagDTO(tag1)).thenReturn(new TagDTO());

        TagDTO deletedTag = tagService.deleteTag(id_tag1);

        verify(tagRepository).delete(tag1);

        assertFalse(tag1.getNotes().contains(note1));
        assertFalse(note1.getTags().contains(tag1));
    }

    @Test
    void deleteTagTest_WhenTagDoesNotExist() {

        when(tagRepository.findById(id_tag1)).thenReturn(Optional.empty());

        TagDTO deletedTag = tagService.deleteTag(id_tag1);

        verify(tagRepository).findById(id_tag1);
        verifyNoMoreInteractions(tagRepository);
        verifyNoInteractions(tagMapper);

        assertNull(deletedTag);
    }
}