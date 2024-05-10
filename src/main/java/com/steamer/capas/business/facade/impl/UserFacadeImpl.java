package com.steamer.capas.business.facade.impl;

import com.steamer.capas.business.facade.UserFacade;
import com.steamer.capas.business.mapper.UserMapper;
import com.steamer.capas.business.mapper.UserRequestMapper;
import com.steamer.capas.business.service.impl.AuthenticationService;
import com.steamer.capas.business.service.UserService;
import com.steamer.capas.domain.dto.request.UpdateRequest;
import com.steamer.capas.domain.dto.response.AuthenticationResponse;
import com.steamer.capas.domain.dto.UserDTO;
import com.steamer.capas.domain.dto.request.LoginRequest;
import com.steamer.capas.domain.dto.request.SignUpRequest;
import com.steamer.capas.domain.document.User;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserFacadeImpl implements UserFacade {

    private final UserService userService;
    private final UserRequestMapper userRequestMapper;
    private final UserMapper userMapper;
    private final AuthenticationService authService;

    @Override
    public void signUp(SignUpRequest request) {
        authService.register(request);
    }
    @Override
    public boolean deleteByUsername(String id) {
        return userService.deleteByUsername(id);                     // Delete User by name
    }

    @Override
    public UserDTO findByUsername(String username) {
        return userService.findByUsername(username);
    }

    @Override
    public UserDTO update(UpdateRequest request, String id) {
        User user = userRequestMapper.toUser(request);  // Convert UserRequest to User
        user.setId(id);                                 // Set ID of the User to ensure correct update
        User updatedUser = userService.update(user);    // Update User in the database
        return userMapper.toUserDTO(updatedUser);       // Convert updated User to UserDTO
    }

    @Override
    public AuthenticationResponse login(LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @Override
    public boolean checkAuth(String authToken) {
        return authService.isAuthenticated(authToken);
    }

    // -------------------------------------------------------------------


    @Override
    public UserDTO getById(String id) {
        User user = userService.getById(id);            // Retrieve User by ID
        if (user != null) {
            return userMapper.toUserDTO(user);          // Convert User to UserDTO
        }
        return null;                                    // Return null if user is not found (could throw exception)
    }

    @Override
    public List<UserDTO> getAll() {
        List<User> users = userService.getAll();        // Retrieve all Users
        return users.stream()
                .map(userMapper::toUserDTO)        // Convert each User to UserDTO
                .collect(Collectors.toList());     // Collect results into a List
    }
}

