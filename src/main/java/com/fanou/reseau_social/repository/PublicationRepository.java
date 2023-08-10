package com.fanou.reseau_social.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fanou.reseau_social.model.Publication;

public interface PublicationRepository extends JpaRepository<Publication,Long>{}
