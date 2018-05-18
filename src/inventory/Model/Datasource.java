package inventory.Model;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import inventory.Entity.Brand;
import inventory.Entity.Employee;
import inventory.Entity.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Datasource {

    //Constants for database connection
    public static final String SERVER_NAME = "localhost";
    public static final String DATABASE_USERNAME = "root";
    public static final String DATABASE_PASSWORD = "azer1234";
    public static final String DB_NAME = "pfe";

    //Constant for database creation
    public static final String CREATE_DATABASE = "CREATE DATABASE IF NOT EXISTS " + DB_NAME
            + " DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci";

    /* --------------------------------- Constants for database initializing ---------------------------------- */

    //Constant for Brand Table creation
    public static final String CREATE_TABLE_BRAND = "CREATE TABLE IF NOT EXISTS " + DB_NAME + "." + BrandModel.TABLE_BRAND
            + " ( " + BrandModel.COLUMN_BRAND_ID + " int(11) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
                    + BrandModel.COLUMN_BRAND_NAME + " varchar(40) NOT NULL, "
                    + BrandModel.COLUMN_BRAND_PICTURE + " longblob, " +
            "UNIQUE INDEX " + BrandModel.TABLE_BRAND + "_" + BrandModel.COLUMN_BRAND_ID + "_uindex (" + BrandModel.COLUMN_BRAND_ID +
            "), " +
            "UNIQUE INDEX " + BrandModel.TABLE_BRAND + "_" + BrandModel.COLUMN_BRAND_NAME + "_uindex (" + BrandModel.COLUMN_BRAND_NAME +
            "))";
    //Constant for Brand Table creation (Constraint)
//    public static final String CREATE_TABLE_BRAND_CONSTRAINT_1 = "CREATE UNIQUE INDEX "
//            + BrandModel.TABLE_BRAND + "_" + BrandModel.COLUMN_BRAND_ID + "_uindex ON " + DB_NAME + "." + BrandModel.TABLE_BRAND
//            + " (" + BrandModel.COLUMN_BRAND_ID + ")";
    //Constant for Brand Table creation (Constraint)
//    public static final String CREATE_TABLE_BRAND_CONSTRAINT_2 = "CREATE UNIQUE INDEX "
//            + BrandModel.TABLE_BRAND + "_" + BrandModel.COLUMN_BRAND_NAME + "_uindex ON " + DB_NAME + "." + BrandModel.TABLE_BRAND
//            + " (" + BrandModel.COLUMN_BRAND_NAME + ")";

    //Constant for Employee Table creation
    public static final String CREATE_TABLE_EMPLOYEE = "CREATE TABLE IF NOT EXISTS " + DB_NAME + "." + EmployeeModel.TABLE_EMPLOYEE
            + " ( " + EmployeeModel.COLUMN_EMPLOYEE_ID + " int(11) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
                    + EmployeeModel.COLUMN_EMPLOYEE_NAME + " varchar(40) NOT NULL, "
                    + EmployeeModel.COLUMN_EMPLOYEE_PHONE + " varchar(14) NOT NULL, "
                    + EmployeeModel.COLUMN_EMPLOYEE_PICTURE + " longblob, "
                    + EmployeeModel.COLUMN_EMPLOYEE_USERNAME + " varchar(40) NOT NULL, "
                    + EmployeeModel.COLUMN_EMPLOYEE_PASSWORD + " varchar(40) NOT NULL, " +
            "UNIQUE INDEX " + EmployeeModel.TABLE_EMPLOYEE + "_" + EmployeeModel.COLUMN_EMPLOYEE_ID + "_uindex (" + EmployeeModel.COLUMN_EMPLOYEE_ID +
            "), " +
            "UNIQUE INDEX " + EmployeeModel.TABLE_EMPLOYEE + "_" + EmployeeModel.COLUMN_EMPLOYEE_PHONE + "_uindex (" + EmployeeModel.COLUMN_EMPLOYEE_PHONE +
            "), " +
            "UNIQUE INDEX " + EmployeeModel.TABLE_EMPLOYEE + "_" + EmployeeModel.COLUMN_EMPLOYEE_USERNAME + "_uindex (" + EmployeeModel.COLUMN_EMPLOYEE_USERNAME +
            "))";
    //Constant for Employee Table creation (Constraint)
//    public static final String CREATE_TABLE_EMPLOYEE_CONSTRAINT_1 = "CREATE UNIQUE INDEX "
//            + EmployeeModel.TABLE_EMPLOYEE + "_" + EmployeeModel.COLUMN_EMPLOYEE_ID + "_uindex ON "
//            + DB_NAME + "." + EmployeeModel.TABLE_EMPLOYEE
//            + " (" + EmployeeModel.COLUMN_EMPLOYEE_ID + ")";
    //Constant for Employee Table creation (Constraint)
//    public static final String CREATE_TABLE_EMPLOYEE_CONSTRAINT_2 = "CREATE UNIQUE INDEX "
//            + EmployeeModel.TABLE_EMPLOYEE + "_" + EmployeeModel.COLUMN_EMPLOYEE_PHONE + "_uindex ON "
//            + DB_NAME + "." + EmployeeModel.TABLE_EMPLOYEE
//            + " (" + EmployeeModel.COLUMN_EMPLOYEE_PHONE + ")";
    //Constant for Employee Table creation (Constraint)
//    public static final String CREATE_TABLE_EMPLOYEE_CONSTRAINT_3 = "CREATE UNIQUE INDEX "
//            + EmployeeModel.TABLE_EMPLOYEE + "_" + EmployeeModel.COLUMN_EMPLOYEE_USERNAME + "_uindex ON "
//            + DB_NAME + "." + EmployeeModel.TABLE_EMPLOYEE
//            + " (" + EmployeeModel.COLUMN_EMPLOYEE_USERNAME + ")";

    //Constant for Supplier Table creation
    public static final String CREATE_TABLE_SUPPLIER = "CREATE TABLE IF NOT EXISTS " + DB_NAME + "." + SupplierModel.TABLE_SUPPLIER
            + " ( " + SupplierModel.COLUMN_SUPPLIER_ID + " int(11) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
                    + SupplierModel.COLUMN_SUPPLIER_NAME + " varchar(40) NOT NULL, "
                    + SupplierModel.COLUMN_SUPPLIER_ADDRESS + " varchar(100) NOT NULL, "
                    + SupplierModel.COLUMN_SUPPLIER_PHONE + " varchar(14) NOT NULL, " +
            "UNIQUE INDEX " + SupplierModel.TABLE_SUPPLIER + "_" + SupplierModel.COLUMN_SUPPLIER_ID + "_uindex (" + SupplierModel.COLUMN_SUPPLIER_ID +
            "), " +
            "UNIQUE INDEX " + SupplierModel.TABLE_SUPPLIER + "_" + SupplierModel.COLUMN_SUPPLIER_NAME + "_uindex (" + SupplierModel.COLUMN_SUPPLIER_NAME +
            "))";
    //Constant for Supplier Table creation (Constraint)
//    public static final String CREATE_TABLE_SUPPLIER_CONSTRAINT_1 = "CREATE UNIQUE INDEX "
//            + SupplierModel.TABLE_SUPPLIER + "_" + SupplierModel.COLUMN_SUPPLIER_ID + "_uindex ON "
//            + DB_NAME + "." + SupplierModel.TABLE_SUPPLIER
//            + " (" + SupplierModel.COLUMN_SUPPLIER_ID + ")";
    //Constant for Supplier Table creation (Constraint)
//    public static final String CREATE_TABLE_SUPPLIER_CONSTRAINT_2 = "CREATE UNIQUE INDEX "
//            + SupplierModel.TABLE_SUPPLIER + "_" + SupplierModel.COLUMN_SUPPLIER_NAME + "_uindex ON "
//            + DB_NAME + "." + SupplierModel.TABLE_SUPPLIER
//            + " (" + SupplierModel.COLUMN_SUPPLIER_NAME + ")";

    //Constant for Invoice Client Table creation
    public static final String CREATE_TABLE_INVOICE_CLIENT = "CREATE TABLE IF NOT EXISTS " + DB_NAME + "." + InvoiceClientModel.TABLE_INVOICE_CLIENT
            + " ( " + InvoiceClientModel.COLUMN_INVOICE_ID + " int(11) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
                    + InvoiceClientModel.COLUMN_DATE + " datetime NOT NULL, " +
            "UNIQUE INDEX " + InvoiceClientModel.TABLE_INVOICE_CLIENT + "_" + InvoiceClientModel.COLUMN_INVOICE_ID + "_uindex (" + InvoiceClientModel.COLUMN_INVOICE_ID +
            "))";
    //Constant for Invoice Client Table creation (Constraint)
//    public static final String CREATE_TABLE_INVOICE_CLIENT_CONSTRAINT_1 = "CREATE UNIQUE INDEX "
//            + InvoiceClientModel.TABLE_INVOICE_CLIENT + "_" + InvoiceClientModel.COLUMN_INVOICE_ID + "_uindex ON "
//            + DB_NAME + "." + InvoiceClientModel.TABLE_INVOICE_CLIENT
//            + " (" + InvoiceClientModel.COLUMN_INVOICE_ID + ")";

    //Constant for Invoice Supplier Table creation
    public static final String CREATE_TABLE_INVOICE_SUPPLIER = "CREATE TABLE IF NOT EXISTS " + DB_NAME + "." + InvoiceSupplierModel.TABLE_INVOICE_SUPPLIER
            + " ( " + InvoiceSupplierModel.COLUMN_INVOICE_ID + " int(11) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
                    + InvoiceSupplierModel.COLUMN_DATE + " datetime NOT NULL, " +
            "UNIQUE INDEX " + InvoiceSupplierModel.TABLE_INVOICE_SUPPLIER + "_" + InvoiceSupplierModel.COLUMN_INVOICE_ID + "_uindex (" + InvoiceSupplierModel.COLUMN_INVOICE_ID +
            "))";
    //Constant for Invoice Supplier Table creation (Constraint)
//    public static final String CREATE_TABLE_INVOICE_SUPPLIER_CONSTRAINT_1 = "CREATE UNIQUE INDEX "
//            + InvoiceSupplierModel.TABLE_INVOICE_SUPPLIER + "_" + InvoiceSupplierModel.COLUMN_INVOICE_ID + "_uindex ON "
//            + DB_NAME + "." + InvoiceSupplierModel.TABLE_INVOICE_SUPPLIER
//            + " (" + InvoiceSupplierModel.COLUMN_INVOICE_ID + ")";

    //Constant for Product Table creation
    public static final String CREATE_TABLE_PRODUCT = "CREATE TABLE IF NOT EXISTS " + DB_NAME + "." + ProductModel.TABLE_PRODUCT
            + " ( " + ProductModel.COLUMN_PRODUCT_ID + " int(11) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
                    + ProductModel.COLUMN_PRODUCT_NAME + " varchar(60) NOT NULL, "
                    + ProductModel.COLUMN_PRODUCT_PICTURE + " longblob, "
                    + ProductModel.COLUMN_PRODUCT_BRAND_ID + " int(11) NOT NULL, "
                    + ProductModel.COLUMN_PRODUCT_SELLING_PRICE + " float NOT NULL, "
                    + ProductModel.COLUMN_PRODUCT_QUANTITY_MINIMUM + " int(11) DEFAULT '0' NOT NULL, "
                    + ProductModel.COLUMN_PRODUCT_QUANTITY_IN_STOCK + " int(11) NOT NULL, "
            + "CONSTRAINT " + ProductModel.TABLE_PRODUCT + "_" + BrandModel.TABLE_BRAND + "_" + ProductModel.COLUMN_PRODUCT_BRAND_ID
            + "_fk FOREIGN KEY (" + ProductModel.COLUMN_PRODUCT_BRAND_ID + ") REFERENCES " + BrandModel.TABLE_BRAND + " ("
                    + BrandModel.COLUMN_BRAND_ID + "), "
            + "UNIQUE INDEX " + ProductModel.TABLE_PRODUCT + "_" + ProductModel.COLUMN_PRODUCT_ID + "_uindex (" + ProductModel.COLUMN_PRODUCT_ID
            + "), "
            + "INDEX " + ProductModel.TABLE_PRODUCT + "_" + BrandModel.TABLE_BRAND + "_" + ProductModel.COLUMN_PRODUCT_BRAND_ID + "_fk (" + ProductModel.COLUMN_PRODUCT_BRAND_ID
            + "))";
    //Constant for Product Table creation (Constraint)
//    public static final String CREATE_TABLE_PRODUCT_CONSTRAINT_1 = "CREATE UNIQUE INDEX "
//            + ProductModel.TABLE_PRODUCT + "_" + ProductModel.COLUMN_PRODUCT_ID + "_uindex ON "
//            + DB_NAME + "." + ProductModel.TABLE_PRODUCT
//            + " (" + ProductModel.COLUMN_PRODUCT_ID + ")";
    //Constant for Product Table creation (Constraint)
//    public static final String CREATE_TABLE_PRODUCT_CONSTRAINT_2 = "CREATE INDEX "
//            + ProductModel.TABLE_PRODUCT + "_" + BrandModel.TABLE_BRAND + "_" + ProductModel.COLUMN_PRODUCT_BRAND_ID
//            + "_fk ON " + DB_NAME + "." + ProductModel.TABLE_PRODUCT
//            + " (" + ProductModel.COLUMN_PRODUCT_BRAND_ID + ")";

    //Constant for Purchases Table creation
    public static final String CREATE_TABLE_PURCHASES = "CREATE TABLE IF NOT EXISTS " + DB_NAME + "." + PurchaseModel.TABLE_PURCHASE
            + " ( " + PurchaseModel.COLUMN_PURCHASE_ID + " int(11) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
                    + PurchaseModel.COLUMN_INVOICE_ID + " int(11) NOT NULL, "
                    + PurchaseModel.COLUMN_SUPPLIER_ID + " int(11) NOT NULL, "
                    + PurchaseModel.COLUMN_PRODUCT_ID + " int(11) NOT NULL, "
                    + PurchaseModel.COLUMN_PRODUCT_COST_PRICE + " float NOT NULL, "
                    + PurchaseModel.COLUMN_QUANTITY + " int(11) NOT NULL, "
                    + PurchaseModel.COLUMN_TOTAL_COST + " float NOT NULL, "
            + "CONSTRAINT " + PurchaseModel.TABLE_PURCHASE + "_" + InvoiceSupplierModel.TABLE_INVOICE_SUPPLIER + "_" + PurchaseModel.COLUMN_INVOICE_ID
            + "_fk FOREIGN KEY (" + PurchaseModel.COLUMN_INVOICE_ID + ") REFERENCES " + InvoiceSupplierModel.TABLE_INVOICE_SUPPLIER + " ("
                    + InvoiceSupplierModel.COLUMN_INVOICE_ID + "), "
            + "CONSTRAINT " + PurchaseModel.TABLE_PURCHASE + "_" + SupplierModel.TABLE_SUPPLIER + "_" + PurchaseModel.COLUMN_SUPPLIER_ID
            + "_fk FOREIGN KEY (" + PurchaseModel.COLUMN_SUPPLIER_ID + ") REFERENCES " + SupplierModel.TABLE_SUPPLIER + " ("
            + SupplierModel.COLUMN_SUPPLIER_ID + "), "
            + "CONSTRAINT " + PurchaseModel.TABLE_PURCHASE + "_" + ProductModel.TABLE_PRODUCT + "_" + PurchaseModel.COLUMN_PRODUCT_ID
            + "_fk FOREIGN KEY (" + PurchaseModel.COLUMN_PRODUCT_ID + ") REFERENCES " + ProductModel.TABLE_PRODUCT + " ("
            + ProductModel.COLUMN_PRODUCT_ID + "), "
            + "UNIQUE INDEX " + PurchaseModel.TABLE_PURCHASE + "_" + PurchaseModel.COLUMN_PURCHASE_ID + "_uindex (" + PurchaseModel.COLUMN_PURCHASE_ID
            + "), "
            + "INDEX " + PurchaseModel.TABLE_PURCHASE + "_" + InvoiceSupplierModel.TABLE_INVOICE_SUPPLIER + "_" + PurchaseModel.COLUMN_INVOICE_ID + "_fk (" + PurchaseModel.COLUMN_INVOICE_ID
            + "), "
            + "INDEX " + PurchaseModel.TABLE_PURCHASE + "_" + SupplierModel.TABLE_SUPPLIER + "_" + PurchaseModel.COLUMN_SUPPLIER_ID + "_fk (" + PurchaseModel.COLUMN_SUPPLIER_ID
            + "), "
            + "INDEX " + PurchaseModel.TABLE_PURCHASE + "_" + ProductModel.TABLE_PRODUCT + "_" + PurchaseModel.COLUMN_PRODUCT_ID + "_fk (" + PurchaseModel.COLUMN_PRODUCT_ID
            + "))";
    //Constant for Purchases Table creation (Constraint)
//    public static final String CREATE_TABLE_PURCHASES_CONSTRAINT_1 = "CREATE UNIQUE INDEX "
//            + PurchaseModel.TABLE_PURCHASE + "_" + PurchaseModel.COLUMN_PURCHASE_ID + "_uindex ON "
//            + DB_NAME + "." + PurchaseModel.TABLE_PURCHASE
//            + " (" + PurchaseModel.COLUMN_PURCHASE_ID + ")";
    //Constant for Purchases Table creation (Constraint)
//    public static final String CREATE_TABLE_PURCHASES_CONSTRAINT_2 = "CREATE INDEX "
//            + PurchaseModel.TABLE_PURCHASE + "_" + InvoiceSupplierModel.TABLE_INVOICE_SUPPLIER + "_" + PurchaseModel.COLUMN_INVOICE_ID
//            + "_fk ON " + DB_NAME + "." + PurchaseModel.TABLE_PURCHASE
//            + " (" + PurchaseModel.COLUMN_INVOICE_ID + ")";
    //Constant for Purchases Table creation (Constraint)
//    public static final String CREATE_TABLE_PURCHASES_CONSTRAINT_3 = "CREATE INDEX "
//            + PurchaseModel.TABLE_PURCHASE + "_" + SupplierModel.TABLE_SUPPLIER + "_" + PurchaseModel.COLUMN_SUPPLIER_ID
//            + "_fk ON " + DB_NAME + "." + PurchaseModel.TABLE_PURCHASE
//            + " (" + PurchaseModel.COLUMN_SUPPLIER_ID + ")";
    //Constant for Purchases Table creation (Constraint)
//    public static final String CREATE_TABLE_PURCHASES_CONSTRAINT_4 = "CREATE INDEX "
//            + PurchaseModel.TABLE_PURCHASE + "_" + ProductModel.TABLE_PRODUCT + "_" + PurchaseModel.COLUMN_PRODUCT_ID
//            + "_fk ON " + DB_NAME + "." + PurchaseModel.TABLE_PURCHASE
//            + " (" + PurchaseModel.COLUMN_PRODUCT_ID + ")";

    //Constant for Sales Table creation
    public static final String CREATE_TABLE_SALES = "CREATE TABLE IF NOT EXISTS " + DB_NAME + "." + SaleModel.TABLE_SALE
            + " ( " + SaleModel.COLUMN_SALE_ID + " int(11) PRIMARY KEY NOT NULL AUTO_INCREMENT, "
                    + SaleModel.COLUMN_INVOICE_ID + " int(11) NOT NULL, "
                    + SaleModel.COLUMN_PRODUCT_ID + " int(11) NOT NULL, "
                    + SaleModel.COLUMN_QUANTITY + " int(11) NOT NULL, "
                    + SaleModel.COLUMN_TOTAL_COST + " float NOT NULL, "
            + "CONSTRAINT " + SaleModel.TABLE_SALE + "_" + InvoiceClientModel.TABLE_INVOICE_CLIENT + "_" + SaleModel.COLUMN_INVOICE_ID
            + "_fk FOREIGN KEY (" + SaleModel.COLUMN_INVOICE_ID + ") REFERENCES " + InvoiceClientModel.TABLE_INVOICE_CLIENT + " ("
            + InvoiceClientModel.COLUMN_INVOICE_ID + "),"
            + "CONSTRAINT " + SaleModel.TABLE_SALE + "_" + ProductModel.TABLE_PRODUCT + "_" + SaleModel.COLUMN_PRODUCT_ID
            + "_fk FOREIGN KEY (" + SaleModel.COLUMN_PRODUCT_ID + ") REFERENCES " + ProductModel.TABLE_PRODUCT + " ("
            + ProductModel.COLUMN_PRODUCT_ID + "), "
            + "UNIQUE INDEX " + SaleModel.TABLE_SALE + "_" + SaleModel.COLUMN_SALE_ID + "_uindex (" + SaleModel.COLUMN_SALE_ID
            + "), "
            + "INDEX " + SaleModel.TABLE_SALE + "_" + InvoiceClientModel.TABLE_INVOICE_CLIENT + "_" + SaleModel.COLUMN_INVOICE_ID + "_fk (" + SaleModel.COLUMN_INVOICE_ID
            + "), "
            + "INDEX " + SaleModel.TABLE_SALE + "_" + ProductModel.TABLE_PRODUCT + "_" + SaleModel.COLUMN_PRODUCT_ID + "_fk (" + SaleModel.COLUMN_PRODUCT_ID
            + "))";
    //Constant for Sales Table creation (Constraint)
//    public static final String CREATE_TABLE_SALES_CONSTRAINT_1 = "CREATE UNIQUE INDEX "
//            + SaleModel.TABLE_SALE + "_" + SaleModel.COLUMN_SALE_ID + "_uindex ON "
//            + DB_NAME + "." + SaleModel.TABLE_SALE
//            + " (" + SaleModel.COLUMN_SALE_ID + ")";
    //Constant for Sales Table creation (Constraint)
//    public static final String CREATE_TABLE_SALES_CONSTRAINT_2 = "CREATE INDEX "
//            + SaleModel.TABLE_SALE + "_" + InvoiceClientModel.TABLE_INVOICE_CLIENT + "_" + SaleModel.COLUMN_INVOICE_ID
//            + "_fk ON " + DB_NAME + "." + SaleModel.TABLE_SALE
//            + " (" + SaleModel.COLUMN_INVOICE_ID + ")";
    //Constant for Sales Table creation (Constraint)
//    public static final String CREATE_TABLE_SALES_CONSTRAINT_3 = "CREATE INDEX "
//            + SaleModel.TABLE_SALE + "_" + ProductModel.TABLE_PRODUCT + "_" + SaleModel.COLUMN_PRODUCT_ID
//            + "_fk ON " + DB_NAME + "." + SaleModel.TABLE_SALE
//            + " (" + SaleModel.COLUMN_PRODUCT_ID + ")";

    //Constant for Product_Info View creation
    public static final String CREATE_VIEW_PRODUCT_INFO = "CREATE OR REPLACE VIEW " + ProductModel.VIEW_PRODUCT_INFO + " AS "
            + "SELECT " + ProductModel.TABLE_PRODUCT + "." + ProductModel.COLUMN_PRODUCT_ID + " AS " + ProductModel.COLUMN_PRODUCT_INFO_ID + ", "
                        + BrandModel.TABLE_BRAND + "." + BrandModel.COLUMN_BRAND_NAME + " AS " + ProductModel.COLUMN_PRODUCT_INFO_BRAND_NAME +  ", "
                        + ProductModel.TABLE_PRODUCT + "." + ProductModel.COLUMN_PRODUCT_NAME + " AS " + ProductModel.COLUMN_PRODUCT_INFO_NAME +  ", "
                        + ProductModel.TABLE_PRODUCT + "." + ProductModel.COLUMN_PRODUCT_SELLING_PRICE + " AS " + ProductModel.COLUMN_PRODUCT_INFO_SELLING_PRICE +  ", "
                        + ProductModel.TABLE_PRODUCT + "." + ProductModel.COLUMN_PRODUCT_QUANTITY_MINIMUM + " AS " + ProductModel.COLUMN_PRODUCT_INFO_QUANTITY_MINIMUM +  ", "
                        + ProductModel.TABLE_PRODUCT + "." + ProductModel.COLUMN_PRODUCT_QUANTITY_IN_STOCK + " AS " + ProductModel.COLUMN_PRODUCT_INFO_QUANTITY_IN_STOCK +
            " FROM " + DB_NAME + "." + ProductModel.TABLE_PRODUCT + " INNER JOIN " + DB_NAME + "." + BrandModel.TABLE_BRAND +
            " ON " + ProductModel.TABLE_PRODUCT + "." + ProductModel.COLUMN_PRODUCT_BRAND_ID + " = " + BrandModel.TABLE_BRAND + "." + BrandModel.COLUMN_BRAND_ID;

    /*----------------------------------------------------------------------------------------------------------*/

    private Connection connection;
    MysqlDataSource dataSrc = new MysqlDataSource();

    /*----------------------------------------------------------------------------------------------------------*/

    //Creating a private static instance of our class to make it a Singleton class
    private static Datasource instance = new Datasource();

    //Making the constructor private to make it used only by this claas to make it a Singleton class
    private Datasource() {
    }

    //A getter of the one and only instance of this class (Singleton class)
    public static Datasource getInstance() {
        return instance;
    }

    /*----------------------------------------------------------------------------------------------------------*/

    //Getter for connection to use it in other Model classes
    public Connection getConnection() {
        return connection;
    }

    //Method that returns true if connection is null, otherwise it returns false
    public boolean isConnectionNull(){
        if (connection != null){
            return false;
        }
        System.out.println("Connection is closed with the database! Connect to the database first");
        return true;
    }

    /*----------------------------------------------------------------------------------------------------------*/

    //Method to open connection with our database
    public boolean open() {
        try {
            //Setting our database's informations
            dataSrc.setServerName(SERVER_NAME);
            dataSrc.setUser(DATABASE_USERNAME);
            dataSrc.setPassword(DATABASE_PASSWORD);
            dataSrc.setUseSSL(true);
            dataSrc.setVerifyServerCertificate(false);
            //Connecting to the database
            connection = dataSrc.getConnection();
            System.out.println("Connection to the server is established");
            connection.prepareStatement(CREATE_DATABASE).execute();
            //Using the database
            connection.setCatalog(DB_NAME);
            System.out.println("Database Opened!");
            initializeDatabase();
//            printSQLStatements();
            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't connect to database: " + e.getMessage());
            return false;
        }
    }


    //Method to close connection with our database
    public void close() {
        try {
            if (connection != null) {
                //Closing connection with the database
                connection.close();
            }
            System.out.println("Database Closed!");
        } catch (SQLException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
        }
    }

    /**
     * Create all the application tables in the database
     * @return True if succeeded, false otherwise
     */
    public boolean initializeDatabase() {
        boolean isSuccessful = false;
        try {
            //Setting Auto Commit Off (Begin Transaction)
            getConnection().setAutoCommit(false);
            /* ------------------ Executing the creation queries ------------------ */
            //Creating the Brand table
            getConnection().prepareStatement(CREATE_TABLE_BRAND).execute();
            //Creating the Employee table
            getConnection().prepareStatement(CREATE_TABLE_EMPLOYEE).execute();
            //Creating the Supplier table
            getConnection().prepareStatement(CREATE_TABLE_SUPPLIER).execute();
            //Creating the Invoice Client table
            getConnection().prepareStatement(CREATE_TABLE_INVOICE_CLIENT).execute();
            //Creating the Invoice Supplier table
            getConnection().prepareStatement(CREATE_TABLE_INVOICE_SUPPLIER).execute();
            //Creating the Product table
            getConnection().prepareStatement(CREATE_TABLE_PRODUCT).execute();
            //Creating the Purchases table
            getConnection().prepareStatement(CREATE_TABLE_PURCHASES).execute();
            //Creating the Sales table
            getConnection().prepareStatement(CREATE_TABLE_SALES).execute();
            //Creating the Product_Info View
//            getConnection().prepareStatement(CREATE_VIEW_PRODUCT_INFO).execute();

            //Committing (End Transaction, start executing transaction)
            getConnection().commit();
            //Creating the admin user
            EmployeeModel.getInstance().insertEmployee("Admin", "000000000", "admin", "admin");
            isSuccessful = true;
            System.out.println("Initializing succeeded!");
        } catch (SQLException exception1){
            System.out.println("Couldn't Initialize Database: " + exception1.getMessage());
            try {
                isSuccessful = false;
                //Performing Rollback
                System.out.println("Performing rollback");
                getConnection().rollback();
            } catch(SQLException exception2) {
                System.out.println("Oh boy! Things are really bad! " + exception2.getMessage());
            }
        } finally {
            try {
                //Resetting default commit behavior
                System.out.println("Resetting default commit behavior");
                getConnection().setAutoCommit(true);
            } catch(SQLException exception3) {
                System.out.println("Couldn't reset auto-commit! " + exception3.getMessage());
            }
        }
        return isSuccessful;
    }

    /*----------------------------------------------------------------------------------------------------------*/

    public void printSQLStatements(){
        System.out.println("Table Brand: ");
        System.out.println(CREATE_TABLE_BRAND);
        System.out.println("Table Employee: ");
        System.out.println(CREATE_TABLE_EMPLOYEE);
        System.out.println("Table Invoice Client: ");
        System.out.println(CREATE_TABLE_INVOICE_CLIENT);
        System.out.println("Table Invoice Supplier: ");
        System.out.println(CREATE_TABLE_INVOICE_SUPPLIER);
        System.out.println("Table Product: ");
        System.out.println(CREATE_TABLE_PRODUCT);
        System.out.println("Table Purchases: ");
        System.out.println(CREATE_TABLE_PURCHASES);
        System.out.println("Table Sales: ");
        System.out.println(CREATE_TABLE_SALES);
        System.out.println("Table Supplier: ");
        System.out.println(CREATE_TABLE_SUPPLIER);
        System.out.println("View Product Info: ");
        System.out.println(CREATE_VIEW_PRODUCT_INFO);
    }

}
