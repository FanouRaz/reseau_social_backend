package com.fanou.reseau_social.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Setter
@Getter
@Table(name="commentaire")
public class Commentaire {
    @Id
    @GeneratedValue
    private long id;

    private String commentaire;

    @Temporal(TemporalType.DATE)
    private Date dateCommentaire = new Date();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="idUser")
    private User user;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idPublication")
    private Publication publication;

    @OneToMany(
        mappedBy = "commentaire",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )    
    private List<ReactionCommentaire> reactions;
}
