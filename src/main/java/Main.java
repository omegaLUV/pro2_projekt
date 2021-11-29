import gui.MainFrame;
import models.*;
import models.database.DatabaseOperations;
import models.database.DbInitializer;
import models.database.JdbcDatabaseOperations;

public class Main {

    public static void main(String[] args) {

        String databaseDriver = "org.apache.derby.jdbc.EmbeddedDriver";
        String url = "jdbc:derby:ClientChatDb";

        try{
            DbInitializer dbInitializer = new DbInitializer(databaseDriver, url);
            //dbInitializer.init();

            ChatClient chatClient = new WebChatClient();

            DatabaseOperations databaseOperations = new JdbcDatabaseOperations(databaseDriver, url);
            chatClient = new DatabaseChatClient(databaseOperations);

            MainFrame mainFrame = new MainFrame(800,600, chatClient);
            mainFrame.setVisible(true);
        } catch(Exception e) {
            e.printStackTrace();
        }
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
