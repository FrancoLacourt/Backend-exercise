package com.example.backend.mapper;

import com.example.backend.dto.request.TagRequestDTO;
import com.example.backend.dto.response.TagResponseDTO;
import com.example.backend.entity.Tag;
import org.hibernate.usertype.LoggableUserType;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TagMapper {

    TagResponseDTO tagToTagResponseDTO(Tag tag);
    Tag tagResponseDTOToTag(TagResponseDTO tagResponseDTO);
    List<TagResponseDTO> toTagResponseListDTO(List <Tag> tags);
    List<Tag> tagResponseListDTOToTagList(List<TagResponseDTO> tagResponseListDTO);
}