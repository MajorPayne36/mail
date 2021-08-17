package com.example.mail.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailCreator {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private HTMLGetter htmlGetter;

    @Value("${spring.mail.username}")
    private String username;

    private SimpleMailMessage createMailMessage(String mail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(username);
        message.setTo(mail);
        message.setSubject("Отчет о состоянии сайтов");

        // Create message body
        StringBuilder text = new StringBuilder();

        text.append("Здраствуйте, дорогая и.о. секретаря");
        text.append("\nЗа последние сутки во вверенных Вам сайтах произошли следующие изменения:");

        var missingUrls = htmlGetter.getMissingUrls();
        if (missingUrls.size() != 0) {
            text.append("\nИсчезли следующие страницы:");
            missingUrls.forEach((key, value) -> text.append("\n  --").append(key));
        }

        var newUrls = htmlGetter.getNewUrls();
        if (newUrls.size() != 0) {
            text.append("\nПоявились следующие новые страницы");
            newUrls.forEach((key, value) -> text.append("\n  --").append(key));
        }

        var modifiedUrls = htmlGetter.getModifiedUrls();
        if (modifiedUrls.size() != 0) {
            text.append("\nИзменились следующие страницы");
            modifiedUrls.forEach((key, value) -> text.append("\n  --").append(key));
        }

        if (modifiedUrls.size() == 0 && newUrls.size() == 0 && missingUrls.size() == 0) {
            text.append("\n<b>Изменений не произошло<b>");
        }

        message.setText(text.toString());
        return message;
    }

    public void send(String mail) {
        emailSender.send(createMailMessage(mail));
    }
}
