package com.steamer.capas.business.service.impl;

import ch.qos.logback.core.boolex.Matcher;
import com.steamer.capas.business.service.PasswordEncoderService;
import com.steamer.capas.business.service.UserService;
import com.steamer.capas.common.exception.UserException;
import com.steamer.capas.domain.document.User;
import com.steamer.capas.domain.dto.UserDTO;
import com.steamer.capas.domain.dto.request.LoginRequest;
import com.steamer.capas.persistence.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoderService passwordEncoderService; // Inject here

    public UserServiceImpl(UserRepository userRepository, PasswordEncoderService passwordEncoderService) {
        this.userRepository = userRepository;
        this.passwordEncoderService = passwordEncoderService;
    }
    @Override
    public User create(User user) {
        return userRepository.save(user);
    }


    @Override
    public User getById(String id) {
        var optionalUser = userRepository.findById(Long.valueOf(id));

        //Implementar String.format(UserConstant.TASK_NOT_FOUND_MESSAGE_ERROR, id)
        if (optionalUser.isEmpty()){
            throw new UserException(HttpStatus.NOT_FOUND,"Error getById");
        }


        return optionalUser.get();
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public void deleteById(String id) {
        userRepository.deleteById(Long.valueOf(id));
    }

    @Override
    public User update(User user) {
        return userRepository.save(user);
    }

    @Override
    public UserDTO login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        System.out.println("email:"+ email);
        System.out.println("password:"+ password);

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
