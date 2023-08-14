package com.fanou.reseau_social.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fanou.reseau_social.model.ReactionMessage;
import com.fanou.reseau_social.service.MessageService;

import jakarta.persistence.EntityNotFoundException;

@RestController
public class MessageController {
    @Autowired
    private MessageService messageService;

    @GetMapping("/api/message/reactions/{id_message}") 
    public ResponseEntity<List<ReactionMessage>> getReactions(@PathVariable("id_message") long id_message){
        try{
            List<ReactionMessage> reactions = messageService.getReactions(id_message);
            return ResponseEntity.ok(reactions); 
        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }    
}
