package com.interview.userservice.feign;

import com.interview.userservice.dto.request.SendNotificationRequest;
import com.interview.userservice.dto.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service", url = "${notification.service.url}")
public interface NotificationServiceClient {

    @PostMapping("/api/v1/notifications/send")
    ApiResponse<Object> sendNotification(@RequestBody SendNotificationRequest request);
}
