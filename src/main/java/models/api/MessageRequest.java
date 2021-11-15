package models.api;

import models.Message;

public class MessageRequest {
    private String token;
    private String text;

    public MessageRequest(String token, Message message) {
        this.token = token;
        text = message.toString();
    }
}

