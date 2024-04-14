package com.webservice.MatchCraft.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.webservice.MatchCraft.model.Message;
import com.webservice.MatchCraft.model.OnlineStatusMessage;
import com.webservice.MatchCraft.service.MessageService;
import com.webservice.MatchCraft.service.UserService;

@Controller
public class ChatController {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload Message chatMessage) {
        Message savedMessage = messageService.saveMessage(chatMessage);
        messagingTemplate.convertAndSend("/topic/publicChat", savedMessage);
    }

    @MessageMapping("/chat.newUser")
    public void newUser(@Payload Message chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        if (chatMessage.getSender() != null && chatMessage.getSender().getId() != null) {
            Integer userId = chatMessage.getSender().getId();
            userService.setUserOnlineStatus(userId, true);
            // Assuming OnlineStatusMessage is a valid class and properly handles userId.
            // Replace OnlineStatusMessage with your actual implementation
            OnlineStatusMessage statusMessage = new OnlineStatusMessage(userId, true);
            messagingTemplate.convertAndSend("/topic/onlineUsers", statusMessage);
        } else {
            // Log or handle the case where sender information is missing
            System.out.println("Sender information is missing in the message");
        }
    }


    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.wrap(event.getMessage());
        Integer userId = headerAccessor.getSessionAttributes() != null ? (Integer) headerAccessor.getSessionAttributes().get("userId") : null;
        if (userId != null) {
            userService.setUserOnlineStatus(userId, false);
            // Ensure OnlineStatusMessage and the following method call are adjusted to use Integer for userId.
            messagingTemplate.convertAndSend("/topic/onlineUsers", new OnlineStatusMessage(userId, false));
        }
    }
}
