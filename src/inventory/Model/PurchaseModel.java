package inventory.Model;

import com.mysql.jdbc.Statement;
import inventory.DAO.PurchaseDAO;
import inventory.Entity.Product;
import inventory.Entity.Purchase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PurchaseModel implements PurchaseDAO {

    //Constants for the sales table
    public static final String TABLE_PURCHASE = "purchases";
    public static final String COLUMN_PURCHASE_ID = "purchaseID";
    public static final String COLUMN_INVOICE_ID = "invoiceID";
    public static final String COLUMN_SUPPLIER_ID = "supplierID";
    public static final String COLUMN_PRODUCT_ID = "productID";
    public static final String COLUMN_PRODUCT_COST_PRICE = "costPrice";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_TOTAL_COST = "totalCost";
    public static final String INDEX_PURCHASE_ID = "1";
    public static final String INDEX_INVOICE_ID = "2";
    public static final String INDEX_SUPPLIER_ID = "3";
    public static final String INDEX_PRODUCT_ID = "4";
    public static final String INDEX_PRODUCT_COST_PRICE = "5";
    public static final String INDEX_QUANTITY = "6";
    public static final String INDEX_TOTAL_COST = "7";

    //Constants for querying purchases
    public static final String QUERY_PURCHASES =
            "SELECT " + TABLE_PURCHASE + "." + COLUMN_PURCHASE_ID + ", " +
                    TABLE_PURCHASE + "." + COLUMN_SUPPLIER_ID + ", " +
                    TABLE_PURCHASE + "." + COLUMN_INVOICE_ID + ", " +
                    TABLE_PURCHASE + "." + COLUMN_PRODUCT_ID + ", " +
                    TABLE_PURCHASE + "." + COLUMN_PRODUCT_COST_PRICE + ", " +
                    TABLE_PURCHASE + "." + COLUMN_QUANTITY + ", " +
                    TABLE_PURCHASE + "." + COLUMN_TOTAL_COST +
                    " FROM " + TABLE_PURCHASE;

    //Constant for querying purchase by its id
    public static final String QUERY_PURCHASE_BY_ID = QUERY_PURCHASES +
            " WHERE " + TABLE_PURCHASE + "." + COLUMN_PURCHASE_ID + " = ?";

    //Constant for querying purchases of a product
    public static final String QUERY_PURCHASE_FOR_PRODUCT = QUERY_PURCHASES +
            " WHERE " + TABLE_PURCHASE + "." + COLUMN_PRODUCT_ID + " = ?";

    //Constant for querying purchases from a supplier
    public static final String QUERY_PURCHASE_FROM_SUPPLIER = QUERY_PURCHASES +
            " WHERE " + TABLE_PURCHASE + "." + COLUMN_SUPPLIER_ID + " = ?";

    //Constant for querying purchases on an invoice
    public static final String QUERY_PURCHASE_INVOICE = QUERY_PURCHASES +
            " WHERE " + TABLE_PURCHASE + "." + COLUMN_INVOICE_ID + " = ?";

    //Constant for inserting new purchase
    public static final String INSERT_PURCHASE =
            "INSERT INTO " + TABLE_PURCHASE +
                    " (" + COLUMN_INVOICE_ID + ", " +
                    COLUMN_SUPPLIER_ID + ", " +
                    COLUMN_PRODUCT_ID + ", " +
                    COLUMN_PRODUCT_COST_PRICE + ", " +
                    COLUMN_QUANTITY + ", " +
                    COLUMN_TOTAL_COST + ") VALUES (?, ?, ?, ?, ?, ?)";

    //Constant for updating a purchase
    public static final String UPDATE_PURCHASE =
            "UPDATE " + TABLE_PURCHASE +
                    " SET " + TABLE_PURCHASE + "." + COLUMN_INVOICE_ID + " = ?, "
                    + TABLE_PURCHASE + "." + COLUMN_SUPPLIER_ID + " = ?, "
                    + TABLE_PURCHASE + "." + COLUMN_PRODUCT_ID + " = ?, "
                    + TABLE_PURCHASE + "." + COLUMN_PRODUCT_COST_PRICE + " = ?, "
                    + TABLE_PURCHASE + "." + COLUMN_QUANTITY + " = ?, "
                    + TABLE_PURCHASE + "." + COLUMN_TOTAL_COST + " = ? WHERE "
                    + TABLE_PURCHASE + "." + COLUMN_PURCHASE_ID + " = ?";

    //Constant for deleting a purchase
    public static final String DELETE_PURCHASE =
            "DELETE FROM " + TABLE_PURCHASE + " WHERE " + TABLE_PURCHASE + "." + COLUMN_PURCHASE_ID + " = ?";

    //Constant for checking if a purchase record exists
    public static final String CHECK_SALE = QUERY_PURCHASES +
            " WHERE (" + TABLE_PURCHASE + "." + COLUMN_INVOICE_ID + " = ?) AND ("
                       + TABLE_PURCHASE + "." + COLUMN_SUPPLIER_ID + " = ?) AND ("
                       + TABLE_PURCHASE + "." + COLUMN_PRODUCT_ID + " = ?)";

    /*----------------------------------------------------------------------------------------------------------*/

    PreparedStatement queryPurchaseByParameter;
    PreparedStatement insertIntoPurchase;
    PreparedStatement updatePurchase;
    PreparedStatement deletePurchase;

    /*----------------------------------------------------------------------------------------------------------*/

    //Creating a private static instance of our class to make it a Singleton class
    private static PurchaseModel instance = new PurchaseModel();

    //Making the constructor private to make it used only by this class to make it a Singleton class
    private PurchaseModel() {
    }

    //A getter of the one and only instance of this class (Singleton class)
    public static PurchaseModel getInstance() {
        return instance;
    }

    /*----------------------------------------------------------------------------------------------------------*/

    /**
     * Method to query list of all purchases
     *
     * @return a list of all purchases
     */
    @Override
    public List<Purchase> queryPurchases() {

        try (PreparedStatement statement = Datasource.getInstance().getConnection().prepareStatement(QUERY_PURCHASES);
             ResultSet results = statement.executeQuery()) {

            //Action when getting a null result set
            if (!results.next()) {
                System.out.println("No purchase was found!");
                return Collections.emptyList();
            }

            //Positioning the ResultSet's cursor back to where it was before the first row
            results.beforeFirst();

            //Creating the list that is going to be returned
            List<Purchase> purchases = new ArrayList<>();

            //Inserting results in the list
            while (results.next()) {
                Purchase purchase = new Purchase();
                purchase.setPurchaseID(results.getInt(COLUMN_PURCHASE_ID));
                purchase.setInvoiceID(results.getInt(COLUMN_INVOICE_ID));
                purchase.setSupplierID(results.getInt(COLUMN_SUPPLIER_ID));
                purchase.setProductID(results.getInt(COLUMN_PRODUCT_ID));
                purchase.setCostPrice(results.getFloat(COLUMN_PRODUCT_COST_PRICE));
                purchase.setQuantity(results.getInt(COLUMN_QUANTITY));
                purchase.setTotalCost(results.getFloat(COLUMN_TOTAL_COST));
                purchase.setSupplierName(SupplierModel.getInstance()
                        .getSupplierByID(results.getInt(COLUMN_SUPPLIER_ID)).getSupplierName());
                purchase.setProductName(ProductModel.getInstance()
                        .getProductByID(results.getInt(COLUMN_PRODUCT_ID)).getBrandName()
                        + " " + ProductModel.getInstance()
                        .getProductByID(results.getInt(COLUMN_PRODUCT_ID)).getProductName());
                purchases.add(purchase);
            }

            return purchases;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Method to query a purchases by its ID
     *
     * @param purchaseID of the purchase
     * @return the purchase instance
     */
    @Override
    public Purchase getPurchaseByID(int purchaseID) {

        //Checking if connection is null
        if (Datasource.getInstance().isConnectionNull()) {
            return null;
        }

        try {
            queryPurchaseByParameter = Datasource.getInstance().getConnection().prepareStatement(QUERY_PURCHASE_BY_ID);
            queryPurchaseByParameter.setInt(1, purchaseID);
        } catch (SQLException e) {
            System.out.println("Couldn't initialize prepared statement: " + e.getMessage());
            return null;
        }

        try (ResultSet results = queryPurchaseByParameter.executeQuery()) {

            //Action when getting a null result set
            if (!results.next()) {
                System.out.println("No purchase was found!");
                return null;
            }

            //Positioning the ResultSet's cursor back to where it was before the first row
            results.beforeFirst();

            //Going to the first and only element in the result set
            results.next();

            //Creating the instance that we want to return
            Purchase purchase = new Purchase();

            //Initializing attributes of that instance
            purchase.setPurchaseID(results.getInt(COLUMN_PURCHASE_ID));
            purchase.setInvoiceID(results.getInt(COLUMN_INVOICE_ID));
            purchase.setSupplierID(results.getInt(COLUMN_SUPPLIER_ID));
            purchase.setProductID(results.getInt(COLUMN_PRODUCT_ID));
            purchase.setCostPrice(results.getFloat(COLUMN_PRODUCT_COST_PRICE));
            purchase.setQuantity(results.getInt(COLUMN_QUANTITY));
            purchase.setTotalCost(results.getFloat(COLUMN_TOTAL_COST));
            purchase.setSupplierName(SupplierModel.getInstance()
                    .getSupplierByID(results.getInt(COLUMN_SUPPLIER_ID)).getSupplierName());
            purchase.setProductName(ProductModel.getInstance()
                    .getProductByID(results.getInt(COLUMN_PRODUCT_ID)).getBrandName()
                    + " " + ProductModel.getInstance()
                    .getProductByID(results.getInt(COLUMN_PRODUCT_ID)).getProductName());

            return purchase;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        } finally {
            try {
                queryPurchaseByParameter.close();
            } catch (SQLException e) {
                System.out.println("Couldn't close prepared statement: " + e.getMessage());
            }
        }
    }

    /**
     * Method to query list of purchases in an invoice
     *
     * @param invoiceID of the invoice
     * @return a list of purchases in that invoice
     */
    @Override
    public List<Purchase> queryPurchasesByInvoice(int invoiceID) {

        //Checking if connection is null
        if (Datasource.getInstance().isConnectionNull()) {
            return Collections.emptyList();
        }

        try {
            queryPurchaseByParameter = Datasource.getInstance().getConnection().prepareStatement(QUERY_PURCHASE_INVOICE);
            queryPurchaseByParameter.setInt(1, invoiceID);
        } catch (SQLException e) {
            System.out.println("Couldn't initialize prepared statement: " + e.getMessage());
            return Collections.emptyList();
        }

        try (ResultSet results = queryPurchaseByParameter.executeQuery()) {

            //Action when getting a null result set
            if (!results.next()) {
                System.out.println("No purchase was found!");
                return Collections.emptyList();
            }
            //Positioning the ResultSet's cursor back to where it was before the first row
            results.beforeFirst();

            //Creating the list that is going to be returned
            List<Purchase> purchases = new ArrayList<>();

            //Inserting results in the list
            while (results.next()) {
                Purchase purchase = new Purchase();
                purchase.setPurchaseID(results.getInt(COLUMN_PURCHASE_ID));
                purchase.setInvoiceID(results.getInt(COLUMN_INVOICE_ID));
                purchase.setSupplierID(results.getInt(COLUMN_SUPPLIER_ID));
                purchase.setProductID(results.getInt(COLUMN_PRODUCT_ID));
                purchase.setCostPrice(results.getFloat(COLUMN_PRODUCT_COST_PRICE));
                purchase.setQuantity(results.getInt(COLUMN_QUANTITY));
                purchase.setTotalCost(results.getFloat(COLUMN_TOTAL_COST));
                purchase.setSupplierName(SupplierModel.getInstance()
                        .getSupplierByID(results.getInt(COLUMN_SUPPLIER_ID)).getSupplierName());
                purchase.setProductName(ProductModel.getInstance()
                        .getProductByID(results.getInt(COLUMN_PRODUCT_ID)).getBrandName()
                        + " " + ProductModel.getInstance()
                        .getProductByID(results.getInt(COLUMN_PRODUCT_ID)).getProductName());
                purchases.add(purchase);
            }

            return purchases;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Method to query list of purchases from a supplier
     *
     * @param supplierID of the supplier
     * @return a list of purchases from the supplier
     */
    @Override
    public List<Purchase> queryPurchasesBySupplier(int supplierID) {

        //Checking if connection is null
        if (Datasource.getInstance().isConnectionNull()) {
            return Collections.emptyList();
        }

        try {
            queryPurchaseByParameter = Datasource.getInstance().getConnection().prepareStatement(QUERY_PURCHASE_FROM_SUPPLIER);
            queryPurchaseByParameter.setInt(1, supplierID);
        } catch (SQLException e) {
            System.out.println("Couldn't initialize prepared statement: " + e.getMessage());
            return Collections.emptyList();
        }

        try (ResultSet results = queryPurchaseByParameter.executeQuery()) {

            //Action when getting a null result set
            if (!results.next()) {
                System.out.println("No purchase was found!");
                return Collections.emptyList();
            }
            //Positioning the ResultSet's cursor back to where it was before the first row
            results.beforeFirst();

            //Creating the list that is going to be returned
            List<Purchase> purchases = new ArrayList<>();

            //Inserting results in the list
            while (results.next()) {
                Purchase purchase = new Purchase();
                purchase.setPurchaseID(results.getInt(COLUMN_PURCHASE_ID));
                purchase.setInvoiceID(results.getInt(COLUMN_INVOICE_ID));
                purchase.setSupplierID(results.getInt(COLUMN_SUPPLIER_ID));
                purchase.setProductID(results.getInt(COLUMN_PRODUCT_ID));
                purchase.setCostPrice(results.getFloat(COLUMN_PRODUCT_COST_PRICE));
                purchase.setQuantity(results.getInt(COLUMN_QUANTITY));
                purchase.setTotalCost(results.getFloat(COLUMN_TOTAL_COST));
                purchase.setSupplierName(SupplierModel.getInstance()
                        .getSupplierByID(results.getInt(COLUMN_SUPPLIER_ID)).getSupplierName());
                purchase.setProductName(ProductModel.getInstance()
                        .getProductByID(results.getInt(COLUMN_PRODUCT_ID)).getBrandName()
                        + " " + ProductModel.getInstance()
                        .getProductByID(results.getInt(COLUMN_PRODUCT_ID)).getProductName());
                purchases.add(purchase);
            }

            return purchases;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Method to query list of purchases of a product
     *
     * @param productID of the product
     * @return a list of purchases of that product
     */
    @Override
    public List<Purchase> queryPurchasesByProduct(int productID) {

        //Checking if connection is null
        if (Datasource.getInstance().isConnectionNull()) {
            return Collections.emptyList();
        }

        try {
            queryPurchaseByParameter = Datasource.getInstance().getConnection().prepareStatement(QUERY_PURCHASE_FOR_PRODUCT);
            queryPurchaseByParameter.setInt(1, productID);
        } catch (SQLException e) {
            System.out.println("Couldn't initialize prepared statement: " + e.getMessage());
            return Collections.emptyList();
        }

        try (ResultSet results = queryPurchaseByParameter.executeQuery()) {

            //Action when getting a null result set
            if (!results.next()) {
                System.out.println("No purchase was found!");
                return Collections.emptyList();
            }
            //Positioning the ResultSet's cursor back to where it was before the first row
            results.beforeFirst();

            //Creating the list that is going to be returned
            List<Purchase> purchases = new ArrayList<>();

            //Inserting results in the list
            while (results.next()) {
                Purchase purchase = new Purchase();
                purchase.setPurchaseID(results.getInt(COLUMN_PURCHASE_ID));
                purchase.setInvoiceID(results.getInt(COLUMN_INVOICE_ID));
                purchase.setSupplierID(results.getInt(COLUMN_SUPPLIER_ID));
                purchase.setProductID(results.getInt(COLUMN_PRODUCT_ID));
                purchase.setCostPrice(results.getFloat(COLUMN_PRODUCT_COST_PRICE));
                purchase.setQuantity(results.getInt(COLUMN_QUANTITY));
                purchase.setTotalCost(results.getFloat(COLUMN_TOTAL_COST));
                purchase.setSupplierName(SupplierModel.getInstance()
                        .getSupplierByID(results.getInt(COLUMN_SUPPLIER_ID)).getSupplierName());
                purchase.setProductName(ProductModel.getInstance()
                        .getProductByID(results.getInt(COLUMN_PRODUCT_ID)).getBrandName()
                        + " " + ProductModel.getInstance()
                        .getProductByID(results.getInt(COLUMN_PRODUCT_ID)).getProductName());
                purchases.add(purchase);
            }

            return purchases;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Method to insert a purchase using
     *
     * @param invoiceID  of the purchase
     * @param supplierID of the purchase
     * @param productID  of the purchase
     * @param costPrice of the purchase
     * @param quantity   of the purchase
     * @param totalCost  of the purchase
     * @return generated ID of the new purchase
     * @throws SQLException in case inserting the new purchase fails
     */
    @Override
    public int insertPurchase(int invoiceID, int supplierID, int productID, float costPrice, int quantity, float totalCost) throws SQLException {

        /*
        Check if purchase record already exists
        Checking invoice ID, supplier ID, and product ID if they exist together in one purchase record
         */

        //Checking purchase record's invoice ID, supplier ID, & product ID
        queryPurchaseByParameter = Datasource.getInstance().getConnection().prepareStatement(CHECK_SALE);
        queryPurchaseByParameter.setInt(1, invoiceID);
        queryPurchaseByParameter.setInt(2, supplierID);
        queryPurchaseByParameter.setInt(3 , productID);
        ResultSet result = queryPurchaseByParameter.executeQuery();

        //Action when the purchase record already exists
        if (result.next()) {
            //Getting the ID of the purchase record that already exists
            int purchaseIDExisted = result.getInt(COLUMN_PURCHASE_ID);
            //Getting the cost price of the purchase record that already exists
            float costPriceExisted = result.getFloat(COLUMN_PRODUCT_COST_PRICE);
            //Getting the quantity of the purchase record that already exists
            int quantityExisted = result.getInt(COLUMN_QUANTITY);
            //Getting the totalCost of the purchase record that already exists
            float totalCostExisted = result.getFloat(COLUMN_TOTAL_COST);
            //Closing the prepared statement
            queryPurchaseByParameter.close();
            //Updating the purchase record that already exists
            updatePurchase(purchaseIDExisted, invoiceID, supplierID, productID, costPriceExisted,
                    quantityExisted + quantity, totalCostExisted + totalCost);
            ProductModel.getInstance().buyProduct(productID, quantity);
            //Returning the purchase ID of the purchase record that already exists
            System.out.println("Purchase record with ID " + purchaseIDExisted + " already existed and is updated");
            return purchaseIDExisted;
        } else {

            //Insert the purchase
            insertIntoPurchase = Datasource.getInstance().getConnection().prepareStatement(INSERT_PURCHASE, Statement.RETURN_GENERATED_KEYS);
            insertIntoPurchase.setInt(1, invoiceID);
            insertIntoPurchase.setInt(2, supplierID);
            insertIntoPurchase.setInt(3, productID);
            insertIntoPurchase.setFloat(4, costPrice);
            insertIntoPurchase.setInt(5, quantity);
            insertIntoPurchase.setFloat(6, totalCost);


            int affectedRows = insertIntoPurchase.executeUpdate();

            //Exception when not being able to insert
            if (affectedRows != 1) {
                insertIntoPurchase.close();
                throw new SQLException("Couldn't insert purchase!");
            }

            //Getting the generated key of the inserted purchase
            ResultSet generatedKeys = insertIntoPurchase.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedKey = generatedKeys.getInt(1);
                insertIntoPurchase.close();
                System.out.println("New purchase created with ID: " + generatedKey);
                ProductModel.getInstance().buyProduct(productID, quantity);
                return generatedKey;
            } else {
                insertIntoPurchase.close();
                throw new SQLException("Couldn't get ID for the purchase");
            }
        }
    }

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
    @Override
    public boolean updatePurchase(int purchaseID, int newInvoiceID, int newSupplierID, int newProductID, float newCostPrice, int newQuantity, float newTotalCost) {

        try {
            //Set fields in the prepared statement
            updatePurchase = Datasource.getInstance().getConnection().prepareStatement(UPDATE_PURCHASE);
            updatePurchase.setInt(1, newInvoiceID);
            updatePurchase.setInt(2, newSupplierID);
            updatePurchase.setInt(3, newProductID);
            updatePurchase.setFloat(4, newCostPrice);
            updatePurchase.setInt(5, newQuantity);
            updatePurchase.setFloat(6, newTotalCost);
            updatePurchase.setInt(7, purchaseID);

            //Checking the update result
            int affectedRecords = updatePurchase.executeUpdate();

            //Returning true if update has been successful
            return (affectedRecords == 1);

        } catch (SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Method to delete a purchase
     *
     * @param purchaseID of the purchase that we want to delete
     * @return true if succeeded, false if failed
     */
    @Override
    public boolean deletePurchase(int purchaseID) {

        try {
            //Set fields in the prepared statement
            deletePurchase = Datasource.getInstance().getConnection().prepareStatement(DELETE_PURCHASE);
            deletePurchase.setInt(1, purchaseID);

            //Checking the delete result
            int affectedRecords = deletePurchase.executeUpdate();

            //Returning true if delete has been successful
            return (affectedRecords == 1);

        } catch (SQLException e) {
            System.out.println("Delete failed: " + e.getMessage());
            return false;
        }
    }
}
