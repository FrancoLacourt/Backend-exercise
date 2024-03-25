package com.example.backend.mapper;

import com.example.backend.dto.request.NoteRequestDTO;
import com.example.backend.entity.Note;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NoteMapper {

    NoteRequestDTO noteToNoteDTO(Note note);
    Note noteDTOToNote(NoteRequestDTO noteDTO);
    List<NoteRequestDTO> toNoteDTOList(List<Note> notes);
}
