package com.example.backend.dto.request;

import com.example.backend.entity.Tag;
import lombok.Data;

import java.util.List;

@Data
public class NoteRequestDTO {

    private String title;
    private String description;
    private List<String> tagNames;
}
