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
        String sCreateCompanyTable = "CREATE TABLE IF NOT EXISTS COMPANY " +
                "(ID INTEGER AUTO_INCREMENT, " +
                "NAME VARCHAR(100) UNIQUE NOT NULL," +
                "PRIMARY KEY (ID)" +
                ")";
        String sCreateCarTable = "CREATE TABLE IF NOT EXISTS CAR" +
                "(ID INTEGER AUTO_INCREMENT, " +
                "NAME VARCHAR(100) UNIQUE NOT NULL, " +
                "COMPANY_ID INTEGER NOT NULL, " +
                "PRIMARY KEY (ID), " +
                "CONSTRAINT fk_company_id " +
                "FOREIGN KEY (COMPANY_ID) " +
                "REFERENCES COMPANY(ID)" +
                ")";
        try (Connection conn = connect();
             Statement statement = conn.createStatement()) {
            statement.executeUpdate(sCreateCompanyTable);
            statement.executeUpdate(sCreateCarTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertCompany(String name) {
        String sInsert = "INSERT INTO COMPANY (NAME) VALUES (?)";
        String sqlQuery = "SELECT * FROM COMPANY";
        String resetId = "ALTER TABLE COMPANY ALTER COLUMN ID RESTART WITH 1";

        try (Connection conn = connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sInsert);
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {

            if (!resultSet.next()) {
                statement.executeUpdate(resetId);
            }
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

    public List<Car> getAllCompanyCars(int carId) {
        List<Car> companyCars = new ArrayList<>();
        String sqlQuery = "Select * FROM CAR WHERE COMPANY_ID = ";

        try (Connection conn = connect();
             Statement statement = conn.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sqlQuery + carId);
            while (resultSet.next()) {
                Car car = new Car();
                car.setID(resultSet.getInt("ID"));
                car.setName(resultSet.getString("NAME"));
                car.setCompanyId(resultSet.getInt("COMPANY_ID"));
                companyCars.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return companyCars;
    }

    public void insertCar(String name, int companyId) {
        String sInsert = "INSERT INTO CAR (NAME, COMPANY_ID) VALUES (?, ?)";
        try (Connection conn = connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sInsert)) {
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, companyId);
            preparedStatement.executeUpdate();
            System.out.println("The car was added!\n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
