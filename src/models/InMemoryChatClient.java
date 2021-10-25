package models;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class InMemoryChatClient implements ChatClient {
    private String loggedUser;
    private List<Message> messages;
    List<String> loggedUsers;

    private List<ActionListener> listenersLoggedUsersChanged = new ArrayList<>();

    public InMemoryChatClient() {
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
        raiseEvenLoggedUsersChanged();

        messages.add(new Message(Message.USER_LOGGED_IN, userName));
    }

    @Override
    public void logout() {
        loggedUsers.remove(loggedUser);
        loggedUser = null;
        raiseEvenLoggedUsersChanged();

        messages.add(new Message(Message.USER_LOGGED_OUT, loggedUser));
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

    @Override
    public void addListenerLoggedUsersChanged(ActionListener toAdd) {
        listenersLoggedUsersChanged.add(toAdd);
    }

    private void raiseEvenLoggedUsersChanged(){
        for (ActionListener al: listenersLoggedUsersChanged) {
            al.actionPerformed(new ActionEvent(this, 1,"listenersLoggedUsersChanged"));
        }
    }
}
