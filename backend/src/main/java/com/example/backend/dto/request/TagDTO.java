package com.example.backend.dto.request;

import com.example.backend.entity.Note;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
public class TagDTO {

    private Long id_tag;
    private String tagName;
    private List<Note> notes;
}