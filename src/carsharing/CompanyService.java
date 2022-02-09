package carsharing;

import java.util.List;

public class CompanyService implements CompanyDao {
    private final DatabaseRepository db;

    public CompanyService(String dbName) {
        this.db = DatabaseRepository.getInstance(dbName);
    }

    @Override
    public List<Company> getAllCompanies() {
        return db.getAllCompanies();
    }

    @Override
    public void addCompany(String name) {
        db.insertCompany(name);
    }

    @Override
    public List<Car> getAllCompanyCars(int companyId) {
        return db.getAllCompanyCars(companyId);
    }

    @Override
    public void addCar(String name, int companyId) {
        db.insertCar(name, companyId);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return db.getAllCustomers();
    }

    @Override
    public void addCustomer(String name) {
        db.insertCustomer(name);
    }

    @Override
    public void getRentedCar(Integer customerId) {
        db.getRentedCarByCustomerId(customerId);
    }

    @Override
    public void rentACar(Integer customerId, Integer carId) {
        db.rentACar(customerId, carId);
    }

    @Override
    public List<Car> getAllCompanyAvailableCars(int companyId) {
        return db.getAllCompanyAvailableCars(companyId);
    }

    @Override
    public void returnCar(Integer customerId) {
        db.returnRentedCar(customerId);
    }
}
