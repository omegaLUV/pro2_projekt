package models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import models.api.MessageRequest;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WebChatClient implements ChatClient{
    private String loggedUser;
    private String token;

    private List<Message> messages;
    List<String> loggedUsers;

    private final List<ActionListener> listenersLoggedUsersChanged = new ArrayList<>();

    private Gson gson;
    private final String BASE_URL = "http://fimuhkpro22021.aspifyhost.cz";


    public WebChatClient() {
        gson = new Gson();
        messages = new ArrayList<>();
        loggedUsers = new ArrayList<>();

        Runnable refreshLoggedUsersRun = () -> {
            Thread.currentThread().setName("RefreshLoggedUsers");
            while(true) {
                if(isAuthenticated()) {
                    refreshLoggedUsers();
                }
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread threadRefreshLoggedUsers = new Thread(refreshLoggedUsersRun);
        threadRefreshLoggedUsers.start();
    }


    @Override
    public Boolean isAuthenticated() {
        return token != null;
    }

    @Override
    public void login(String userName) {
        String url = BASE_URL + "/api/chat/login";
        HttpPost post = new HttpPost(url);
        StringEntity body = new StringEntity("\""+userName+"\"", "utf-8");
        body.setContentType("application/json");
        post.setEntity(body);
        try(CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(post)) {

            if(response.getStatusLine().getStatusCode() == 200) {
                loggedUser = userName;
                token = EntityUtils.toString(response.getEntity());
                token = token.replaceAll("\"", "");

                addMessage(new Message(Message.USER_LOGGED_IN, loggedUser));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void logout() {
        String url = BASE_URL + "/api/chat/logout";
        HttpPost post = new HttpPost(url);
        StringEntity body = new StringEntity("\""+token+"\"", "utf-8");
        body.setContentType("application/json");
        post.setEntity(body);
        try(CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(post)) {

            if(response.getStatusLine().getStatusCode() == 204) {
                addMessage(new Message(Message.USER_LOGGED_OUT, loggedUser));
                loggedUser = null;
                token = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    }

    private void raiseEvenLoggedUsersChanged(){
        for (ActionListener al: listenersLoggedUsersChanged) {
            al.actionPerformed(new ActionEvent(this, 1,"listenersLoggedUsersChanged"));
        }
    }

    private void addMessage(Message message) {
        try{
            MessageRequest msgRequest = new MessageRequest(token, message);
            String url = BASE_URL + "/api/chat/sendMessage";
            HttpPost post = new HttpPost(url);
            StringEntity body = new StringEntity(gson.toJson(msgRequest), "utf-8");
            body.setContentType("application/json");
            post.setEntity(body);
            try(CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = httpClient.execute(post)) {

                if(response.getStatusLine().getStatusCode() == 200) {
                    refreshMessages();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void refreshLoggedUsers() {
        String url = BASE_URL + "/api/chat/getLoggedUsers";
        HttpGet get = new HttpGet(url);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try(CloseableHttpResponse response = httpClient.execute(get)){
            HttpEntity entity = response.getEntity();
            if(entity != null) {
                String result = EntityUtils.toString(entity);
                loggedUsers = gson.fromJson(result, new TypeToken<ArrayList<String>>(){}.getType());
                raiseEvenLoggedUsersChanged();
            }
        } catch(Exception e) {

        }
    }

    private void refreshMessages() {
        String url = BASE_URL + "/api/chat/getMessages";
        HttpGet get = new HttpGet(url);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try(CloseableHttpResponse response = httpClient.execute(get)){
            HttpEntity entity = response.getEntity();
            if(entity != null) {
                String result = EntityUtils.toString(entity);
                messages = gson.fromJson(result, new TypeToken<ArrayList<Message>>(){}.getType());

                //refresh
            }
        } catch(Exception e) {

        }
    }
}

//rozparsovat DateTime created
