package com.intakhab.hospitalmanagementhackonit.ServiceImpl;

import com.intakhab.hospitalmanagementhackonit.Model.ChatBot;
import com.intakhab.hospitalmanagementhackonit.Model.ChatBotDb;
import com.intakhab.hospitalmanagementhackonit.Model.User;
import com.intakhab.hospitalmanagementhackonit.Repository.ChatBotRepo;
import com.intakhab.hospitalmanagementhackonit.Repository.DoctorRepo;
import com.intakhab.hospitalmanagementhackonit.Service.ChatBotDbService;
import com.intakhab.hospitalmanagementhackonit.Service.ChatBotService;
import com.intakhab.hospitalmanagementhackonit.Service.SecurityService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatBotServiceImpl implements ChatBotService {

    private final ChatBotDbService chatBotDbService;
    private final ChatBotRepo chatBotRepo;
    private final SecurityService securityService;
    private final DoctorRepo doctorRepo;

    @Value("${flask.server.url}")
    private String FLASK_SERVER_URL;

    @Override
    public ChatBot getResponse(String message) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = FLASK_SERVER_URL + "/chat";
            String requestPayLoad = "{\"input\": \"" + message + "\"}";
            ChatBotResponse response = restTemplate.postForObject(url, requestPayLoad, ChatBotResponse.class);


            assert response != null;
            if (response.getQuestionNo().equals("1")) {
                ChatBotDb chatBotDb = new ChatBotDb();
                User user = securityService.currentUser();
                chatBotDb.setChatBotTime(LocalDateTime.now());
                chatBotDb.setPatientName(user.getName());
                chatBotDb.setGender(user.getGender());
                chatBotDb.setDoctor(doctorRepo.findByName("Dr. Ashutosh Shukla").getId());
                chatBotRepo.save(chatBotDb);
            } else if (response.getQuestionNo().equals("2")) {
                ChatBotDb chatBotDb = chatBotDbService.getLatestChat();
                chatBotDb.setSymptom(response.getUser_symptom());
                chatBotRepo.save(chatBotDb);
            }else if(response.getQuestionNo().equals("9")) {
                ChatBotDb chatBotDb = chatBotDbService.getLatestChat();
                chatBotDb.setInsurance(response.getInsurance());
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
        private String insurance;
    }
}


