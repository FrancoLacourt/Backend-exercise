package com.example.backend.dto.response;

import com.example.backend.entity.Tag;
import lombok.Data;

import java.util.List;

@Data
public class NoteResponseDTO {

    private Long id_note;
    private String title;
    private String description;
    private boolean enabled = true;
    private List<Tag> tags;

}
