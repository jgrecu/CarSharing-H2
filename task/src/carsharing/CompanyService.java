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
}
