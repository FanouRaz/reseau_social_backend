package com.fanou.reseau_social.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
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
@Table(name = "publication")
public class Publication{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long idPublication;
    private String text;

    @JsonIgnore
    @ManyToOne(targetEntity = User.class,cascade = CascadeType.REMOVE)
    private User user;
    

    @Temporal(TemporalType.DATE)
    private Date datePublication;


    @OneToMany(
        mappedBy = "publication",
        cascade = CascadeType.ALL,
        orphanRemoval=true    
    )
    List<ReactionPublication> reactions;

    @PrePersist
    public void setDefaultDatePublication() {
        if (datePublication == null) datePublication = new Date();
    }
}
