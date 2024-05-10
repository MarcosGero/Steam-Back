package com.steamer.capas.business.mapper;
import com.steamer.capas.domain.document.User;
import com.steamer.capas.domain.dto.request.SignUpRequest;
import com.steamer.capas.domain.dto.request.UpdateRequest;
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
    @Mapping(target = "profileVisibility", source = "profileVisibility")
    @Mapping(target = "lastLogin", ignore = true)
    @Mapping(target = "ownedGames", ignore = true)
    @Mapping(target = "wishListGames", ignore = true)
    @Mapping(target = "online", ignore = true)
    @Mapping(target = "avatarUrl", source = "avatarUrl")
    @Mapping(target = "enableNotifications", source = "enableNotifications")
    @Mapping(target = "preferredLanguage", source = "preferredLanguage")
    @Mapping(target = "accountEnabled", ignore = true)
    User toUser(SignUpRequest signUpRequest);

    User toUser(UpdateRequest signUpRequest);
    // Actualizar User existente con datos de UserRequest
    @Mapping(target = "id", ignore = true)
    void updateUserFromRequest(SignUpRequest signUpRequest, @MappingTarget User user);
}
