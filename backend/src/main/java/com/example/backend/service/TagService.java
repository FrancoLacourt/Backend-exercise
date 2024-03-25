package com.example.backend.service;

import com.example.backend.dto.request.NoteDTO;
import com.example.backend.dto.request.TagDTO;
import com.example.backend.exception.MyException;

import java.util.List;

public interface TagService {

    TagDTO createTag(String tagName) throws MyException;
    List<TagDTO> getAllTags();
    TagDTO findTagById(Long id_tag);
    List<TagDTO> getOrCreateTags(List<String> tagNames) throws MyException;
    List<NoteDTO> getNotes(Long id_tag);
    TagDTO updateTag(Long id_tag, String newTagName) throws MyException;
    TagDTO deleteTag(Long id_tag);
}
