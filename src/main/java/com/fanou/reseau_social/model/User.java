package com.fanou.reseau_social.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
public class User{
    @Id
    @GeneratedValue
    private long id_user;
    private String username;
    private String email;
    private String phoneNumber;

    @JsonIgnore
    private String password;

    @Temporal(TemporalType.DATE)
    private Date birthday;


    @OneToMany(targetEntity = Publication.class, cascade= CascadeType.REMOVE)
    private List<Publication> publications;
  
    private String profilePicture = "uploads/users/user_placeholder.png";
    
    public void addPublication(Publication publication){
        publications.add(publication);
        publication.setUser(this);
    }

    public void deletePublication(Publication publication){
        publications.remove(publication);
        publication.setUser(null);
    }
}
