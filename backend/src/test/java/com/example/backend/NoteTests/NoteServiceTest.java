package com.example.backend.NoteTests;

import com.example.backend.dto.request.NoteRequestDTO;
import com.example.backend.dto.request.TagRequestDTO;
import com.example.backend.dto.request.UpdatedNoteRequestDTO;
import com.example.backend.dto.response.NoteResponseDTO;
import com.example.backend.dto.response.TagResponseDTO;
import com.example.backend.entity.Note;
import com.example.backend.entity.Tag;
import com.example.backend.entity.UserEntity;
import com.example.backend.exception.MyException;
import com.example.backend.mapper.NoteMapper;
import com.example.backend.mapper.TagMapper;
import com.example.backend.repository.NoteRepository;
import com.example.backend.repository.TagRepository;
import com.example.backend.repository.UserRepository;
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
    private UserRepository userRepository;

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
    private TagResponseDTO tagResponseDTO1;
    private TagResponseDTO tagResponseDTO2;
    private NoteResponseDTO noteResponseDTO1;
    private NoteResponseDTO noteResponseDTO2;
    private NoteRequestDTO inputNoteRequestDTO;
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

        inputNoteRequestDTO = new NoteRequestDTO();

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
    }

    @Test
    void testConstructor() {
        NoteServiceImpl noteService = new NoteServiceImpl();
        assertNotNull(noteService);
    }

    @Test
    void createNoteTest() throws MyException {

        Long id_user = 1L;

        UserEntity user = new UserEntity();

        NoteResponseDTO newNoteResponseDTO = new NoteResponseDTO();
        Note newNote = new Note();

        inputNoteRequestDTO.setTitle("Valid title");
        inputNoteRequestDTO.setDescription("Valid description");

        when(tagService.getOrCreateTags(inputNoteRequestDTO.getTagNames())).thenReturn(tagResponseListDTO);
        when(tagMapper.tagResponseListDTOToTagList(tagResponseListDTO)).thenReturn(tags);

        when(noteMapper.noteRequestDTOToNoteResponseDTO(inputNoteRequestDTO)).thenReturn(newNoteResponseDTO);

        newNoteResponseDTO.setTitle(inputNoteRequestDTO.getTitle());
        newNoteResponseDTO.setDescription(inputNoteRequestDTO.getDescription());

        note1.setTitle(newNoteResponseDTO.getTitle());
        note1.setDescription(newNoteResponseDTO.getDescription());
        note1.setTags(tags);
        note1.setEnabled(true);

        when(noteMapper.noteResponseDTOToNote(newNoteResponseDTO)).thenReturn(note1);
        when(noteRepository.save(note1)).thenReturn(newNote);
        when(userRepository.findById(id_user)).thenReturn(Optional.of(user));
        when(noteMapper.noteToNoteResponseDTO(newNote)).thenReturn(new NoteResponseDTO());

        // Llamada al método a probar
            NoteResponseDTO resultNoteResponseDTO = noteService.createNote(inputNoteRequestDTO, id_user);

        verify(tagRepository).saveAll(tags);

        for (Tag tag : tags) {
            assertTrue(tag.getNotes().contains(newNote));
        }

        // Verificaciones
        assertNotNull(resultNoteResponseDTO);
        assertEquals(newNoteResponseDTO.getTitle(), inputNoteRequestDTO.getTitle());
        assertEquals(newNoteResponseDTO.getDescription(), inputNoteRequestDTO.getDescription());
        assertEquals(newNoteResponseDTO.getTags(), note1.getTags());
        assertTrue(newNoteResponseDTO.isEnabled());
        assertTrue(user.getNotes().contains(newNote));

        // Verificación de llamadas a los mocks
        verify(tagService).getOrCreateTags(any());
        verify(tagMapper).tagResponseListDTOToTagList(tagResponseListDTO);
        verify(noteMapper).noteResponseDTOToNote(newNoteResponseDTO);
        verify(noteRepository).save(note1);
        verify(noteMapper).noteToNoteResponseDTO(newNote);
    }

    @Test
    void createNoteTest_WhenUserDoesNotExist() throws MyException {

        Long id_user = 1L;

        UserEntity user = new UserEntity();

        NoteResponseDTO newNoteResponseDTO = new NoteResponseDTO();
        Note newNote = new Note();

        inputNoteRequestDTO.setTitle("Valid title");
        inputNoteRequestDTO.setDescription("Valid description");

        when(tagService.getOrCreateTags(inputNoteRequestDTO.getTagNames())).thenReturn(tagResponseListDTO);
        when(tagMapper.tagResponseListDTOToTagList(tagResponseListDTO)).thenReturn(tags);

        when(noteMapper.noteRequestDTOToNoteResponseDTO(inputNoteRequestDTO)).thenReturn(newNoteResponseDTO);

        newNoteResponseDTO.setTitle(inputNoteRequestDTO.getTitle());
        newNoteResponseDTO.setDescription(inputNoteRequestDTO.getDescription());

        note1.setTitle(newNoteResponseDTO.getTitle());
        note1.setDescription(newNoteResponseDTO.getDescription());
        note1.setTags(tags);
        note1.setEnabled(true);

        when(noteMapper.noteResponseDTOToNote(newNoteResponseDTO)).thenReturn(note1);
        when(noteRepository.save(note1)).thenReturn(newNote);
        when(userRepository.findById(id_user)).thenReturn(Optional.empty());
        when(noteMapper.noteToNoteResponseDTO(newNote)).thenReturn(new NoteResponseDTO());

        // Llamada al método a probar
        NoteResponseDTO resultNoteResponseDTO = noteService.createNote(inputNoteRequestDTO, id_user);

        verify(tagService).getOrCreateTags(any());
        verify(tagMapper).tagResponseListDTOToTagList(tagResponseListDTO);
        verify(noteMapper).noteResponseDTOToNote(newNoteResponseDTO);
        verify(noteRepository).save(note1);
        assertNull(resultNoteResponseDTO);
    }


    @Test
    void getAllNotesTest() {

        when(noteRepository.findAll()).thenReturn(notes);
        when(noteMapper.toNoteResponseDTOList(notes)).thenReturn(noteResponseListDTO);

        List<NoteResponseDTO> collectedNotesResponseDTO = noteService.getAllNotes();

        assertEquals(noteResponseListDTO, collectedNotesResponseDTO);
    }

    @Test
    void getEnabledNotesTest() {

        noteResponseDTO1.setEnabled(true);
        noteResponseDTO2.setEnabled(false);

        when(noteService.getAllNotes()).thenReturn(noteResponseListDTO);

        List<NoteResponseDTO> enabledNoteResponseListDTO = noteService.getEnabledNotes();

        assertEquals(1, enabledNoteResponseListDTO.size());
        assertEquals(enabledNoteResponseListDTO.get(0), noteResponseDTO1);
    }

    @Test
    void getDisabledNotesTest() {

        noteResponseDTO1.setEnabled(true);
        noteResponseDTO2.setEnabled(false);

        when(noteService.getAllNotes()).thenReturn(noteResponseListDTO);

        List<NoteResponseDTO> disabledNoteResponseListDTO = noteService.getDisabledNotes();

        assertEquals(1, disabledNoteResponseListDTO.size());
    }

    @Test
    void findNoteByIdTest() {

        when(noteRepository.findById(id_note1)).thenReturn(Optional.of(note1));
        when(noteMapper.noteToNoteResponseDTO(note1)).thenReturn(noteResponseDTO1);

        NoteResponseDTO collectedNoteResponseDTO = noteService.findNoteById(id_note1);

        assertEquals(id_note1, collectedNoteResponseDTO.getId_note());
    }

    @Test
    void findNoteByIdTest_WhenNoteDoesNotExist() {

        when(noteRepository.findById(id_note1)).thenReturn(Optional.empty());
        NoteResponseDTO collectedNoteResponseDTO = noteService.findNoteById(id_note1);

        assertNull(collectedNoteResponseDTO);
        verify(noteRepository).findById(id_note1);
        verifyNoInteractions(noteMapper);

    }

    @Test
    void updateNoteTest() throws MyException {

        UpdatedNoteRequestDTO updatedNoteRequestDTO = new UpdatedNoteRequestDTO();

        updatedNoteRequestDTO.setTitle("Updated title");
        updatedNoteRequestDTO.setDescription("Updated description");

        when(noteRepository.findById(id_note1)).thenReturn(Optional.of(note1));

        when(noteRepository.save(any(Note.class))).thenReturn(note1);
        when(noteMapper.noteToNoteResponseDTO(any())).thenReturn(new NoteResponseDTO());

        NoteResponseDTO resultNoteDTO = noteService.updateNote(id_note1, updatedNoteRequestDTO);

        verify(noteRepository).save(note1);

        assertEquals(updatedNoteRequestDTO.getTitle(), note1.getTitle());
        assertEquals(updatedNoteRequestDTO.getDescription(), note1.getDescription());
    }

    @Test
    void updateNoteTest_WhenNoteDoesNotExist() throws MyException {

        UpdatedNoteRequestDTO updatedNoteRequestDTO = new UpdatedNoteRequestDTO();

        updatedNoteRequestDTO.setTitle("Updated title");
        updatedNoteRequestDTO.setDescription("Updated description");

        when(noteRepository.findById(id_note1)).thenReturn(Optional.empty());

        NoteResponseDTO resultNoteResponseDTO = noteService.updateNote(id_note1, updatedNoteRequestDTO);

        assertNull(resultNoteResponseDTO);
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
        when(noteMapper.noteToNoteResponseDTO(newNote)).thenReturn(new NoteResponseDTO()); // Mock del mapper

        // Llamada al método bajo prueba
        NoteResponseDTO resultNoteResponseDTO = noteService.addTagToNote(id_note1, id_tag1);

        // Verificación
        assertNotNull(resultNoteResponseDTO); // Verifica que el resultado no sea nulo

        // Verifica que se llamaron los métodos necesarios
        verify(noteRepository).findById(id_note1);
        verify(tagRepository).findById(id_tag1);
        verify(noteRepository).save(newNote);
        verify(tagRepository).save(newTag);
        verify(noteMapper).noteToNoteResponseDTO(newNote);

        assertTrue(newNote.getTags().contains(newTag));
        assertTrue(newTag.getNotes().contains(newNote));
    }

    @Test
    void addTagToNoteTest_WhenNoteDoesNotExist() {

        when(noteRepository.findById(id_note1)).thenReturn(Optional.empty());
        when(tagRepository.findById(id_tag1)).thenReturn(Optional.ofNullable(tag1));

        NoteResponseDTO resultNoteResponseDTO = noteService.addTagToNote(id_note1, id_tag1);

        repeatedVerifications(resultNoteResponseDTO);
    }

    @Test
    void addTagToNoteTest_WhenTagDoesNotExist() {

        when(noteRepository.findById(id_note1)).thenReturn(Optional.ofNullable(note1));
        when(tagRepository.findById(id_tag1)).thenReturn(Optional.empty());

        NoteResponseDTO resultNoteResponseDTO = noteService.addTagToNote(id_note1, id_tag1);

        repeatedVerifications(resultNoteResponseDTO);
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
        when(noteMapper.noteToNoteResponseDTO(newNote)).thenReturn(new NoteResponseDTO()); // Mock del mapper

        // Llamada al método bajo prueba
        NoteResponseDTO resultNoteResponseDTO = noteService.removeTagFromNote(id_note1, id_tag1);

        // Verificación
        assertNotNull(resultNoteResponseDTO); // Verifica que el resultado no sea nulo

        // Verifica que se llamaron los métodos necesarios
        verify(noteRepository).findById(id_note1);
        verify(tagRepository).findById(id_tag1);
        verify(noteRepository).save(newNote);
        verify(tagRepository).save(newTag);
        verify(noteMapper).noteToNoteResponseDTO(newNote);

        assertFalse(newNote.getTags().contains(newTag));
        assertFalse(newTag.getNotes().contains(newNote));
    }

    @Test
    void removeTagFromNote_WhenNoteDoesNotExist() {

        when(noteRepository.findById(id_note1)).thenReturn(Optional.empty());
        when(tagRepository.findById(id_tag1)).thenReturn(Optional.of(tag1));

        NoteResponseDTO resultNoteResponseDTO = noteService.removeTagFromNote(id_note1, id_tag1);

        repeatedVerifications(resultNoteResponseDTO);
    }

    @Test
    void removeTagFromNote_WhenTagDoesNotExist() {

        when(noteRepository.findById(id_note1)).thenReturn(Optional.of(note1));
        when(tagRepository.findById(id_tag1)).thenReturn(Optional.empty());

        NoteResponseDTO resultNoteResponseDTO = noteService.removeTagFromNote(id_note1, id_tag1);

        repeatedVerifications(resultNoteResponseDTO);
    }

    @Test
    void disableNoteTest() {

        note1.setEnabled(true);

        when(noteRepository.findById(id_note1)).thenReturn(java.util.Optional.of(note1));
        when(noteMapper.noteToNoteResponseDTO(any())).thenReturn(new NoteResponseDTO());

        NoteResponseDTO disabledNoteResponseDTO = noteService.disableNote(id_note1);

        verify(noteRepository).save(note1);

        assertFalse(note1.isEnabled());
    }

    @Test
    void disableNoteTest_WhenNoteDoesNotExist() {

        when(noteRepository.findById(id_note1)).thenReturn(Optional.empty());

        NoteResponseDTO disabledNoteResponseDTO = noteService.disableNote(id_note1);

        verify(noteRepository).findById(id_note1);
        verifyNoMoreInteractions(noteRepository);
        verifyNoInteractions(noteMapper);

        assertNull(disabledNoteResponseDTO);
    }

    @Test
    void enableNoteTest() {

        note1.setEnabled(true);

        when(noteRepository.findById(id_note1)).thenReturn(java.util.Optional.of(note1));
        when(noteMapper.noteToNoteResponseDTO(any())).thenReturn(new NoteResponseDTO());

        NoteResponseDTO enabledNoteResponseDTO = noteService.enableNote(id_note1);

        verify(noteRepository).save(note1);

        assertTrue(note1.isEnabled());
    }

    @Test
    void enableNoteTest_WhenNoteDoesNotExist() {

        when(noteRepository.findById(id_note1)).thenReturn(Optional.empty());

        NoteResponseDTO disabledNoteResponseDTO = noteService.enableNote(id_note1);

        verify(noteRepository).findById(id_note1);
        verifyNoMoreInteractions(noteRepository);
        verifyNoInteractions(noteMapper);

        assertNull(disabledNoteResponseDTO);
    }

    @Test
    void deleteNoteTest() {

        when(noteRepository.findById(id_note1)).thenReturn(java.util.Optional.of(note1));

        when(noteMapper.noteToNoteResponseDTO(note1)).thenReturn(new NoteResponseDTO());

        NoteResponseDTO deletedNoteResponseDTO = noteService.deleteNote(id_note1);

        verify(noteRepository).delete(note1);

        assertFalse(note1.getTags().contains(tag1));
        assertFalse(tag1.getNotes().contains(note1));
    }

    @Test
    void deleteNoteTest_WhenNoteDoesNotExist() {

        when(noteRepository.findById(id_note1)).thenReturn(Optional.empty());

        NoteResponseDTO deletedNoteResponseDTO = noteService.deleteNote(id_note1);

        verify(noteRepository).findById(id_note1);
        verifyNoMoreInteractions(noteRepository);
        verifyNoInteractions(noteMapper);

        assertNull(deletedNoteResponseDTO);
    }


    @Test
    void validNoteDTO() {
        NoteRequestDTO noteDTO = new NoteRequestDTO();

        noteDTO.setTitle("Valid title");
        noteDTO.setDescription("valid description");

        assertDoesNotThrow(() -> noteService.validate(noteDTO));
    }

    @Test
    void validateNoteDTO_NullTitle() {
        NoteRequestDTO noteDTO = new NoteRequestDTO();

        noteDTO.setDescription("Valid Description");

        MyException exception = assertThrows(MyException.class, () -> noteService.validate(noteDTO));
        assertEquals("Note's title or description can't be null or empty.", exception.getMessage());
    }

    @Test
    void validateNoteDTO_NullDescription() {
        NoteRequestDTO noteDTO = new NoteRequestDTO();

        noteDTO.setTitle("Valid Title");

        MyException exception = assertThrows(MyException.class, () -> noteService.validate(noteDTO));
        assertEquals("Note's title or description can't be null or empty.", exception.getMessage());
    }

    @Test
    void validateNoteDTO_OnlySpacesTitle() {
        NoteRequestDTO noteDTO = new NoteRequestDTO();

        noteDTO.setTitle("   ");
        noteDTO.setDescription("Valid Description");

        MyException exception = assertThrows(MyException.class, () -> noteService.validate(noteDTO));
        assertEquals("Note's title or description can't be null or empty.", exception.getMessage());
    }

    @Test
    void validateNoteDTO_OnlySpacesDescription() {
        NoteRequestDTO noteDTO = new NoteRequestDTO();

        noteDTO.setTitle("Valid Title");
        noteDTO.setDescription("  ");

        MyException exception = assertThrows(MyException.class, () -> noteService.validate(noteDTO));
        assertEquals("Note's title or description can't be null or empty.", exception.getMessage());
    }




    private void repeatedVerifications(NoteResponseDTO resultNoteResponseDTO) {

        verify(noteRepository).findById(id_note1);
        verify(tagRepository).findById(id_tag1);
        verifyNoMoreInteractions(noteRepository);
        verifyNoMoreInteractions(tagRepository);
        verifyNoInteractions(noteMapper);

        assertNull(resultNoteResponseDTO);
    }


}