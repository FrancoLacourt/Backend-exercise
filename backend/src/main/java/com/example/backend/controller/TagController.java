package com.example.backend.controller;

import com.example.backend.dto.request.NoteRequestDTO;
import com.example.backend.dto.request.TagRequestDTO;
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
    public ResponseEntity<TagRequestDTO> createTag(@RequestParam String tagName) throws MyException {

        if (tagName == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } else {

            TagRequestDTO savedTagDTO = tagService.createTag(tagName);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedTagDTO);
        }
    }

    @GetMapping("/listOfTags")
    public ResponseEntity<List<TagRequestDTO>> getTags() {
        List<TagRequestDTO> tagsDTO = tagService.getAllTags();

        if (tagsDTO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(tagsDTO);
    }

    @GetMapping("/{id_tag}")
    public ResponseEntity<TagRequestDTO> findTagById(@PathVariable Long id_tag) {
        TagRequestDTO tagDTO = tagService.findTagById(id_tag);

        if (tagDTO != null) {
            return ResponseEntity.status(HttpStatus.OK).body(tagDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/getNotesByTag/{id_tag}")
    public ResponseEntity<List<NoteRequestDTO>> getNotesByTag(@PathVariable Long id_tag) {
        List<NoteRequestDTO> notesDTO = tagService.getNotes(id_tag);

        if (notesDTO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(notesDTO);
        }
    }

    @PutMapping("/update/{id_tag}")
    public ResponseEntity<TagRequestDTO> updateTag(@PathVariable Long id_tag, @RequestParam String newTagName) throws MyException {

        TagRequestDTO tagDTO;

        try {
            tagDTO = tagService.updateTag(id_tag, newTagName);
        } catch (MyException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(tagDTO);
    }

    @DeleteMapping("/delete/{id_tag}")
    public ResponseEntity<TagRequestDTO> deleteTag(@PathVariable Long id_tag) {
        TagRequestDTO tagDTO = tagService.findTagById(id_tag);

        if (tagDTO != null) {
            tagService.deleteTag(id_tag);
            return ResponseEntity.status(HttpStatus.OK).body(tagDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}