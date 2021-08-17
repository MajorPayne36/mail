package com.example.mail.controllers;

import com.example.mail.utils.MailCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mail")
public class MailController {

    @Autowired
    private MailCreator mailCreator;

    @GetMapping(value="/send")
    public void getMail(String mail){
        System.out.println(mail);
        mailCreator.send(mail);
        System.out.println("Mail sended");
    }
}
