package com.fanou.reseau_social.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fanou.reseau_social.model.Message;
import com.fanou.reseau_social.model.ReactionMessage;
import com.fanou.reseau_social.repository.MessageRepository;
import com.fanou.reseau_social.repository.ReactionMessageRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class MessageService {
    @Autowired
    private ReactionMessageRepository reactionRepository;

    @Autowired
    private MessageRepository messageRepository;

    public List<ReactionMessage> getReactions(long id_message) throws EntityNotFoundException{
        Message message = messageRepository.findById(id_message)
                                           .orElseThrow(EntityNotFoundException::new);
        
        return message.getReactions();
    }
}
