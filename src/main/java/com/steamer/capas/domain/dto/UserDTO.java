package com.steamer.capas.domain.dto;

import java.util.Date;
import java.util.List;

public record UserDTO (
        String id,
        String userName,
        String email,
        String country,
        Date lastLogin,
        String profileVisibility,
        List<String> ownedGames,
        String image,
        List<String> wishListGames,
        List<String> carritoGames,
        boolean accountEnabled,
        boolean isOnline,
        String avatarUrl,
        String imageMimeType,
        boolean enableNotifications,
        String preferredLanguage
) {
    // Los records ya incluyen constructores, getters, equals, hashCode y toString
}