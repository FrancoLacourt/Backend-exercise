package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_note;

    private String title;
    private String description;
    private boolean enabled = true;

    @ManyToMany(mappedBy = "notes")
    private List<Tag> tags = new ArrayList<>();
}