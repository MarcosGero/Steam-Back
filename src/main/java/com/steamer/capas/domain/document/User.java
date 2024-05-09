package com.steamer.capas.domain.document;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@Document(collection = "users")
@Entity
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    private String id;  // Cambio a String porque MongoDB usa BSON ObjectId

    @Indexed(name = "meta_userName_index_unique",unique = true)
    private String userName;

    private String email;
    private String country;
    private String password;

    // Datos adicionales comunes en una plataforma de juegos como STEAM
    private Date lastLogin;
    private List<String> friendsList;
    private String profileVisibility;  // Ejemplo: "public", "friends-only", "private"
    private List<String> ownedGames;   // Lista de IDs de juegos que el usuario posee
    private List<String> wishListGames; // Lista de IDs de juegos que el usuario desea
    private boolean isOnline;         // Estado en línea o fuera de línea
    private String avatarUrl;         // URL a la imagen de perfil del usuario

    // Configuración relacionada con la interacción en la comunidad
    private boolean enableNotifications; // Si el usuario recibe notificaciones
    private String preferredLanguage;    // Idioma preferido del usuario

    @Enumerated(EnumType.STRING)
    private Role role;
    public User(String userName, String email, String country,String password) {
        this.userName = userName;
        this.email = email;
        this.country = country;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public List<String> getFriendsList() {
        return friendsList;
    }

    public void setFriendsList(List<String> friendsList) {
        this.friendsList = friendsList;
    }

    public String getProfileVisibility() {
        return profileVisibility;
    }

    public void setProfileVisibility(String profileVisibility) {
        this.profileVisibility = profileVisibility;
    }

    public List<String> getOwnedGames() {
        return ownedGames;
    }

    public void setOwnedGames(List<String> ownedGames) {
        this.ownedGames = ownedGames;
    }

    public List<String> getWishListGames() {
        return wishListGames;
    }

    public void setWishListGames(List<String> wishListGames) {
        this.wishListGames = wishListGames;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public boolean isEnableNotifications() {
        return enableNotifications;
    }

    public void setEnableNotifications(boolean enableNotifications) {
        this.enableNotifications = enableNotifications;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }
}
