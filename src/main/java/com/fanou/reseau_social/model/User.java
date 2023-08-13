package com.fanou.reseau_social.model;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user")
public class User{
    @Id
    @GeneratedValue
    private long idUser;
    private String username;
    private String email;
    private String phoneNumber;
    private String password;
    private String profilePicture = "uploads/users/user_placeholder.png";


    @OneToMany(
        mappedBy = "user",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )    
    @JsonIgnore
    private List<ReactionPublication> reactions;
    
    @OneToMany(
        mappedBy = "user",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )    
    @JsonIgnore
    private List<Commentaire> commentaires;

    @OneToMany(
        mappedBy = "user",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )    
    @JsonIgnore
    private List<ReactionCommentaire> reactionsCommentaires;
    
    @Temporal(TemporalType.DATE)
    private Date birthday;


    @OneToMany(
        targetEntity = Publication.class,
        cascade= CascadeType.REMOVE
    )
    private List<Publication> publications;
    

    @OneToMany(
        mappedBy = "sender",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )    
    @JsonIgnore
    private List<FriendRequest> sentFriendRequests;

    @OneToMany(
        mappedBy = "receiver",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )    
    @JsonIgnore
    private List<FriendRequest> receivedFriendRequests;
    
    //Amitié
    @JsonIgnore
    @ManyToMany(cascade={CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.MERGE})
    @JoinTable(
        name = "friendship",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private Set<User> friends = new HashSet<User>();
    
    //Bloquage
    @JsonIgnore
    @ManyToMany(cascade={CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.MERGE})
    @JoinTable(
        name = "block",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "user_blocked_id")
    )
    private Set<User> blocks = new HashSet<User>();
   
    //messages
    @OneToMany(
        mappedBy = "sender",
        cascade = {CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.MERGE},
        orphanRemoval = true
    )    
    @JsonIgnore
    private List<Message> sentMessages;

    @OneToMany(
        mappedBy = "receiver",
        cascade = {CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.MERGE},
        orphanRemoval = true
    )    
    @JsonIgnore
    private List<Message> receivedMessages;

    @OneToMany(
        mappedBy = "user",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )    
    @JsonIgnore
    private List<ReactionMessage> reactionsMessages;

    public void addPublication(Publication publication){
        publications.add(publication);
        publication.setUser(this);
    }

    public void deletePublication(Publication publication){
        publications.remove(publication);
        publication.setUser(null);
    }
}
