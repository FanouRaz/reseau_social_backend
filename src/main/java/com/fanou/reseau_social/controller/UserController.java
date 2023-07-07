package com.fanou.reseau_social.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fanou.reseau_social.model.User;
import com.fanou.reseau_social.service.UserService;

import jakarta.persistence.EntityNotFoundException;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/api/users")
    public ResponseEntity<List<User>> getUsers(){
        List<User> users = userService.getUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/api/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") long id){
        try{
            User user = userService.getUserById(id);
            return ResponseEntity.ok(user);                       
        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/api/user/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable("id") long id){
        try{
            userService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully");
        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/api/createUser")
    public ResponseEntity<User> createUser(@RequestBody User user){
        try{
            User newUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        }catch(MethodArgumentNotValidException e){
            return ResponseEntity.badRequest().build();
        }
    }
}
