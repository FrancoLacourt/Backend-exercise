package com.example.backend.dto.response;

import com.example.backend.entity.Note;
import lombok.Data;

import java.util.List;

@Data
public class TagResponseDTO {

    private Long id_tag;
    private String tagName;
    private List<Note> notes;

}
