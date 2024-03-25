package com.example.backend.service.impl;

import com.example.backend.dto.request.NoteRequestDTO;
import com.example.backend.dto.request.TagRequestDTO;
import com.example.backend.entity.Note;
import com.example.backend.entity.Tag;
import com.example.backend.exception.ExceptionMethods;
import com.example.backend.exception.MyException;
import com.example.backend.mapper.NoteMapper;
import com.example.backend.mapper.TagMapper;
import com.example.backend.repository.TagRepository;
import com.example.backend.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {

    private TagRepository tagRepository;
    private TagMapper tagMapper;
    private NoteMapper noteMapper;


    @Autowired
    public TagServiceImpl(TagRepository tagRepository, TagMapper tagMapper, NoteMapper noteMapper) {
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
        this.noteMapper = noteMapper;
    }

    public TagServiceImpl() {
    }

    @Override
    @Transactional
    public TagRequestDTO createTag(String tagName) throws MyException {
        validate(tagName);

        Tag tag = new Tag();
        tag.setTagName(tagName);
        Tag createdTag = tagRepository.save(tag);

        return tagMapper.tagToTagDTO(createdTag);
    }

    @Override
    public List<TagRequestDTO> getAllTags() {
        List<Tag> tags = tagRepository.findAll();
        return tagMapper.toTagDTOList(tags);
    }

    @Override
    public TagRequestDTO findTagById(Long id_tag) {

        Tag tag = tagRepository.findById(id_tag).orElse(null);

        if (tag != null) {
            return tagMapper.tagToTagDTO(tag);
        } else {
            System.out.println("It wasn't possible to find a tag with the ID: " + id_tag);
            return null;
        }

    }

    /*
    If the tag already exists, returns it.
    If the category does not exist, it creates it.
     */
    @Override
    public List<TagRequestDTO> getOrCreateTags(List<String> tagNames) throws MyException {
        List<TagRequestDTO> resultTags = new ArrayList<>();

        for (String tagName : tagNames) {
            Optional<Tag> existingTag = tagRepository.findTagByTagName(tagName);

            if (existingTag.isPresent()) {
                resultTags.add(tagMapper.tagToTagDTO(existingTag.get()));
            } else {
                TagRequestDTO createdTagDTO = createTag(tagName);
                // Guardar la etiqueta antes de agregarla a la lista

                resultTags.add(createdTagDTO);
            }
        }
        return resultTags;
    }
    @Override
    public List<NoteRequestDTO> getNotes(Long id_tag) {

        List<Note> notes = tagRepository.findById(id_tag).get().getNotes();
        return noteMapper.toNoteDTOList(notes);
    }

    @Override
    public TagRequestDTO updateTag(Long id_tag, String newTagName) throws MyException{
        validate(newTagName);

        Tag tag = tagRepository.findById(id_tag).orElse(null);

        if (tag != null) {
            tag.setTagName(newTagName);
            Tag savedTag = tagRepository.save(tag);
            return tagMapper.tagToTagDTO(savedTag);
        } else {
            System.out.println("It wasn't possible to find a tag with the ID: " + id_tag);
            return null;
        }
    }

    @Override
    public TagRequestDTO deleteTag(Long id_tag) {
        Tag tag = tagRepository.findById(id_tag).orElse(null);

        if (tag != null) {
            for (Note note : tag.getNotes()) {
                note.getTags().remove(tag);
            }
            tag.getNotes().clear();

            tagRepository.delete(tag);

            return tagMapper.tagToTagDTO(tag);
        } else {
            System.out.println("It wasn't possible to find a tag with the ID: " + id_tag);
            return null;
        }
    }

    public void validate(String tagName) throws MyException {
        if (tagName == null || ExceptionMethods.onlySpaces(tagName)) {
            throw new MyException("Tag's name can't be null or empty.");
        }
    }
}
