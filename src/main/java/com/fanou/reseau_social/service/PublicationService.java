package com.fanou.reseau_social.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.fanou.reseau_social.repository.PublicationRepository;
import com.fanou.reseau_social.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

import com.fanou.reseau_social.model.Publication;
import com.fanou.reseau_social.model.User;

import java.util.List;

public class PublicationService {
    @Autowired
    private PublicationRepository publicationRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Publication> getPublications(){
        return publicationRepository.findAll();
    }

    public Publication getPublicationById(long id) throws EntityNotFoundException{
        Publication pub = publicationRepository.findById(id)
                                                .orElseThrow(EntityNotFoundException::new);
        
        return pub;
    }

    public void deletePublication(long id) throws EntityNotFoundException{
        Publication publicationToErase = publicationRepository.findById(id)
                                                   .orElseThrow(EntityNotFoundException::new);
        //récuperation de l'utilisateur ayant :publier la publication à effacer                                   
        User user = publicationToErase.getUser();
        
        user.deletePublication(publicationToErase);
        userRepository.save(user);

        publicationRepository.delete(publicationToErase);
    }

    public Publication createPublication(long id_user, Publication publication) throws EntityNotFoundException,MethodArgumentNotValidException{
        User user = userRepository.findById(id_user)
                                  .orElseThrow(EntityNotFoundException::new);
        
        user.addPublication(publication);
        userRepository.save(user);

        return publicationRepository.save(publication);
    }
}
