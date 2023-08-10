package com.fanou.reseau_social.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.fanou.reseau_social.repository.PublicationRepository;
import com.fanou.reseau_social.repository.ReactionPublicationRepository;
import com.fanou.reseau_social.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

import com.fanou.reseau_social.model.Commentaire;
import com.fanou.reseau_social.model.Publication;
import com.fanou.reseau_social.model.ReactionPublication;
import com.fanou.reseau_social.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class PublicationService {
    @Autowired
    private PublicationRepository publicationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReactionPublicationRepository reactionRepository;

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

        publicationRepository.save(publication);
        userRepository.save(user);
        
        return publication;
    }

    public Publication updatePublication(long id, Publication publication)throws MethodArgumentNotValidException,EntityNotFoundException{
        Publication current = publicationRepository.findById(id)
                                                   .orElseThrow(EntityNotFoundException::new);
        current.setText(publication.getText());
        return publicationRepository.save(current);    
    }

    //Reactions
    public List<User> getReactorByType(long id,String type) throws EntityNotFoundException{
        Publication publication = publicationRepository.findById(id)
                                                       .orElseThrow(EntityNotFoundException::new);
        
        
        List<User> reactors = new ArrayList<>();

        for(ReactionPublication reaction : publication.getReactions())
            if(reaction.getReaction().equals(type)) reactors.add(reaction.getUser());
        
        return reactors;
    }

    public List<ReactionPublication> getReactor(long id) throws EntityNotFoundException{
        Publication publication = publicationRepository.findById(id)
                                                       .orElseThrow(EntityNotFoundException::new);
        
        return publication.getReactions();
    }

    public ReactionPublication updateReaction(long id_user,long id_publication, ReactionPublication reaction)throws MethodArgumentNotValidException,EntityNotFoundException{
        ReactionPublication current =  reactionRepository.findByUser_IdUserAndPublication_IdPublication(id_user,id_publication)
                                                         .orElseThrow(EntityNotFoundException::new);
                                                   
        current.setReaction(reaction.getReaction());
        return reactionRepository.save(current);    
    }

    //Commentaire
    public List<Commentaire> getCommentaires(long id) throws EntityNotFoundException{
        Publication publication = publicationRepository.findById(id)
                                                       .orElseThrow(EntityNotFoundException::new);

        return publication.getCommentaires();
    }
}

