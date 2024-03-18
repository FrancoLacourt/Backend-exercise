package com.example.backend.dto;

import com.example.backend.entity.Tag;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class NoteDTO {

    private Long id_note;
    private String title;
    private String description;
    private boolean enabled = true;
    private List<String> tagNames;
    private List<Tag> tags;
}
