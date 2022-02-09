package carsharing;

public class Main {

    public static void main(String[] args) {
        String dbName = "carsharing";

        if (args.length > 1 && args[0].equals("-databaseFileName")) {
            dbName = args[1];
        }

        CompanyController gui = new CompanyController(dbName);
        gui.run();
    }
}