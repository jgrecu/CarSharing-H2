package carsharing;

import java.sql.*;

public class DatabaseRepository {
    private final String databaseFileName;
    private static DatabaseRepository INSTANCE = null;

    private DatabaseRepository(String databaseFileName) {
        this.databaseFileName = databaseFileName;
    }

    public static DatabaseRepository getInstance(String databaseFileName) {
        if (INSTANCE == null) {
            INSTANCE = new DatabaseRepository(databaseFileName);
            return INSTANCE;
        }
        return INSTANCE;
    }

    public void createDatabase() {
        String sCreateDb = "CREATE TABLE IF NOT EXISTS COMPANY(ID INTEGER PRIMARY KEY auto_increment,NAME VARCHAR(64))";
        try (Connection conn = connect();
             Statement statement = conn.createStatement()) {
            statement.executeUpdate(sCreateDb);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertCompany(String name) {
        String sInsert = "INSERT INTO COMPANY(NAME) VALUES(?)";
        try (Connection conn = connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sInsert)) {
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection connect() {
        Connection connection = null;
        try {
            Class.forName("org.h2.Driver");
            String url = "jdbc:h2:./src/carsharing/db/";
            connection = DriverManager.getConnection(url + databaseFileName);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
