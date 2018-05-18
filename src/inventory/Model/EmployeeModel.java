package inventory.Model;

import com.mysql.jdbc.Statement;
import inventory.DAO.EmployeeDAO;
import inventory.Entity.Employee;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EmployeeModel implements EmployeeDAO {

    //Constants for the employees table
    public static final String TABLE_EMPLOYEE = "employee";
    public static final String COLUMN_EMPLOYEE_ID = "employeeID";
    public static final String COLUMN_EMPLOYEE_NAME = "employeeName";
    public static final String COLUMN_EMPLOYEE_PHONE = "employeePhone";
    public static final String COLUMN_EMPLOYEE_PICTURE= "employeePicture";
    public static final String COLUMN_EMPLOYEE_USERNAME = "username";
    public static final String COLUMN_EMPLOYEE_PASSWORD = "password";
    public static final String INDEX_EMPLOYEE_ID = "1";
    public static final String INDEX_EMPLOYEE_NAME = "2";
    public static final String INDEX_EMPLOYEE_PHONE = "3";
    public static final String INDEX_EMPLOYEE_PICTURE= "4";
    public static final String INDEX_EMPLOYEE_USERNAME = "5";
    public static final String INDEX_EMPLOYEE_PASSWORD = "6";

    //Constants for querying employees
    public static final String QUERY_EMPLOYEES =
            "SELECT " + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_ID + ", " +
                        TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_NAME + ", " +
                        TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_PHONE + ", " +
                        TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_PICTURE + ", " +
                        TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_USERNAME + ", " +
                        TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_PASSWORD +
                    " FROM " + TABLE_EMPLOYEE;

    //Constant for querying employee by its ID
    public static final String QUERY_EMPLOYEE_BY_ID = QUERY_EMPLOYEES +
            " WHERE " + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_ID + " = ?";

    //Constant for querying employee by its name
    public static final String QUERY_EMPLOYEE_BY_NAME = QUERY_EMPLOYEES +
            " WHERE " + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_NAME + " = ?";

    //Constant for querying employee by its username
    public static final String QUERY_EMPLOYEE_BY_USERNAME = QUERY_EMPLOYEES +
            " WHERE " + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_USERNAME + " = ?";

    //Constant for inserting new employee
    public static final String INSERT_EMPLOYEE =
            "INSERT INTO " + TABLE_EMPLOYEE +
                      " (" + COLUMN_EMPLOYEE_NAME + ", " +
                             COLUMN_EMPLOYEE_PHONE + ", " +
                             COLUMN_EMPLOYEE_USERNAME + ", " +
                             COLUMN_EMPLOYEE_PASSWORD + ") VALUES (?, ?, ?, ?)";

    //Constant for updating an employee
    public static final String UPDATE_EMPLOYEE =
                 "UPDATE " + TABLE_EMPLOYEE +
                    " SET " + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_NAME + " = ?, "
                            + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_PHONE + " = ?, "
                            + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_USERNAME + " = ?, "
                            + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_PASSWORD + " = ? WHERE "
                            + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_ID + " = ?";

    //Constant for deleting an employee
    public static final String DELETE_EMPLOYEE =
            "DELETE FROM " + TABLE_EMPLOYEE + " WHERE " + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_ID + " = ?";

    //Constant for querying login informations
    public static final String LOGIN_INFORMATIONS = QUERY_EMPLOYEES +
            " WHERE (" + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_USERNAME + " = ?) AND ("
                       + TABLE_EMPLOYEE + "." + COLUMN_EMPLOYEE_PASSWORD + " = ?)";

    /*----------------------------------------------------------------------------------------------------------*/

    PreparedStatement queryEmployeeByParameter;
    PreparedStatement insertIntoEmployee;
    PreparedStatement updateEmployee;
    PreparedStatement deleteEmployee;

    /*----------------------------------------------------------------------------------------------------------*/

    //Creating a private static instance of our class to make it a Singleton class
    private static EmployeeModel instance = new EmployeeModel();

    //Making the constructor private to make it used only by this claas to make it a Singleton class
    private EmployeeModel() {
    }

    //A getter of the one and only instance of this class (Singleton class)
    public static EmployeeModel getInstance() {
        return instance;
    }

    /*----------------------------------------------------------------------------------------------------------*/

    /**
     * Method to query list of all employees
     *
     * @return a list of all employees
     */
    @Override
    public List<Employee> queryEmployees() {

        try (PreparedStatement statement = Datasource.getInstance().getConnection().prepareStatement(QUERY_EMPLOYEES);
             ResultSet results = statement.executeQuery()) {

            //Action when getting a null result set
            if (!results.next()){
                System.out.println("No employee was found!");
                return Collections.emptyList();
            }

            //Positioning the ResultSet's cursor back to where it was before the first row
            results.beforeFirst();

            //Creating the list that is going to be returned
            List<Employee> employees = new ArrayList<>();

            //Inserting results in the list
            while (results.next()){
                Employee employee = new Employee();
                employee.setEmployeeName(results.getString(COLUMN_EMPLOYEE_NAME));
                employee.setEmployeePhone(results.getString(COLUMN_EMPLOYEE_PHONE));
                employee.setUsername(results.getString(COLUMN_EMPLOYEE_USERNAME));
                employees.add(employee);
            }

            return employees;

        } catch (SQLException e){
            System.out.println("Query failed: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Method that returns a Employee instance that matches an ID given
     * @param employeeID the ID given to the method to make the search on the database
     * @return the Employee instance that has got the same ID that was given
     */
    @Override
    public Employee getEmployeeByID(int employeeID) {

        //Checking if connection is null
        if (Datasource.getInstance().isConnectionNull()){
            return null;
        }

        try {
            queryEmployeeByParameter = Datasource.getInstance().getConnection().prepareStatement(QUERY_EMPLOYEE_BY_ID);
            queryEmployeeByParameter.setInt(1, employeeID);
        } catch (SQLException e) {
            System.out.println("Couldn't initialize prepared statement: " + e.getMessage());
            return null;
        }

        try (ResultSet results = queryEmployeeByParameter.executeQuery()) {

            //Action when getting a null result set
            if (!results.next()){
                System.out.println("No employee was found!");
                return null;
            }

            //Positioning the ResultSet's cursor back to where it was before the first row
            results.beforeFirst();

            //Going to the first and only element in the result set
            results.next();

            //Creating the instance that we want to return
            Employee employee = new Employee();

            //Initializing attributes of that instance
            employee.setEmployeeID(results.getInt(COLUMN_EMPLOYEE_ID));
            employee.setEmployeeName(results.getString(COLUMN_EMPLOYEE_NAME));
            employee.setEmployeePhone(results.getString(COLUMN_EMPLOYEE_PHONE));
            employee.setUsername(results.getString(COLUMN_EMPLOYEE_USERNAME));
            employee.setPassword(results.getString(COLUMN_EMPLOYEE_PASSWORD));

            return employee;

        } catch (SQLException e){
            System.out.println("Query failed: " + e.getMessage());
            return null;
        } finally {
            try {
                queryEmployeeByParameter.close();
            } catch (SQLException e){
                System.out.println("Couldn't close prepared statement: " + e.getMessage());
            }
        }
    }

    /**
     * Method that returns a Employee instance that matches a name given
     * @param employeeName the name given to the method to make the search on the database
     * @return the Employee instance that has got the same name that was given
     */
    @Override
    public Employee getEmployeeByName(String employeeName) {

        //Checking if connection is null
        if (Datasource.getInstance().isConnectionNull()){
            return null;
        }

        try {
            queryEmployeeByParameter = Datasource.getInstance().getConnection().prepareStatement(QUERY_EMPLOYEE_BY_NAME);
            queryEmployeeByParameter.setString(1, employeeName);
        } catch (SQLException e) {
            System.out.println("Couldn't initialize prepared statement: " + e.getMessage());
            return null;
        }

        try (ResultSet results = queryEmployeeByParameter.executeQuery()) {

            //Action when getting a null result set
            if (!results.next()){
                System.out.println("No employee was found!");
                return null;
            }

            //Positioning the ResultSet's cursor back to where it was before the first row
            results.beforeFirst();

            //Going to the first and only element in the result set
            results.next();

            //Creating the instance that we want to return
            Employee employee = new Employee();

            //Initializing attributes of that instance
            employee.setEmployeeID(results.getInt(COLUMN_EMPLOYEE_ID));
            employee.setEmployeeName(results.getString(COLUMN_EMPLOYEE_NAME));
            employee.setEmployeePhone(results.getString(COLUMN_EMPLOYEE_PHONE));
            employee.setUsername(results.getString(COLUMN_EMPLOYEE_USERNAME));
            employee.setPassword(results.getString(COLUMN_EMPLOYEE_PASSWORD));

            return employee;

        } catch (SQLException e){
            System.out.println("Query failed: " + e.getMessage());
            return null;
        } finally {
            try {
                queryEmployeeByParameter.close();
            } catch (SQLException e){
                System.out.println("Couldn't close prepared statement: " + e.getMessage());
            }
        }
    }

    /**
     * Method that returns a Employee instance that matches a username given
     *
     * @param employeeUsername the username given to the method to make the search on the database
     * @return the Employee instance that has got the same username that was given
     */
    @Override
    public Employee getEmployeeByUsername(String employeeUsername) {

        //Checking if connection is null
        if (Datasource.getInstance().isConnectionNull()){
            return null;
        }

        try {
            queryEmployeeByParameter = Datasource.getInstance().getConnection().prepareStatement(QUERY_EMPLOYEE_BY_USERNAME);
            queryEmployeeByParameter.setString(1, employeeUsername);
        } catch (SQLException e) {
            System.out.println("Couldn't initialize prepared statement: " + e.getMessage());
            return null;
        }

        try (ResultSet results = queryEmployeeByParameter.executeQuery()) {

            //Action when getting a null result set
            if (!results.next()){
                System.out.println("No employee was found!");
                return null;
            }

            //Positioning the ResultSet's cursor back to where it was before the first row
            results.beforeFirst();

            //Going to the first and only element in the result set
            results.next();

            //Creating the instance that we want to return
            Employee employee = new Employee();

            //Initializing attributes of that instance
            employee.setEmployeeID(results.getInt(COLUMN_EMPLOYEE_ID));
            employee.setEmployeeName(results.getString(COLUMN_EMPLOYEE_NAME));
            employee.setEmployeePhone(results.getString(COLUMN_EMPLOYEE_PHONE));
            employee.setUsername(results.getString(COLUMN_EMPLOYEE_USERNAME));
            employee.setPassword(results.getString(COLUMN_EMPLOYEE_PASSWORD));

            return employee;

        } catch (SQLException e){
            System.out.println("Query failed: " + e.getMessage());
            return null;
        } finally {
            try {
                queryEmployeeByParameter.close();
            } catch (SQLException e){
                System.out.println("Couldn't close prepared statement: " + e.getMessage());
            }
        }
    }

    /**
     * Method to insert employee using
     * @param employee object that we want to insert
     * @return generated ID of the new employee
     * @throws SQLException in case inserting the new employee fails
     */
    @Override
    public int insertEmployee(Employee employee) throws SQLException{

        //Check if employee already exists
        queryEmployeeByParameter = Datasource.getInstance().getConnection().prepareStatement(QUERY_EMPLOYEE_BY_USERNAME);
        queryEmployeeByParameter.setString(1, employee.getUsername());
        ResultSet result = queryEmployeeByParameter.executeQuery();

        //Action when username already exists
        if (result.next()){
            //Return the ID of the employee that already exists
            int employeeID = result.getInt(1);
            queryEmployeeByParameter.close();
            System.out.println("Employee with username \"" + employee.getUsername() + "\" already exists with ID: " + employeeID);
            return employeeID;
        }
        //If username does not exist
        else {
            //Closing prepared statement that we won't use anymore
            queryEmployeeByParameter.close();

            //Insert the employee
            insertIntoEmployee = Datasource.getInstance().getConnection().prepareStatement(INSERT_EMPLOYEE, Statement.RETURN_GENERATED_KEYS);
            insertIntoEmployee.setString(1, employee.getEmployeeName());
            insertIntoEmployee.setString(2, employee.getEmployeePhone());
            insertIntoEmployee.setString(3, employee.getUsername());
            insertIntoEmployee.setString(4, employee.getPassword());

            int affectedRows = insertIntoEmployee.executeUpdate();

            //Exception when not being able to insert
            if (affectedRows != 1) {
                insertIntoEmployee.close();
                throw new SQLException("Couldn't insert employee!");
            }

            //Getting the generated key of the inserted employee
            ResultSet generatedKeys = insertIntoEmployee.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedKey = generatedKeys.getInt(1);
                insertIntoEmployee.close();
                System.out.println("New employee created with ID: " + generatedKey);
                return generatedKey;
            } else {
                insertIntoEmployee.close();
                throw new SQLException("Couldn't get ID for the employee");
            }
        }
    }

    /**
     * Method to insert employee using
     * @param name of the new employee
     * @param phone of the new employee
     * @param username of the new employee
     * @param password of the new employee
     * @return generated ID of the new employee
     * @throws SQLException in case inserting the new employee fails
     */
    @Override
    public int insertEmployee(String name, String phone, String username, String password) throws SQLException {

        //Check if employee already exists
        queryEmployeeByParameter = Datasource.getInstance().getConnection().prepareStatement(QUERY_EMPLOYEE_BY_USERNAME);
        queryEmployeeByParameter.setString(1, username);
        ResultSet result = queryEmployeeByParameter.executeQuery();

        //Action when username already exists
        if (result.next()){
            //Return the ID of the employee that already exists
            int employeeID = result.getInt(1);
            queryEmployeeByParameter.close();
            System.out.println("Employee with username \"" + username + "\" already exists with ID: " + employeeID);
            return employeeID;
        }
        //If username does not exist
        else {
            //Closing prepared statement that we won't use anymore
            queryEmployeeByParameter.close();

            //Insert the employee
            insertIntoEmployee = Datasource.getInstance().getConnection().prepareStatement(INSERT_EMPLOYEE, Statement.RETURN_GENERATED_KEYS);
            insertIntoEmployee.setString(1, name);
            insertIntoEmployee.setString(2, phone);
            insertIntoEmployee.setString(3, username);
            insertIntoEmployee.setString(4, password);

            int affectedRows = insertIntoEmployee.executeUpdate();

            //Exception when not being able to insert
            if (affectedRows != 1) {
                insertIntoEmployee.close();
                throw new SQLException("Couldn't insert employee!");
            }

            //Getting the generated key of the inserted employee
            ResultSet generatedKeys = insertIntoEmployee.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedKey = generatedKeys.getInt(1);
                insertIntoEmployee.close();
                System.out.println("New employee created with ID: " + generatedKey);
                return generatedKey;
            } else {
                insertIntoEmployee.close();
                throw new SQLException("Couldn't get ID for the employee");
            }
        }
    }

    /**
     * Method to update an employee using
     * @param employeeID of the employee that we want to update
     * @param newName of the employee
     * @param newPhone of the employee
     * @param newUsername of the employee
     * @param newPassword of the employee
     * @return true if succeeded, false if failed
     */
    @Override
    public boolean updateEmployee(int employeeID, String newName, String newPhone, String newUsername, String newPassword) {

        try {
            //Set fields in the prepared statement
            updateEmployee = Datasource.getInstance().getConnection().prepareStatement(UPDATE_EMPLOYEE);
            updateEmployee.setString(1, newName);
            updateEmployee.setString(2, newPhone);
            updateEmployee.setString(3, newUsername);
            updateEmployee.setString(4, newPassword);
            updateEmployee.setInt(5, employeeID);

            //Checking the update result
            int affectedRecords = updateEmployee.executeUpdate();

            //Returning true if update has been successful
            return (affectedRecords == 1);

        } catch (SQLException e){
            System.out.println("Update failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Method to delete an employee
     * @param employeeID of the employee that we want to delete
     * @return true if succeeded, false if failed
     */
    @Override
    public boolean deleteEmployee(int employeeID) {

        try {
            //Set fields in the prepared statement
            deleteEmployee = Datasource.getInstance().getConnection().prepareStatement(DELETE_EMPLOYEE);
            deleteEmployee.setInt(1, employeeID);

            //Checking the delete result
            int affectedRecords = deleteEmployee.executeUpdate();

            //Returning true if delete has been successful
            return (affectedRecords == 1);

        } catch (SQLException e){
            System.out.println("Delete failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Method to check Login Informations
     * @param username
     * @param password
     * @return true if informations are true, false otherwise
     */
    @Override
    public boolean checkLogin(String username, String password){

        //Checking if connection is null
        if (Datasource.getInstance().isConnectionNull()){
            return false;
        }

        try {
            queryEmployeeByParameter = Datasource.getInstance().getConnection().prepareStatement(LOGIN_INFORMATIONS);
            queryEmployeeByParameter.setString(1, username);
            queryEmployeeByParameter.setString(2, password);
        } catch (SQLException e) {
            System.out.println("Couldn't initialize prepared statement: " + e.getMessage());
            return false;
        }

        try (ResultSet results = queryEmployeeByParameter.executeQuery()) {
            //Return true if there is a record, false otherwise
            return  (results.next());
        } catch (SQLException e){
            System.out.println("Query Login Informations failed: " + e.getMessage());
            return false;
        }
    }
}
