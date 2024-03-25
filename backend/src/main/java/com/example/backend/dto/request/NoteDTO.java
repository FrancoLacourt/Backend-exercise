package com.example.backend.dto.request;

import com.example.backend.entity.Tag;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;

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
