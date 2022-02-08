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
                    mainMenu();
                    break;
                case "0":
                    scanner.close();
                    return;
                default:
                    break;
            }
        }
    }

    private void mainMenu() {
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

    private void printLoginMenu() {
        System.out.println("1. Log in as a manager\n" +
                "0. Exit");
    }

    private void printMainMenu() {
        System.out.println("\n1. Company list\n" +
                "2. Create a company\n" +
                "0. Back");
    }

}
