package models.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DbInitializer {
    private final String driver;
    private final String url;

    public DbInitializer(String driver, String url) {
        this.driver = driver;
        this.url = url;
    }

    public void init() {
        try{
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url);
            //Vytvorit statement
            Statement statement = connection.createStatement();

//            String dropMessageTableSql = "DROP TABLE ChatMessages";
//            statement.executeUpdate(dropMessageTableSql);

            String createMessageTableSql =
                    "Create table ChatMessages "
                    + "( "
                    + "id INT NOT NULL GENERATED ALWAYS AS IDENTITY "
                    + "CONSTRAINT ChatMessages_pk PRIMARY KEY, "
                    + "author VARCHAR(50), "
                    + "text VARCHAR(1000), "
                    + "created TIMESTAMP "
                    + ")";
            statement.executeUpdate(createMessageTableSql);
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
