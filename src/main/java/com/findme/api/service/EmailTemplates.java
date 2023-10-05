package com.findme.api.service;


import com.findme.api.model.User;
import com.mailgun.model.message.Message;
import org.springframework.core.env.Environment;

public class EmailTemplates {


    public Message mailConfMessage(User user, Environment environment) {
        System.out.println("template");
        return Message.builder()
                .from("FindMe <"+environment.getProperty("mailgun.email")+">")
                .to(user.getEmail())
                .subject("Validez votre adresse email")
                .html("<p>Bonjour "+user.getUsername()+" !</p><p>Afin de valider votre adresse email, veuillez cliquer sur le bouton suivant :</p><a href='"+environment.getProperty("frontend.url")+"valid-mail?code="+user.getConfirmationCode()+"' target=\"_blank\"><button>Cliquez ici</button></a>")
                .text("Bonjour "+user.getUsername()+" ! Code : "+user.getConfirmationCode())
                .build();
    }
}
