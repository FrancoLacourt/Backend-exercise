package com.example.backend.mapper;

import com.example.backend.dto.request.NoteRequestDTO;
import com.example.backend.dto.request.UpdatedNoteRequestDTO;
import com.example.backend.dto.response.NoteResponseDTO;
import com.example.backend.entity.Note;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NoteMapper {

    NoteResponseDTO noteToNoteResponseDTO(Note note);
    Note noteResponseDTOToNote(NoteResponseDTO noteResponseDTO);
    NoteResponseDTO noteRequestDTOToNoteResponseDTO(NoteRequestDTO noteRequestDTO);
    List<NoteResponseDTO> toNoteResponseDTOList(List<Note> notes);
}