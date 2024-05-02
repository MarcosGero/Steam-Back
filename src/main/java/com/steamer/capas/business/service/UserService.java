package com.steamer.capas.business.service;

import com.steamer.capas.domain.document.User;

import java.util.List;

public interface UserService {
    User create(User user);
    User getById(String id);
    List<User> getAll();
    void deleteById(String id);
    User update(User user);
}
