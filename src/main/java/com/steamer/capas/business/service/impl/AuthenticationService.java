package com.steamer.capas.business.service.impl;

import com.steamer.capas.business.mapper.UserRequestMapper;
import com.steamer.capas.business.service.UserService;
import com.steamer.capas.common.exception.UserException;
import com.steamer.capas.domain.document.ConfirmationToken;
import com.steamer.capas.domain.dto.response.AuthenticationResponse;
import com.steamer.capas.domain.document.Role;
import com.steamer.capas.domain.document.User;
import com.steamer.capas.domain.dto.request.LoginRequest;
import com.steamer.capas.domain.dto.request.SignUpRequest;
import com.steamer.capas.persistence.ConfirmationTokenRepository;
import com.steamer.capas.persistence.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final static String USER_NOT_FOUND_MSG =
            "user with email %s not found";

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoderService passwordEncoderService; // Inject here
    private final UserRequestMapper userRequestMapper;
    private final UserService userService;
    private final ConfirmationTokenService confirmationTokenService;
    public AuthenticationResponse register(SignUpRequest request) {
        if (userRepository.existsByUserName(request.getUserName())) {
            throw new UserException(HttpStatus.FORBIDDEN, "Usuario ya registrado");
        }

        User user = userRequestMapper.toUser(request);  // Convert UserRequest to User
        user.setPassword(passwordEncoderService.hashPassword(user.getPassword()));
        user.setRole(Role.USER);
        User savedUser = userService.create(user);      // Save User in the database
        var jwtToken = jwtService.generateToken(savedUser);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                String.format(USER_NOT_FOUND_MSG, email)));
    }

    public String signUpUser(User user) {
        boolean userExists = userRepository
                .findByEmail(user.getEmail())
                .isPresent();
        if (userExists) {
            throw new IllegalStateException("email already taken");
        }
        user.setPassword(passwordEncoderService.hashPassword(user.getPassword()));
        user.setRole(Role.USER);
        User savedUser = userService.create(user);

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                savedUser
        );

        confirmationTokenService.saveConfirmationToken(
                confirmationToken);

        return token;
    }

    // Metodo enableUser(String email) : Usa el repositorio de user

    public boolean isAuthenticated(String authToken){
        return jwtService.isTokenValid(authToken);
    }
    public AuthenticationResponse login(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        System.out.println("username:"+ username);
        System.out.println("password:"+ password);

        User user = userRepository.findByUserName(username); // Busca por email
        if (user == null) {
            throw new UserException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }


        if (!passwordEncoderService.matches(user.getPassword(),password)) {
            throw new UserException(HttpStatus.UNAUTHORIZED, "Contraseña incorrecta");
        }
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
