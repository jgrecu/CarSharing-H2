package carsharing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

    public static void main(String[] args) {
        String dbName = "carsharing";

        if (args.length > 0 && args[0].equals("-databaseFileName")) {
            dbName = args[1];
        }

        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection conn = DriverManager.getConnection("jdbc:h2:./src/carsharing/db/" + dbName)) {
            conn.setAutoCommit(true);
            Statement st = conn.createStatement();
            st.executeUpdate("CREATE TABLE IF NOT EXISTS COMPANY (" +
                    "ID INTEGER not NULL," +
                    "NAME VARCHAR," +
                    "PRIMARY KEY ( id )" +
                    ")");
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}