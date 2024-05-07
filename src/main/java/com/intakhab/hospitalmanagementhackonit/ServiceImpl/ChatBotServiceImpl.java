package com.intakhab.hospitalmanagementhackonit.ServiceImpl;

import com.intakhab.hospitalmanagementhackonit.Model.ChatBot;
import com.intakhab.hospitalmanagementhackonit.Service.ChatBotService;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChatBotServiceImpl implements ChatBotService {

    private static final String FLASK_SERVER_URL = "http://localhost:5000";


    @Override
    public ChatBot getResponse(String message) {
        System.out.println(message);
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = FLASK_SERVER_URL + "/chat";
            String requestPayLoad = "{\"input\": \"" + message + "\"}";
            ChatBotResponse response = restTemplate.postForObject(url, requestPayLoad, ChatBotResponse.class);
            assert response != null;
            return new ChatBot(message, response.getResponse());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Data
    public static class ChatBotResponse {
        private String response;
    }
}


