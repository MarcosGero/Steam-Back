package com.steamer.capas.business.facade.impl;

import com.steamer.capas.business.facade.UserFacade;
import com.steamer.capas.business.mapper.UserMapper;
import com.steamer.capas.business.mapper.UserRequestMapper;
import com.steamer.capas.business.service.impl.AuthenticationService;
import com.steamer.capas.business.service.UserService;
import com.steamer.capas.business.service.impl.CompraService;
import com.steamer.capas.domain.document.Game;
import com.steamer.capas.domain.dto.request.UpdateRequest;
import com.steamer.capas.domain.dto.response.AuthenticationResponse;
import com.steamer.capas.domain.dto.UserDTO;
import com.steamer.capas.domain.dto.request.LoginRequest;
import com.steamer.capas.domain.dto.request.SignUpRequest;
import com.steamer.capas.domain.document.User;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserFacadeImpl implements UserFacade {

    private final UserService userService;
    private final UserRequestMapper userRequestMapper;
    private final UserMapper userMapper;
    private final AuthenticationService authService;
    private final CompraService compraService;

    @Override
    public void signUp(SignUpRequest request) {
        authService.register(request);
    }
    @Override
    public boolean deleteByUsername(String id) {
        return userService.deleteByUsername(id);
    }

    @Override
    public UserDTO findByUsername(String username) {
        return userService.findByUsername(username);
    }

    @Override
    public UserDTO update(UpdateRequest request, String id) {
        User user = userRequestMapper.toUser(request);
        user.setId(id);
        User updatedUser = userService.update(user);
        return userMapper.toUserDTO(updatedUser);
    }
    @Override
    public void updateEmail(String username, String newEmail) {
        userService.updateEmail(username, newEmail);
    }
    @Override
    public AuthenticationResponse login(LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @Override
    public boolean checkAuth(String authToken) {
        return authService.isAuthenticated(authToken);
    }

    @Override
    public void asociarImagenAlUsuario(String userName, MultipartFile file) throws IOException {
        userService.asociarImagenAlUsuario(userName,file);
    }

    @Override
    public List<Game> getOwnedGames(String username) {
        return compraService.getOwnedGames(username);
    }

    // -------------------------------------------------------------------


    @Override
    public UserDTO getById(String id) {
        User user = userService.getById(id);
        if (user != null) {
            return userMapper.toUserDTO(user);
        }
        return null;
    }

    @Override
    public List<UserDTO> getAll() {
        List<User> users = userService.getAll();
        return users.stream()
                .map(userMapper::toUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean addGameToCarrito(String username, String gameId) {
            return compraService.addGameToCarrito(username,gameId);
    }
    @Override
    public List<Game> getCarritoGames(String username) {
        return compraService.getCarritoGames(username);

    }
    @Override
    public void clearCarritoGames(String username) {
        compraService.clearCarritoGames(username);

    }
    @Override
    public boolean removeGameFromCarrito(String username, String gameId) {
        return compraService.removeGameFromCarrito(username,gameId);
    }
    public boolean purchase(String username, double totalPrice) {
        return compraService.purchaseGame(username, totalPrice);
    }
}

