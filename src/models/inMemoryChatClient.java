package models;

import java.util.ArrayList;
import java.util.List;

public class inMemoryChatClient implements ChatClient {
    private String loggedUser;
    private List<Message> messages;
    List<String> loggedUsers;

    public inMemoryChatClient() {
        messages = new ArrayList<>();
        loggedUsers = new ArrayList<>();
    }

    @Override
    public Boolean isAuthenticated() {
        return loggedUser != null;
    }

    @Override
    public void login(String userName) {
        loggedUser = userName;
        loggedUsers.add(userName);
    }

    @Override
    public void logout() {
        loggedUsers.remove(loggedUser);
        loggedUser = null;
    }

    @Override
    public void sendMessage(String text) {
        messages.add(new Message(loggedUser, text));
    }

    @Override
    public List<String> getLoggedUsers() {
        return loggedUsers;
    }

    @Override
    public List<Message> getMessages() {
        return messages;
    }
}
