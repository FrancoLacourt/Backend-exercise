package com.example.backend.dto.request;

import com.example.backend.entity.Note;
import lombok.Data;

import java.util.List;

@Data
public class TagRequestDTO {

    private String tagName;
}