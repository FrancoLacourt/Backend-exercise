package com.example.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Data
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_tag;

    private String tagName;

    @ManyToMany
    @JoinTable(
            name = "note_tag",
            joinColumns = @JoinColumn(name = "tag_id"),
            inverseJoinColumns = @JoinColumn(name = "note_id"))
    @JsonIgnore
    private List<Note> notes = new ArrayList<>();

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id_tag +
                ", tagName='" + tagName + '\'' +
                '}';
    }


}
