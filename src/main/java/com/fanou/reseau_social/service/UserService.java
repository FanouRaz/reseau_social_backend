package com.fanou.reseau_social.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.fanou.reseau_social.repository.CommentaireRepository;
import com.fanou.reseau_social.repository.FriendRequestRepository;
import com.fanou.reseau_social.repository.NotificationRepository;
import com.fanou.reseau_social.repository.PublicationRepository;
import com.fanou.reseau_social.repository.ReactionCommentaireRepository;
import com.fanou.reseau_social.repository.ReactionPublicationRepository;
import com.fanou.reseau_social.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

import com.fanou.reseau_social.model.Commentaire;
import com.fanou.reseau_social.model.FriendRequest;
import com.fanou.reseau_social.model.Notification;
import com.fanou.reseau_social.model.NotificationType;
import com.fanou.reseau_social.model.Publication;
import com.fanou.reseau_social.model.ReactionCommentaire;
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
    
    @Autowired 
    private CommentaireRepository commentaireRepository;

    @Autowired
    private ReactionCommentaireRepository reactionCommentaireRepository;

    @Autowired
    private FriendRequestRepository requestRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private MentionService mentionService;

     //Indique si une demande a déjà été envoyé
    private boolean hasReceivedFriendRequest(User receiver, User sender) {
        for (FriendRequest receivedRequest : receiver.getReceivedFriendRequests()) 
            if (receivedRequest.getSender().equals(sender)) return true;
        
        return false;
    }

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
        
        //retirer de la liste d'ami de ses amis
        for(User friend : user.getFriends()){
            friend.getFriends()
                  .remove(user);
            userRepository.save(friend);
        }

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

        //Déclenche une notification prévenant le propriétaire de la publicatio
        if(id_user != publication.getUser().getIdUser()){
            Notification notification = new Notification();

            notification.setTriggerId(id_user);
            notification.setType(NotificationType.REACTION_PUBLICATION);
            notification.setReceiverId(publication.getUser().getIdUser());
            notification.setContentId(id_publication);
            notification.setRead(false);

            notificationRepository.save(notification);
        }


        //Réaction
        reaction.setUser(user);
        reaction.setPublication(publication);

        user.getReactions()
            .add(reaction);

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


    public Commentaire commentPublication(long id_user,long id_publication,Commentaire commentaire) throws EntityNotFoundException,MethodArgumentNotValidException{
        User user = userRepository.findById(id_user)
                                  .orElseThrow(EntityNotFoundException::new);

        Publication publication = publicationRepository.findById(id_publication)
                                                       .orElseThrow(EntityNotFoundException::new);
                                                       
        //Notifier le propriétaire de la publication
        if(id_user != publication.getUser().getIdUser()){
            Notification notification = new Notification();
            notification.setRead(false);
            notification.setTriggerId(id_user);
            notification.setReceiverId(publication.getUser().getIdUser());
            notification.setContentId(id_publication);
            notification.setType(NotificationType.COMMENT);

            notificationRepository.save(notification);
        }

        commentaire.setUser(user);
        commentaire.setPublication(publication);

        //Si le contenu du commentaire contient une ou plusieurs mention, on notifie l'utilisateur
        if(mentionService.handleMentionsContent(commentaire.getCommentaire()).size() != 0){
            for(long id_mentionned : mentionService.handleMentionsContent(commentaire.getCommentaire())){
                Notification mention = new Notification();
                
                if(id_user != id_mentionned){
                    mention.setTriggerId(id_user);
                    mention.setReceiverId(id_mentionned);
                    mention.setContentId(commentaire.getId());
                    mention.setType(NotificationType.MENTION_COMMENT);
    
                    notificationRepository.save(mention);
                }
            }
        }

        user.getCommentaires()
            .add(commentaire);

        publication.getCommentaires()
                   .add(commentaire);
        
        commentaireRepository.save(commentaire);
        userRepository.save(user);
        publicationRepository.save(publication);

        return commentaire;
    }

    //Reactions Commmentaires
    public ReactionCommentaire reactCommentaire(long id_user, long id_commentaire, ReactionCommentaire reaction) throws EntityNotFoundException, MethodArgumentNotValidException{
        User user = userRepository.findById(id_user)
                                  .orElseThrow(EntityNotFoundException::new);

        Commentaire commentaire = commentaireRepository.findById(id_commentaire)
                                                       .orElseThrow(EntityNotFoundException::new);

        //Déclenche une notification au propriétaire du commentaire
        if(id_user != commentaire.getUser().getIdUser()){
            Notification notification = new Notification();

            notification.setTriggerId(id_user);
            notification.setType(NotificationType.REACTION_COMMENT);
            notification.setReceiverId(commentaire.getUser().getIdUser());
            notification.setContentId(id_commentaire);
            notification.setRead(false);

            notificationRepository.save(notification);
        }

        reaction.setUser(user);
        reaction.setCommentaire(commentaire);

        user.getReactionsCommentaires()
            .add(reaction);

        commentaire.getReactions()
                   .add(reaction);

        reactionCommentaireRepository.save(reaction);
        commentaireRepository.save(commentaire);
        userRepository.save(user);

        return reaction;
    }

    public void removeReactionCommentaire(long id_user,long id_commentaire) throws EntityNotFoundException{
        User user = userRepository.findById(id_user)
                                  .orElseThrow(EntityNotFoundException::new);
        
        Commentaire commentaire = commentaireRepository.findById(id_commentaire)
                                                       .orElseThrow(EntityNotFoundException::new);
        
        ReactionCommentaire react = reactionCommentaireRepository.findByUser_IdUserAndCommentaire_Id(id_user, id_commentaire)
                                                                 .orElseThrow(EntityNotFoundException::new); 
                                                               

        user.getReactionsCommentaires()
            .remove(react);
        
        commentaire.getReactions()
                   .remove(react);
 
        reactionCommentaireRepository.delete(react);
        userRepository.save(user);
        commentaireRepository.save(commentaire);
    }

    public ReactionCommentaire updateReactionCommentaire(long id_user,long id_commentaire,ReactionCommentaire reaction) throws EntityNotFoundException,MethodArgumentNotValidException{
        ReactionCommentaire current = reactionCommentaireRepository.findByUser_IdUserAndCommentaire_Id(id_user, id_commentaire)
                                                                   .orElseThrow(EntityNotFoundException::new);
        
        current.setReaction(reaction.getReaction());

        reactionCommentaireRepository.save(current);
        
        return current;
    }

    //Friend Request
    public FriendRequest sendFriendRequest(long id_sender,long id_receiver,FriendRequest request) throws EntityNotFoundException,IllegalArgumentException{ 
        User sender = userRepository.findById(id_sender)
                                    .orElseThrow(EntityNotFoundException::new);
        
        User receiver = userRepository.findById(id_receiver)
                                      .orElseThrow(EntityNotFoundException::new);
        
        //Si les 2 utilisateurs sont déjà amis ou si une demande a déjà été envoyé par l'un des 2 ou si une personne envoi une demande à lui même
        if(receiver.getFriends().contains(sender) || hasReceivedFriendRequest(receiver,sender) || id_sender == id_receiver) throw new IllegalArgumentException("Friend request not allowed");

        request.setReceiver(receiver);
        request.setSender(sender);

        sender.getSentFriendRequests()
              .add(request);
        receiver.getReceivedFriendRequests()
                .add(request);

        requestRepository.save(request);
        userRepository.save(sender);
        userRepository.save(receiver);

        return request;       
    }

    public List<User> getSentRequest(long id_sender){
        List<FriendRequest> requests = requestRepository.findAllBySender_IdUser(id_sender);
        List<User> receivers = new ArrayList<>();

        for(FriendRequest request : requests)
            receivers.add(request.getReceiver());

        return receivers;
    }


    public List<User> getReceivedRequest(long id_receiver){
        List<FriendRequest> requests = requestRepository.findAllByReceiver_IdUser(id_receiver);
        List<User> senders = new ArrayList<>();

        for(FriendRequest request : requests)
            senders.add(request.getSender());
            
        return senders;
    }


    public void removeFriendRequest(long id_sender,long id_receiver) throws EntityNotFoundException{
        FriendRequest toRemove = requestRepository.findBySender_IdUserAndReceiver_IdUser(id_sender, id_receiver)
                                                  .orElseThrow(EntityNotFoundException::new);
        
        //Retirer la demande d'amis de la liste de demande envoyé de l'envoyeur et de la liste de demande reçu du destinataire
        toRemove.getSender()
                .getSentFriendRequests()
                .remove(toRemove);

        toRemove.getReceiver()
                .getReceivedFriendRequests()
                .remove(toRemove);

        userRepository.save(toRemove.getSender());
        userRepository.save(toRemove.getReceiver());
        requestRepository.delete(toRemove);
    }

    //Amitié
    public void addFriend(long id_sender,long id_receiver) throws EntityNotFoundException{
        FriendRequest request = requestRepository.findBySender_IdUserAndReceiver_IdUser(id_sender, id_receiver)
                                                 .orElseThrow(EntityNotFoundException::new);
        Notification notification = new Notification();

        //Lorsque la demande est accepté par le destinataire, l'expediteur et le destinataire de la demande sont amis
        request.getReceiver()
               .getFriends()
               .add(request.getSender());

        request.getSender()
               .getFriends()
               .add(request.getReceiver());
        
        //Declenche une notification à l'envoyeur de la demande 
        notification.setTriggerId(id_receiver);
        notification.setType(NotificationType.FRIEND_REQUEST_ACCEPTED);
        notification.setReceiverId(id_sender);
        notification.setContentId(id_receiver);
        notification.setRead(false);

        notificationRepository.save(notification);
        userRepository.save(request.getReceiver());
        userRepository.save(request.getSender());

        //La demande d'amis est supprimé
        requestRepository.delete(request);
    }   

    public void removeFriend(long id_remover,long id_toRemove) throws EntityNotFoundException,IllegalArgumentException{
        User remover = userRepository.findById(id_remover)
                                     .orElseThrow(EntityNotFoundException::new);
        User toRemove = userRepository.findById(id_toRemove)
                                      .orElseThrow(EntityNotFoundException::new);
        
        if(!remover.getFriends().contains(toRemove)) throw new IllegalArgumentException("Unfriend not allowed! You're not friends");
        
        else{
            toRemove.getFriends()
                    .remove(remover);

            remover.getFriends()
                   .remove(toRemove);
            
            userRepository.save(toRemove);
            userRepository.save(remover);
        } 
    }

    public Set<User> getFriends(long id_user){
        User user = userRepository.findById(id_user)
                                  .orElseThrow(EntityNotFoundException::new);

        return user.getFriends();
    }


    //bloquage
    public void blockUser(long id_blocker, long id_to_block) throws EntityNotFoundException, IllegalArgumentException{
        User blocker  = userRepository.findById(id_blocker)
                                      .orElseThrow(EntityNotFoundException::new);

        User toBlock = userRepository.findById(id_to_block)
                                     .orElseThrow(EntityNotFoundException::new);

        if(blocker.getBlocks().contains(toBlock) || id_blocker == id_to_block) throw new IllegalArgumentException("Block not Allowed");
        else {
            blocker.getBlocks()
                   .add(toBlock);
            
            userRepository.save(blocker);
        }
    }

    public Set<User> getBlockList(long id) throws EntityNotFoundException{
        User user = userRepository.findById(id)
                                  .orElseThrow(EntityNotFoundException::new);

        return user.getBlocks();
    }

    public void unblock(long id_user,long id_blocked) throws EntityNotFoundException,IllegalArgumentException{
        User user = userRepository.findById(id_user)
                                  .orElseThrow(EntityNotFoundException::new);        
        
        User blocked = userRepository.findById(id_blocked)
                                     .orElseThrow(EntityNotFoundException::new);

        if(!user.getBlocks().contains(blocked)) throw new IllegalArgumentException("Unblock not allowed");
       
        user.getBlocks()
            .remove(blocked);

        userRepository.save(user);
    }
}
