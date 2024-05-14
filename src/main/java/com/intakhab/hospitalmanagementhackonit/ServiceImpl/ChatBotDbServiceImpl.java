package com.intakhab.hospitalmanagementhackonit.ServiceImpl;

import com.intakhab.hospitalmanagementhackonit.Model.ChatBotDb;
import com.intakhab.hospitalmanagementhackonit.Repository.ChatBotRepo;
import com.intakhab.hospitalmanagementhackonit.Service.ChatBotDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatBotDbServiceImpl implements ChatBotDbService {

    private final ChatBotRepo chatBotRepo;
    @Override
    public ChatBotDb getLatestChat() {
        return chatBotRepo.findAll().stream()
                .max(Comparator.comparing(ChatBotDb::getChatBotTime))
                .orElse(null);
    }
}
