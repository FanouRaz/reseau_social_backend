package com.fanou.reseau_social.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fanou.reseau_social.model.Notification;
import com.fanou.reseau_social.service.NotificationService;

import jakarta.persistence.EntityNotFoundException;

@RestController
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/api/user/notifications/{id_user}")
    public ResponseEntity<List<Notification>> getNotifications(@PathVariable("id_user") long id){
        List<Notification> notifications = notificationService.getNotifications(id);
        return ResponseEntity.ok(notifications);
    }

    @PutMapping("/api/notification/{id}")
    public ResponseEntity<Notification> markAsRead(@PathVariable("id") long id){
        try {
            Notification notification = notificationService.markAsRead(id);
            return ResponseEntity.ok(notification);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/api/notification/{id}")
    public ResponseEntity<String> deleteNotification(@PathVariable("id") long id){
        try{
            notificationService.removeNotification(id);
            return ResponseEntity.ok("Notification deleted successfully!");
        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }
}
