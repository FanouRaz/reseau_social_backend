package com.fanou.reseau_social.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fanou.reseau_social.model.Commentaire;
import com.fanou.reseau_social.model.ReactionCommentaire;
import com.fanou.reseau_social.model.User;
import com.fanou.reseau_social.service.CommentaireService;

import jakarta.persistence.EntityNotFoundException;

@RestController
public class CommentaireController {
    @Autowired
    private CommentaireService commentaireService;

    @DeleteMapping("/api/commentaire/{id}")
    public ResponseEntity<String> deleteCommentaire(@PathVariable("id") long id){
        try{
            commentaireService.deleteCommentaire(id);
            return ResponseEntity.ok("Comment deleted successfully!");
        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/api/commentaire/{id}")
    public ResponseEntity<Commentaire> updateCommentaire(@PathVariable("id") long id, @RequestBody Commentaire commentaire){
        try{
            Commentaire updated = commentaireService.updateCommentaire(id,commentaire);
            return ResponseEntity.ok(updated);
        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }catch(MethodArgumentNotValidException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/api/commentaire/reactions/{id}")
    public ResponseEntity<List<ReactionCommentaire>> getReactions(@PathVariable("id") long id){
        try{
            List<ReactionCommentaire> reactions = commentaireService.getReactions(id);
            return ResponseEntity.ok(reactions);
        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/commentaire/reactions/{id}/{reaction}")
    public ResponseEntity<List<User>> getReactorByReaction(@PathVariable("id") long id, @PathVariable("reaction") String reaction){
        try{
            List<User> reactors = commentaireService.getReactorsByReaction(id, reaction);
            return ResponseEntity.ok(reactors);
        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }
}