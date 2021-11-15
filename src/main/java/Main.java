import gui.MainFrame;
import models.*;

public class Main {

    public static void main(String[] args) {
        ChatClient chatClient = new WebChatClient();
         MainFrame mainFrame = new MainFrame(800,600, chatClient);
         mainFrame.setVisible(true);
    }

    private static void testChat() {
        ChatClient chatClient = new InMemoryChatClient();

        chatClient.login("Matvei");
        System.out.println("is logged: " + chatClient.isAuthenticated());

        System.out.println("logged users: ");
        for (String user:chatClient.getLoggedUsers()) {
            System.out.println(user);
        }


        System.out.println("message 1");
        chatClient.sendMessage("hello world");

        System.out.println("message 2");
        chatClient.sendMessage("ahoj");


        System.out.println("logging out");
        chatClient.logout();

        System.out.println("is logged: " + chatClient.isAuthenticated());

        System.out.println("all messages");
        for (Message message:chatClient.getMessages()) {
            System.out.println(message.toString());
        }
    }
}
