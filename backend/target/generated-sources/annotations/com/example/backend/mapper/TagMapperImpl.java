package com.example.backend.mapper;

import com.example.backend.dto.response.TagResponseDTO;
import com.example.backend.entity.Note;
import com.example.backend.entity.Tag;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-30T19:08:52-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.9 (Eclipse Adoptium)"
)
@Component
public class TagMapperImpl implements TagMapper {

    @Override
    public TagResponseDTO tagToTagResponseDTO(Tag tag) {
        if ( tag == null ) {
            return null;
        }

        TagResponseDTO tagResponseDTO = new TagResponseDTO();

        tagResponseDTO.setId_tag( tag.getId_tag() );
        tagResponseDTO.setTagName( tag.getTagName() );
        List<Note> list = tag.getNotes();
        if ( list != null ) {
            tagResponseDTO.setNotes( new ArrayList<Note>( list ) );
        }

        return tagResponseDTO;
    }

    @Override
    public Tag tagResponseDTOToTag(TagResponseDTO tagResponseDTO) {
        if ( tagResponseDTO == null ) {
            return null;
        }

        Tag tag = new Tag();

        tag.setId_tag( tagResponseDTO.getId_tag() );
        tag.setTagName( tagResponseDTO.getTagName() );
        List<Note> list = tagResponseDTO.getNotes();
        if ( list != null ) {
            tag.setNotes( new ArrayList<Note>( list ) );
        }

        return tag;
    }

    @Override
    public List<TagResponseDTO> toTagResponseListDTO(List<Tag> tags) {
        if ( tags == null ) {
            return null;
        }

        List<TagResponseDTO> list = new ArrayList<TagResponseDTO>( tags.size() );
        for ( Tag tag : tags ) {
            list.add( tagToTagResponseDTO( tag ) );
        }

        return list;
    }

    @Override
    public List<Tag> tagResponseListDTOToTagList(List<TagResponseDTO> tagResponseListDTO) {
        if ( tagResponseListDTO == null ) {
            return null;
        }

        List<Tag> list = new ArrayList<Tag>( tagResponseListDTO.size() );
        for ( TagResponseDTO tagResponseDTO : tagResponseListDTO ) {
            list.add( tagResponseDTOToTag( tagResponseDTO ) );
        }

        return list;
    }
}
