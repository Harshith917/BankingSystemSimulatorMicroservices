package com.bankingSystem.controller;

import com.bankingSystem.dto.NotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private static final Logger log = LoggerFactory.getLogger(NotificationController.class);

    @PostMapping("/send")
    public ResponseEntity<Void> sendNotification(@RequestBody NotificationRequest request) {

//        log.info("Sending EMAIL to '{}': MESSAGE: {}", request.getEmail(), request.getMessage());
        log.info("📩 Sending EMAIL to '" + request.getEmail() + "' — MESSAGE: " + request.getMessage());


        return ResponseEntity.accepted().build();
    }
}
