package com.steamer.capas.business.mapper;
import com.steamer.capas.domain.document.User;
import com.steamer.capas.domain.dto.request.UserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserRequestMapper {
    UserRequestMapper INSTANCE = Mappers.getMapper(UserRequestMapper.class);

    // Mapeo de UserRequest a User
    @Mapping(target = "userName", source = "userName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "country", source = "country")
    @Mapping(target = "profileVisibility", ignore = true)  // Assume default or not included in the request
    @Mapping(target = "lastLogin", ignore = true)          // Not set during creation
    @Mapping(target = "ownedGames", ignore = true)         // Empty initially
    @Mapping(target = "wishListGames", ignore = true)      // Empty initially
    @Mapping(target = "online", ignore = true)           // Defaults to false
    @Mapping(target = "avatarUrl", ignore = true)          // Default or not included in request
    @Mapping(target = "enableNotifications", ignore = true)// Assume default
    @Mapping(target = "preferredLanguage", ignore = true)  // Default or set separately
    User toUser(UserRequest userRequest);

    // Actualizar User existente con datos de UserRequest
    @Mapping(target = "id", ignore = true)  // ID should not be overwritten
    void updateUserFromRequest(UserRequest userRequest, @MappingTarget User user);
}
