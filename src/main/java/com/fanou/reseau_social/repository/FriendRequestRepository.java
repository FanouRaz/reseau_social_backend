package com.fanou.reseau_social.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fanou.reseau_social.model.FriendRequest;

public interface FriendRequestRepository extends JpaRepository<FriendRequest,Long>{
    // Retourne toutes les demandes envoyées par un utilisateur 
    public List<FriendRequest> findAllBySender_IdUser(long id_sender);
    
    // Retourne toutes les demandes reçues par un utilisateur
    public List<FriendRequest> findAllByReceiver_IdUser(long id_receiver);

    // Retourne la demande d'ami correspondant à celle envoyé par un utilisateur vers un destinataire
    public Optional<FriendRequest> findBySender_IdUserAndReceiver_IdUser(long id_sender,long id_receiver);
}
