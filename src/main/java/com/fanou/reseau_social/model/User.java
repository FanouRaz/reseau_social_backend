package com.fanou.reseau_social.model;

import java.util.Date;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
    private String password;

    @Temporal(TemporalType.DATE)
    @Nullable
    private Date birthday;

}
