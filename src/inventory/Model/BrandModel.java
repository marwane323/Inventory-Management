package inventory.Model;

import com.mysql.jdbc.Statement;
import inventory.DAO.BrandDAO;
import inventory.Entity.Brand;
import inventory.Entity.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BrandModel implements BrandDAO {

    //Constants for the brands table
    public static final String TABLE_BRAND = "brand";
    public static final String COLUMN_BRAND_ID = "brandID";
    public static final String COLUMN_BRAND_NAME = "brandName";
    public static final String COLUMN_BRAND_PICTURE = "brandPicture";
    public static final String INDEX_BRAND_ID = "1";
    public static final String INDEX_BRAND_NAME = "2";
    public static final String INDEX_BRAND_PICTURE = "3";

    //Constants for querying brands
    public static final String QUERY_BRANDS =
            "SELECT " + TABLE_BRAND + "." + COLUMN_BRAND_ID + ", " +
                        TABLE_BRAND + "." + COLUMN_BRAND_NAME + ", " +
                        TABLE_BRAND + "." + COLUMN_BRAND_PICTURE +
                    " FROM " + TABLE_BRAND;

    //Constant for querying brand by its id
    public static final String QUERY_BRAND_BY_ID = QUERY_BRANDS +
            " WHERE " + TABLE_BRAND + "." + COLUMN_BRAND_ID + " = ?";

    //Constant for querying brand by its name
    public static final String QUERY_BRAND_BY_NAME = QUERY_BRANDS +
            " WHERE " + TABLE_BRAND + "." + COLUMN_BRAND_NAME + " = ?";

    //Constant for inserting new brand
    public static final String INSERT_BRAND =
            "INSERT INTO " + TABLE_BRAND +
                    " (" + COLUMN_BRAND_NAME + ") VALUES (?)";

    //Constant for updating a brand's name
    public static final String UPDATE_BRAND_NAME =
                    "UPDATE " + TABLE_BRAND +
                    " SET " + TABLE_BRAND + "." + COLUMN_BRAND_NAME + " = ? WHERE " +
                              TABLE_BRAND + "." + COLUMN_BRAND_ID + " = ?";

    //Constant for updating a brand's picture
    public static final String UPDATE_BRAND_PICTURE =
                    "UPDATE " + TABLE_BRAND +
                    " SET " + TABLE_BRAND + "." + COLUMN_BRAND_PICTURE + " = ? WHERE " +
                              TABLE_BRAND + "." + COLUMN_BRAND_ID + " = ?";

    //Constant for deleting a brand
    public static final String DELETE_BRAND =
                    "DELETE FROM " + TABLE_BRAND + " WHERE " + TABLE_BRAND + "." + COLUMN_BRAND_ID + " = ?";

    /*----------------------------------------------------------------------------------------------------------*/

    PreparedStatement queryBrandByParameter;
    PreparedStatement insertIntoBrand;
    PreparedStatement updateBrandName;
    PreparedStatement deleteBrand;

    /*----------------------------------------------------------------------------------------------------------*/

    //Creating a private static instance of our class to make it a Singleton class
    private static BrandModel instance = new BrandModel();

    //Making the constructor private to make it used only by this class to make it a Singleton class
    private BrandModel() {
    }

    //A getter of the one and only instance of this class (Singleton class)
    public static BrandModel getInstance() {
        return instance;
    }

    /*----------------------------------------------------------------------------------------------------------*/

    /**
     * Method to query list of all brands
     *
     * @return a list of all brands
     */
    @Override
    public List<Brand> queryBrands(){

        try (PreparedStatement statement = Datasource.getInstance().getConnection().prepareStatement(QUERY_BRANDS);
             ResultSet results = statement.executeQuery()) {

            //Action when getting a null result set
            if (!results.next()){
                System.out.println("No brand was found!");
                return Collections.emptyList();
            }

            //Positioning the ResultSet's cursor back to where it was before the first row
            results.beforeFirst();

            //Creating the list that is going to be returned
            List<Brand> brands = new ArrayList<>();

            //Inserting results in the list
            while (results.next()){
                Brand brand = new Brand();
                brand.setBrandID(results.getInt(COLUMN_BRAND_ID));
                brand.setBrandName(results.getString(COLUMN_BRAND_NAME));
                brands.add(brand);
            }

            return brands;

        } catch (SQLException e){
            System.out.println("Query failed: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Method that queries list of Brands' names only
     * @return a list of strings containing Brands' names
     */
    @Override
    public List<String> getBrandsNames() {

        //Querying all the brands from the database
        List<Brand> brands = queryBrands();

        //Checking if the list is empty
        if (brands == null){
            System.out.println("No Brands");
            return Collections.emptyList();
        }

        //Creating the list of the products' Names
        List<String> brandsNames = new ArrayList<>();

        //Adding products' names to the list
        for (Brand brand : brands){
            brandsNames.add(brand.getBrandName());
        }

        return brandsNames;
    }

    /**
     * Method that returns a Brand instance that matches an ID given
     * @param brandID the ID given to the method to make the search on the database
     * @return the Brand instance that has got the same ID that was given
     */
    @Override
    public Brand getBrandByID(int brandID) {

        //Checking if connection is null
        if (Datasource.getInstance().isConnectionNull()){
            return null;
        }

        try {
            queryBrandByParameter = Datasource.getInstance().getConnection().prepareStatement(QUERY_BRAND_BY_ID);
            queryBrandByParameter.setInt(1, brandID);
        } catch (SQLException e) {
            System.out.println("Couldn't initialize prepared statement: " + e.getMessage());
            return null;
        }

        try (ResultSet results = queryBrandByParameter.executeQuery()) {

            //Action when getting a null result set
            if (!results.next()){
                System.out.println("No brand was found!");
                return null;
            }

            //Positioning the ResultSet's cursor back to where it was before the first row
            results.beforeFirst();

            //Going to the first and only element in the result set
            results.next();

            //Creating the instance that we want to return
            Brand brand = new Brand();

            //Initializing attributes of that instance
            brand.setBrandID(results.getInt(COLUMN_BRAND_ID));
            brand.setBrandName(results.getString(COLUMN_BRAND_NAME));

            return brand;

        } catch (SQLException e){
            System.out.println("Query failed: " + e.getMessage());
            return null;
        } finally {
            try {
                queryBrandByParameter.close();
            } catch (SQLException e){
                System.out.println("Couldn't close prepared statement: " + e.getMessage());
            }
        }
    }

    /**
     * Method that returns a Brand instance that matches a name given
     * @param brandName the name given to the method to make the search on the database
     * @return the Brand instance that has got the same name that was given
     */
    @Override
    public Brand getBrandByName(String brandName) {
        //Checking if connection is null
        if (Datasource.getInstance().isConnectionNull()){
            return null;
        }

        try {
            queryBrandByParameter = Datasource.getInstance().getConnection().prepareStatement(QUERY_BRAND_BY_NAME);
            queryBrandByParameter.setString(1, brandName);
        } catch (SQLException e) {
            System.out.println("Couldn't initialize prepared statement: " + e.getMessage());
            return null;
        }

        try (ResultSet results = queryBrandByParameter.executeQuery()) {

            //Action when getting a null result set
            if (!results.next()){
                System.out.println("No brand was found!");
                return null;
            }

            //Positioning the ResultSet's cursor back to where it was before the first row
            results.beforeFirst();

            //Going to the first and only element in the result set
            results.next();

            //Creating the instance that we want to return
            Brand brand = new Brand();

            //Initializing attributes of that instance
            brand.setBrandID(results.getInt(COLUMN_BRAND_ID));
            brand.setBrandName(results.getString(COLUMN_BRAND_NAME));

            return brand;

        } catch (SQLException e){
            System.out.println("Query failed: " + e.getMessage());
            return null;
        } finally {
            try {
                queryBrandByParameter.close();
            } catch (SQLException e){
                System.out.println("Couldn't close prepared statement: " + e.getMessage());
            }
        }
    }

    /**
     * Method to insert brand using
     * @param brand object that we want to insert
     * @return generated ID of the new brand
     * @throws SQLException in case inserting the new brand fails
     */
    @Override
    public int insertBrand(Brand brand) throws SQLException {

        //Check if brand already exists
        queryBrandByParameter = Datasource.getInstance().getConnection().prepareStatement(QUERY_BRAND_BY_NAME);
        queryBrandByParameter.setString(1, brand.getBrandName());
        ResultSet result = queryBrandByParameter.executeQuery();

        //Action when brand's name already exists
        if (result.next()){
            //Return the ID of the brand that already exists
            int brandID = result.getInt(1);
            queryBrandByParameter.close();
            System.out.println("Brand with name \"" + brand.getBrandName() + "\" already exists with ID: " + brandID);
            return brandID;
        }
        //If brand's name does not exist
        else {
            //Closing prepared statement that we won't use anymore
            queryBrandByParameter.close();

            //Insert the brand
            insertIntoBrand = Datasource.getInstance().getConnection().prepareStatement(INSERT_BRAND, Statement.RETURN_GENERATED_KEYS);
            insertIntoBrand.setString(1, brand.getBrandName());


            int affectedRows = insertIntoBrand.executeUpdate();

            //Exception when not being able to insert
            if (affectedRows != 1) {
                insertIntoBrand.close();
                throw new SQLException("Couldn't insert brand!");
            }

            //Getting the generated key of the inserted brand
            ResultSet generatedKeys = insertIntoBrand.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedKey = generatedKeys.getInt(1);
                insertIntoBrand.close();
                System.out.println("New brand created with ID: " + generatedKey);
                return generatedKey;
            } else {
                insertIntoBrand.close();
                throw new SQLException("Couldn't get ID for the brand");
            }
        }
    }

    /**
     * Method to insert brand using
     * @param name of the new brand
     * @return generated ID of the new brand
     * @throws SQLException in case inserting the new brand fails
     */
    @Override
    public int insertBrand(String name) throws SQLException {

        //Check if brand already exists
        queryBrandByParameter = Datasource.getInstance().getConnection().prepareStatement(QUERY_BRAND_BY_NAME);
        queryBrandByParameter.setString(1, name);
        ResultSet result = queryBrandByParameter.executeQuery();

        //Action when brand's name already exists
        if (result.next()){
            //Return the ID of the brand that already exists
            int brandID = result.getInt(1);
            queryBrandByParameter.close();
            System.out.println("Brand with name \"" + name + "\" already exists with ID: " + brandID);
            return brandID;
        }
        //If brand's name does not exist
        else {
            //Closing prepared statement that we won't use anymore
            queryBrandByParameter.close();

            //Insert the brand
            insertIntoBrand = Datasource.getInstance().getConnection().prepareStatement(INSERT_BRAND, Statement.RETURN_GENERATED_KEYS);
            insertIntoBrand.setString(1, name);


            int affectedRows = insertIntoBrand.executeUpdate();

            //Exception when not being able to insert
            if (affectedRows != 1) {
                insertIntoBrand.close();
                throw new SQLException("Couldn't insert brand!");
            }

            //Getting the generated key of the inserted brand
            ResultSet generatedKeys = insertIntoBrand.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedKey = generatedKeys.getInt(1);
                insertIntoBrand.close();
                System.out.println("New brand created with ID: " + generatedKey);
                return generatedKey;
            } else {
                insertIntoBrand.close();
                throw new SQLException("Couldn't get ID for the brand");
            }
        }
    }

    /**
     * Method to update a brand's name
     * @param brandID of the brand that we want to update
     * @param newBrandName
     * @return true if succeeded, false if failed
     */
    @Override
    public boolean updateBrandName(int brandID, String newBrandName) {

        try {
            //Set fields in the prepared statement
            updateBrandName = Datasource.getInstance().getConnection().prepareStatement(UPDATE_BRAND_NAME);
            updateBrandName.setString(1, newBrandName);
            updateBrandName.setInt(2, brandID);

            //Checking the update result
            int affectedRecords = updateBrandName.executeUpdate();

            //Returning true if update has been successful
            return (affectedRecords == 1);

        } catch (SQLException e){
            System.out.println("Update failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Method to delete a brand
     * @param brandID of the brand that we want to delete
     * @return true if succeeded, false if failed
     */
    @Override
    public boolean deleteBrand(int brandID) {

        try {
            //Set fields in the prepared statement
            deleteBrand = Datasource.getInstance().getConnection().prepareStatement(DELETE_BRAND);
            deleteBrand.setInt(1, brandID);

            //Checking the delete result
            int affectedRecords = deleteBrand.executeUpdate();

            //Returning true if delete has been successful
            return (affectedRecords == 1);

            } catch (SQLException e){
            System.out.println("Delete failed: " + e.getMessage());
            return false;
        }
    }
}
