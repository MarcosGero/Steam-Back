package com.steamer.capas.business.mapper;

import com.steamer.capas.domain.document.User;
import com.steamer.capas.domain.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    // Mapeo de la entidad User a UserDTO
    @Mapping(target = "id", source = "id")
    @Mapping(target = "userName", source = "userName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "country", source = "country")
    @Mapping(target = "lastLogin", source = "lastLogin")
    @Mapping(target = "profileVisibility", source = "profileVisibility")
    @Mapping(target = "ownedGames", source = "ownedGames")
    @Mapping(target = "wishListGames", source = "wishListGames")
    @Mapping(target = "isOnline", source = "online")
    @Mapping(target = "avatarUrl", source = "avatarUrl")
    @Mapping(target = "enableNotifications", source = "enableNotifications")
    @Mapping(target = "preferredLanguage", source = "preferredLanguage")
    UserDTO toUserDTO(User user);

}
