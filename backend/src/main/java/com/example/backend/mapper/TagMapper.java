package com.example.backend.mapper;

import com.example.backend.dto.request.TagRequestDTO;
import com.example.backend.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TagMapper {

    TagRequestDTO tagToTagDTO(Tag tag);
    Tag tagDTOToTag(TagRequestDTO tagDTO);
    List<TagRequestDTO> toTagDTOList(List<Tag> tags);
    List<Tag> toTagList(List<TagRequestDTO> tagsDTO);
}
