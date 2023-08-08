package com.fanou.reseau_social.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.fanou.reseau_social.repository.PublicationRepository;
import com.fanou.reseau_social.repository.ReactionPublicationRepository;
import com.fanou.reseau_social.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

import com.fanou.reseau_social.model.Publication;
import com.fanou.reseau_social.model.ReactionPublication;
import com.fanou.reseau_social.model.User;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ReactionPublicationRepository reactionPublicationRepository;

    @Autowired
    private PublicationRepository publicationRepository;
    
    public List<User> getUsers(){
        return userRepository.findAll();    
    }    

    public User getUserById(long id) throws EntityNotFoundException{
        return userRepository.findById(id)
                             .orElseThrow(EntityNotFoundException::new);
    }

    public void deleteUser(long id) throws EntityNotFoundException, IOException{
        User user = userRepository.findById(id)
                                  .orElseThrow(EntityNotFoundException::new);
        
        Path imgPath = Paths.get(user.getProfilePicture());

        //Suppression de la photo de profil de l'utilisateur si sa photo est différent de la photo par défaut
        if(!(user.getProfilePicture()).equals("uploads/users/user_placeholder.png")) Files.delete(imgPath);
        
        userRepository.delete(user);       
    }

    public User createUser(User user) throws MethodArgumentNotValidException{
       return userRepository.save(user);
    }


    public User updateUser(long id, User user) throws MethodArgumentNotValidException,EntityNotFoundException{
        User current = userRepository.findById(id)
                                     .orElseThrow(EntityNotFoundException::new);

        current.setUsername(user.getUsername());
        current.setEmail(user.getEmail());
        current.setPhoneNumber(user.getPhoneNumber());
        current.setBirthday(user.getBirthday());
        current.setProfilePicture(user.getProfilePicture());
        return userRepository.save(current);    
    }


    public List<Publication> getUserPublications(long id) throws EntityNotFoundException{
        User user = userRepository.findById(id)
                                  .orElseThrow(EntityNotFoundException::new);
      
        return user.getPublications();
    }

    public void uploadProfilePicture(long id, MultipartFile file) throws EntityNotFoundException,IOException,MethodArgumentNotValidException{
        User user = userRepository.findById(id)
                                  .orElseThrow(EntityNotFoundException::new);
        
        if(!file.isEmpty()){
            byte[] bytes = file.getBytes();
            Path path = Paths.get("uploads/users/" + file.getOriginalFilename());
            Path oldPicture = Paths.get(user.getProfilePicture());
            
            //Suppression de la photo de profil de l'utilisateur si sa photo est différent de la photo par défaut
            if(!(user.getProfilePicture()).equals("uploads/users/user_placeholder.png")) Files.delete(oldPicture);
        
            Files.write(path, bytes);
            user.setProfilePicture(path.toString());
            updateUser(id, user);
        }
    }

    //Reactions Publication
    public ReactionPublication reactPublication(long id_user, long id_publication, ReactionPublication reaction) throws EntityNotFoundException, MethodArgumentNotValidException{
        User user = userRepository.findById(id_user)
                                  .orElseThrow(EntityNotFoundException::new);

        Publication publication = publicationRepository.findById(id_publication)
                                                       .orElseThrow(EntityNotFoundException::new);

        reaction.setUser(user);
        reaction.setPublication(publication);


        publication.getReactions()
                   .add(reaction);

        reactionPublicationRepository.save(reaction);
        publicationRepository.save(publication);
        userRepository.save(user);

        return reaction;
    }

    public void removeReaction(long id_user,long id_publication) throws EntityNotFoundException{
        User user = userRepository.findById(id_user)
                                  .orElseThrow(EntityNotFoundException::new);
        
        Publication publication = publicationRepository.findById(id_publication)
                                                       .orElseThrow(EntityNotFoundException::new);
        
        ReactionPublication react = reactionPublicationRepository.findByUser_IdUserAndPublication_IdPublication(id_user, id_publication)
                                                                 .orElseThrow(EntityNotFoundException::new); 
                                                               

        user.getReactions()
            .remove(react);
        
        publication.getReactions()
                    .remove(react);
 
        reactionPublicationRepository.delete(react);
        userRepository.save(user);
        publicationRepository.save(publication);
    }
}
