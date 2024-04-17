package com.example.backend.controller;

import com.example.backend.dto.request.NoteRequestDTO;
import com.example.backend.dto.request.UpdatedNoteRequestDTO;
import com.example.backend.dto.response.NoteResponseDTO;
import com.example.backend.dto.response.TagResponseDTO;
import com.example.backend.dto.response.UserResponseDTO;
import com.example.backend.entity.Note;
import com.example.backend.exception.MyException;
import com.example.backend.repository.NoteRepository;
import com.example.backend.service.NoteService;
import com.example.backend.service.TagService;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("v1/api/note")
public class NoteController {

    private final NoteService noteService;
    private final TagService tagService;
    private final NoteRepository noteRepository;
    private final UserService userService;

    @Autowired
    public NoteController(NoteService noteService, TagService tagService,
                          NoteRepository noteRepository, UserService userService) {
        this.noteService = noteService;
        this.tagService = tagService;
        this.noteRepository = noteRepository;
        this.userService = userService;
    }


    @PostMapping("/create/{id_user}")
    public ResponseEntity<NoteResponseDTO> createNote(@RequestBody NoteRequestDTO noteRequestDTO, @PathVariable Long id_user) throws MyException {

        UserResponseDTO userResponseDTO = userService.findUserById(id_user);

        if (userResponseDTO != null) {

            if (noteRequestDTO.getTitle() == null || noteRequestDTO.getDescription() == null) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            } else {

                NoteResponseDTO savedNoteResponseDTO = noteService.createNote(noteRequestDTO, id_user);
                return ResponseEntity.status(HttpStatus.CREATED).body(savedNoteResponseDTO);
            }

        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }

    @GetMapping("/listOfNotes")
    public ResponseEntity<List<NoteResponseDTO>> getNotes() {
        List<NoteResponseDTO> noteResponseListDTO = noteService.getEnabledNotes();

        if (noteResponseListDTO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(noteResponseListDTO);
        }
    }

    @GetMapping("/listOfTagsByUserNotes/{id_user}")
    public ResponseEntity<List<TagResponseDTO>> getTagsByUserNotes (@PathVariable Long id_user) {

        UserResponseDTO userResponseDTO = userService.findUserById(id_user);

        if (userResponseDTO != null) {

            List<TagResponseDTO> tagResponseListDTO = noteService.getAllTagsByUserNotes(id_user);

            if (tagResponseListDTO.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(tagResponseListDTO);
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/listOfDisabledNotes")
    public ResponseEntity<List<NoteResponseDTO>> getDisabledNotes() {
        List<NoteResponseDTO> noteResponseListDTO = noteService.getDisabledNotes();

        if (noteResponseListDTO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(noteResponseListDTO);
        }
    }

    @GetMapping("/allNotesByUser/{id_user}")
    public ResponseEntity<List<NoteResponseDTO>> getAllNotesByUser(@PathVariable Long id_user) {

        UserResponseDTO userResponseDTO = userService.findUserById(id_user);

        if (userResponseDTO != null) {

            List<NoteResponseDTO> notesByUser = noteService.getAllNotesByUser(id_user);

            if (notesByUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(notesByUser);
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/allEnabledNotesByUser/{id_user}")
    public ResponseEntity<List<NoteResponseDTO>> getAllEnabledNotesByUser(@PathVariable Long id_user) {
        List<NoteResponseDTO> enabledNotesByUser = noteService.getAllEnabledNotesByUser(id_user);

        if (enabledNotesByUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(enabledNotesByUser);
        }
    }

    @GetMapping("/{id_note}")
    public ResponseEntity<NoteResponseDTO> findNoteById(@PathVariable Long id_note) {

        NoteResponseDTO noteResponseDTO = noteService.findNoteById(id_note);

        if (noteResponseDTO != null) {
            return ResponseEntity.status(HttpStatus.OK).body(noteResponseDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/update/{id_note}")
    public ResponseEntity<NoteResponseDTO> updateNote(@PathVariable Long id_note, @RequestBody UpdatedNoteRequestDTO updatedNoteRequestDTO) throws MyException {

        Note note = noteRepository.findById(id_note).orElse(null);

        if (note != null) {

            NoteResponseDTO noteResponseDTO = noteService.updateNote(id_note, updatedNoteRequestDTO);

            if (updatedNoteRequestDTO.getTitle() == null || updatedNoteRequestDTO.getDescription() == null) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }

            return ResponseEntity.status(HttpStatus.OK).body(noteResponseDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/disable/{id_note}")
    public ResponseEntity<NoteResponseDTO> disableNote(@PathVariable Long id_note) {
        NoteResponseDTO noteResponseDTO = noteService.findNoteById(id_note);

        if (noteResponseDTO != null) {
            NoteResponseDTO disabledNoteResponseDTO = noteService.disableNote(id_note);
            return ResponseEntity.status(HttpStatus.OK).body(disabledNoteResponseDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/enable/{id_note}")
    public ResponseEntity<NoteResponseDTO> enableNote(@PathVariable Long id_note) {
        NoteResponseDTO noteResponseDTO = noteService.findNoteById(id_note);

        if (noteResponseDTO != null) {
            NoteResponseDTO enabledNoteResponseDTO = noteService.enableNote(id_note);
            return ResponseEntity.status(HttpStatus.OK).body(enabledNoteResponseDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/addTag/{id_note}")
    public ResponseEntity<NoteResponseDTO> addTagToNote(@PathVariable Long id_note, @RequestParam String tagName) throws MyException {
        NoteResponseDTO noteResponseDTO = noteService.findNoteById(id_note);

        if (noteResponseDTO != null) {
            List<TagResponseDTO> tagResponseListDTO = tagService.getOrCreateTags(Collections.singletonList(tagName));
            Long id_tag = tagResponseListDTO.get(0).getId_tag();
            NoteResponseDTO updatedNoteResponseDTO = noteService.addTagToNote(id_note, id_tag);
            return ResponseEntity.status(HttpStatus.OK).body(updatedNoteResponseDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/removeTag/{id_note}/{id_tag}")
    public ResponseEntity<NoteResponseDTO> removeTagFromNote(@PathVariable Long id_note, @PathVariable Long id_tag) {

        NoteResponseDTO updatedNoteResponseDTO = noteService.removeTagFromNote(id_note, id_tag);

        if (updatedNoteResponseDTO != null) {
            return ResponseEntity.status(HttpStatus.OK).body(updatedNoteResponseDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/delete/{id_note}/{id_user}")
    public ResponseEntity<NoteResponseDTO> deleteNote(@PathVariable Long id_note, @PathVariable Long id_user) {
        NoteResponseDTO noteResponseDTO = noteService.findNoteById(id_note);
        UserResponseDTO userResponseDTO = userService.findUserById(id_user);

        if (noteResponseDTO != null && userResponseDTO != null) {

            NoteResponseDTO deletedNoteResponseDTO = noteService.deleteNote(id_note, id_user);

            if (deletedNoteResponseDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(noteResponseDTO);
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}