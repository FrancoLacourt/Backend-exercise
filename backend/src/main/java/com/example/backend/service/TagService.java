package com.example.backend.service;

import com.example.backend.dto.response.NoteResponseDTO;
import com.example.backend.dto.response.TagResponseDTO;
import com.example.backend.exception.MyException;

import java.util.List;

public interface TagService {

    TagResponseDTO createTag(String tagName) throws MyException;
    List<TagResponseDTO> getAllTags();
    TagResponseDTO findTagById(Long id_tag);
    List<TagResponseDTO> getOrCreateTags(List<String> tagNames) throws MyException;
    List<NoteResponseDTO> getNotes(Long id_tag);
    TagResponseDTO updateTag(Long id_tag, String newTagName) throws MyException;
    TagResponseDTO deleteTag(Long id_tag);
}
