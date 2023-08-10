package com.fanou.reseau_social.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fanou.reseau_social.model.ReactionPublication;

public interface ReactionPublicationRepository extends JpaRepository<ReactionPublication,Long> {
   Optional<ReactionPublication> findByUser_IdUserAndPublication_IdPublication(long userId, long publicationId);
}
