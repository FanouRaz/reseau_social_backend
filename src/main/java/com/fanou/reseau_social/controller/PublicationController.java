package com.fanou.reseau_social.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.fanou.reseau_social.service.PublicationService;

import jakarta.persistence.EntityNotFoundException;

import com.fanou.reseau_social.model.Commentaire;
import com.fanou.reseau_social.model.Publication;
import com.fanou.reseau_social.model.ReactionPublication;
import com.fanou.reseau_social.model.User;

@RestController
public class PublicationController {
    @Autowired
    private PublicationService publicationService;

    @GetMapping("/api/publications")
    public ResponseEntity<List<Publication>> getPublications(){
        List<Publication> publications = publicationService.getPublications();         
        System.out.println(publications);
        return ResponseEntity.ok(publications);
    }

    @GetMapping("/api/publication/{id}")
    public ResponseEntity<Publication> getPublicationById(@PathVariable("id") long id){
        try{
            Publication publication = publicationService.getPublicationById(id);
            return ResponseEntity.ok(publication);
        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/api/publication/{id}")
    public ResponseEntity<String> deletePublication(@PathVariable("id") long id){
        try{
            publicationService.deletePublication(id);
            return ResponseEntity.ok("Publication deleted successfuly");
        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/api/publication/{id_user}")
    public ResponseEntity<Publication> createPublication(@PathVariable("id_user") long id, @RequestBody Publication publication){
        try{
            Publication newPublication = publicationService.createPublication(id, publication);
            return ResponseEntity.ok(newPublication);
        }catch(EntityNotFoundException e){
            System.out.println("User with id "+id+"not found");
            return ResponseEntity.notFound().build();
        }catch(MethodArgumentNotValidException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/api/publication/{id}")
    public ResponseEntity<Publication> updatePublication(@PathVariable("id") long id, @RequestBody Publication publication){
           try {
            Publication updatedPublication = publicationService.updatePublication(id, publication);
            return ResponseEntity.ok(updatedPublication);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().build();
        } catch (MethodArgumentNotValidException e){
            return ResponseEntity.badRequest().build();
        }
    }

    //Reactions
    @GetMapping("/api/publication/reactions/{id}")
    public ResponseEntity<List<ReactionPublication>> getReactor(@PathVariable("id") long id){
        try{
            List<ReactionPublication> reactors = publicationService.getReactor(id);
            return ResponseEntity.ok(reactors);
        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/publication/reactions/{id_publication}/{type}")
    public ResponseEntity<List<User>> getReactorByTypeOfReaction(@PathVariable("id_publication") long id,@PathVariable("type") String type){
        try{
            List<User> reactors = publicationService.getReactorByType(id, type);
            return ResponseEntity.ok(reactors);
        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/api/publication/reaction/{id_user}/{id_publication}")
    public ResponseEntity<ReactionPublication> updateReaction(@PathVariable("id_user") long id_user,@PathVariable("id_publication") long id_publication,@RequestBody ReactionPublication react){
        try{
            ReactionPublication updated = publicationService.updateReaction(id_user,id_publication, react); 
            return ResponseEntity.ok(updated);
        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }catch(MethodArgumentNotValidException e){
            return ResponseEntity.badRequest().build();
        }
    }

    //Commentaires
    @GetMapping("/api/publication/commentaires/{id_publication}")
    public ResponseEntity<List<Commentaire>> getCommentaires(@PathVariable("id_publication") long id_publication){
        try{
            List<Commentaire> commentaires = publicationService.getCommentaires(id_publication);
            return ResponseEntity.ok(commentaires);
        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }
} 