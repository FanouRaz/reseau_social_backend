package com.fanou.reseau_social.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Notification {
    @Id
    @GeneratedValue
    private long id;

    private long triggerId; //Id de l'utilisateur ayant effectué l'action déclencheur de la notification
    private long receiverId; //Id de l'utilisateur ayant reçu la notification
    private long contentId;//Id du contenu lié à la notification (ex: Id de la publication réagi, id de la publication commenté, id de la demande d'ami)

    @Enumerated(EnumType.STRING)
    private NotificationType type; //REACTION, COMMENT, FRIEND_REQUEST

    private boolean isRead;

    @Temporal(TemporalType.DATE)
    private Date createdAt = new Date();
}
