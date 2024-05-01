package com.steamer.capas.business.service.impl;

import com.steamer.capas.business.service.UserService;
import com.steamer.capas.domain.document.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Transactional
public class UserServiceImpl implements UserService {
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
}
