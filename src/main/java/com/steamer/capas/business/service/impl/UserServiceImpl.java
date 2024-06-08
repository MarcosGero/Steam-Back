package com.steamer.capas.business.service.impl;

import com.mongodb.DuplicateKeyException;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.steamer.capas.business.mapper.UserMapper;
import com.steamer.capas.business.service.UserService;
import com.steamer.capas.common.exception.UserException;
import com.steamer.capas.domain.document.User;
import com.steamer.capas.domain.dto.UserDTO;
import com.steamer.capas.persistence.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ImageService imageService;
    private final GridFsTemplate gridFsTemplate;
    @Override
    public boolean enableUser(User user) {

        Query query = new Query(Criteria.where("id").is(user.getId()));
        Update update = new Update();
        update.set("accountEnabled", true);
        try {
            mongoTemplate.updateFirst(query, update, User.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void updateEmail(String username, String newEmail) {
        User user = userRepository.findByUserName(username);
        user.setEmail(newEmail);
        userRepository.save(user);
    }
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
        var optionalUser = userRepository.findById(id);

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
            userRepository.deleteByUserName(username);
        } else {
            throw new UserException(HttpStatus.NOT_FOUND,"User not found with username: " + username);
        }
        return true;
    }
    @Override
    public UserDTO findByUsername(String username) {
        User user = userRepository.findByUserName(username);
        if (user == null) {
            throw new UserException(HttpStatus.NOT_FOUND, "User not found with username: " + username);
        }

        byte[] imageBytes = null;
        String mimeType = null;
        if (user.getImage() != null) {
            GridFSFile imageFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(user.getImage())));
            if (imageFile != null) {
                try {
                    GridFsResource resource = gridFsTemplate.getResource(imageFile);
                    imageBytes = resource.getInputStream().readAllBytes();
                    mimeType = resource.getContentType();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return userMapper.toUserDTO(user, imageBytes,mimeType);
    }
    @Override
    public User update(User user) {
        return userRepository.save(user);
    }

    @Override
    public void asociarImagenAlUsuario(String userName, MultipartFile file) throws IOException {
        UserDTO userdto = findByUsername(userName);
        User user = getById(userdto.id());
        String imageId = imageService.guardarImagen(file);
        user.setImage(imageId);
        userRepository.save(user);
    }

}
