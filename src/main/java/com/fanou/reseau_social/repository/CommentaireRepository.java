package com.fanou.reseau_social.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fanou.reseau_social.model.Commentaire;

public interface CommentaireRepository extends JpaRepository<Commentaire,Long>{}
