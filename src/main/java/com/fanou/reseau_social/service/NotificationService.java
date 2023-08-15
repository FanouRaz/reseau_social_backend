package com.fanou.reseau_social.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fanou.reseau_social.model.Notification;
import com.fanou.reseau_social.repository.NotificationRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public List<Notification> getNotifications(long id){
        List<Notification> notifications = notificationRepository.findAllByReceiverIdOrderByCreatedAtDesc(id);
        return notifications;
    }

    public Notification markAsRead(long id) throws EntityNotFoundException{
        Notification notification = notificationRepository.findById(id)
                                                          .orElseThrow(EntityNotFoundException::new);
                                                          
        notification.setRead(true);
        notificationRepository.save(notification);

        return notification;
    }

    public void removeNotification(long id) throws EntityNotFoundException{
        Notification notification = notificationRepository.findById(id)
                                                          .orElseThrow(EntityNotFoundException::new);

        notificationRepository.delete(notification);
    }
}
