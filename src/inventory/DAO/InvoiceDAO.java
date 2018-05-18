package inventory.DAO;

import inventory.Entity.Invoice;

import java.sql.SQLException;
import java.util.List;

public interface InvoiceDAO {

    /**
     * Method to query list of all invoices
     *
     * @return a list of all invoices
     */
    public List<Invoice> queryInvoices();

    /**
     * Method that returns an Invoice instance that matches an ID given
     * @param invoiceID the ID given to the method to make the search on the database
     * @return the Invoice instance that has got the same ID that was given
     */
    public Invoice getInvoiceByID(int invoiceID);

    /**
     * Method to query list of invoices in a date
     *
     * @return a list of invoices
     */
    public List<Invoice> queryInvoicesByDate(String date);

    /**
     * Method to insert an invoice using
     * @return generated ID of the new invoice
     * @throws SQLException in case inserting the new invoice fails
     */
    public int insertInvoice() throws SQLException;

    /**
     * Method to update an invoice's date
     * @param invoiceID of the invoice that we want to update
     * @param newDate
     * @return true if succeeded, false if failed
     */
    public boolean updateInvoice(int invoiceID, String newDate);

    /**
     * Method to delete an invoice
     * @param invoiceID of the invoice that we want to delete
     * @return true if succeeded, false if failed
     */
    public boolean deleteInvoice(int invoiceID);
}
