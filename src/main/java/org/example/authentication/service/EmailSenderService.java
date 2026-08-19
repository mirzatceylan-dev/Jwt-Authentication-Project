package org.example.authentication.service;

public interface EmailSenderService { // dependency injection araştır

   void sendEmail(String to, String subject, String body);

}
