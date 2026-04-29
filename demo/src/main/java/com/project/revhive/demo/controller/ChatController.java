package com.project.revhive.demo.controller;

import com.project.revhive.demo.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Date;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;


    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage message, Principal principal) {
        String sender = principal.getName();

        message.setSender(sender);
        message.setTimestamp(new Date());


        messagingTemplate.convertAndSendToUser(
                message.getReceiver(),
                "/queue/messages",
                message
        );
    }
}
