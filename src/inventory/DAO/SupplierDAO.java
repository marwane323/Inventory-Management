package inventory.DAO;

import inventory.Entity.Supplier;

import java.sql.SQLException;
import java.util.List;

public interface SupplierDAO {

    /**
     * Method to query list of all suppliers
     *
     * @return a list of all suppliers
     */
    public List<Supplier> querySuppliers();

    /**
     * Method that returns a Supplier instance that matches an ID given
     * @param supplierID the ID given to the method to make the search on the database
     * @return the Supplier instance that has got the same ID that was given
     */
    public Supplier getSupplierByID(int supplierID);

    /**
     * Method that returns a Supplier instance that matches a name given
     * @param supplierName the name given to the method to make the search on the database
     * @return the Supplier instance that has got the same name that was given
     */
    public Supplier getSupplierByName(String supplierName);

    /**
     * Method to insert supplier using
     * @param supplier object that we want to insert
     * @return generated ID of the new supplier
     * @throws SQLException in case inserting the new supplier fails
     */
    public int insertSupplier(Supplier supplier) throws SQLException;

    /**
     * Method to insert supplier using
     * @param name of the new supplier
     * @param address of the new supplier
     * @param phone of the new supplier
     * @return generated ID of the new supplier
     * @throws SQLException in case inserting the new supplier fails
     */
    public int insertSupplier(String name, String address, String phone) throws SQLException;

    /**
     * Method to update a supplier using
     * @param supplierID of the supplier that we want to update
     * @param newName of the supplier
     * @param newPhone of the supplier
     * @param newAddress of the supplier
     * @return true if succeeded, false if failed
     */
    public boolean updateSupplier(int supplierID, String newName, String newAddress, String newPhone);

    /**
     * Method to delete a supplier
     * @param supplierID of the supplier that we want to delete
     * @return true if succeeded, false if failed
     */
    public boolean deleteSupplier(int supplierID);

}
