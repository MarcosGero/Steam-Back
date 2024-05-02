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
        List<String> wishListGames,
        boolean isOnline,
        String avatarUrl,
        boolean enableNotifications,
        String preferredLanguage
) {
    // Los records ya incluyen constructores, getters, equals, hashCode y toString
}