package com.example.backend.NoteTests;

import com.example.backend.dto.request.NoteDTO;
import com.example.backend.dto.request.TagDTO;
import com.example.backend.entity.Note;
import com.example.backend.entity.Tag;
import com.example.backend.exception.MyException;
import com.example.backend.mapper.NoteMapper;
import com.example.backend.mapper.TagMapper;
import com.example.backend.repository.NoteRepository;
import com.example.backend.repository.TagRepository;
import com.example.backend.service.TagService;
import com.example.backend.service.impl.NoteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class NoteServiceTest {


    @Mock
    private NoteRepository noteRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private NoteMapper noteMapper;

    @Mock
    private TagService tagService;

    @Mock
    private TagMapper tagMapper;

    @InjectMocks
    private NoteServiceImpl noteService;

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
    private NoteDTO inputNoteDTO;
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

        inputNoteDTO = new NoteDTO();

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
    }

    @Test
    void testConstructor() {
        NoteServiceImpl noteService = new NoteServiceImpl();
        assertNotNull(noteService);
    }

    @Test
    void createNoteTest() throws MyException {

        when(tagService.getOrCreateTags(inputNoteDTO.getTagNames())).thenReturn(tagsDTO);
        when(tagMapper.toTagList(tagsDTO)).thenReturn(tags);

        inputNoteDTO.setTitle("valid title");
        inputNoteDTO.setDescription("valid description");

        note1.setTitle(inputNoteDTO.getTitle());
        note1.setDescription(inputNoteDTO.getDescription());
        note1.setTags(tags);
        note1.setEnabled(true);

        when(noteMapper.noteDTOToNote(inputNoteDTO)).thenReturn(note1);
        when(noteRepository.save(note1)).thenReturn(note1);
        when(noteMapper.noteToNoteDTO(note1)).thenReturn(new NoteDTO());

        // Llamada al método a probar
            NoteDTO resultNoteDTO = noteService.createNote(inputNoteDTO);

        verify(tagRepository).saveAll(tags);

        for (Tag tag : tags) {
            assertTrue(tag.getNotes().contains(note1));
        }

        // Verificaciones
        assertNotNull(resultNoteDTO);
        assertEquals(inputNoteDTO.getTitle(), note1.getTitle());
        assertEquals(inputNoteDTO.getDescription(), note1.getDescription());
        assertEquals(inputNoteDTO.getTags(), note1.getTags());
        assertTrue(inputNoteDTO.isEnabled());
        assertTrue(note1.isEnabled());

        // Verificación de llamadas a los mocks
        verify(tagService).getOrCreateTags(any());
        verify(tagMapper).toTagList(tagsDTO);
        verify(noteMapper).noteDTOToNote(inputNoteDTO);
        verify(noteRepository).save(note1);
        verify(noteMapper).noteToNoteDTO(note1);
    }

    @Test
    void getAllNotesTest() {

        when(noteRepository.findAll()).thenReturn(notes);
        when(noteMapper.toNoteDTOList(notes)).thenReturn(noteDTOS);

        List<NoteDTO> collectedNotesDTO = noteService.getAllNotes();

        assertEquals(noteDTOS, collectedNotesDTO);
    }

    @Test
    void getEnabledNotesTest() {

        noteDTO1.setEnabled(true);
        noteDTO2.setEnabled(false);

        when(noteService.getAllNotes()).thenReturn(noteDTOS);

        List<NoteDTO> enabledNotesDTO = noteService.getEnabledNotes();

//        verify(noteService.getAllNotes());

        assertEquals(1, enabledNotesDTO.size());
        assertEquals(enabledNotesDTO.get(0), noteDTO1);
    }

    @Test
    void getDisabledNotesTest() {

        noteDTO1.setEnabled(true);
        noteDTO2.setEnabled(false);

        when(noteService.getAllNotes()).thenReturn(noteDTOS);

        List<NoteDTO> disabledNotesDTO = noteService.getDisabledNotes();

        assertEquals(1, disabledNotesDTO.size());
    }

    @Test
    void findNoteByIdTest() {

        when(noteRepository.findById(id_note1)).thenReturn(Optional.of(note1));
        when(noteMapper.noteToNoteDTO(note1)).thenReturn(noteDTO1);

        NoteDTO collectedNoteDTO = noteService.findNoteById(id_note1);

        assertEquals(id_note1, collectedNoteDTO.getId_note());
    }

    @Test
    void findNoteByIdTest_WhenNoteDoesNotExist() {

        when(noteRepository.findById(id_note1)).thenReturn(Optional.empty());
        NoteDTO collectedNoteDTO = noteService.findNoteById(id_note1);

        assertNull(collectedNoteDTO);
        verify(noteRepository).findById(id_note1);
        verifyNoInteractions(noteMapper);

    }

    @Test
    void updateNoteTest() throws MyException {

        NoteDTO updatedNoteDTO = new NoteDTO();

        updatedNoteDTO.setTitle("Updated title");
        updatedNoteDTO.setDescription("Updated description");
        updatedNoteDTO.setEnabled(true);

        when(noteRepository.findById(id_note1)).thenReturn(Optional.of(note1));

        when(noteRepository.save(any(Note.class))).thenReturn(note1);
        when(noteMapper.noteToNoteDTO(any())).thenReturn(new NoteDTO());

        NoteDTO resultNoteDTO = noteService.updateNote(id_note1, updatedNoteDTO);

        verify(noteRepository).save(note1);

        assertEquals(updatedNoteDTO.getTitle(), note1.getTitle());
        assertEquals(updatedNoteDTO.getDescription(), note1.getDescription());
    }

    @Test
    void updateNoteTest_WhenNoteDoesNotExist() throws MyException {

        NoteDTO updatedNoteDTO = new NoteDTO();

        updatedNoteDTO.setTitle("Updated title");
        updatedNoteDTO.setDescription("Updated description");
        updatedNoteDTO.setEnabled(true);

        when(noteRepository.findById(id_note1)).thenReturn(Optional.empty());

        NoteDTO resultNoteDTO = noteService.updateNote(id_note1, updatedNoteDTO);

        assertNull(resultNoteDTO);
        verify(noteRepository).findById(id_note1);
        verifyNoInteractions(noteMapper);
        verifyNoMoreInteractions(noteRepository);
    }

    @Test
    void addTagToNoteTest() {

        // Datos de prueba
        Note newNote = new Note();
        Tag newTag = new Tag();

        newNote.setId_note(id_note1);
        newNote.setTitle("Valid title");
        newNote.setDescription("Valid description");
        newNote.setId_note(id_note1);

        newTag.setId_tag(id_tag1);
        newTag.setTagName("Musssiiccc");

        // Configuración de los mocks
        when(noteRepository.findById(id_note1)).thenReturn(java.util.Optional.of(newNote));
        when(tagRepository.findById(id_tag1)).thenReturn(java.util.Optional.of(newTag));
        when(noteMapper.noteToNoteDTO(newNote)).thenReturn(new NoteDTO()); // Mock del mapper

        // Llamada al método bajo prueba
        NoteDTO result = noteService.addTagToNote(id_note1, id_tag1);

        // Verificación
        assertNotNull(result); // Verifica que el resultado no sea nulo

        // Verifica que se llamaron los métodos necesarios
        verify(noteRepository).findById(id_note1);
        verify(tagRepository).findById(id_tag1);
        verify(noteRepository).save(newNote);
        verify(tagRepository).save(newTag);
        verify(noteMapper).noteToNoteDTO(newNote);

        assertTrue(newNote.getTags().contains(newTag));
        assertTrue(newTag.getNotes().contains(newNote));
    }

    @Test
    void addTagToNoteTest_WhenNoteDoesNotExist() {

        when(noteRepository.findById(id_note1)).thenReturn(Optional.empty());
        when(tagRepository.findById(id_tag1)).thenReturn(Optional.ofNullable(tag1));

        NoteDTO result = noteService.addTagToNote(id_note1, id_tag1);

        repeatedVerifications(result);
    }

    @Test
    void addTagToNoteTest_WhenTagDoesNotExist() {

        when(noteRepository.findById(id_note1)).thenReturn(Optional.ofNullable(note1));
        when(tagRepository.findById(id_tag1)).thenReturn(Optional.empty());

        NoteDTO result = noteService.addTagToNote(id_note1, id_tag1);

        repeatedVerifications(result);
    }

    @Test
    void removeTagFromNoteTest() {

        Note newNote = new Note();
        Tag newTag = new Tag();

        List<Note> newNotes = new ArrayList<>();
        List<Tag> newTags = new ArrayList<>();

        newNote.setId_note(id_note1);
        newNote.setTitle("Valid title");
        newNote.setDescription("Valid description");
        newNote.setId_note(id_note1);
        newNotes.add(newNote);

        newTag.setId_tag(id_tag1);
        newTag.setTagName("Musssiiccc");
        newTags.add(newTag);

        newNote.setTags(tags);
        newTag.setNotes(notes);

        // Configuración de los mocks
        when(noteRepository.findById(id_note1)).thenReturn(java.util.Optional.of(newNote));
        when(tagRepository.findById(id_tag1)).thenReturn(java.util.Optional.of(newTag));
        when(noteMapper.noteToNoteDTO(newNote)).thenReturn(new NoteDTO()); // Mock del mapper

        // Llamada al método bajo prueba
        NoteDTO result = noteService.removeTagFromNote(id_note1, id_tag1);

        // Verificación
        assertNotNull(result); // Verifica que el resultado no sea nulo

        // Verifica que se llamaron los métodos necesarios
        verify(noteRepository).findById(id_note1);
        verify(tagRepository).findById(id_tag1);
        verify(noteRepository).save(newNote);
        verify(tagRepository).save(newTag);
        verify(noteMapper).noteToNoteDTO(newNote);

        assertFalse(newNote.getTags().contains(newTag));
        assertFalse(newTag.getNotes().contains(newNote));
    }

    @Test
    void removeTagFromNote_WhenNoteDoesNotExist() {

        when(noteRepository.findById(id_note1)).thenReturn(Optional.empty());
        when(tagRepository.findById(id_tag1)).thenReturn(Optional.of(tag1));

        NoteDTO result = noteService.removeTagFromNote(id_note1, id_tag1);

        repeatedVerifications(result);
    }

    @Test
    void removeTagFromNote_WhenTagDoesNotExist() {

        when(noteRepository.findById(id_note1)).thenReturn(Optional.of(note1));
        when(tagRepository.findById(id_tag1)).thenReturn(Optional.empty());

        NoteDTO result = noteService.removeTagFromNote(id_note1, id_tag1);

        repeatedVerifications(result);
    }

    @Test
    void disableNoteTest() {

        note1.setEnabled(true);

        when(noteRepository.findById(id_note1)).thenReturn(java.util.Optional.of(note1));
        when(noteMapper.noteToNoteDTO(any())).thenReturn(new NoteDTO());

        NoteDTO disabledNote = noteService.disableNote(id_note1);

        verify(noteRepository).save(note1);

        assertFalse(note1.isEnabled());
    }

    @Test
    void disableNoteTest_WhenNoteDoesNotExist() {

        when(noteRepository.findById(id_note1)).thenReturn(Optional.empty());

        NoteDTO disabledNote = noteService.disableNote(id_note1);

        verify(noteRepository).findById(id_note1);
        verifyNoMoreInteractions(noteRepository);
        verifyNoInteractions(noteMapper);

        assertNull(disabledNote);
    }

    @Test
    void enableNoteTest() {

        note1.setEnabled(true);

        when(noteRepository.findById(id_note1)).thenReturn(java.util.Optional.of(note1));
        when(noteMapper.noteToNoteDTO(any())).thenReturn(new NoteDTO());

        NoteDTO enabledNote = noteService.enableNote(id_note1);

        verify(noteRepository).save(note1);

        assertTrue(note1.isEnabled());
    }

    @Test
    void enableNoteTest_WhenNoteDoesNotExist() {

        when(noteRepository.findById(id_note1)).thenReturn(Optional.empty());

        NoteDTO disabledNote = noteService.enableNote(id_note1);

        verify(noteRepository).findById(id_note1);
        verifyNoMoreInteractions(noteRepository);
        verifyNoInteractions(noteMapper);

        assertNull(disabledNote);
    }

    @Test
    void deleteNoteTest() {

        when(noteRepository.findById(id_note1)).thenReturn(java.util.Optional.of(note1));

        when(noteMapper.noteToNoteDTO(note1)).thenReturn(new NoteDTO());

        NoteDTO deletedNote = noteService.deleteNote(id_note1);

        verify(noteRepository).delete(note1);

        assertFalse(note1.getTags().contains(tag1));
        assertFalse(tag1.getNotes().contains(note1));
    }

    @Test
    void deleteNoteTest_WhenNoteDoesNotExist() {

        when(noteRepository.findById(id_note1)).thenReturn(Optional.empty());

        NoteDTO deletedNote = noteService.deleteNote(id_note1);

        verify(noteRepository).findById(id_note1);
        verifyNoMoreInteractions(noteRepository);
        verifyNoInteractions(noteMapper);

        assertNull(deletedNote);
    }


    @Test
    void validNoteDTO() {
        NoteDTO noteDTO = new NoteDTO();

        noteDTO.setTitle("Valid title");
        noteDTO.setDescription("valid description");

        assertDoesNotThrow(() -> noteService.validate(noteDTO));
    }

    @Test
    void validateNoteDTO_NullTitle() {
        NoteDTO noteDTO = new NoteDTO();

        noteDTO.setDescription("Valid Description");

        MyException exception = assertThrows(MyException.class, () -> noteService.validate(noteDTO));
        assertEquals("Note's title or description can't be null or empty.", exception.getMessage());
    }

    @Test
    void validateNoteDTO_NullDescription() {
        NoteDTO noteDTO = new NoteDTO();

        noteDTO.setTitle("Valid Title");

        MyException exception = assertThrows(MyException.class, () -> noteService.validate(noteDTO));
        assertEquals("Note's title or description can't be null or empty.", exception.getMessage());
    }

    @Test
    void validateNoteDTO_OnlySpacesTitle() {
        NoteDTO noteDTO = new NoteDTO();

        noteDTO.setTitle("   ");
        noteDTO.setDescription("Valid Description");

        MyException exception = assertThrows(MyException.class, () -> noteService.validate(noteDTO));
        assertEquals("Note's title or description can't be null or empty.", exception.getMessage());
    }

    @Test
    void validateNoteDTO_OnlySpacesDescription() {
        NoteDTO noteDTO = new NoteDTO();

        noteDTO.setTitle("Valid Title");
        noteDTO.setDescription("  ");

        MyException exception = assertThrows(MyException.class, () -> noteService.validate(noteDTO));
        assertEquals("Note's title or description can't be null or empty.", exception.getMessage());
    }




    private void repeatedVerifications(NoteDTO result) {

        verify(noteRepository).findById(id_note1);
        verify(tagRepository).findById(id_tag1);
        verifyNoMoreInteractions(noteRepository);
        verifyNoMoreInteractions(tagRepository);
        verifyNoInteractions(noteMapper);

        assertNull(result);
    }


}