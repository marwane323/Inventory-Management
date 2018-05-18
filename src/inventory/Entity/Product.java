package inventory.Entity;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Product {

    /* ------------------------------- Variables ------------------------------- */

    private SimpleIntegerProperty productID;
    private SimpleStringProperty productName;
    private SimpleIntegerProperty brandID;
    private SimpleFloatProperty sellingPrice;
    private SimpleIntegerProperty quantityMinimum;
    private SimpleIntegerProperty quantityInStock;

    /*-------------------------------- UI Variables ---------------------------- */

    private SimpleStringProperty brandName;

    /* ------------------------------- Constructor ------------------------------- */

    public Product() {
        //Initializing Variables
        this.productID = new SimpleIntegerProperty();
        this.productName = new SimpleStringProperty();
        this.brandID = new SimpleIntegerProperty();
        this.sellingPrice = new SimpleFloatProperty();
        this.quantityMinimum = new SimpleIntegerProperty();
        this.quantityInStock = new SimpleIntegerProperty();
        this.brandName = new SimpleStringProperty();
    }

    /* ------------------------------- Setters & Getters ------------------------------- */

    public void setProductID(int productID) {
        (this.productID).set(productID);
    }

    public void setProductName(String productName) {
        (this.productName).set(productName);
    }

    public void setBrandID(int brandID) {
        (this.brandID).set(brandID);
    }

    public void setSellingPrice(float sellingPrice) {
        (this.sellingPrice).set(sellingPrice);
    }

    public void setQuantityMinimum(int quantityMinimum) {
        (this.quantityMinimum).set(quantityMinimum);
    }

    public void setQuantityInStock(int quantityInStock) {
        (this.quantityInStock).set(quantityInStock);
    }

    public int getProductID() {
        return productID.get();
    }

    public String getProductName() {
        return productName.get();
    }

    public int getBrandID() {
        return brandID.get();
    }

    public float getSellingPrice() {
        return sellingPrice.get();
    }

    public int getQuantityMinimum() {
        return quantityMinimum.get();
    }

    public int getQuantityInStock() {
        return quantityInStock.get();
    }

    public SimpleIntegerProperty getProductIDProperty() {
        return productID;
    }

    public void setProductIDProperty(SimpleIntegerProperty productID) {
        this.productID = productID;
    }

    public SimpleStringProperty getProductNameProperty() {
        return productName;
    }

    public void setProductNameProperty(SimpleStringProperty productName) {
        this.productName = productName;
    }

    public SimpleIntegerProperty getBrandIDProperty() {
        return brandID;
    }

    public void setBrandIDProperty(SimpleIntegerProperty brandID) {
        this.brandID = brandID;
    }

    public SimpleFloatProperty getSellingPriceProperty() {
        return sellingPrice;
    }

    public void setSellingPriceProperty(SimpleFloatProperty sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public SimpleIntegerProperty getQuantityMinimumProperty() {
        return quantityMinimum;
    }

    public void setQuantityMinimumProperty(SimpleIntegerProperty quantityMinimum) {
        this.quantityMinimum = quantityMinimum;
    }

    public SimpleIntegerProperty getQuantityInStockProperty() {
        return quantityInStock;
    }

    public void setQuantityInStockProperty(SimpleIntegerProperty quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public String getBrandName() {
        return brandName.get();
    }

    public SimpleStringProperty getBrandNameProperty() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName.set(brandName);
    }

    public void setBrandNameProperty(SimpleStringProperty brandName){
        this.brandName = brandName;
    }

    /* ---------------------------------------------------------------------------- */

    @Override
    public String toString() {
        return "\n Product's ID: " + productID.get() +
               "\n Product's Name: " + productName.get() +
               "\n Brand's ID: " + brandID.get() +
               "\n Selling Price: " + sellingPrice.get() +
               "\n Minimum Quantity: " + quantityMinimum.get() +
               "\n Quantity In Stock: " + quantityInStock.get();
    }

    //Overriding equals() to use it in TableView in ProductsWindowController (Selection after update or insert)
    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return this.productID.get() == ((Product) obj).productID.get();
    }
}
