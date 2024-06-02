package com.steamer.capas.business.service;

import com.steamer.capas.domain.document.User;
import com.steamer.capas.domain.dto.UserDTO;
import com.steamer.capas.domain.dto.request.LoginRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    User create(User user);
    User getById(String id);
    List<User> getAll();
    boolean deleteByUsername(String id);
    UserDTO findByUsername(String username);
    User update(User user);
    void updateEmail(String username, String newEmail);
    boolean enableUser(User user);
}
