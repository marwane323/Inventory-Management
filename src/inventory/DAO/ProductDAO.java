package inventory.DAO;

import inventory.Entity.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductDAO {

    /**
     * Method to query list of all products
     *
     * @return a list of all products
     */
    public List<Product> queryProducts();

    /**
     * Method that queries list of products' names only
     * @return a list of strings containing products' names
     */
    public List<String> getProductsNames();

    /**
     * Method that returns a Product instance that matches an ID given
     * @param productID the ID given to the method to make the search on the database
     * @return the Product instance that has got the same ID that was given
     */
    public Product getProductByID(int productID);

    /**
     * Method that returns a Product instance that matches a name given
     * @param productName the name given to the method to make the search on the database
     * @return the Product instance that has got the same name that was given
     */
    public Product getProductByName(String productName);

    /**
     * Method to insert product using
     * @param product object that we want to insert
     * @return ID of the new product
     * @throws SQLException in case  inserting the new product fails
     */
    public int insertProduct(Product product) throws SQLException;

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
    public int insertProduct(int id, String name, int brandID, float sellingPrice, int quantityMinimum,
                             int quantityInStock) throws SQLException;

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
    public boolean updateProduct(int productID, String newName, int newBrandID, float newSellingPrice,
                                 int newQuantityMinimum, int newQuantityInStock);

    /**
     * Method to delete a product
     * @param productID of the product that we want to delete
     * @return true if succeeded, false if failed
     */
    public boolean deleteProduct(int productID);

    /**
     * Method to buy a product from a supplier with a specified quantity
     * @param productID of the product
     * @param boughtQuantity quantity getting in of the stock
     * @return true if succeeded, false if failed
     */
    public boolean buyProduct(int productID, int boughtQuantity);

    /**
     * Method to sell a product to a client with a specified quantity
     * @param productID of the product
     * @param soldQuantity quantity getting out of the stock
     * @return true if succeeded, false if failed
     */
    public boolean sellProduct(int productID, int soldQuantity);

}
