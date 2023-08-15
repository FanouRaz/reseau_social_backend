package com.fanou.reseau_social.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fanou.reseau_social.model.User;
import com.fanou.reseau_social.repository.UserRepository;

@Service
public class MentionService {
    @Autowired
    private UserRepository userRepository;


    //Gère les mentions d'utilisateur dans les commentaires et publications
    public List<Long> handleMentionsContent(String content){
        //Detection de mentions dans le 
        Pattern mentionPattern = Pattern.compile("@(\\w+)");
        Matcher matcher = mentionPattern.matcher(content);

        List<Long> mentionned = new ArrayList<Long>();

        while(matcher.find()){
            String username = matcher.group(1);
            System.out.println(username.replace("_"," ") + " mentionned");

            User mentionnedUser = userRepository.findByUsername(username.replace("_", " "))
                                                .orElse(null);
            
            if(mentionnedUser != null) mentionned.add(mentionnedUser.getIdUser());
        }

        return mentionned;
    }
}
