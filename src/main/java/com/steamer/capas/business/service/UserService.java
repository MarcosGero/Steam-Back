package com.steamer.capas.business.service;

import com.steamer.capas.domain.document.User;
import com.steamer.capas.domain.dto.UserDTO;
import com.steamer.capas.domain.dto.request.LoginRequest;

import java.util.List;

public interface UserService {
    User create(User user);
    User getById(Long id);
    List<User> getAll();
    void deleteById(Long id);
    User update(User user);
    UserDTO login(LoginRequest loginRequest);
}
