package com.fanou.reseau_social.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import com.fanou.reseau_social.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

import com.fanou.reseau_social.model.Publication;
import com.fanou.reseau_social.model.User;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    public List<User> getUsers(){
        return userRepository.findAll();    
    }    

    public User getUserById(long id) throws EntityNotFoundException{
        return userRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("L'utilisateur ayant pour id "+id+" est introuvable"));
    }

    public void deleteUser(long id) throws EntityNotFoundException{
        User user = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        userRepository.delete(user);       
    }

    public User createUser(User user) throws MethodArgumentNotValidException{
       return userRepository.save(user);
    }

    public User updatUser(long id, User user) throws MethodArgumentNotValidException,EntityNotFoundException{
        User current = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        current.setUsername(user.getUsername());
        current.setEmail(user.getEmail());
        current.setPhoneNumber(user.getPhoneNumber());
        current.setBirthday(user.getBirthday());
        return userRepository.save(current);    
    }

    public List<Publication> getUserPublications(long id) throws EntityNotFoundException{
        User user = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return user.getPublications();
    }
}
