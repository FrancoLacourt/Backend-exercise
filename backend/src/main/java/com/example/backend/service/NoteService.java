package com.example.backend.service;

import com.example.backend.dto.request.NoteRequestDTO;
import com.example.backend.exception.MyException;

import java.util.List;

public interface NoteService {

    NoteRequestDTO createNote(NoteRequestDTO noteDTO) throws MyException;
    List<NoteRequestDTO> getAllNotes();
    List<NoteRequestDTO> getEnabledNotes();
    List<NoteRequestDTO> getDisabledNotes();
    NoteRequestDTO findNoteById(Long id_note);
    NoteRequestDTO updateNote(Long id_note, NoteRequestDTO updatedNote) throws MyException;
    NoteRequestDTO addTagToNote(Long id_note, Long id_tag);
    NoteRequestDTO removeTagFromNote(Long id_note, Long id_tag);
    NoteRequestDTO disableNote(Long id_note);
    NoteRequestDTO enableNote(Long id_note);
    NoteRequestDTO deleteNote(Long id_note);


}
