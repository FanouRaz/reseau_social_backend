package com.fanou.reseau_social.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fanou.reseau_social.model.ReactionMessage;

public interface ReactionMessageRepository extends JpaRepository<ReactionMessage,Long>{
    public Optional<ReactionMessage> findByUser_IdUserAndMessage_Id(long user_IdUser, long message_Id);
}