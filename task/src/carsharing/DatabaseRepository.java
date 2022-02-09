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
        String sCreateCustomerTable = "CREATE TABLE IF NOT EXISTS CUSTOMER" +
                "(ID INTEGER AUTO_INCREMENT, " +
                "NAME VARCHAR(100) UNIQUE NOT NULL, " +
                "RENTED_CAR_ID INTEGER NULL, " +
                "PRIMARY KEY (ID), " +
                "CONSTRAINT fk_rented_car_id " +
                "FOREIGN KEY (RENTED_CAR_ID) " +
                "REFERENCES CAR(ID)" +
                ")";
        try (Connection conn = connect();
             Statement statement = conn.createStatement()) {
            statement.executeUpdate(sCreateCompanyTable);
            statement.executeUpdate(sCreateCarTable);
            statement.executeUpdate(sCreateCustomerTable);
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

    public List<Car> getAllCompanyCars(int companyId) {
        List<Car> companyCars = new ArrayList<>();
        String sqlQuery = "SELECT * FROM CAR WHERE COMPANY_ID = ";

        try (Connection conn = connect();
             Statement statement = conn.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sqlQuery + companyId);
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

    public List<Car> getAllCompanyAvailableCars(int companyId) {
        List<Car> availableCars = new ArrayList<>();
        String sqlQuery = "SELECT CAR.ID, CAR.NAME, CAR.COMPANY_ID " +
                "FROM CAR LEFT JOIN CUSTOMER " +
                "ON CAR.ID = CUSTOMER.RENTED_CAR_ID " +
                "WHERE CUSTOMER.NAME IS NULL AND CAR.COMPANY_ID = ";

        try (Connection conn = connect();
             Statement statement = conn.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sqlQuery + companyId);
            while (resultSet.next()) {
                Car car = new Car();
                car.setID(resultSet.getInt("ID"));
                car.setName(resultSet.getString("NAME"));
                car.setCompanyId(resultSet.getInt("COMPANY_ID"));
                availableCars.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return availableCars;
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

    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sqlQuery = "SELECT * FROM CUSTOMER";

        try (Connection conn = connect();
             Statement statement = conn.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sqlQuery);

            while (resultSet.next()) {
                Customer customer = new Customer();
                customer.setID(resultSet.getInt("ID"));
                customer.setName(resultSet.getString("NAME"));
                customer.setRentedCarId(resultSet.getInt("RENTED_CAR_ID"));
                customers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    public void insertCustomer(String name) {
        String sInsert = "INSERT INTO CUSTOMER (NAME) VALUES (?)";
        String sqlQuery = "SELECT * FROM CUSTOMER";
        String resetId = "ALTER TABLE CUSTOMER ALTER COLUMN ID RESTART WITH 1";

        try (Connection conn = connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sInsert);
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {

            if (!resultSet.next()) {
                statement.executeUpdate(resetId);
            }
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
            System.out.println("The customer was added!\n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getRentedCarByCustomerId(Integer customerId) {
        String sqlQuery = "SELECT CAR.NAME, COMPANY.NAME FROM CUSTOMER, CAR, COMPANY " +
                "WHERE CAR.COMPANY_ID = COMPANY.ID " +
                "AND CUSTOMER.RENTED_CAR_ID = CAR.ID " +
                "AND CUSTOMER.ID = ";

        try (Connection conn = connect();
             Statement statement = conn.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sqlQuery + customerId);
            if (resultSet.next()) {
                System.out.println("\nYour rented car:" +
                        "\n" + resultSet.getString(1) +
                        "\nCompany:" +
                        "\n" + resultSet.getString(2));
            } else {
                System.out.println("\nYou didn't rent a car!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void rentACar(Integer customerId, Integer carId) {
        String sInsertCar = "UPDATE CUSTOMER SET RENTED_CAR_ID = ? WHERE ID = ?";
        String sqlQuery = "SELECT * FROM CAR WHERE ID = ";
        try (Connection conn = connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sInsertCar);
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery + carId)) {
            if (resultSet.next()) {
                preparedStatement.setInt(1, carId);
                preparedStatement.setInt(2, customerId);
                preparedStatement.executeUpdate();
                System.out.println("You rented '" + resultSet.getString("NAME") + "'\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void returnRentedCar(Integer customerId) {
        String sInsertCar = "UPDATE CUSTOMER SET RENTED_CAR_ID = NULL WHERE ID = ?";
        try (Connection conn = connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sInsertCar);
             Statement statement = conn.createStatement()) {
                preparedStatement.setInt(1, customerId);
                preparedStatement.executeUpdate();
                System.out.println("You've returned a rented car!\n");
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
