package carsharing;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseRepository {
    private final String databaseFileName;
    private static DatabaseRepository INSTANCE = null;

    private DatabaseRepository(String databaseFileName) {
        this.databaseFileName = databaseFileName;
        createDatabase();
    }

    public static DatabaseRepository getInstance(String databaseFileName) {
        if (INSTANCE == null) {
            INSTANCE = new DatabaseRepository(databaseFileName);
            return INSTANCE;
        }
        return INSTANCE;
    }

    private void createDatabase() {
        String sCreateDb = "CREATE TABLE IF NOT EXISTS COMPANY " +
                "(ID INTEGER AUTO_INCREMENT, " +
                "NAME VARCHAR(64) UNIQUE NOT NULL," +
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
            System.out.println("The company was created!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Company> getAllCompanies() {
        List<Company> companies = new ArrayList<>();
        String sqlQuery = "SELECT * FROM COMPANY";

        try (Connection conn = connect();
             Statement statement = conn.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            while (resultSet.next()) {
                Company company = new Company();
                company.setID(resultSet.getInt("ID"));
                company.setName(resultSet.getString("NAME"));
                companies.add(company);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return companies;
    }

    private Connection connect() {
        final String DB_URL = "jdbc:h2:./src/carsharing/db/";
        Connection connection = null;

        try {
            Class.forName("org.h2.Driver");
//            connection = DriverManager.getConnection(DB_URL + databaseFileName, "sa", "");
            connection = DriverManager.getConnection(DB_URL + databaseFileName);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

}
