package carsharing;

import java.util.List;
import java.util.Scanner;

public class CompanyService {
    private final Scanner scanner;
    private final DatabaseRepository db;

    public CompanyService(String dbName) {
        this.scanner = new Scanner(System.in);
        this.db = DatabaseRepository.getInstance(dbName);
        this.db.createDatabase();
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
        List<Company> companies = db.getAllCompanies();
        if (companies.isEmpty()) {
            System.out.println("\nThe company list is empty!");
        } else {
            System.out.println("\nCompany list:");
            companies.forEach(company -> System.out.printf("%d. %s%n", company.getID(), company.getName()));
        }
    }

    private void createCompany() {
        System.out.println("\nEnter the company name:");
        String companyName = scanner.nextLine();
        db.insertCompany(companyName);
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
