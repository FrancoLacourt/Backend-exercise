package com.example.backend.service;

import com.example.backend.dto.NoteDTO;
import com.example.backend.exception.MyException;

import java.util.List;

public interface NoteService {

    NoteDTO createNote(NoteDTO noteDTO) throws MyException;
    List<NoteDTO> getAllNotes();
    List<NoteDTO> getEnabledNotes();
    List<NoteDTO> getDisabledNotes();
    NoteDTO findNoteById(Long id_note);
    NoteDTO updateNote(Long id_note, NoteDTO updatedNote) throws MyException;
    NoteDTO addTagToNote(Long id_note, Long id_tag);
    NoteDTO removeTagFromNote(Long id_note, Long id_tag);
    NoteDTO disableNote(Long id_note);
    NoteDTO enableNote(Long id_note);
    NoteDTO deleteNote(Long id_note);


}
