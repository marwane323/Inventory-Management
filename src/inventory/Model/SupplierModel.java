package inventory.Model;

import com.mysql.jdbc.Statement;
import inventory.DAO.SupplierDAO;
import inventory.Entity.Supplier;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SupplierModel implements SupplierDAO {

    //Constants for the suppliers table
    public static final String TABLE_SUPPLIER = "supplier";
    public static final String COLUMN_SUPPLIER_ID = "supplierID";
    public static final String COLUMN_SUPPLIER_NAME = "supplierName";
    public static final String COLUMN_SUPPLIER_ADDRESS = "supplierAddress";
    public static final String COLUMN_SUPPLIER_PHONE = "supplierPhone";
    public static final String INDEX_SUPPLIER_ID = "1";
    public static final String INDEX_SUPPLIER_NAME = "2";
    public static final String INDEX_SUPPLIER_ADDRESS = "3";
    public static final String INDEX_SUPPLIER_PHONE = "4";

    //Constants for querying suppliers
    public static final String QUERY_SUPPLIERS =
            "SELECT " + TABLE_SUPPLIER + "." + COLUMN_SUPPLIER_ID + ", " +
                        TABLE_SUPPLIER + "." + COLUMN_SUPPLIER_NAME + ", " +
                        TABLE_SUPPLIER + "." + COLUMN_SUPPLIER_ADDRESS + ", " +
                        TABLE_SUPPLIER + "." + COLUMN_SUPPLIER_PHONE +
                    " FROM " + TABLE_SUPPLIER;

    //Constant for querying supplier by its id
    public static final String QUERY_SUPPLIER_BY_ID = QUERY_SUPPLIERS +
            " WHERE " + TABLE_SUPPLIER + "." + COLUMN_SUPPLIER_ID + " = ?";

    //Constant for querying supplier by its name
    public static final String QUERY_SUPPLIER_BY_NAME = QUERY_SUPPLIERS +
            " WHERE " + TABLE_SUPPLIER + "." + COLUMN_SUPPLIER_NAME + " = ?";

    //Constant for inserting new supplier
    public static final String INSERT_SUPPLIER =
            "INSERT INTO " + TABLE_SUPPLIER +
                      " (" + COLUMN_SUPPLIER_NAME + ", " +
                             COLUMN_SUPPLIER_ADDRESS + ", " +
                             COLUMN_SUPPLIER_PHONE + ") VALUES (?, ?, ?)";

    //Constant for updating a supplier
    public static final String UPDATE_SUPPLIER =
            "UPDATE " + TABLE_SUPPLIER +
                    " SET " + TABLE_SUPPLIER + "." + COLUMN_SUPPLIER_NAME + " = ?, "
                            + TABLE_SUPPLIER + "." + COLUMN_SUPPLIER_ADDRESS + " = ?, "
                            + TABLE_SUPPLIER + "." + COLUMN_SUPPLIER_PHONE + " = ? WHERE "
                            + TABLE_SUPPLIER + "." + COLUMN_SUPPLIER_ID + " = ?";

    //Constant for deleting a supplier
    public static final String DELETE_SUPPLIER =
            "DELETE FROM " + TABLE_SUPPLIER + " WHERE " + TABLE_SUPPLIER + "." + COLUMN_SUPPLIER_ID + " = ?";

    /*----------------------------------------------------------------------------------------------------------*/

    PreparedStatement querySupplierByParameter;
    PreparedStatement insertIntoSupplier;
    PreparedStatement updateSupplier;
    PreparedStatement deleteSupplier;

    /*----------------------------------------------------------------------------------------------------------*/

    //Creating a private static instance of our class to make it a Singleton class
    private static SupplierModel instance = new SupplierModel();

    //Making the constructor private to make it used only by this claas to make it a Singleton class
    private SupplierModel() {
    }

    //A getter of the one and only instance of this class (Singleton class)
    public static SupplierModel getInstance() {
        return instance;
    }

    /*----------------------------------------------------------------------------------------------------------*/

    /**
     * Method to query list of all suppliers
     *
     * @return a list of all suppliers
     */
    @Override
    public List<Supplier> querySuppliers(){

        try (PreparedStatement statement = Datasource.getInstance().getConnection().prepareStatement(QUERY_SUPPLIERS);
             ResultSet results = statement.executeQuery()) {

            //Action when getting a null result set
            if (!results.next()){
                System.out.println("No supplier was found!");
                return Collections.emptyList();
            }

            //Positioning the ResultSet's cursor back to where it was before the first row
            results.beforeFirst();

            //Creating the list that is going to be returned
            List<Supplier> suppliers = new ArrayList<>();

            //Inserting results in the list
            while (results.next()){
                Supplier supplier = new Supplier();
                supplier.setSupplierID(results.getInt(COLUMN_SUPPLIER_ID));
                supplier.setSupplierName(results.getString(COLUMN_SUPPLIER_NAME));
                supplier.setSupplierAddress(results.getString(COLUMN_SUPPLIER_ADDRESS));
                supplier.setSupplierPhone(results.getString(COLUMN_SUPPLIER_PHONE));
                suppliers.add(supplier);
            }

            return suppliers;

        } catch (SQLException e){
            System.out.println("Query failed: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Method that returns a Supplier instance that matches an ID given
     * @param supplierID the ID given to the method to make the search on the database
     * @return the Supplier instance that has got the same ID that was given
     */
    @Override
    public Supplier getSupplierByID(int supplierID) {
        //Checking if connection is null
        if (Datasource.getInstance().isConnectionNull()){
            return null;
        }

        try {
            querySupplierByParameter = Datasource.getInstance().getConnection().prepareStatement(QUERY_SUPPLIER_BY_ID);
            querySupplierByParameter.setInt(1, supplierID);
        } catch (SQLException e) {
            System.out.println("Couldn't initialize prepared statement: " + e.getMessage());
            return null;
        }

        try (ResultSet results = querySupplierByParameter.executeQuery()) {

            //Action when getting a null result set
            if (!results.next()){
                System.out.println("No supplier was found!");
                return null;
            }

            //Positioning the ResultSet's cursor back to where it was before the first row
            results.beforeFirst();

            //Going to the first and only element in the result set
            results.next();

            //Creating the instance that we want to return
            Supplier supplier = new Supplier();

            //Initializing attributes of that instance
            supplier.setSupplierID(results.getInt(COLUMN_SUPPLIER_ID));
            supplier.setSupplierName(results.getString(COLUMN_SUPPLIER_NAME));
            supplier.setSupplierAddress(results.getString(COLUMN_SUPPLIER_ADDRESS));
            supplier.setSupplierPhone(results.getString(COLUMN_SUPPLIER_PHONE));

            return supplier;

        } catch (SQLException e){
            System.out.println("Query failed: " + e.getMessage());
            return null;
        } finally {
            try {
                querySupplierByParameter.close();
            } catch (SQLException e){
                System.out.println("Couldn't close prepared statement: " + e.getMessage());
            }
        }
    }

    /**
     * Method that returns a Supplier instance that matches a name given
     * @param supplierName the name given to the method to make the search on the database
     * @return the Supplier instance that has got the same name that was given
     */
    @Override
    public Supplier getSupplierByName(String supplierName) {
        //Checking if connection is null
        if (Datasource.getInstance().isConnectionNull()){
            return null;
        }

        try {
            querySupplierByParameter = Datasource.getInstance().getConnection().prepareStatement(QUERY_SUPPLIER_BY_NAME);
            querySupplierByParameter.setString(1, supplierName);
        } catch (SQLException e) {
            System.out.println("Couldn't initialize prepared statement: " + e.getMessage());
            return null;
        }

        try (ResultSet results = querySupplierByParameter.executeQuery()) {

            //Action when getting a null result set
            if (!results.next()){
                System.out.println("No supplier was found!");
                return null;
            }

            //Positioning the ResultSet's cursor back to where it was before the first row
            results.beforeFirst();

            //Going to the first and only element in the result set
            results.next();

            //Creating the instance that we want to return
            Supplier supplier = new Supplier();

            //Initializing attributes of that instance
            supplier.setSupplierID(results.getInt(COLUMN_SUPPLIER_ID));
            supplier.setSupplierName(results.getString(COLUMN_SUPPLIER_NAME));
            supplier.setSupplierAddress(results.getString(COLUMN_SUPPLIER_ADDRESS));
            supplier.setSupplierPhone(results.getString(COLUMN_SUPPLIER_PHONE));

            return supplier;

        } catch (SQLException e){
            System.out.println("Query failed: " + e.getMessage());
            return null;
        } finally {
            try {
                querySupplierByParameter.close();
            } catch (SQLException e){
                System.out.println("Couldn't close prepared statement: " + e.getMessage());
            }
        }
    }

    /**
     * Method to insert supplier using
     * @param supplier object that we want to insert
     * @return generated ID of the new supplier
     * @throws SQLException in case inserting the new supplier fails
     */
    @Override
    public int insertSupplier(Supplier supplier) throws SQLException {

        //Check if supplier already exists
        querySupplierByParameter = Datasource.getInstance().getConnection().prepareStatement(QUERY_SUPPLIER_BY_NAME);
        querySupplierByParameter.setString(1, supplier.getSupplierName());
        ResultSet result = querySupplierByParameter.executeQuery();

        //Action when name already exists
        if (result.next()) {
            //Return the ID of the supplier that already exists
            int supplierID = result.getInt(1);
            querySupplierByParameter.close();
            System.out.println("Supplier with name \"" + supplier.getSupplierName() + "\" already exists with ID: " + supplierID);
            return supplierID;
        }
        //If name does not exist
        else {
            //Closing prepared statement that we won't use anymore
            querySupplierByParameter.close();

            //Insert the supplier
            insertIntoSupplier = Datasource.getInstance().getConnection().prepareStatement(INSERT_SUPPLIER, Statement.RETURN_GENERATED_KEYS);
            insertIntoSupplier.setString(1, supplier.getSupplierName());
            insertIntoSupplier.setString(2, supplier.getSupplierAddress());
            insertIntoSupplier.setString(3, supplier.getSupplierPhone());

            int affectedRows = insertIntoSupplier.executeUpdate();

            //Exception when not being able to insert
            if (affectedRows != 1) {
                insertIntoSupplier.close();
                throw new SQLException("Couldn't insert supplier!");
            }

            //Getting the generated key of the inserted supplier
            ResultSet generatedKeys = insertIntoSupplier.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedKey = generatedKeys.getInt(1);
                insertIntoSupplier.close();
                System.out.println("New supplier created with ID: " + generatedKey);
                return generatedKey;
            } else {
                insertIntoSupplier.close();
                throw new SQLException("Couldn't get ID for the supplier");
            }
        }
    }

    /**
     * Method to insert supplier using
     * @param name of the new supplier
     * @param address of the new supplier
     * @param phone of the new supplier
     * @return generated ID of the new supplier
     * @throws SQLException in case inserting the new supplier fails
     */
    @Override
    public int insertSupplier(String name, String address, String phone) throws SQLException {
        //Check if supplier already exists
        querySupplierByParameter = Datasource.getInstance().getConnection().prepareStatement(QUERY_SUPPLIER_BY_NAME);
        querySupplierByParameter.setString(1, name);
        ResultSet result = querySupplierByParameter.executeQuery();

        //Action when name already exists
        if (result.next()) {
            //Return the ID of the supplier that already exists
            int supplierID = result.getInt(1);
            querySupplierByParameter.close();
            System.out.println("Supplier with name \"" + name + "\" already exists with ID: " + supplierID);
            return supplierID;
        }
        //If name does not exist
        else {
            //Closing prepared statement that we won't use anymore
            querySupplierByParameter.close();

            //Insert the supplier
            insertIntoSupplier = Datasource.getInstance().getConnection().prepareStatement(INSERT_SUPPLIER, Statement.RETURN_GENERATED_KEYS);
            insertIntoSupplier.setString(1, name);
            insertIntoSupplier.setString(2, address);
            insertIntoSupplier.setString(3, phone);

            int affectedRows = insertIntoSupplier.executeUpdate();

            //Exception when not being able to insert
            if (affectedRows != 1) {
                insertIntoSupplier.close();
                throw new SQLException("Couldn't insert supplier!");
            }

            //Getting the generated key of the inserted supplier
            ResultSet generatedKeys = insertIntoSupplier.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedKey = generatedKeys.getInt(1);
                insertIntoSupplier.close();
                System.out.println("New supplier created with ID: " + generatedKey);
                return generatedKey;
            } else {
                insertIntoSupplier.close();
                throw new SQLException("Couldn't get ID for the supplier");
            }
        }
    }

    /**
     * Method to update a supplier using
     * @param supplierID of the supplier that we want to update
     * @param newName of the supplier
     * @param newPhone of the supplier
     * @param newAddress of the supplier
     * @return true if succeeded, false if failed
     */
    @Override
    public boolean updateSupplier(int supplierID, String newName, String newAddress, String newPhone) {

        try {
            //Set fields in the prepared statement
            updateSupplier = Datasource.getInstance().getConnection().prepareStatement(UPDATE_SUPPLIER);
            updateSupplier.setString(1, newName);
            updateSupplier.setString(2, newAddress);
            updateSupplier.setString(3, newPhone);
            updateSupplier.setInt(4, supplierID);

            //Checking the update result
            int affectedRecords = updateSupplier.executeUpdate();

            //Returning true if update has been successful
            return (affectedRecords == 1);

        } catch (SQLException e){
            System.out.println("Update failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Method to delete a supplier
     * @param supplierID of the supplier that we want to delete
     * @return true if succeeded, false if failed
     */
    @Override
    public boolean deleteSupplier(int supplierID) {

        try {
            //Set fields in the prepared statement
            deleteSupplier = Datasource.getInstance().getConnection().prepareStatement(DELETE_SUPPLIER);
            deleteSupplier.setInt(1, supplierID);

            //Checking the delete result
            int affectedRecords = deleteSupplier.executeUpdate();

            //Returning true if delete has been successful
            return (affectedRecords == 1);

        } catch (SQLException e){
            System.out.println("Delete failed: " + e.getMessage());
            return false;
        }
    }
}
