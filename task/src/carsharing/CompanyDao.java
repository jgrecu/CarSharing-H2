package carsharing;

import java.util.List;

public interface CompanyDao {
    public List<Company> getAllCompanies();
    public void addCompany(String name);
    public List<Car> getAllCompanyCars(int carId);
    public void addCar(String name, int companyId);
}
