package com.project.revhive.demo.service;

import com.project.revhive.demo.model.Message;
import com.project.revhive.demo.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    public void saveMessage(String sender,String receiver ,String content)
    {
        Message message=Message.builder()
                .sender(sender)
                .receiver(receiver)
                .content(content)
                .timestamp(LocalDateTime.now())
                .build();

        messageRepository.save(message);
    }
}
