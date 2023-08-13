package com.fanou.reseau_social.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fanou.reseau_social.model.Message;

public interface MessageRepository extends JpaRepository<Message,Long>{
    // Retourne toutes les messages envoyées par un utilisateur 
    public List<Message> findAllBySender_IdUser(long id_sender);
    
    // Retourne toutes les messages reçues par un utilisateur
    public List<Message> findAllByReceiver_IdUser(long id_receiver);

    // Retourne les messages correspondant à ceux envoyé par un utilisateur vers un destinataire
    public List<Message> findAllBySender_IdUserAndReceiver_IdUser(long id_sender,long id_receiver);
}
