package inventory.Model;

import com.mysql.jdbc.Statement;
import inventory.DAO.InvoiceDAO;
import inventory.Entity.Invoice;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class InvoiceSupplierModel implements InvoiceDAO {

    //Constant for the client's invoices table
    public static final String TABLE_INVOICE_SUPPLIER = "invoicesupplier";
    public static final String COLUMN_INVOICE_ID = "invoiceID";
    public static final String COLUMN_DATE = "date";
    public static final String INDEX_INVOICE_ID = "1";
    public static final String INDEX_DATE = "2";

    //Constant for the ORDER BY clause in queries
    public static final String ORDER_BY_DES = " ORDER BY " + COLUMN_DATE + " DESC";

    //Constants for querying invoices
    public static final String QUERY_INVOICES =
            "SELECT " + TABLE_INVOICE_SUPPLIER + "." + COLUMN_INVOICE_ID + ", "
                      + TABLE_INVOICE_SUPPLIER + "." + COLUMN_DATE +
                    " FROM " + TABLE_INVOICE_SUPPLIER ;

    //Constant for querying invoice by its id
    public static final String QUERY_INVOICE_BY_ID = QUERY_INVOICES +
            " WHERE " + TABLE_INVOICE_SUPPLIER + "." + COLUMN_INVOICE_ID + " = ?" + ORDER_BY_DES;

    //Constant for querying invoices by their date
    public static final String QUERY_INVOICES_BY_DATE = QUERY_INVOICES +
            " WHERE " + TABLE_INVOICE_SUPPLIER + "." + COLUMN_DATE + " BETWEEN ? AND ?" + ORDER_BY_DES;

    //Constant for inserting new invoice
    public static final String INSERT_INVOICE =
            "INSERT INTO " + TABLE_INVOICE_SUPPLIER +
                    " (" + COLUMN_DATE + ") VALUES (?)";

    //Constant for updating invoice's date
    public static final String UPDATE_INVOICE =
            "UPDATE " + TABLE_INVOICE_SUPPLIER +
                    " SET " + TABLE_INVOICE_SUPPLIER + "." + COLUMN_DATE + " = ? WHERE " +
                    TABLE_INVOICE_SUPPLIER + "." + COLUMN_INVOICE_ID + " = ?";

    //Constant for deleting an invoice
    public static final String DELETE_INVOICE =
            "DELETE FROM " + TABLE_INVOICE_SUPPLIER + " WHERE " + TABLE_INVOICE_SUPPLIER + "." + COLUMN_INVOICE_ID + " = ?";

    /*----------------------------------------------------------------------------------------------------------*/

    PreparedStatement queryInvoiceByParameter;
    PreparedStatement insertIntoInvoice;
    PreparedStatement updateInvoice;
    PreparedStatement deleteInvoice;

    /*----------------------------------------------------------------------------------------------------------*/

    //Creating a private static instance of our class to make it a Singleton class
    private static InvoiceSupplierModel instance = new InvoiceSupplierModel();

    //Making the constructor private to make it used only by this class to make it a Singleton class
    private InvoiceSupplierModel() {}

    //A getter of the one and only instance of this class (Singleton class)
    public static InvoiceSupplierModel getInstance() {
        return instance;
    }

    /*----------------------------------------------------------------------------------------------------------*/

    /**
     * Method to query list of all invoices
     *
     * @return a list of all invoices
     */
    @Override
    public List<Invoice> queryInvoices() {

        try (PreparedStatement statement = Datasource.getInstance().getConnection().prepareStatement(QUERY_INVOICES + ORDER_BY_DES);
             ResultSet results = statement.executeQuery()) {

            //Action when getting a null result set
            if (!results.next()){
                System.out.println("No invoice was found!");
                return Collections.emptyList();
            }

            //Positioning the ResultSet's cursor back to where it was before the first row
            results.beforeFirst();

            //Creating the list that is going to be returned
            List<Invoice> invoices = new ArrayList<>();

            //Inserting results in the list
            while (results.next()){
                Invoice invoice = new Invoice();
                invoice.setInvoiceID(results.getInt(COLUMN_INVOICE_ID));
                invoice.setDate(results.getString(COLUMN_DATE));
                invoices.add(invoice);
            }

            return invoices;

        } catch (SQLException e){
            System.out.println("Query failed: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Method that returns an Invoice instance that matches an ID given
     * @param invoiceID the ID given to the method to make the search on the database
     * @return the Invoice instance that has got the same ID that was given
     */
    @Override
    public Invoice getInvoiceByID(int invoiceID) {

        //Checking if connection is null
        if (Datasource.getInstance().isConnectionNull()){
            return null;
        }

        try {
            queryInvoiceByParameter = Datasource.getInstance().getConnection().prepareStatement(QUERY_INVOICE_BY_ID);
            queryInvoiceByParameter.setInt(1, invoiceID);
        } catch (SQLException e) {
            System.out.println("Couldn't initialize prepared statement: " + e.getMessage());
            return null;
        }

        try (ResultSet results = queryInvoiceByParameter.executeQuery()) {

            //Action when getting a null result set
            if (!results.next()){
                System.out.println("No invoice was found!");
                return null;
            }

            //Positioning the ResultSet's cursor back to where it was before the first row
            results.beforeFirst();

            //Going to the first and only element in the result set
            results.next();

            //Creating the instance that we want to return
            Invoice invoice = new Invoice();

            //Initializing attributes of that instance
            invoice.setInvoiceID(results.getInt(COLUMN_INVOICE_ID));
            invoice.setDate(results.getString(COLUMN_DATE));

            return invoice;

        } catch (SQLException e){
            System.out.println("Query failed: " + e.getMessage());
            return null;
        } finally {
            try {
                queryInvoiceByParameter.close();
            } catch (SQLException e){
                System.out.println("Couldn't close prepared statement: " + e.getMessage());
            }
        }
    }

    /**
     * Method to query list of invoices in a date
     *
     * @return a list of invoices
     */
    @Override
    public List<Invoice> queryInvoicesByDate(String date) {

        // Date verification !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        //Checking if connection is null
        if (Datasource.getInstance().isConnectionNull()){
            return Collections.emptyList();
        }

        try {
            queryInvoiceByParameter = Datasource.getInstance().getConnection().prepareStatement(QUERY_INVOICES_BY_DATE);
            queryInvoiceByParameter.setString(1, date + " 00:00:00");
            queryInvoiceByParameter.setString(2, date + " 23:59:59");
        } catch (SQLException e) {
            System.out.println("Couldn't initialize prepared statement: " + e.getMessage());
            return Collections.emptyList();
        }

        try (ResultSet results = queryInvoiceByParameter.executeQuery()) {

            //Action when getting a null result set
            if (!results.next()) {
                System.out.println("No invoice was found!");
                return Collections.emptyList();
            }
            //Positioning the ResultSet's cursor back to where it was before the first row
            results.beforeFirst();

            //Creating the list that is going to be returned
            List<Invoice> invoices = new ArrayList<>();

            //Inserting results in the list
            while (results.next()){
                Invoice invoice = new Invoice();
                invoice.setInvoiceID(results.getInt(COLUMN_INVOICE_ID));
                invoice.setDate(results.getString(COLUMN_DATE));
                invoices.add(invoice);
            }

            return invoices;

        } catch (SQLException e){
            System.out.println("Query failed: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Method to insert an invoice using
     * @return generated ID of the new invoice
     * @throws SQLException in case inserting the new invoice fails
     */
    @Override
    public int insertInvoice() throws SQLException {

        //Getting the actual present date
        Date date = new Date();
        Object param = new java.sql.Timestamp(date.getTime());

        //Insert the invoice
        insertIntoInvoice = Datasource.getInstance().getConnection().prepareStatement(INSERT_INVOICE, Statement.RETURN_GENERATED_KEYS);
        insertIntoInvoice.setObject(1, param);


        int affectedRows = insertIntoInvoice.executeUpdate();

        //Exception when not being able to insert
        if (affectedRows != 1) {
            insertIntoInvoice.close();
            throw new SQLException("Couldn't insert brand!");
        }

        //Getting the generated key of the inserted invoice
        ResultSet generatedKeys = insertIntoInvoice.getGeneratedKeys();
        if (generatedKeys.next()) {
            int generatedKey = generatedKeys.getInt(1);
            insertIntoInvoice.close();
            System.out.println("New invoice created with ID: " + generatedKey);
            return generatedKey;
        } else {
            insertIntoInvoice.close();
            throw new SQLException("Couldn't get ID for the invoice");
        }
    }

    /**
     * Method to update an invoice's date
     * @param invoiceID of the invoice that we want to update
     * @param newDate
     * @return true if succeeded, false if failed
     */
    @Override
    public boolean updateInvoice(int invoiceID, String newDate) {

        // Date verification !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        try {
            //Set fields in the prepared statement
            updateInvoice = Datasource.getInstance().getConnection().prepareStatement(UPDATE_INVOICE);
            updateInvoice.setString(1, newDate);
            updateInvoice.setInt(2, invoiceID);

            //Checking the update result
            int affectedRecords = updateInvoice.executeUpdate();

            //Returning true if update has been successful
            return (affectedRecords == 1);

        } catch (SQLException e){
            System.out.println("Update failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Method to delete an invoice
     * @param invoiceID of the invoice that we want to delete
     * @return true if succeeded, false if failed
     */
    @Override
    public boolean deleteInvoice(int invoiceID) {

        try {
            //Set fields in the prepared statement
            deleteInvoice = Datasource.getInstance().getConnection().prepareStatement(DELETE_INVOICE);
            deleteInvoice.setInt(1, invoiceID);

            //Checking the delete result
            int affectedRecords = deleteInvoice.executeUpdate();

            //Returning true if delete has been successful
            return (affectedRecords == 1);

        } catch (SQLException e) {
            System.out.println("Delete failed: " + e.getMessage());
            return false;
        }
    }
}
