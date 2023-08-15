package com.fanou.reseau_social.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fanou.reseau_social.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification,Long>{
    List<Notification> findAllByReceiverIdOrderByCreatedAtDesc(long receiverId);
}
