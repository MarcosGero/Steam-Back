package com.steamer.capas.business.facade.impl;

import com.steamer.capas.business.facade.UserFacade;
import com.steamer.capas.business.mapper.UserMapper;
import com.steamer.capas.business.mapper.UserRequestMapper;
import com.steamer.capas.business.service.UserService;
import com.steamer.capas.domain.dto.UserDTO;
import com.steamer.capas.domain.dto.request.UserRequest;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserFacadeImpl implements UserFacade {

    private final UserService userService;
    private final UserRequestMapper userRequestMapper;
    private final UserMapper userMapper;
    public UserFacadeImpl(UserService userService, UserRequestMapper userRequestMapper, UserMapper userMapper) {
        this.userService = userService;
        this.userRequestMapper = userRequestMapper;
        this.userMapper = userMapper;
    }

    @Override
    public UserDTO createNew(UserRequest request) {
        return null;
    }

    @Override
    public UserDTO getById(Long id) {
        return null;
    }

    @Override
    public List<UserDTO> getAll() {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public UserDTO update(UserRequest request, Long id) {
        return null;
    }
}
