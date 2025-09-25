package com.example.billiardsstore.controller;

import com.example.billiardsstore.dto.contact.ContactRequest;
import com.example.billiardsstore.service.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @PostMapping
    public ResponseEntity<String> submitContactForm(@RequestBody @Valid ContactRequest request) {
        contactService.processContactForm(request);
        return ResponseEntity.ok("Tin nhắn của bạn đã được gửi thành công! Chúng tôi sẽ phản hồi trong vòng 24 giờ.");
    }
}