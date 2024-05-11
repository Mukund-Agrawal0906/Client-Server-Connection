import java.io.Serializable;

class Employee implements Serializable {
    private int employeeID;
    private String name;
    private double salary;
    private String district;
    private String employeeType;

    public Employee(int employeeID, String name, double salary, String district, String employeeType) {
        this.employeeID = employeeID;
        this.name = name;
        this.salary = salary;
        this.district = district;
        this.employeeType = employeeType;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public String getName() {
        return name;
    }

    public double getSalary() {
        return salary;
    }

    public String getDistrict() {
        return district;
    }

    public String getEmployeeType() {
        return employeeType;
    }
}

class TempEmployee extends Employee {
    public TempEmployee(int employeeID, String name, double salary, String district) {
        super(employeeID, name, salary, district, "Temporary");
    }
}

class PermanentEmployee extends Employee {
    public PermanentEmployee(int employeeID, String name, double salary, String district) {
        super(employeeID, name, salary, district, "Permanent");
    }
}
