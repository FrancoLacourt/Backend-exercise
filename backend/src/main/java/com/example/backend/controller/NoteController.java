package com.example.backend.controller;

import com.example.backend.dto.request.NoteRequestDTO;
import com.example.backend.dto.request.TagRequestDTO;
import com.example.backend.dto.response.NoteResponseDTO;
import com.example.backend.dto.response.TagResponseDTO;
import com.example.backend.exception.MyException;
import com.example.backend.service.NoteService;
import com.example.backend.service.TagService;
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

    @Autowired
    public NoteController(NoteService noteService, TagService tagService) {
        this.noteService = noteService;
        this.tagService = tagService;
    }


    @PostMapping("/create")
    public ResponseEntity<NoteResponseDTO> createNote(@RequestBody NoteRequestDTO noteRequestDTO) throws MyException {

        if (noteRequestDTO.getTitle() == null || noteRequestDTO.getDescription() == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } else {

            NoteResponseDTO savedNoteResponseDTO = noteService.createNote(noteRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedNoteResponseDTO);
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

    @GetMapping("/listOfDisabledNotes")
    public ResponseEntity<List<NoteResponseDTO>> getDisabledNotes() {
        List<NoteResponseDTO> noteResponseListDTO = noteService.getDisabledNotes();

        if (noteResponseListDTO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(noteResponseListDTO);
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
    public ResponseEntity<NoteResponseDTO> updateNote(@PathVariable Long id_note, @RequestBody NoteRequestDTO updatedNoteRequestDTO) throws MyException {

        NoteResponseDTO noteResponseDTO = noteService.updateNote(id_note, updatedNoteRequestDTO);

        if (updatedNoteRequestDTO.getTitle() == null || updatedNoteRequestDTO.getDescription() == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(noteResponseDTO);
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

    @DeleteMapping("/delete/{id_note}")
    public ResponseEntity<NoteResponseDTO> deleteNote(@PathVariable Long id_note) {
        NoteResponseDTO noteResponseDTO = noteService.findNoteById(id_note);

        if (noteResponseDTO != null) {
            noteService.deleteNote(id_note);
            return ResponseEntity.status(HttpStatus.OK).body(noteResponseDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}