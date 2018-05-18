package inventory.DAO;

import inventory.Entity.Employee;

import java.sql.SQLException;
import java.util.List;

public interface EmployeeDAO {

    /**
     * Method to query list of all employees
     *
     * @return a list of all employees
     */
    public List<Employee> queryEmployees();

    /**
     * Method that returns a Employee instance that matches an ID given
     * @param employeeID the ID given to the method to make the search on the database
     * @return the Employee instance that has got the same ID that was given
     */
    public Employee getEmployeeByID(int employeeID);

    /**
     * Method that returns a Employee instance that matches a name given
     * @param employeeName the name given to the method to make the search on the database
     * @return the Employee instance that has got the same name that was given
     */
    public Employee getEmployeeByName(String employeeName);

    /**
     * Method that returns a Employee instance that matches a username given
     * @param employeeUsername the username given to the method to make the search on the database
     * @return the Employee instance that has got the same username that was given
     */
    public Employee getEmployeeByUsername(String employeeUsername);

    /**
     * Method to insert employee using
     * @param employee object that we want to insert
     * @return generated ID of the new employee
     * @throws SQLException in case inserting the new employee fails
     */
    public int insertEmployee(Employee employee) throws SQLException;

    /**
     * Method to insert employee using
     * @param name of the new employee
     * @param phone of the new employee
     * @param username of the new employee
     * @param password of the new employee
     * @return generated ID of the new employee
     * @throws SQLException in case inserting the new employee fails
     */
    public int insertEmployee(String name, String phone, String username, String password) throws SQLException;

    /**
     * Method to update an employee using
     * @param employeeID of the employee that we want to update
     * @param newName of the employee
     * @param newPhone of the employee
     * @param newUsername of the employee
     * @param newPassword of the employee
     * @return true if succeeded, false if failed
     */
    public boolean updateEmployee(int employeeID, String newName, String newPhone, String newUsername, String newPassword);

    /**
     * Method to delete an employee
     * @param employeeID of the employee that we want to delete
     * @return true if succeeded, false if failed
     */
    public boolean deleteEmployee(int employeeID);

    /**
     * Method to check Login Informations
     * @param username
     * @param password
     * @return true if informations are true, false otherwise
     */
    public boolean checkLogin(String username, String password);
}
