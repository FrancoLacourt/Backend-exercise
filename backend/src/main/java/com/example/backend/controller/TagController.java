package com.example.backend.controller;

import com.example.backend.dto.request.NoteRequestDTO;
import com.example.backend.dto.request.TagRequestDTO;
import com.example.backend.dto.response.NoteResponseDTO;
import com.example.backend.dto.response.TagResponseDTO;
import com.example.backend.entity.Tag;
import com.example.backend.exception.MyException;
import com.example.backend.repository.TagRepository;
import com.example.backend.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/api/tag")
public class TagController {

    private final TagService tagService;
    private final TagRepository tagRepository;

    public TagController(TagService tagService,
                         TagRepository tagRepository) {
        this.tagService = tagService;
        this.tagRepository = tagRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<TagResponseDTO> createTag(@RequestParam String tagName) throws MyException {

        if (tagName == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } else {

            TagResponseDTO savedTagResponseDTO = tagService.createTag(tagName);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedTagResponseDTO);
        }
    }

    @GetMapping("/listOfTags")
    public ResponseEntity<List<TagResponseDTO>> getTags() {

        List<TagResponseDTO> tagResponseListDTO = tagService.getAllTags();

        if (tagResponseListDTO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(tagResponseListDTO);
    }

    @GetMapping("/{id_tag}")
    public ResponseEntity<TagResponseDTO> findTagById(@PathVariable Long id_tag) {

        TagResponseDTO tagResponseDTO = tagService.findTagById(id_tag);

        if (tagResponseDTO != null) {
            return ResponseEntity.status(HttpStatus.OK).body(tagResponseDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/getNotesByTag/{id_tag}")
    public ResponseEntity<List<NoteResponseDTO>> getNotesByTag(@PathVariable Long id_tag) {

        Tag tag = tagRepository.findById(id_tag).orElse(null);

        if (tag != null) {

            List<NoteResponseDTO> noteResponseListDTO = tagService.getNotes(id_tag);

            if (noteResponseListDTO.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(noteResponseListDTO);
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/update/{id_tag}")
    public ResponseEntity<TagResponseDTO> updateTag(@PathVariable Long id_tag, @RequestParam String newTagName) throws MyException {

        TagResponseDTO tagResponseDTO;

        try {
            tagResponseDTO = tagService.updateTag(id_tag, newTagName);
        } catch (MyException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(tagResponseDTO);
    }

    @DeleteMapping("/delete/{id_tag}")
    public ResponseEntity<TagResponseDTO> deleteTag(@PathVariable Long id_tag) {
        TagResponseDTO tagResponseDTO = tagService.findTagById(id_tag);

        if (tagResponseDTO != null) {
            tagService.deleteTag(id_tag);
            return ResponseEntity.status(HttpStatus.OK).body(tagResponseDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}