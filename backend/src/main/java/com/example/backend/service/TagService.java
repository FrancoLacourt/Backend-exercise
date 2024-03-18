package com.example.backend.service;

import com.example.backend.dto.NoteDTO;
import com.example.backend.dto.TagDTO;
import com.example.backend.exception.MyException;

import java.util.List;

public interface TagService {

    TagDTO createTag(String tagName) throws MyException;
    List<TagDTO> getAllTags();
    TagDTO findTagById(Long id_tag);
    List<TagDTO> getOrCreateTags(List<String> tagNames) throws MyException;
    List<NoteDTO> getNotes(Long id_tag);
}
