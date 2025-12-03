package com.bankingSystem.controller;

import com.bankingSystem.dto.NotificationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class NotificationControllerTest {

    @Test
    void testSendNotification() {
        NotificationController controller = new NotificationController();

        NotificationRequest req =
                new NotificationRequest("ashu@gmail.com", "Test Message");

        ResponseEntity<Void> response = controller.sendNotification(req);

        assertEquals(202, response.getStatusCodeValue());
    }
}
