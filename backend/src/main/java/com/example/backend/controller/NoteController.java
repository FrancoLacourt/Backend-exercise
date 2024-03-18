package com.example.backend.controller;

import com.example.backend.dto.NoteDTO;
import com.example.backend.dto.TagDTO;
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
    public ResponseEntity<NoteDTO> createNote(@RequestBody NoteDTO noteDTO) throws MyException {

        if (noteDTO.getTitle() == null || noteDTO.getDescription() == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } else {

            NoteDTO savedNoteDTO = noteService.createNote(noteDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedNoteDTO);
        }
    }

    @GetMapping("/listOfNotes")
    public ResponseEntity<List<NoteDTO>> getNotes() {
        List<NoteDTO> notesDTO = noteService.getEnabledNotes();

        if (notesDTO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(notesDTO);
        }
    }

    @GetMapping("/listOfDisabledNotes")
    public ResponseEntity<List<NoteDTO>> getDisabledNotes() {
        List<NoteDTO> notesDTO = noteService.getDisabledNotes();

        if (notesDTO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(notesDTO);
        }
    }

    @GetMapping("/{id_note}")
    public ResponseEntity<NoteDTO> findNoteById(@PathVariable Long id_note) {

        NoteDTO noteDTO = noteService.findNoteById(id_note);

        if (noteDTO != null) {
            return ResponseEntity.status(HttpStatus.OK).body(noteDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/update/{id_note}")
    public ResponseEntity<NoteDTO> updateNote(@PathVariable Long id_note, @RequestBody NoteDTO updatedNoteDTO) throws MyException {

        NoteDTO noteDTO = noteService.updateNote(id_note, updatedNoteDTO);

        if (updatedNoteDTO.getTitle() == null || updatedNoteDTO.getDescription() == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(noteDTO);
    }

    @PutMapping("/disable/{id_note}")
    public ResponseEntity<NoteDTO> disableNote(@PathVariable Long id_note) {
        NoteDTO noteDTO = noteService.findNoteById(id_note);

        if (noteDTO != null) {
            NoteDTO disabledNoteDTO = noteService.disableNote(id_note);
            return ResponseEntity.status(HttpStatus.OK).body(disabledNoteDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/enable/{id_note}")
    public ResponseEntity<NoteDTO> enableNote(@PathVariable Long id_note) {
        NoteDTO noteDTO = noteService.findNoteById(id_note);

        if (noteDTO != null) {
            NoteDTO enabledNoteDTO = noteService.enableNote(id_note);
            return ResponseEntity.status(HttpStatus.OK).body(enabledNoteDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/addTag/{id_note}")
    public ResponseEntity<NoteDTO> addTagToNote(@PathVariable Long id_note, @RequestParam String tagName) throws MyException {
        NoteDTO noteDTO = noteService.findNoteById(id_note);

        if (noteDTO != null) {
            List<TagDTO> tagsDTO = tagService.getOrCreateTags(Collections.singletonList(tagName));
            Long id_tag = tagsDTO.get(0).getId_tag();
            NoteDTO updatedNoteDTO = noteService.addTagToNote(id_note, id_tag);
            return ResponseEntity.status(HttpStatus.OK).body(updatedNoteDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @PutMapping("/removeTag/{id_note}")
    public ResponseEntity<NoteDTO> removeTagFromNote(@PathVariable Long id_note, @RequestParam String tagName) throws MyException {
        NoteDTO noteDTO = noteService.findNoteById(id_note);

        if (noteDTO != null) {
            List<TagDTO> tagsDTO = tagService.getOrCreateTags(Collections.singletonList(tagName));
            Long id_tag = tagsDTO.get(0).getId_tag();
            NoteDTO updatedNoteDTO = noteService.removeTagFromNote(id_note, id_tag);
            return ResponseEntity.status(HttpStatus.OK).body(updatedNoteDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }



    @DeleteMapping("/delete/{id_note}")
    public ResponseEntity<NoteDTO> deleteNote(@PathVariable Long id_note) {
        NoteDTO noteDTO = noteService.findNoteById(id_note);

        if (noteDTO != null) {
            noteService.deleteNote(id_note);
            return ResponseEntity.status(HttpStatus.OK).body(noteDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}