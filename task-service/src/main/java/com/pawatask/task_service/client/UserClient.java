package com.pawatask.task_service.client;

import com.pawatask.task_service.dto.UserInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

@Component
public class UserClient {

    @Value("${user-service.url}")
    private String userServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public UserInfo getCurrentUser(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<UserInfo> response = restTemplate.exchange(
                userServiceUrl + "/api/users/me",
                HttpMethod.GET,
                entity,
                UserInfo.class
        );

        return response.getBody();
    }
}
