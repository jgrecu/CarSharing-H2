package carsharing;

public class Company {
    private int ID;
    private String name;

    public Company() {
    }
    public Company(int id, String name) {
        ID = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    @Override
    public String toString() {
        return "Company{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                '}';
    }
}
