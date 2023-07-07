package com.fanou.reseau_social.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Publication {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id_publication;
    private String text;
    
    @Temporal(TemporalType.DATE)
    private Date datePublication;
}
