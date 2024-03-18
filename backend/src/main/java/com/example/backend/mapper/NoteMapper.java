package com.example.backend.mapper;

import com.example.backend.dto.NoteDTO;
import com.example.backend.entity.Note;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NoteMapper {

    NoteDTO noteToNoteDTO(Note note);
    Note noteDTOToNote(NoteDTO noteDTO);
    List<NoteDTO> toNoteDTOList(List<Note> notes);
}
