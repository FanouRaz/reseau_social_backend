package com.fanou.reseau_social.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import com.fanou.reseau_social.service.PublicationService;

import jakarta.persistence.EntityNotFoundException;

import com.fanou.reseau_social.model.Publication;

public class PublicationController {
    @Autowired
    private PublicationService publicationService;

    @GetMapping("/api/publications")
    public ResponseEntity<List<Publication>> getPublications(){
        return ResponseEntity.ok(publicationService.getPublications());
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
            return ResponseEntity.notFound().build();
        }catch(MethodArgumentNotValidException e){
            return ResponseEntity.badRequest().build();
        }
    }
}
