package com.project.revhive.demo.model;

import lombok.Data;

import java.util.Date;

@Data
public class ChatMessage {
    private String sender;
    private String receiver;
    private String content;
    private String type;

    public void setTimestamp(Date date) {
    }
}
