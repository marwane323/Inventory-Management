package inventory.DAO;

import inventory.Entity.Sale;

import java.sql.SQLException;
import java.util.List;

public interface SaleDAO {

    /**
     * Method to query list of all sales
     *
     * @return a list of all sales
     */
    public List<Sale> querySales();

    /**
     * Method to query a sale by its ID
     * @param saleID of the sale
     * @return the sale instance
     */
    public Sale getSaleByID(int saleID);

    /**
     * Method to query list of sales in an invoice
     *
     * @param invoiceID of the invoice
     * @return a list of sales in that invoice
     */
    public List<Sale> querySalesByInvoice(int invoiceID);

    /**
     * Method to query list of sales of a product
     *
     * @param productID of the product
     * @return a list of sales of that product
     */
    public List<Sale> querySalesByProduct(int productID);

    /**
     * Method to insert a sale using
     * @param invoiceID of the sale
     * @param productID of the sale
     * @param quantity of the sale
     * @param totalCost of the sale
     * @return generated ID of the new sale
     * @throws SQLException in case inserting the new sale fails
     */
    public int insertSale(int invoiceID, int productID,
                          int quantity, float totalCost) throws SQLException;

    /**
     * Method to update a sale
     * @param saleID of the sale that we want to update
     * @param newInvoiceID
     * @param newProductID
     * @param newQuantity
     * @param newTotalCost
     * @return true if succeeded, false if failed
     */
    public boolean updateSale(int saleID, int newInvoiceID, int newProductID,
                              int newQuantity, float newTotalCost);

    /**
     * Method to delete a sale
     * @param saleID of the sale that we want to delete
     * @return true if succeeded, false if failed
     */
    public boolean deleteSale(int saleID);
}
