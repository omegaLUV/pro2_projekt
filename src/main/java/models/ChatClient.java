package models;

import java.awt.event.ActionListener;
import java.util.List;

public interface ChatClient {
    Boolean isAuthenticated();
    void login(String userName);
    void  logout();
    void sendMessage(String text);
    List<String> getLoggedUsers();
    List<Message> getMessages();


    void addListenerLoggedUsersChanged(ActionListener toAdd);
}
