package com.fanou.reseau_social.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.fanou.reseau_social.model.Commentaire;
import com.fanou.reseau_social.model.ReactionCommentaire;
import com.fanou.reseau_social.model.User;
import com.fanou.reseau_social.repository.CommentaireRepository;
import com.fanou.reseau_social.repository.PublicationRepository;
import com.fanou.reseau_social.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CommentaireService {
    @Autowired
    private CommentaireRepository commentaireRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PublicationRepository publicationRepository;

    public void deleteCommentaire(long id) throws EntityNotFoundException{
        Commentaire commentaire = commentaireRepository.findById(id)
                                                       .orElseThrow(EntityNotFoundException::new);

        commentaire.getPublication()
                   .getCommentaires()
                   .remove(commentaire);

        commentaire.getUser()
                   .getCommentaires()
                   .remove(commentaire);
        
        
        publicationRepository.save(commentaire.getPublication());
        userRepository.save(commentaire.getUser());
        commentaireRepository.delete(commentaire);
    }

    public Commentaire updateCommentaire(long id,Commentaire commentaire) throws EntityNotFoundException,MethodArgumentNotValidException{
        Commentaire current = commentaireRepository.findById(id)
                                                   .orElseThrow(EntityNotFoundException::new);
        
        current.setCommentaire(commentaire.getCommentaire());
        commentaireRepository.save(current);

        return current;
    }

    public List<ReactionCommentaire> getReactions(long id) throws EntityNotFoundException{
        Commentaire commentaire =  commentaireRepository.findById(id)
                                                        .orElseThrow(EntityNotFoundException::new);
        
        return commentaire.getReactions();
    }

    public List<User> getReactorsByReaction(long id,String react) throws EntityNotFoundException{
        Commentaire commentaire = commentaireRepository.findById(id)
                                                       .orElseThrow(EntityNotFoundException::new);
        
        List<User> users = new ArrayList<>();

        for(ReactionCommentaire reaction : commentaire.getReactions())
            if(reaction.getReaction().equals(react)) users.add(reaction.getUser());
        
        return users;
    }
}
