package com.example.backend.mapper;

import com.example.backend.dto.request.NoteRequestDTO;
import com.example.backend.dto.response.NoteResponseDTO;
import com.example.backend.entity.Note;
import com.example.backend.entity.Tag;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-07T15:42:28-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.9 (Eclipse Adoptium)"
)
@Component
public class NoteMapperImpl implements NoteMapper {

    @Override
    public NoteResponseDTO noteToNoteResponseDTO(Note note) {
        if ( note == null ) {
            return null;
        }

        NoteResponseDTO noteResponseDTO = new NoteResponseDTO();

        noteResponseDTO.setId_note( note.getId_note() );
        noteResponseDTO.setTitle( note.getTitle() );
        noteResponseDTO.setDescription( note.getDescription() );
        noteResponseDTO.setEnabled( note.isEnabled() );
        List<Tag> list = note.getTags();
        if ( list != null ) {
            noteResponseDTO.setTags( new ArrayList<Tag>( list ) );
        }

        return noteResponseDTO;
    }

    @Override
    public Note noteResponseDTOToNote(NoteResponseDTO noteResponseDTO) {
        if ( noteResponseDTO == null ) {
            return null;
        }

        Note note = new Note();

        note.setId_note( noteResponseDTO.getId_note() );
        note.setTitle( noteResponseDTO.getTitle() );
        note.setDescription( noteResponseDTO.getDescription() );
        note.setEnabled( noteResponseDTO.isEnabled() );
        List<Tag> list = noteResponseDTO.getTags();
        if ( list != null ) {
            note.setTags( new ArrayList<Tag>( list ) );
        }

        return note;
    }

    @Override
    public NoteResponseDTO noteRequestDTOToNoteResponseDTO(NoteRequestDTO noteRequestDTO) {
        if ( noteRequestDTO == null ) {
            return null;
        }

        NoteResponseDTO noteResponseDTO = new NoteResponseDTO();

        noteResponseDTO.setTitle( noteRequestDTO.getTitle() );
        noteResponseDTO.setDescription( noteRequestDTO.getDescription() );

        return noteResponseDTO;
    }

    @Override
    public List<NoteResponseDTO> toNoteResponseDTOList(List<Note> notes) {
        if ( notes == null ) {
            return null;
        }

        List<NoteResponseDTO> list = new ArrayList<NoteResponseDTO>( notes.size() );
        for ( Note note : notes ) {
            list.add( noteToNoteResponseDTO( note ) );
        }

        return list;
    }
}
