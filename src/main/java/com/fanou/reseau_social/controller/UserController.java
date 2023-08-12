package com.fanou.reseau_social.controller;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fanou.reseau_social.model.User;
import com.fanou.reseau_social.service.UserService;
import com.fanou.reseau_social.model.Commentaire;
import com.fanou.reseau_social.model.FriendRequest;
import com.fanou.reseau_social.model.Publication;
import com.fanou.reseau_social.model.ReactionCommentaire;
import com.fanou.reseau_social.model.ReactionPublication;

import jakarta.persistence.EntityNotFoundException;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/api/users")
    public ResponseEntity<List<User>> getUsers(){
        List<User> users = userService.getUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/api/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") long id){
        try{
            User user = userService.getUserById(id);
            return ResponseEntity.ok(user);                       
        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/api/user/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable("id") long id){
        try{
            userService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully");
        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }catch(IOException e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/api/user")
    public ResponseEntity<User> createUser(@RequestBody User user){
        try{
            User newUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        }catch(MethodArgumentNotValidException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/api/user/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @RequestBody User user) {
        try {
            User updatedUser = userService.updateUser(id, user);
            return ResponseEntity.ok(updatedUser);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().build();
        } catch (MethodArgumentNotValidException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/api/publications/{id_utilisateur}")
    public ResponseEntity<List<Publication>> getUserPublications(@PathVariable("id_utilisateur") long id){
        try{
            List<Publication> publications = userService.getUserPublications(id); 
            return ResponseEntity.ok(publications);     
        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }
          
    @PostMapping("/api/user/uploadProfilePicture/{id_user}")
    public ResponseEntity<String> uploadProfilePicture(@PathVariable("id_user") long id, @RequestParam("file") MultipartFile file){
        try{
            userService.uploadProfilePicture(id, file);
            return ResponseEntity.ok("File uploaded successfully");
        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }catch(IOException e){
            return ResponseEntity.internalServerError().build();
        }catch(MethodArgumentNotValidException e){
            return ResponseEntity.badRequest().build();
        }
    }

    //Reactions
    @PostMapping("/api/publication/react/{id_user}/{id_publication}")
    public ResponseEntity<String> reactPublication(@PathVariable("id_user") long id_user,@PathVariable("id_publication") long id_publication, @RequestBody ReactionPublication reaction){
        try{
            userService.reactPublication(id_user, id_publication, reaction);
            return ResponseEntity.ok().build();
        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }catch(MethodArgumentNotValidException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/api/publication/react/{id_user}/{id_publication}")
    public ResponseEntity<String> removeReaction(@PathVariable("id_user") long id_user,@PathVariable("id_publication") long id_publication){
        try{
            userService.removeReaction(id_user, id_publication);
            return ResponseEntity.ok("Reaction removed successfully");
        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    //Commentaires
    @PostMapping("/api/publication/comment/{id_user}/{id_publication}")
    public ResponseEntity<Commentaire> commentPublication(@PathVariable("id_user") long id_user, @PathVariable("id_publication") long id_publication, @RequestBody Commentaire commentaire){
        try{
            Commentaire comment = userService.commentPublication(id_user, id_publication, commentaire);
            return ResponseEntity.ok(comment);
        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }catch(MethodArgumentNotValidException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/api/commentaire/react/{id_user}/{id_commentaire}")
    public ResponseEntity<ReactionCommentaire> reactCommentaire(@PathVariable("id_user") long id_user,@PathVariable("id_commentaire") long id_commentaire,@RequestBody ReactionCommentaire reaction){
        try{
            ReactionCommentaire react = userService.reactCommentaire(id_user, id_commentaire, reaction);
            return ResponseEntity.ok(react);
        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }catch(MethodArgumentNotValidException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/api/commentaire/react/{id_user}/{id_commentaire}")
    public ResponseEntity<String> deleteCommentaireReaction(@PathVariable("id_user") long id_user,@PathVariable("id_commentaire") long id_commentaire){
        try{
            userService.removeReactionCommentaire(id_user, id_commentaire);
            return ResponseEntity.ok("Reaction deleted successfully!!");
        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/api/commentaire/react/{id_user}/{id_commentaire}")
    public ResponseEntity<ReactionCommentaire> updateCommentaireReaction(@PathVariable("id_user") long id_user,@PathVariable("id_commentaire") long id_commentaire, @RequestBody ReactionCommentaire reaction){
        try{
            ReactionCommentaire updatedReaction = userService.updateReactionCommentaire(id_user, id_commentaire, reaction);
            return ResponseEntity.ok(updatedReaction);
        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }catch(MethodArgumentNotValidException e){
            return ResponseEntity.badRequest().build();
        }
    }

    //Friend Request
    @PostMapping("/api/friendRequest/{id_sender}/{id_receiver}")
    public ResponseEntity<Object> sendFriendRequest(@PathVariable("id_sender") long id_sender,@PathVariable("id_receiver") long id_receiver,@RequestBody FriendRequest req){
        try{
            FriendRequest request  = userService.sendFriendRequest(id_sender, id_receiver,req);
            return ResponseEntity.ok(request);

        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("friend Request not allowed");
        }
    }

    @DeleteMapping("/api/friendRequest/{id_sender}/{id_receiver}")
    public ResponseEntity<String> removeFriendRequest(@PathVariable("id_sender") long id_sender, @PathVariable("id_receiver") long id_receiver){
        try{
            userService.removeFriendRequest(id_sender, id_receiver);
            return ResponseEntity.ok("Friend request deleted successfully!");
        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/friendRequest/sent/{id_user}")
    public ResponseEntity<List<User>> getSentRequests(@PathVariable("id_user") long id_user){
        try{    
            List<User> receivers = userService.getSentRequest(id_user);
            return ResponseEntity.ok(receivers); 

        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/friendRequest/received/{id_user}")
    public ResponseEntity<List<User>> getReceivedRequests(@PathVariable("id_user") long id_user){
        try{    
            List<User> sender = userService.getReceivedRequest(id_user);
            return ResponseEntity.ok(sender); 

        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/api/friendRequest/accept/{id_sender}/{id_receiver}")
    public ResponseEntity<String> accceptRequest(@PathVariable("id_sender") long id_sender,@PathVariable("id_receiver") long id_receiver){
        try{
            userService.addFriend(id_sender, id_receiver);
            return ResponseEntity.ok("Request accepted!");
        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/user/friends/{id_user}")
    public ResponseEntity<Set<User>> getFriendList(@PathVariable("id_user") long id_user){
        try{
            Set<User> friendList = userService.getFriends(id_user);
            return ResponseEntity.ok(friendList);
        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/api/user/unfriend/{id_remover}/{id_remove}")
    public ResponseEntity<String> unfriend(@PathVariable("id_remover") long id_remover,@PathVariable("id_remove") long id_toRemove){
        try{
            userService.removeFriend(id_remover, id_toRemove);
            return ResponseEntity.ok("Unfriend successfully!");
        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unfriend not allowed");
        }
    }
}
