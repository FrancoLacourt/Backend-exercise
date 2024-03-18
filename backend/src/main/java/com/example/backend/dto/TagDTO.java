package com.example.backend.dto;

import com.example.backend.entity.Note;
import lombok.Data;

import java.util.List;

@Data
public class TagDTO {

    private Long id_tag;
    private String tagName;
    private List<Note> notes;
}