package expense;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBconnection {

    private static final String URL = "jdbc:sqlite:expenses.db";

    public static Connection connect() {
        try {
            Connection conn = DriverManager.getConnection(URL);
            return conn;
        } catch (Exception e) {
            System.out.println("Connection failed");
            return null;
        }
    }
}