package models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ToFileChatClient implements ChatClient {
    private String loggedUser;
    private List<Message> messages;
    List<String> loggedUsers;

    private final List<ActionListener> listenersLoggedUsersChanged = new ArrayList<>();

    private ChatDataFileOperations fileOperations;

    Gson gson;

    private static final String MESSAGES_PATH = "./messages.json";

    public ToFileChatClient() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        messages = new ArrayList<>();
        loggedUsers = new ArrayList<>();

        readMessagesFromFile();
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
        writeMessagesToFile();
    }

    private void writeMessagesToFile() {
        String jsonText = gson.toJson(messages);
        try {
            FileWriter writer = new FileWriter(MESSAGES_PATH);
            writer.write(jsonText);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readMessagesFromFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(MESSAGES_PATH));
            StringBuilder jsonText = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null) {
                jsonText.append(line);
            }

            Type targetType = new TypeToken<ArrayList<Message>>(){}.getType();

            messages= gson.fromJson(jsonText.toString(), targetType);

            System.out.println("a");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //dve tridy na Gson a CSV (insperace ChatClient)
    //ukladani uzivatelu
}
