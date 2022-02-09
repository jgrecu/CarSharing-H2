package carsharing;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class CompanyController {
    private final Scanner scanner;
    private final CompanyService dbService;

    public CompanyController(String dbName) {
        this.scanner = new Scanner(System.in);
        this.dbService = new CompanyService(dbName);
    }

    public void run() {
        while (true) {
            printLoginMenu();
            String option = scanner.nextLine();
            switch (option) {
                case "1":
                    managerMenu();
                    break;
                case "2":
                    customerList();
                    break;
                case "3":
                    addCustomer();
                    break;
                case "0":
                    scanner.close();
                    return;
                default:
                    break;
            }
        }
    }

    private void managerMenu() {
        while (true) {
            printMainMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    companyList();
                    break;
                case "2":
                    createCompany();
                    break;
                case "0":
                    return;
                default:
                    break;
            }
        }
    }

    private void companyMenu(Company company) {
        System.out.println("\n'" + company.getName() + "' company:");
        while (true) {
            System.out.println("1. Car list\n" +
                    "2. Create a car\n" +
                    "0. Back");
            String option = scanner.nextLine();
            switch (option) {
                case "0":
                    return;
                case "1":
                    carList(company);
                    break;
                case "2":
                    addCar(company.getID());
                    break;
                default:
                    break;
            }
        }
    }

    private void customerMenu(Customer customer) {
        while (true) {
            System.out.println("\n1. Rent a car\n" +
                    "2. Return a rented car\n" +
                    "3. My rented car\n" +
                    "0. Back");
            String option = scanner.nextLine();

            switch (option) {
                case "0":
                    return;
                case "1":
                    rentACarMenu(customer);
                    break;
                case "2":
                    returnCar(customer);
                    break;
                case "3":
                    rentedCarList(customer);
                    break;
                default:
                    break;
            }
        }
    }

    private void companyList() {
        List<Company> companies = dbService.getAllCompanies();
        if (companies.isEmpty()) {
            System.out.println("\nThe company list is empty!");
        } else {
            System.out.println("\nChoose a company:");
            companies.forEach(company -> System.out.printf("%d. %s%n", company.getID(), company.getName()));
            System.out.println("0. Back");

            String option = scanner.nextLine();

            Optional<Company> companyOptional = companies.stream()
                    .filter(company -> Integer.toString(company.getID()).equals(option))
                    .findAny();
            companyOptional.ifPresent(this::companyMenu);
        }
    }

    private void carList(Company company) {
        List<Car> cars = dbService.getAllCompanyCars(company.getID());
        if (cars.isEmpty()) {
            System.out.println("\nThe car list is empty!\n");
        } else {
            int carId = 1;
            System.out.println("\n'" + company.getName() + "' cars:");
            for (Car car : cars) {
                System.out.printf("%d. %s%n", carId++, car.getName());
            }
            System.out.println();
        }
    }

    private void customerList() {
        List<Customer> customers = dbService.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("\nThe customer list is empty!\n");
        } else {
            System.out.println("\nChoose a customer:");
            customers.forEach(customer -> System.out.printf("%d. %s%n", customer.getID(), customer.getName()));
            System.out.println("0. Back");
            String option = scanner.nextLine();

            Optional<Customer> customerOptional = customers.stream()
                    .filter(customer -> Integer.toString(customer.getID()).equals(option))
                    .findAny();
            customerOptional.ifPresent(this::customerMenu);
        }
    }

    private void customerCompanyList(Customer customer) {
        List<Company> companies = dbService.getAllCompanies();
        if (companies.isEmpty()) {
            System.out.println("\nThe company list is empty!");
        } else {
            System.out.println("\nChoose a company:");
            companies.forEach(company -> System.out.printf("%d. %s%n", company.getID(), company.getName()));
            System.out.println("0. Back");

            String option = scanner.nextLine();

            Optional<Company> companyOptional = companies.stream()
                    .filter(company -> Integer.toString(company.getID()).equals(option))
                    .findAny();
            companyOptional.ifPresent(company -> companyCarList(company, customer));
        }
    }

    private void companyCarList(Company company, Customer customer) {
        List<Car> cars = dbService.getAllCompanyAvailableCars(company.getID());
        if (cars.isEmpty()) {
            System.out.println("\nNo available cars in the '" + company.getName() + "' company");
        } else {
            int carId = 1;
            System.out.println("\nChoose a car:");
            for (Car car : cars) {
                System.out.printf("%d. %s%n", carId++, car.getName());
            }
            System.out.println("0. Back");

            String option = scanner.nextLine();

            if ("0".equals(option)) {
                return;
            }

            Car carOptional = cars.get(Integer.parseInt(option) - 1);

            dbService.rentACar(customer.getID(), carOptional.getID());
            customer.setRentedCarId(carOptional.getID());
        }
    }

    private void rentedCarList(Customer customer) {
        dbService.getRentedCar(customer.getID());
    }

    private void rentACarMenu(Customer customer) {
        if (customer.getRentedCarId() > 0) {
            System.out.println("\nYou've already rented a car!");
        } else {
            customerCompanyList(customer);
        }
    }

    private void returnCar(Customer customer) {
        if (customer.getRentedCarId() > 0) {
            dbService.returnCar(customer.getID());
            customer.setRentedCarId(0);
        } else {
            System.out.println("\nYou didn't rent a car!");
        }
    }

    private void createCompany() {
        System.out.println("\nEnter the company name:");
        String companyName = scanner.nextLine();
        dbService.addCompany(companyName);
    }

    private void addCar(int company_id) {
        System.out.println("\nEnter the car name:");
        String carName = scanner.nextLine();
        dbService.addCar(carName, company_id);
    }

    private void addCustomer() {
        System.out.println("\nEnter the customer name:");
        String customerName = scanner.nextLine();
        dbService.addCustomer(customerName);
    }

    private void printLoginMenu() {
        System.out.println("1. Log in as a manager\n" +
                "2. Log in as a customer\n" +
                "3. Create a customer\n" +
                "0. Exit");
    }

    private void printMainMenu() {
        System.out.println("\n1. Company list\n" +
                "2. Create a company\n" +
                "0. Back");
    }

}
