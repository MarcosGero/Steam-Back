package com.steamer.capas.business.service.impl;

import com.steamer.capas.business.service.PasswordEncoderService;
import com.steamer.capas.business.service.UserService;
import com.steamer.capas.domain.document.User;
<<<<<<< Updated upstream
import jakarta.transaction.Transactional;
=======
import com.steamer.capas.domain.dto.UserDTO;
import com.steamer.capas.domain.dto.request.LoginRequest;
import com.steamer.capas.persistence.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
>>>>>>> Stashed changes
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
<<<<<<< Updated upstream
=======

    private final UserRepository userRepository;
    private final PasswordEncoderService passwordEncoderService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoderService passwordEncoderService) {
        this.userRepository = userRepository;
        this.passwordEncoderService = passwordEncoderService;
    }
>>>>>>> Stashed changes
    @Override
    public User create(User user) {
        return null;
    }

    @Override
    public User getById(Long id) {
        return null;
    }

    @Override
    public List<User> getAll() {
        return null;
    }

    @Override
    public void deleteById(Long id) {}
    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public UserDTO login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        User user = userRepository.findByEmail(email); // Busca por email
        if (user == null) {
            throw new UserException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        if (!passwordEncoderService.matches(password, user.getPassword())) {
            throw new UserException(HttpStatus.UNAUTHORIZED, "Contraseña incorrecta");
        }

        String country = user.getCountry();
        Date lastLogin = user.getLastLogin();
        String profileVisibility = user.getProfileVisibility();
        List<String> ownedGames = user.getOwnedGames();
        List<String> wishListGames = user.getWishListGames();
        boolean isOnline = user.isOnline();
        String avatarUrl = user.getAvatarUrl();
        boolean enableNotifications = user.isEnableNotifications();
        String preferredLanguage = user.getPreferredLanguage();

        return new UserDTO(user.getId(), user.getUserName(), user.getEmail(), country,
                lastLogin, profileVisibility, ownedGames, wishListGames, isOnline,
                avatarUrl, enableNotifications, preferredLanguage);
    }

}
