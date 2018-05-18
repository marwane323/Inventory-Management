package inventory.DAO;

import inventory.Entity.Purchase;

import java.sql.SQLException;
import java.util.List;

public interface PurchaseDAO {

    /**
     * Method to query list of all purchases
     *
     * @return a list of all purchases
     */
    public List<Purchase> queryPurchases();

    /**
     * Method to query a purchases by its ID
     *
     * @param purchaseID of the purchase
     * @return the purchase instance
     */
    public Purchase getPurchaseByID(int purchaseID);

    /**
     * Method to query list of purchases in an invoice
     *
     * @param invoiceID of the invoice
     * @return a list of purchases in that invoice
     */
    public List<Purchase> queryPurchasesByInvoice(int invoiceID);

    /**
     * Method to query list of purchases from a supplier
     *
     * @param supplierID of the supplier
     * @return a list of purchases from the supplier
     */
    public List<Purchase> queryPurchasesBySupplier(int supplierID);

    /**
     * Method to query list of purchases of a product
     *
     * @param productID of the product
     * @return a list of purchases of that product
     */
    public List<Purchase> queryPurchasesByProduct(int productID);

    /**
     * Method to insert a purchase using
     *
     * @param invoiceID  of the purchase
     * @param supplierID of the purchase
     * @param productID  of the purchase
     * @param costPrice  of the purchase
     * @param quantity   of the purchase
     * @param totalCost  of the purchase
     * @return generated ID of the new purchase
     * @throws SQLException in case inserting the new purchase fails
     */
    public int insertPurchase(int invoiceID, int supplierID, int productID, float costPrice,
                              int quantity, float totalCost) throws SQLException;

    /**
     * Method to update a purchase
     *
     * @param purchaseID    of the purchase that we want to update
     * @param newInvoiceID
     * @param newSupplierID
     * @param newProductID
     * @param newCostPrice
     * @param newQuantity
     * @param newTotalCost
     * @return true if succeeded, false if failed
     */
    public boolean updatePurchase(int purchaseID, int newInvoiceID, int newSupplierID,
                                  int newProductID, float newCostPrice, int newQuantity, float newTotalCost);

    /**
     * Method to delete a purchase
     *
     * @param purchaseID of the purchase that we want to delete
     * @return true if succeeded, false if failed
     */
    public boolean deletePurchase(int purchaseID);
}
