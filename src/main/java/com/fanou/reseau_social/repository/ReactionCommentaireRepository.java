package com.fanou.reseau_social.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fanou.reseau_social.model.ReactionCommentaire;

public interface ReactionCommentaireRepository extends JpaRepository<ReactionCommentaire,Long>{
    public Optional<ReactionCommentaire> findByUser_IdUserAndCommentaire_Id(long user_IdUser, long commentaire_Id);
}
