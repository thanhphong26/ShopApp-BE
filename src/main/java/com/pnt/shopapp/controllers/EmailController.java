package com.pnt.shopapp.controllers;

import com.pnt.shopapp.requests.EmailRequest;
import com.pnt.shopapp.services.email.MailSenderCustom;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.thymeleaf.context.Context;

@RestController
@AllArgsConstructor
@RequestMapping("${api.prefix}/email")
public class EmailController {
    private final MailSenderCustom emailService;
    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest emailRequest) {
        Context context = new Context();
        context.setVariable("name", emailRequest.getName());
        context.setVariable("message", emailRequest.getMessage());
        try {
            emailService.sendMessageHtml(emailRequest.getTo(), emailRequest.getSubject(), "email-template", context);
            return ResponseEntity.ok("Email sent successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error sending email: " + e.getMessage());
        }
    }
}
