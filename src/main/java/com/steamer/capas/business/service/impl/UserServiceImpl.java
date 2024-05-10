package com.steamer.capas.business.service.impl;

import com.mongodb.DuplicateKeyException;
import com.steamer.capas.business.mapper.UserMapper;
import com.steamer.capas.business.service.UserService;
import com.steamer.capas.common.exception.UserException;
import com.steamer.capas.domain.document.User;
import com.steamer.capas.domain.dto.UserDTO;
import com.steamer.capas.persistence.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Override
    public User create(User user) {
        try {
            return userRepository.save(user);
        } catch (DuplicateKeyException e) {
            // Maneja la excepción si el userName ya existe
            throw new ServiceException("El nombre de usuario ya está en uso. Por favor, elige otro.");
        }
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
    public boolean deleteByUsername(String username) {
        if (userRepository.existsByUserName(username)) {
            userRepository.deleteByUserName(username);  // Perform the deletion
        } else {
            throw new UserException(HttpStatus.NOT_FOUND,"User not found with username: " + username);
        }
        return true;
    }
    public UserDTO findByUsername(String username){
        if (userRepository.existsByUserName(username)) {
            return userMapper.toUserDTO(userRepository.findByUserName(username));
        }else {
            throw new UserException(HttpStatus.NOT_FOUND, "User not found with username: " + username);
        }
    }
    @Override
    public User update(User user) {
        return userRepository.save(user);
    }

}
