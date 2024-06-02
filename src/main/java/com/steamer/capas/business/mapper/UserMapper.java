package com.steamer.capas.business.mapper;

import com.steamer.capas.domain.document.User;
import com.steamer.capas.domain.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import java.util.Base64;
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
    @Mapping(target = "accountEnabled", source = "accountEnabled")
    @Mapping(target = "image", ignore = true) // Ignorar el mapeo de la imagen por ahora
    UserDTO toUserDTO(User user);
    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "userName", source = "user.userName")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "country", source = "user.country")
    @Mapping(target = "lastLogin", source = "user.lastLogin")
    @Mapping(target = "profileVisibility", source = "user.profileVisibility")
    @Mapping(target = "ownedGames", source = "user.ownedGames")
    @Mapping(target = "wishListGames", source = "user.wishListGames")
    @Mapping(target = "isOnline", source = "user.online")
    @Mapping(target = "avatarUrl", source = "user.avatarUrl")
    @Mapping(target = "enableNotifications", source = "user.enableNotifications")
    @Mapping(target = "preferredLanguage", source = "user.preferredLanguage")
    @Mapping(target = "accountEnabled", source = "user.accountEnabled")
    @Mapping(target = "image", expression = "java(com.steamer.capas.business.mapper.UserMapper.bytesToBase64(image))")
    @Mapping(target = "imageMimeType", source = "mimeType")
    UserDTO toUserDTO(User user, byte[] image, String mimeType);

    static String bytesToBase64(byte[] image) {
        return image != null ? Base64.getEncoder().encodeToString(image) : null;
    }
}
