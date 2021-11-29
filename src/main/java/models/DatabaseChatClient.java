package models;

import models.database.DatabaseOperations;

import javax.xml.crypto.Data;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class DatabaseChatClient implements ChatClient{
    private String loggedUser;
    private List<Message> messages;
    List<String> loggedUsers;

    private List<ActionListener> listenersLoggedUsersChanged = new ArrayList<>();


    private DatabaseOperations databaseOperations;
    public DatabaseChatClient(DatabaseOperations databaseOperations) {
        this.databaseOperations = databaseOperations;
        messages = databaseOperations.readMessages();
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

        addMessage(new Message(Message.USER_LOGGED_IN, userName));
    }

    @Override
    public void logout() {
        loggedUsers.remove(loggedUser);
        loggedUser = null;
        raiseEvenLoggedUsersChanged();

        addMessage(new Message(Message.USER_LOGGED_OUT, loggedUser));
    }

    @Override
    public void sendMessage(String text) {
        addMessage(new Message(loggedUser, text));
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

    private void addMessage(Message message) {
        messages.add(message);
        databaseOperations.addMessage(message);
        //raiseEvenMessagesChanged();
    }

}
