package inventory.Model;

import com.mysql.jdbc.Statement;
import inventory.DAO.ProductDAO;
import inventory.Entity.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductModel implements ProductDAO {

    //Constants for the products table
    public static final String TABLE_PRODUCT = "product";
    public static final String COLUMN_PRODUCT_ID = "productID";
    public static final String COLUMN_PRODUCT_NAME = "productName";
    public static final String COLUMN_PRODUCT_PICTURE = "productPicture";
    public static final String COLUMN_PRODUCT_BRAND_ID = "brandID";
    public static final String COLUMN_PRODUCT_SELLING_PRICE = "sellingPrice";
    public static final String COLUMN_PRODUCT_QUANTITY_MINIMUM = "quantityMinimum";
    public static final String COLUMN_PRODUCT_QUANTITY_IN_STOCK = "quantityInStock";
    public static final String INDEX_PRODUCT_ID = "1";
    public static final String INDEX_PRODUCT_NAME = "2";
    public static final String INDEX_PRODUCT_PICTURE = "3";
    public static final String INDEX_PRODUCT_BRAND_ID = "4";
    public static final String INDEX_PRODUCT_SELLING_PRICE = "5";
    public static final String INDEX_PRODUCT_QUANTITY_MINIMUM = "6";
    public static final String INDEX_PRODUCT_QUANTITY_IN_STOCK = "7";

    //Constants for the PRODUCT_INFO View
    public static final String VIEW_PRODUCT_INFO = "product_info";
    public static final String COLUMN_PRODUCT_INFO_ID = COLUMN_PRODUCT_ID;
    public static final String COLUMN_PRODUCT_INFO_NAME = COLUMN_PRODUCT_NAME;
    public static final String COLUMN_PRODUCT_INFO_BRAND_NAME = "brandName";
    public static final String COLUMN_PRODUCT_INFO_SELLING_PRICE = COLUMN_PRODUCT_SELLING_PRICE;
    public static final String COLUMN_PRODUCT_INFO_QUANTITY_MINIMUM = COLUMN_PRODUCT_QUANTITY_MINIMUM;
    public static final String COLUMN_PRODUCT_INFO_QUANTITY_IN_STOCK = COLUMN_PRODUCT_QUANTITY_IN_STOCK;
    public static final String INDEX_PRODUCT_INFO_ID = "1";
    public static final String INDEX_PRODUCT_INFO_NAME = "2";
    public static final String INDEX_PRODUCT_INFO_BRAND_NAME = "3";
    public static final String INDEX_PRODUCT_INFO_SELLING_PRICE = "4";
    public static final String INDEX_PRODUCT_INFO_QUANTITY_MINIMUM = "5";
    public static final String INDEX_PRODUCT_INFO_QUANTITY_IN_STOCK = "6";

    //Constants for querying products
    public static final String QUERY_PRODUCTS =
            "SELECT " + TABLE_PRODUCT + "." + COLUMN_PRODUCT_ID + ", " +
                        TABLE_PRODUCT + "." + COLUMN_PRODUCT_NAME + ", " +
                        TABLE_PRODUCT + "." + COLUMN_PRODUCT_PICTURE + ", " +
                        TABLE_PRODUCT + "." + COLUMN_PRODUCT_BRAND_ID + ", " +
                        TABLE_PRODUCT + "." + COLUMN_PRODUCT_SELLING_PRICE + ", " +
                        TABLE_PRODUCT + "." + COLUMN_PRODUCT_QUANTITY_MINIMUM + ", " +
                        TABLE_PRODUCT + "." + COLUMN_PRODUCT_QUANTITY_IN_STOCK +
                    " FROM " + TABLE_PRODUCT;

    //Constant for querying product by its id
    public static final String QUERY_PRODUCT_BY_ID = QUERY_PRODUCTS +
            " WHERE " + TABLE_PRODUCT + "." + COLUMN_PRODUCT_ID + " = ?";

    //Constant for querying product by its name
    public static final String QUERY_PRODUCT_BY_NAME = QUERY_PRODUCTS +
            " WHERE " + TABLE_PRODUCT + "." + COLUMN_PRODUCT_NAME + " = ?";

    //Constant for querying product by its name & its brand
    public static final String QUERY_PRODUCT_BY_NAME_AND_BRAND = QUERY_PRODUCT_BY_NAME +
            " AND " + TABLE_PRODUCT + "." + COLUMN_PRODUCT_BRAND_ID + " = ?";

    //Constant for inserting new product
    public static final String INSERT_PRODUCT =
            "INSERT INTO " + TABLE_PRODUCT +
                    " (" + COLUMN_PRODUCT_ID + ", " +
                           COLUMN_PRODUCT_NAME + ", " +
                           COLUMN_PRODUCT_BRAND_ID + ", " +
                           COLUMN_PRODUCT_SELLING_PRICE + ", " +
                           COLUMN_PRODUCT_QUANTITY_MINIMUM + ", " +
                           COLUMN_PRODUCT_QUANTITY_IN_STOCK + ") VALUES (?, ?, ?, ?, ?, ?)";

    //Constant for updating a product
    public static final String UPDATE_PRODUCT =
            "UPDATE " + TABLE_PRODUCT +
                    " SET " + TABLE_PRODUCT + "." + COLUMN_PRODUCT_NAME + " = ?, "
                            + TABLE_PRODUCT + "." + COLUMN_PRODUCT_BRAND_ID + " = ?, "
                            + TABLE_PRODUCT + "." + COLUMN_PRODUCT_SELLING_PRICE + " = ?, "
                            + TABLE_PRODUCT + "." + COLUMN_PRODUCT_QUANTITY_MINIMUM + " = ?, "
                            + TABLE_PRODUCT + "." + COLUMN_PRODUCT_QUANTITY_IN_STOCK + " = ? WHERE "
                            + TABLE_PRODUCT + "." + COLUMN_PRODUCT_ID + " = ?";

    //Constant for deleting a product
    public static final String DELETE_PRODUCT =
            "DELETE FROM " + TABLE_PRODUCT + " WHERE " + TABLE_PRODUCT + "." + COLUMN_PRODUCT_ID + " = ?";

    //Constant for updating product's quantity
    public static final String UPDATE_PRODUCT_QUANTITY =
            "UPDATE " + TABLE_PRODUCT +
                    " SET " + TABLE_PRODUCT + "." + COLUMN_PRODUCT_QUANTITY_IN_STOCK + " = "
                            + TABLE_PRODUCT + "." + COLUMN_PRODUCT_QUANTITY_IN_STOCK + " + ( ? ) WHERE "
                            + TABLE_PRODUCT + "." + COLUMN_PRODUCT_ID + " = ?";

    /*----------------------------------------------------------------------------------------------------------*/

    PreparedStatement queryProductByParameter;
    PreparedStatement insertIntoProduct;
    PreparedStatement updateProduct;
    PreparedStatement deleteProduct;
    PreparedStatement updateProductQuantity;

    /*----------------------------------------------------------------------------------------------------------*/

    //Creating a private static instance of our class to make it a Singleton class
    private static ProductModel instance = new ProductModel();

    //Making the constructor private to make it used only by this claas to make it a Singleton class
    private ProductModel() {
    }

    //A getter of the one and only instance of this class (Singleton class)
    public static ProductModel getInstance() {
        return instance;
    }

    /*----------------------------------------------------------------------------------------------------------*/

    /**
     * Method to query list of all products
     *
     * @return a list of all products
     */
    @Override
    public List<Product> queryProducts(){

        //Checking if connection is null
        if (Datasource.getInstance().isConnectionNull()){
            return Collections.emptyList();
        }

        try (PreparedStatement statement = Datasource.getInstance().getConnection().prepareStatement(QUERY_PRODUCTS);
             ResultSet results = statement.executeQuery()) {

            //Action when getting a null result set
            if (!results.next()){
                System.out.println("No product was found!");
                return Collections.emptyList();
            }

            //Positioning the ResultSet's cursor back to where it was before the first row
            results.beforeFirst();

            //Creating the list that is going to be returned
            List<Product> products = new ArrayList<>();

            //Inserting results in the list
            while (results.next()){
                Product product = new Product();
                product.setProductID(results.getInt(COLUMN_PRODUCT_ID));
                product.setProductName(results.getString(COLUMN_PRODUCT_NAME));
                product.setBrandID(results.getInt(COLUMN_PRODUCT_BRAND_ID));
                product.setSellingPrice(results.getFloat(COLUMN_PRODUCT_SELLING_PRICE));
                product.setQuantityMinimum(results.getInt(COLUMN_PRODUCT_QUANTITY_MINIMUM));
                product.setQuantityInStock(results.getInt(COLUMN_PRODUCT_QUANTITY_IN_STOCK));
                product.setBrandName(BrandModel.getInstance()
                        .getBrandByID(results.getInt(COLUMN_PRODUCT_BRAND_ID)).getBrandName());
                products.add(product);
            }

            return products;

        } catch (SQLException e){
            System.out.println("Query failed: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Method that queries list of products' names only
     * @return a list of strings containing products' names
     */
    @Override
    public List<String> getProductsNames() {

        //Querying all the products from the database
        List<Product> products = queryProducts();

        //Checking if the list is empty
        if (products == null){
            System.out.println("No Products");
            return Collections.emptyList();
        }

        //Creating the list of the products' Names
        List<String> productsNames = new ArrayList<>();

        //Adding products' names to the list
        for (Product product : products){
            productsNames.add(product.getProductName());
        }

        return productsNames;
    }

    /**
     * Method that returns a Product instance that matches an ID given
     * @param productID the ID given to the method to make the search on the database
     * @return the Product instance that has got the same ID that was given
     */
    @Override
    public Product getProductByID(int productID) {
        //Checking if connection is null
        if (Datasource.getInstance().isConnectionNull()){
            return null;
        }

        try {
            queryProductByParameter = Datasource.getInstance().getConnection().prepareStatement(QUERY_PRODUCT_BY_ID);
            queryProductByParameter.setInt(1, productID);
        } catch (SQLException e) {
            System.out.println("Couldn't initialize prepared statement: " + e.getMessage());
            return null;
        }

        try (ResultSet results = queryProductByParameter.executeQuery()) {

            //Action when getting a null result set
            if (!results.next()){
                System.out.println("No product was found!");
                return null;
            }

            //Positioning the ResultSet's cursor back to where it was before the first row
            results.beforeFirst();

            //Going to the first and only element in the result set
            results.next();

            //Creating the instance that we want to return
            Product product = new Product();

            //Initializing attributes of that instance
            product.setProductID(results.getInt(COLUMN_PRODUCT_ID));
            product.setProductName(results.getString(COLUMN_PRODUCT_NAME));
            product.setBrandID(results.getInt(COLUMN_PRODUCT_BRAND_ID));
            product.setSellingPrice(results.getFloat(COLUMN_PRODUCT_SELLING_PRICE));
            product.setQuantityMinimum(results.getInt(COLUMN_PRODUCT_QUANTITY_MINIMUM));
            product.setQuantityInStock(results.getInt(COLUMN_PRODUCT_QUANTITY_IN_STOCK));
            product.setBrandName(BrandModel.getInstance()
                    .getBrandByID(results.getInt(COLUMN_PRODUCT_BRAND_ID)).getBrandName());

            return product;

        } catch (SQLException e){
            System.out.println("Query failed: " + e.getMessage());
            return null;
        } finally {
            try {
                queryProductByParameter.close();
            } catch (SQLException e){
                System.out.println("Couldn't close prepared statement: " + e.getMessage());
            }
        }
    }

    /**
     * Method that returns a Product instance that matches a name given
     * @param productName the name given to the method to make the search on the database
     * @return the Product instance that has got the same name that was given
     */
    @Override
    public Product getProductByName(String productName) {
        //Checking if connection is null
        if (Datasource.getInstance().isConnectionNull()){
            return null;
        }

        try {
            queryProductByParameter = Datasource.getInstance().getConnection().prepareStatement(QUERY_PRODUCT_BY_NAME);
            queryProductByParameter.setString(1, productName);
        } catch (SQLException e) {
            System.out.println("Couldn't initialize prepared statement: " + e.getMessage());
            return null;
        }

        try (ResultSet results = queryProductByParameter.executeQuery()) {

            //Action when getting a null result set
            if (!results.next()){
                System.out.println("No product was found!");
                return null;
            }

            //Positioning the ResultSet's cursor back to where it was before the first row
            results.beforeFirst();

            //Going to the first and only element in the result set
            results.next();

            //Creating the instance that we want to return
            Product product = new Product();

            //Initializing attributes of that instance
            product.setProductID(results.getInt(COLUMN_PRODUCT_ID));
            product.setProductName(results.getString(COLUMN_PRODUCT_NAME));
            product.setBrandID(results.getInt(COLUMN_PRODUCT_BRAND_ID));
            product.setSellingPrice(results.getFloat(COLUMN_PRODUCT_SELLING_PRICE));
            product.setQuantityMinimum(results.getInt(COLUMN_PRODUCT_QUANTITY_MINIMUM));
            product.setQuantityInStock(results.getInt(COLUMN_PRODUCT_QUANTITY_IN_STOCK));
            product.setBrandName(BrandModel.getInstance()
                    .getBrandByID(results.getInt(COLUMN_PRODUCT_BRAND_ID)).getBrandName());

            return product;

        } catch (SQLException e){
            System.out.println("Query failed: " + e.getMessage());
            return null;
        } finally {
            try {
                queryProductByParameter.close();
            } catch (SQLException e){
                System.out.println("Couldn't close prepared statement: " + e.getMessage());
            }
        }
    }

    /**
     * Method to insert product using
     * @param product object that we want to insert
     * @return ID of the new product
     * @throws SQLException in case  inserting the new product fails
     */
    @Override
    public int insertProduct(Product product) throws SQLException {

        /*
        Check if product already exists
        Checking ID and then checking name and brand of the product
         */

        //Checking product's ID
        queryProductByParameter = Datasource.getInstance().getConnection().prepareStatement(QUERY_PRODUCT_BY_ID);
        queryProductByParameter.setInt(1, product.getProductID());
        ResultSet result = queryProductByParameter.executeQuery();

        //Action when product's ID already exists
        if (result.next()) {
            //Return the ID of the product that already exists
            int productID = result.getInt(COLUMN_PRODUCT_ID);
            queryProductByParameter.close();
            //We could've just returend the inserted ID but just for confirming
            System.out.println("Product with ID \"" + productID + "\" already exists");
            return productID;
        } else {

            //Closing the prepared statement and the result set
            result.close();
            queryProductByParameter.close();

            //Reinitializing the prepared statement and the result set with null values
            queryProductByParameter = null;
            result = null;

            //Checking product's name
            queryProductByParameter = Datasource.getInstance().getConnection().prepareStatement(QUERY_PRODUCT_BY_NAME_AND_BRAND);
            queryProductByParameter.setString(1, product.getProductName());
            queryProductByParameter.setInt(2, product.getBrandID());
            result = queryProductByParameter.executeQuery();

            //Action when product's name already exists
            if (result.next()) {
                //Return the ID of the product that already exists
                int productID = result.getInt(1);
                queryProductByParameter.close();
                System.out.println("Product with name \"" + product.getProductName() + "\" and brand \"" + product.getBrandID()
                                                            + "\" already exists with ID: " + productID);
                return productID;
            }
            //If product's name does not exist
            else {
                //Closing prepared statement that we won't use anymore
                queryProductByParameter.close();

                //Insert the product
                insertIntoProduct = Datasource.getInstance().getConnection().prepareStatement(INSERT_PRODUCT, Statement.RETURN_GENERATED_KEYS);
                insertIntoProduct.setInt(1, product.getProductID());
                insertIntoProduct.setString(2, product.getProductName());
                insertIntoProduct.setInt(3, product.getBrandID());
                insertIntoProduct.setFloat(4, product.getSellingPrice());
                insertIntoProduct.setInt(5, product.getQuantityMinimum());
                insertIntoProduct.setInt(6, product.getQuantityInStock());


                int affectedRows = insertIntoProduct.executeUpdate();

                //Exception when not being able to insert
                if (affectedRows != 1) {
                    insertIntoProduct.close();
                    throw new SQLException("Couldn't insert product!");
                }

                //Returning the ID of the inserted product
                /*
                 It should be the same as the inserted ID above but I'm doing this procedure to check if it has been
                 inserted correctly
                 */
                ResultSet generatedKeys = insertIntoProduct.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int generatedKey = generatedKeys.getInt(1);
                    insertIntoProduct.close();
                    System.out.println("New product created with ID: " + generatedKey);
                    return generatedKey;
                } else {
                    insertIntoProduct.close();
                    throw new SQLException("Couldn't get ID for the product");
                }
            }
        }
    }

    /**
     * Method to insert product using
     * @param id of the new product
     * @param name of the new product
     * @param brandID of the new product (reference to Brands)
     * @param sellingPrice of the new product
     * @param quantityMinimum of the new product
     * @param quantityInStock of the new product
     * @return ID of the new product
     * @throws SQLException in case  inserting the new product fails
     */
    @Override
    public int insertProduct(int id, String name, int brandID, float sellingPrice,
                             int quantityMinimum, int quantityInStock) throws SQLException {

        /*
        Check if product already exists
        Checking ID and then checking name and brand of the product
         */

        //Checking product's ID
        queryProductByParameter = Datasource.getInstance().getConnection().prepareStatement(QUERY_PRODUCT_BY_ID);
        queryProductByParameter.setInt(1, id);
        ResultSet result = queryProductByParameter.executeQuery();

        //Action when product's ID already exists
        if (result.next()) {
            //Return the ID of the product that already exists
            int productID = result.getInt(1);
            queryProductByParameter.close();
            //We could've just returend the inserted ID but just for confirming
            System.out.println("Product with ID \"" + productID + "\" already exists");
            return productID;
        } else {
            //Closing the prepared statement and the result set
            result.close();
            queryProductByParameter.close();

            //Reinitializing the prepared statement and the result set with null values
            queryProductByParameter = null;
            result = null;

            //Checking product's name
            queryProductByParameter = Datasource.getInstance().getConnection().prepareStatement(QUERY_PRODUCT_BY_NAME_AND_BRAND);
            queryProductByParameter.setString(1, name);
            queryProductByParameter.setInt(2, brandID);
            result = queryProductByParameter.executeQuery();

            //Action when product's name already exists
            if (result.next()) {
                //Return the ID of the product that already exists
                int productID = result.getInt(1);
                queryProductByParameter.close();
                System.out.println("Product with name \"" + name + "\" and brand \"" + brandID
                        +  "\" already exists with ID: " + productID);
                return productID;
            }
            //If product's name does not exist
            else {
                //Closing prepared statement that we won't use anymore
                queryProductByParameter.close();

                //Insert the product
                insertIntoProduct = Datasource.getInstance().getConnection().prepareStatement(INSERT_PRODUCT, Statement.RETURN_GENERATED_KEYS);
                insertIntoProduct.setInt(1, id);
                insertIntoProduct.setString(2, name);
                insertIntoProduct.setInt(3, brandID);
                insertIntoProduct.setFloat(4, sellingPrice);
                insertIntoProduct.setInt(5, quantityMinimum);
                insertIntoProduct.setInt(6, quantityInStock);


                int affectedRows = insertIntoProduct.executeUpdate();

                //Exception when not being able to insert
                if (affectedRows != 1) {
                    insertIntoProduct.close();
                    throw new SQLException("Couldn't insert product!");
                }

                //Returning the ID of the inserted product
                /*
                It should be the same as the inserted ID above but I'm doing this procedure to check if it has been
                inserted correctly
                 */
                ResultSet generatedKeys = insertIntoProduct.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int generatedKey = generatedKeys.getInt(1);
                    insertIntoProduct.close();
                    System.out.println("New product created with ID: " + generatedKey);
                    return generatedKey;
                } else {
                    insertIntoProduct.close();
                    throw new SQLException("Couldn't get ID for the product");
                }
            }
        }
    }

    /**
     * Method to update a product using
     * @param productID of the product that we want to update
     * @param newName of the product
     * @param newBrandID of the product
     * @param newSellingPrice of the product
     * @param newQuantityMinimum of the product
     * @param newQuantityInStock of the product
     * @return true if succeeded, false if failed
     */
    @Override
    public boolean updateProduct(int productID, String newName, int newBrandID, float newSellingPrice, int newQuantityMinimum, int newQuantityInStock) {

        try {
            //Set fields in the prepared statement
            updateProduct = Datasource.getInstance().getConnection().prepareStatement(UPDATE_PRODUCT);
            updateProduct.setString(1, newName);
            updateProduct.setInt(2, newBrandID);
            updateProduct.setFloat(3, newSellingPrice);
            updateProduct.setInt(4, newQuantityMinimum);
            updateProduct.setInt(5, newQuantityInStock);
            updateProduct.setInt(6, productID);

            //Checking the update result
            int affectedRecords = updateProduct.executeUpdate();

            //Returning true if update has been successful
            return (affectedRecords == 1);

        } catch (SQLException e){
            System.out.println("Update failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Method to delete a product
     * @param productID of the product that we want to delete
     * @return true if succeeded, false if failed
     */
    @Override
    public boolean deleteProduct(int productID) {

        try {
            //Set fields in the prepared statement
            deleteProduct = Datasource.getInstance().getConnection().prepareStatement(DELETE_PRODUCT);
            deleteProduct.setInt(1, productID);

            //Checking the delete result
            int affectedRecords = deleteProduct.executeUpdate();

            //Returning true if delete has been successful
            return (affectedRecords == 1);

        } catch (SQLException e){
            System.out.println("Delete failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Method to buy a product from a supplier with a specified quantity
     * @param productID of the product
     * @param boughtQuantity quantity getting in of the stock
     * @return true if succeeded, false if failed
     */
    @Override
    public boolean buyProduct(int productID, int boughtQuantity){

        //Checking if bought quantity is < 1
        if (boughtQuantity < 1){
            return false;
        }

        try {
            //Set fields in the prepared statement
            updateProductQuantity = Datasource.getInstance().getConnection().prepareStatement(UPDATE_PRODUCT_QUANTITY);
            updateProductQuantity.setInt(1, boughtQuantity);
            updateProductQuantity.setInt(2, productID);

            //Checking the update result
            int affectedRecords = updateProductQuantity.executeUpdate();

            //Returning true if update has been successful
            return (affectedRecords == 1);

        } catch (SQLException e){
            System.out.println("Update failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Method to sell a product to a client with a specified quantity
     * @param productID of the product
     * @param soldQuantity quantity getting out of the stock
     * @return true if succeeded, false if failed
     */
    @Override
    public boolean sellProduct(int productID, int soldQuantity){

        //Checking if sold quantity is < 1
        if (soldQuantity < 1){
            return false;
        }

        //Checking the quantity in stock of the product
        if (getProductByID(productID).getQuantityInStock() < soldQuantity){
            System.out.println("Can't sell what you don't have..");
            return false;
        }

        //Reversing sold quantity to a negative integer
        soldQuantity *= -1;

        try {
            //Set fields in the prepared statement
            updateProductQuantity = Datasource.getInstance().getConnection().prepareStatement(UPDATE_PRODUCT_QUANTITY);
            updateProductQuantity.setInt(1, soldQuantity);
            updateProductQuantity.setInt(2, productID);

            //Checking the update result
            int affectedRecords = updateProductQuantity.executeUpdate();

            //Returning true if update has been successful
            return (affectedRecords == 1);

        } catch (SQLException e){
            System.out.println("Update failed: " + e.getMessage());
            return false;
        }
    }
}
