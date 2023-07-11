package com.fanou.reseau_social.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.fanou.reseau_social.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

import com.fanou.reseau_social.model.User;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    public List<User> getUsers(){
        return userRepository.findAll();    
    }    

    public User getUserById(long id) throws EntityNotFoundException{
        return userRepository.findById(id)
                             .orElseThrow(EntityNotFoundException::new);
    }

    public void deleteUser(long id) throws EntityNotFoundException{
        User user = userRepository.findById(id)
                                  .orElseThrow(EntityNotFoundException::new);
        
        userRepository.delete(user);       
    }

    public User createUser(User user) throws MethodArgumentNotValidException{
       return userRepository.save(user);
    }

    public User updateUser(long id, User user) throws MethodArgumentNotValidException,EntityNotFoundException{
        User current = userRepository.findById(id)
                                     .orElseThrow(EntityNotFoundException::new);

        current.setUsername(user.getUsername());
        current.setEmail(user.getEmail());
        current.setPhoneNumber(user.getPhoneNumber());
        current.setBirthday(user.getBirthday());
        current.setProfilePicture(user.getProfilePicture());
        return userRepository.save(current);    
    }

    public void uploadProfilePicture(long id, MultipartFile file) throws EntityNotFoundException,IOException,MethodArgumentNotValidException{
        User user = userRepository.findById(id)
                                  .orElseThrow(EntityNotFoundException::new);
        
        if(!file.isEmpty()){
            byte[] bytes = file.getBytes();
            Path path = Paths.get("uploads/users/" + file.getOriginalFilename());
            Path oldPicture = Paths.get(user.getProfilePicture());
            
            if(!(user.getProfilePicture()).equals("uploads/users/user_placeholder.png")) Files.delete(oldPicture);
        
            Files.write(path, bytes);
            user.setProfilePicture(path.toString());
            updateUser(id, user);
        }
    }
}
