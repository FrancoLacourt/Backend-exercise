package com.example.backend.service;

import com.example.backend.dto.request.NoteRequestDTO;
import com.example.backend.dto.request.TagRequestDTO;
import com.example.backend.exception.MyException;

import java.util.List;

public interface TagService {

    TagRequestDTO createTag(String tagName) throws MyException;
    List<TagRequestDTO> getAllTags();
    TagRequestDTO findTagById(Long id_tag);
    List<TagRequestDTO> getOrCreateTags(List<String> tagNames) throws MyException;
    List<NoteRequestDTO> getNotes(Long id_tag);
    TagRequestDTO updateTag(Long id_tag, String newTagName) throws MyException;
    TagRequestDTO deleteTag(Long id_tag);
}
