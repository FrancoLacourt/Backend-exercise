package com.example.backend.mapper;

import com.example.backend.dto.request.TagDTO;
import com.example.backend.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TagMapper {

    TagDTO tagToTagDTO(Tag tag);
    Tag tagDTOToTag(TagDTO tagDTO);
    List<TagDTO> toTagDTOList(List<Tag> tags);
    List<Tag> toTagList(List<TagDTO> tagsDTO);
}
