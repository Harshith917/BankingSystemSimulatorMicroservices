package com.bankingSystem.feign;

import com.bankingSystem.dto.NotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class NotificationFallbackFactory implements FallbackFactory<NotificationClient> {

    private static final Logger log = LoggerFactory.getLogger(NotificationFallbackFactory.class);

    @Override
    public NotificationClient create(Throwable cause) {
        return request -> {
            log.warn("⚠ Notification failed because: {}", cause.toString());
            log.warn("Skipping notification. Request: {}", request);
        };
    }
}
