package carsharing;

public class Main {

    public static void main(String[] args) {
        String dbName = "carsharing.db";

        if (args.length > 0 && args[0].equals("-databaseFileName")) {
            dbName = args[1];
        }

        CompanyController gui = new CompanyController(dbName);
        gui.run();
    }
}