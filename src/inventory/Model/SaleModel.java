package inventory.Model;

import com.mysql.jdbc.Statement;
import inventory.DAO.SaleDAO;
import inventory.Entity.Sale;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SaleModel implements SaleDAO {

    //Constants for the sales table
    public static final String TABLE_SALE = "sales";
    public static final String COLUMN_SALE_ID = "saleID";
    public static final String COLUMN_INVOICE_ID = "invoiceID";
    public static final String COLUMN_PRODUCT_ID = "productID";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_TOTAL_COST = "totalCost";
    public static final String INDEX_PURCHASE_ID = "1";
    public static final String INDEX_INVOICE_ID = "2";
    public static final String INDEX_PRODUCT_ID = "3";
    public static final String INDEX_QUANTITY = "4";
    public static final String INDEX_TOTAL_COST = "5";

    //Constant for the ORDER BY clause in queries
    public static final String ORDER_BY_DES = " ORDER BY " + COLUMN_SALE_ID + " DESC";

    //Constants for querying sales
    public static final String QUERY_SALES =
            "SELECT " + TABLE_SALE + "." + COLUMN_SALE_ID + ", " +
                        TABLE_SALE + "." + COLUMN_INVOICE_ID + ", " +
                        TABLE_SALE + "." + COLUMN_PRODUCT_ID + ", " +
                        TABLE_SALE + "." + COLUMN_QUANTITY + ", " +
                        TABLE_SALE + "." + COLUMN_TOTAL_COST +
                    " FROM " + TABLE_SALE;

    //Constant for querying sale by its id
    public static final String QUERY_SALE_BY_ID = QUERY_SALES +
            " WHERE " + TABLE_SALE + "." + COLUMN_SALE_ID + " = ?" + ORDER_BY_DES;

    //Constant for querying sales of a product
    public static final String QUERY_SALE_FOR_PRODUCT = QUERY_SALES +
            " WHERE " + TABLE_SALE + "." + COLUMN_PRODUCT_ID + " = ?" + ORDER_BY_DES;

    //Constant for querying sales on an invoice
    public static final String QUERY_SALE_INVOICE = QUERY_SALES +
            " WHERE " + TABLE_SALE + "." + COLUMN_INVOICE_ID + " = ?" + ORDER_BY_DES;

    //Constant for inserting new sale
    public static final String INSERT_SALE =
            "INSERT INTO " + TABLE_SALE +
                    " (" + COLUMN_INVOICE_ID + ", " +
                           COLUMN_PRODUCT_ID + ", " +
                           COLUMN_QUANTITY + ", " +
                           COLUMN_TOTAL_COST + ") VALUES (?, ?, ?, ?)";

    //Constant for updating a sale
    public static final String UPDATE_SALE =
            "UPDATE " + TABLE_SALE +
                    " SET " + TABLE_SALE + "." + COLUMN_INVOICE_ID + " = ?, "
                            + TABLE_SALE + "." + COLUMN_PRODUCT_ID + " = ?, "
                            + TABLE_SALE + "." + COLUMN_QUANTITY + " = ?, "
                            + TABLE_SALE + "." + COLUMN_TOTAL_COST + " = ? WHERE "
                            + TABLE_SALE + "." + COLUMN_SALE_ID + " = ?";

    //Constant for deleting a sale
    public static final String DELETE_SALE =
            "DELETE FROM " + TABLE_SALE + " WHERE " + TABLE_SALE + "." + COLUMN_SALE_ID + " = ?";

    //Constant for checking if a sale record exists
    public static final String CHECK_SALE = QUERY_SALES +
            " WHERE (" + TABLE_SALE + "." + COLUMN_INVOICE_ID + " = ?) AND ("
                       + TABLE_SALE + "." + COLUMN_PRODUCT_ID + " = ?)";

    /*----------------------------------------------------------------------------------------------------------*/

    PreparedStatement querySaleByParameter;
    PreparedStatement insertIntoSale;
    PreparedStatement updateSale;
    PreparedStatement deleteSale;

    /*----------------------------------------------------------------------------------------------------------*/

    //Creating a private static instance of our class to make it a Singleton class
    private static SaleModel instance = new SaleModel();

    //Making the constructor private to make it used only by this class to make it a Singleton class
    private SaleModel() {
    }

    //A getter of the one and only instance of this class (Singleton class)
    public static SaleModel getInstance() {
        return instance;
    }

    /*----------------------------------------------------------------------------------------------------------*/

    /**
     * Method to query list of all sales
     *
     * @return a list of all sales
     */
    @Override
    public List<Sale> querySales() {

        try (PreparedStatement statement = Datasource.getInstance().getConnection().prepareStatement(QUERY_SALES + ORDER_BY_DES);
             ResultSet results = statement.executeQuery()) {

            //Action when getting a null result set
            if (!results.next()) {
                System.out.println("No sale was found!");
                return Collections.emptyList();
            }

            //Positioning the ResultSet's cursor back to where it was before the first row
            results.beforeFirst();

            //Creating the list that is going to be returned
            List<Sale> sales = new ArrayList<>();

            //Inserting results in the list
            while (results.next()) {
                Sale sale = new Sale();
                sale.setSaleID(results.getInt(COLUMN_SALE_ID));
                sale.setInvoiceID(results.getInt(COLUMN_INVOICE_ID));
                sale.setProductID(results.getInt(COLUMN_PRODUCT_ID));
                sale.setQuantity(results.getInt(COLUMN_QUANTITY));
                sale.setTotalCost(results.getFloat(COLUMN_TOTAL_COST));
                sale.setBrandName(ProductModel.getInstance().
                        getProductByID(results.getInt(COLUMN_PRODUCT_ID)).getBrandName());
                sale.setProductName(ProductModel.getInstance().
                        getProductByID(results.getInt(COLUMN_PRODUCT_ID)).getProductName());
                sale.setSellingPrice(ProductModel.getInstance().
                        getProductByID(results.getInt(COLUMN_PRODUCT_ID)).getSellingPrice());

                sales.add(sale);
            }

            return sales;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Method to query a sale by its ID
     * @param saleID of the sale
     * @return the sale instance
     */
    @Override
    public Sale getSaleByID(int saleID) {

        //Checking if connection is null
        if (Datasource.getInstance().isConnectionNull()) {
            return null;
        }

        try {
            querySaleByParameter = Datasource.getInstance().getConnection().prepareStatement(QUERY_SALE_BY_ID);
            querySaleByParameter.setInt(1, saleID);
        } catch (SQLException e) {
            System.out.println("Couldn't initialize prepared statement: " + e.getMessage());
            return null;
        }

        try (ResultSet results = querySaleByParameter.executeQuery()) {

            //Action when getting a null result set
            if (!results.next()) {
                System.out.println("No sale was found!");
                return null;
            }

            //Positioning the ResultSet's cursor back to where it was before the first row
            results.beforeFirst();

            //Going to the first and only element in the result set
            results.next();

            //Creating the instance that we want to return
            Sale sale = new Sale();

            //Initializing attributes of that instance
            sale.setSaleID(results.getInt(COLUMN_SALE_ID));
            sale.setInvoiceID(results.getInt(COLUMN_INVOICE_ID));
            sale.setProductID(results.getInt(COLUMN_PRODUCT_ID));
            sale.setQuantity(results.getInt(COLUMN_QUANTITY));
            sale.setTotalCost(results.getFloat(COLUMN_TOTAL_COST));
            sale.setBrandName(ProductModel.getInstance().
                    getProductByID(results.getInt(COLUMN_PRODUCT_ID)).getBrandName());
            sale.setProductName(ProductModel.getInstance().
                    getProductByID(results.getInt(COLUMN_PRODUCT_ID)).getProductName());
            sale.setSellingPrice(ProductModel.getInstance().
                    getProductByID(results.getInt(COLUMN_PRODUCT_ID)).getSellingPrice());

            return sale;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        } finally {
            try {
                querySaleByParameter.close();
            } catch (SQLException e) {
                System.out.println("Couldn't close prepared statement: " + e.getMessage());
            }
        }
    }

    /**
     * Method to query list of sales in an invoice
     *
     * @param invoiceID of the invoice
     * @return a list of sales in that invoice
     */
    @Override
    public List<Sale> querySalesByInvoice(int invoiceID) {

        //Checking if connection is null
        if (Datasource.getInstance().isConnectionNull()) {
            return Collections.emptyList();
        }

        try {
            querySaleByParameter = Datasource.getInstance().getConnection().prepareStatement(QUERY_SALE_INVOICE);
            querySaleByParameter.setInt(1, invoiceID);
        } catch (SQLException e) {
            System.out.println("Couldn't initialize prepared statement: " + e.getMessage());
            return Collections.emptyList();
        }

        try (ResultSet results = querySaleByParameter.executeQuery()) {

            //Action when getting a null result set
            if (!results.next()) {
                System.out.println("No sale was found!");
                return Collections.emptyList();
            }
            //Positioning the ResultSet's cursor back to where it was before the first row
            results.beforeFirst();

            //Creating the list that is going to be returned
            List<Sale> sales = new ArrayList<>();

            //Inserting results in the list
            while (results.next()) {
                Sale sale = new Sale();
                sale.setSaleID(results.getInt(COLUMN_SALE_ID));
                sale.setInvoiceID(results.getInt(COLUMN_INVOICE_ID));
                sale.setProductID(results.getInt(COLUMN_PRODUCT_ID));
                sale.setQuantity(results.getInt(COLUMN_QUANTITY));
                sale.setTotalCost(results.getFloat(COLUMN_TOTAL_COST));
                sale.setBrandName(ProductModel.getInstance().
                        getProductByID(results.getInt(COLUMN_PRODUCT_ID)).getBrandName());
                sale.setProductName(ProductModel.getInstance().
                        getProductByID(results.getInt(COLUMN_PRODUCT_ID)).getProductName());
                sale.setSellingPrice(ProductModel.getInstance().
                        getProductByID(results.getInt(COLUMN_PRODUCT_ID)).getSellingPrice());

                sales.add(sale);
            }

            return sales;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Method to query list of sales of a product
     *
     * @param productID of the product
     * @return a list of sales of that product
     */
    @Override
    public List<Sale> querySalesByProduct(int productID) {

        //Checking if connection is null
        if (Datasource.getInstance().isConnectionNull()) {
            return Collections.emptyList();
        }

        try {
            querySaleByParameter = Datasource.getInstance().getConnection().prepareStatement(QUERY_SALE_FOR_PRODUCT);
            querySaleByParameter.setInt(1, productID);
        } catch (SQLException e) {
            System.out.println("Couldn't initialize prepared statement: " + e.getMessage());
            return Collections.emptyList();
        }

        try (ResultSet results = querySaleByParameter.executeQuery()) {

            //Action when getting a null result set
            if (!results.next()) {
                System.out.println("No sale was found!");
                return Collections.emptyList();
            }
            //Positioning the ResultSet's cursor back to where it was before the first row
            results.beforeFirst();

            //Creating the list that is going to be returned
            List<Sale> sales = new ArrayList<>();

            //Inserting results in the list
            while (results.next()) {
                Sale sale = new Sale();
                sale.setSaleID(results.getInt(COLUMN_SALE_ID));
                sale.setInvoiceID(results.getInt(COLUMN_INVOICE_ID));
                sale.setProductID(results.getInt(COLUMN_PRODUCT_ID));
                sale.setQuantity(results.getInt(COLUMN_QUANTITY));
                sale.setTotalCost(results.getFloat(COLUMN_TOTAL_COST));
                sale.setBrandName(ProductModel.getInstance().
                        getProductByID(results.getInt(COLUMN_PRODUCT_ID)).getBrandName());
                sale.setProductName(ProductModel.getInstance().
                        getProductByID(results.getInt(COLUMN_PRODUCT_ID)).getProductName());
                sale.setSellingPrice(ProductModel.getInstance().
                        getProductByID(results.getInt(COLUMN_PRODUCT_ID)).getSellingPrice());

                sales.add(sale);
            }

            return sales;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Method to insert a sale using
     * @param invoiceID of the sale
     * @param productID of the sale
     * @param quantity of the sale
     * @param totalCost of the sale
     * @return generated ID of the new sale
     * @throws SQLException in case inserting the new sale fails
     */
    @Override
    public int insertSale(int invoiceID, int productID, int quantity, float totalCost) throws SQLException {

        /*
        Check if sale record already exists
        Checking invoice ID and product ID if they exist together in one sale record
         */

        //Checking sale record's invoice ID & product ID
        querySaleByParameter = Datasource.getInstance().getConnection().prepareStatement(CHECK_SALE);
        querySaleByParameter.setInt(1, invoiceID);
        querySaleByParameter.setInt(2, productID);
        ResultSet result = querySaleByParameter.executeQuery();

        //Action when the sale record already exists
        if (result.next()) {
            //Getting the ID of the sale record that already exists
            int saleIDExisted = result.getInt(COLUMN_SALE_ID);
            //Getting the quantity of the sale record that already exists
            int quantityExisted = result.getInt(COLUMN_QUANTITY);
            //Getting the totalCost of the sale record that already exists
            float totalCostExisted = result.getFloat(COLUMN_TOTAL_COST);
            //Closing the prepared statement
            querySaleByParameter.close();
            //Updating the sale record that already exists
            updateSale(saleIDExisted, invoiceID, productID, quantityExisted + quantity,
                    totalCostExisted + totalCost);
            ProductModel.getInstance().sellProduct(productID, quantity);
            //Returning the sale ID of the sale record that already exists
            System.out.println("Sale record with ID " + saleIDExisted + " already existed and is updated");
            return saleIDExisted;
        } else {

            //Insert the sale
            insertIntoSale = Datasource.getInstance().getConnection().prepareStatement(INSERT_SALE, Statement.RETURN_GENERATED_KEYS);
            insertIntoSale.setInt(1, invoiceID);
            insertIntoSale.setInt(2, productID);
            insertIntoSale.setInt(3, quantity);
            insertIntoSale.setFloat(4, totalCost);


            int affectedRows = insertIntoSale.executeUpdate();

            //Exception when not being able to insert
            if (affectedRows != 1) {
                insertIntoSale.close();
                throw new SQLException("Couldn't insert sale!");
            }

            //Getting the generated key of the inserted sale
            ResultSet generatedKeys = insertIntoSale.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedKey = generatedKeys.getInt(1);
                insertIntoSale.close();
                System.out.println("New sale created with ID: " + generatedKey);
                ProductModel.getInstance().sellProduct(productID, quantity);
                return generatedKey;
            } else {
                insertIntoSale.close();
                throw new SQLException("Couldn't get ID for the sale");
            }
        }
    }

    /**
     * Method to update a sale
     * @param saleID of the sale that we want to update
     * @param newInvoiceID
     * @param newProductID
     * @param newQuantity
     * @param newTotalCost
     * @return true if succeeded, false if failed
     */
    @Override
    public boolean updateSale(int saleID, int newInvoiceID, int newProductID, int newQuantity, float newTotalCost) {

        try {
            //Set fields in the prepared statement
            updateSale = Datasource.getInstance().getConnection().prepareStatement(UPDATE_SALE);
            updateSale.setInt(1, newInvoiceID);
            updateSale.setInt(2, newProductID);
            updateSale.setInt(3, newQuantity);
            updateSale.setFloat(4, newTotalCost);
            updateSale.setInt(5, saleID);

            //Checking the update result
            int affectedRecords = updateSale.executeUpdate();

            //Returning true if update has been successful
            return (affectedRecords == 1);

        } catch (SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Method to delete a sale
     * @param saleID of the sale that we want to delete
     * @return true if succeeded, false if failed
     */
    @Override
    public boolean deleteSale(int saleID) {

        try {
            //Set fields in the prepared statement
            deleteSale = Datasource.getInstance().getConnection().prepareStatement(DELETE_SALE);
            deleteSale.setInt(1, saleID);

            //Checking the delete result
            int affectedRecords = deleteSale.executeUpdate();

            //Returning true if delete has been successful
            return (affectedRecords == 1);

        } catch (SQLException e) {
            System.out.println("Delete failed: " + e.getMessage());
            return false;
        }
    }
}
