package com.example.backend.service;

import com.example.backend.dto.request.NoteRequestDTO;
import com.example.backend.dto.request.UpdatedNoteRequestDTO;
import com.example.backend.dto.response.NoteResponseDTO;
import com.example.backend.exception.MyException;

import java.util.List;

public interface NoteService {

    NoteResponseDTO createNote(NoteRequestDTO noteDTO) throws MyException;
    List<NoteResponseDTO> getAllNotes();
    List<NoteResponseDTO> getEnabledNotes();
    List<NoteResponseDTO> getDisabledNotes();
    NoteResponseDTO findNoteById(Long id_note);
    NoteResponseDTO updateNote(Long id_note, UpdatedNoteRequestDTO updatedNoteRequestDTO) throws MyException;
    NoteResponseDTO addTagToNote(Long id_note, Long id_tag);
    NoteResponseDTO removeTagFromNote(Long id_note, Long id_tag);
    NoteResponseDTO disableNote(Long id_note);
    NoteResponseDTO enableNote(Long id_note);
    NoteResponseDTO deleteNote(Long id_note);


}
