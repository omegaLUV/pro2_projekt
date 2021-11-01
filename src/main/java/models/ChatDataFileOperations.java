package models;

import java.util.List;
import java.util.Stack;

public interface ChatDataFileOperations {
    public List<Message> readMessages();
    void writeMessages(List<Message> messages);
    List<String> readUsers();
    void writeUsers(List<String> users);
}
