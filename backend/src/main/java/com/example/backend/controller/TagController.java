package com.example.backend.controller;

import com.example.backend.dto.NoteDTO;
import com.example.backend.dto.TagDTO;
import com.example.backend.exception.MyException;
import com.example.backend.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/api/tag")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping("/create")
    public ResponseEntity<TagDTO> createTag(@RequestParam String tagName) throws MyException {

        if (tagName == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } else {

            TagDTO savedTagDTO = tagService.createTag(tagName);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedTagDTO);
        }
    }

    @GetMapping("/listOfTags")
    public ResponseEntity<List<TagDTO>> getTags() {
        List<TagDTO> tagsDTO = tagService.getAllTags();

        if (tagsDTO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(tagsDTO);
    }

    @GetMapping("/{id_tag}")
    public ResponseEntity<TagDTO> findTagById(@PathVariable Long id_tag) {
        TagDTO tagDTO = tagService.findTagById(id_tag);

        if (tagDTO != null) {
            return ResponseEntity.status(HttpStatus.OK).body(tagDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/getNotesByTag/{id_tag}")
    public ResponseEntity<List<NoteDTO>> getNotesByTag(@PathVariable Long id_tag) {
        List<NoteDTO> notesDTO = tagService.getNotes(id_tag);

        if (notesDTO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(notesDTO);
        }
    }
}