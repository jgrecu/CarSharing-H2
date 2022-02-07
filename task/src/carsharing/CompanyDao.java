package carsharing;

import java.util.List;

public interface CompanyDao {
    public List<Company> getAllCompanies();
    public void addCompany(String name);
//    public Company getCompanyById(int id);
//    public void updateCompany(Company company);
//    public void deleteCompany(Company company);
}
