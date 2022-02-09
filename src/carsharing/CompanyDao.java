package carsharing;

import java.util.List;

public interface CompanyDao {
    public List<Company> getAllCompanies();
    public void addCompany(String name);
    public List<Car> getAllCompanyCars(int carId);
    public void addCar(String name, int companyId);
    public List<Customer> getAllCustomers();
    public void addCustomer(String name);
    public void getRentedCar(Integer customerId);
    public void rentACar(Integer customerId, Integer carId);
    public List<Car> getAllCompanyAvailableCars(int companyId);
    public void returnCar(Integer customerId);
}
