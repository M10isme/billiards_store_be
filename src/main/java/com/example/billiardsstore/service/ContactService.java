package com.example.billiardsstore.service;

import com.example.billiardsstore.dto.contact.ContactRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactService {

    public void processContactForm(ContactRequest request) {
        // Log the contact form submission
        log.info("Contact form submitted at {}: Name={}, Email={}, Subject={}", 
                LocalDateTime.now(), request.getName(), request.getEmail(), request.getSubject());
        
        // Here you could:
        // 1. Save to database for admin review
        // 2. Send email notification to admin
        // 3. Send confirmation email to customer
        // 4. Integrate with CRM system
        
        // For now, we'll just log it
        log.info("Contact message: {}", request.getMessage());
        
        // In a real application, you might want to:
        // - Save to a ContactMessage entity
        // - Send email using EmailService
        // - Create a ticket in support system
    }
}