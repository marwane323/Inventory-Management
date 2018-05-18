package inventory.Entity;

public class Employee {

    private int employeeID;
    private String employeeName;
    private String employeePhone;
    private String username;
    private String password;

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeePhone() {
        return employeePhone;
    }

    public void setEmployeePhone(String employeePhone) {
        this.employeePhone = employeePhone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return " Employee's ID: " + employeeID +
               " Employee's Name: " + employeeName +
               " Employee's Phone: " + employeePhone +
               " Employee's Username: " + username +
               " Employee's Password: " + password;
    }
}
