package com.steamer.capas.business.service.impl;

import com.steamer.capas.business.service.UserService;
import com.steamer.capas.common.exception.UserException;
import com.steamer.capas.domain.document.User;
import com.steamer.capas.persistence.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}
