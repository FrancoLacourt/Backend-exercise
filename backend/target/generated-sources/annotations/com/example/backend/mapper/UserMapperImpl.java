package com.example.backend.mapper;

import com.example.backend.dto.response.UserResponseDTO;
import com.example.backend.entity.UserEntity;
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
public class UserMapperImpl implements UserMapper {

    @Override
    public UserResponseDTO userToUserResponseDTO(UserEntity user) {
        if ( user == null ) {
            return null;
        }

        UserResponseDTO userResponseDTO = new UserResponseDTO();

        if ( user.getId() != null ) {
            userResponseDTO.setId( user.getId() );
        }
        userResponseDTO.setEmail( user.getEmail() );
        userResponseDTO.setName( user.getName() );
        userResponseDTO.setLastName( user.getLastName() );
        userResponseDTO.setDisplayName( user.getDisplayName() );
        userResponseDTO.setRegistrationDate( user.getRegistrationDate() );

        return userResponseDTO;
    }

    @Override
    public List<UserResponseDTO> toUserResponseListDTO(List<UserEntity> users) {
        if ( users == null ) {
            return null;
        }

        List<UserResponseDTO> list = new ArrayList<UserResponseDTO>( users.size() );
        for ( UserEntity userEntity : users ) {
            list.add( userToUserResponseDTO( userEntity ) );
        }

        return list;
    }
}
