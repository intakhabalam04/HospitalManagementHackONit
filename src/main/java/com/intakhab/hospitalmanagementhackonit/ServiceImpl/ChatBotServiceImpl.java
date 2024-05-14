package com.intakhab.hospitalmanagementhackonit.ServiceImpl;

import com.intakhab.hospitalmanagementhackonit.Model.ChatBot;
import com.intakhab.hospitalmanagementhackonit.Model.ChatBotDb;
import com.intakhab.hospitalmanagementhackonit.Model.User;
import com.intakhab.hospitalmanagementhackonit.Repository.ChatBotRepo;
import com.intakhab.hospitalmanagementhackonit.Service.ChatBotDbService;
import com.intakhab.hospitalmanagementhackonit.Service.ChatBotService;
import com.intakhab.hospitalmanagementhackonit.Service.SecurityService;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
public class ChatBotServiceImpl implements ChatBotService {

    private static final String FLASK_SERVER_URL = "http://localhost:5000";

    private final ChatBotDbService chatBotDbService;
    private final ChatBotRepo chatBotRepo;

    private final SecurityService securityService;

    public ChatBotServiceImpl(ChatBotDbService chatBotDbService, ChatBotRepo chatBotRepo, SecurityService securityService) {
        this.chatBotDbService = chatBotDbService;
        this.chatBotRepo = chatBotRepo;
        this.securityService = securityService;
    }


    @Override
    public ChatBot getResponse(String message) {
        System.out.println(message);
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = FLASK_SERVER_URL + "/chat";
            String requestPayLoad = "{\"input\": \"" + message + "\"}";
            ChatBotResponse response = restTemplate.postForObject(url, requestPayLoad, ChatBotResponse.class);

            System.out.println(response);

            assert response != null;
            if (response.getQuestionNo().equals("1")) {
                ChatBotDb chatBotDb = new ChatBotDb();
                User user = securityService.currentUser();
                chatBotDb.setChatBotTime(LocalDateTime.now());
                chatBotDb.setPatientName(user.getName());
                chatBotRepo.save(chatBotDb);
            } else if (response.getQuestionNo().equals("2")) {
                ChatBotDb chatBotDb = chatBotDbService.getLatestChat();
                chatBotDb.setSymptom(response.getUser_symptom());
                chatBotRepo.save(chatBotDb);
            }


            return new ChatBot(message, response.getResponse());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Data
    public static class ChatBotResponse {
        private String response;
        private String questionNo;
        private String user_symptom;
    }
}


