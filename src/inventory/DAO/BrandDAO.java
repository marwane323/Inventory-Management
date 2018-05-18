package inventory.DAO;

import inventory.Entity.Brand;

import java.sql.SQLException;
import java.util.List;

public interface BrandDAO {

    /**
     * Method to query list of all brands
     *
     * @return a list of all brands
     */
    public List<Brand> queryBrands();

    /**
     * Method that queries list of Brands' names only
     * @return a list of strings containing Brands' names
     */
    public List<String> getBrandsNames();

    /**
     * Method that returns a Brand instance that matches an ID given
     * @param brandID the ID given to the method to make the search on the database
     * @return the Brand instance that has got the same ID that was given
     */
    public Brand getBrandByID(int brandID);

    /**
     * Method that returns a Brand instance that matches a name given
     * @param brandName the name given to the method to make the search on the database
     * @return the Brand instance that has got the same name that was given
     */
    public Brand getBrandByName(String brandName);

    /**
     * Method to insert brand using
     * @param brand object that we want to insert
     * @return generated ID of the new brand
     * @throws SQLException in case inserting the new brand fails
     */
    public int insertBrand(Brand brand) throws SQLException;

    /**
     * Method to insert brand using
     * @param name of the new brand
     * @return generated ID of the new brand
     * @throws SQLException in case inserting the new brand fails
     */
    public int insertBrand(String name) throws SQLException;

    /**
     * Method to update a brand's name
     * @param brandID of the brand that we want to update
     * @param newBrandName
     * @return true if succeeded, false if failed
     */
    public boolean updateBrandName(int brandID, String newBrandName);

    /**
     * Method to delete a brand
     * @param brandID of the brand that we want to delete
     * @return true if succeeded, false if failed
     */
    public boolean deleteBrand(int brandID);

}
