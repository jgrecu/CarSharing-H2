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
        String sCreateDb = "CREATE TABLE IF NOT EXISTS COMPANY " +
                "(ID INTEGER not NULL, " +
                "NAME VARCHAR(64)" +
                "PRIMARY KEY (ID)" +
                ")";
        try (Connection conn = connect();
             Statement statement = conn.createStatement()) {
            statement.executeUpdate(sCreateDb);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertCompany(String name) {
        String sInsert = "INSERT INTO COMPANY (NAME) VALUES (?)";
        try (Connection conn = connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sInsert)) {
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection connect() {
        final String DB_URL = "jdbc:h2:./src/carsharing/db/";
        Connection connection = null;

        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(DB_URL + databaseFileName);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
