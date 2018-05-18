package inventory.Entity;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Sale {

    /* ------------------------------- Variables ------------------------------- */

    private SimpleIntegerProperty saleID;
    private SimpleIntegerProperty invoiceID;
    private SimpleIntegerProperty productID;
    private SimpleIntegerProperty quantity;
    private SimpleFloatProperty totalCost;

    /*-------------------------------- UI Variables ---------------------------- */

    private SimpleStringProperty brandName;
    private SimpleStringProperty productName;
    private SimpleFloatProperty sellingPrice;

    /* ------------------------------- Constructor ------------------------------- */

    public Sale() {
        saleID = new SimpleIntegerProperty();
        invoiceID = new SimpleIntegerProperty();
        productID = new SimpleIntegerProperty();
        quantity = new SimpleIntegerProperty();
        totalCost = new SimpleFloatProperty();
        brandName = new SimpleStringProperty();
        productName = new SimpleStringProperty();
        sellingPrice = new SimpleFloatProperty();
    }

    /* ------------------------------- Setters & Getters ------------------------------- */

    public int getSaleID() {
        return saleID.get();
    }

    public SimpleIntegerProperty getSaleIDProperty() {
        return saleID;
    }

    public void setSaleID(int saleID) {
        (this.saleID).set(saleID);
    }

    public int getInvoiceID() {
        return invoiceID.get();
    }

    public SimpleIntegerProperty getInvoiceIDProperty() {
        return invoiceID;
    }

    public void setInvoiceID(int invoiceID) {
        (this.invoiceID).set(invoiceID);
    }

    public int getProductID() {
        return productID.get();
    }

    public SimpleIntegerProperty getProductIDProperty() {
        return productID;
    }

    public void setProductID(int productID) {
        (this.productID).set(productID);
    }

    public int getQuantity() {
        return quantity.get();
    }

    public SimpleIntegerProperty getQuantityProperty() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        (this.quantity).set(quantity);
    }

    public float getTotalCost() {
        return totalCost.get();
    }

    public SimpleFloatProperty getTotalCostProperty() {
        return totalCost;
    }

    public void setTotalCost(float totalCost) {
        (this.totalCost).set(totalCost);
    }

    public String getBrandName() {
        return brandName.get();
    }

    public SimpleStringProperty getBrandNameProperty() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        (this.brandName).set(brandName);
    }

    public String getProductName() {
        return productName.get();
    }

    public SimpleStringProperty getProductNameProperty() {
        return productName;
    }

    public void setProductName(String productName) {
        (this.productName).set(productName);
    }

    public float getSellingPrice() {
        return sellingPrice.get();
    }

    public SimpleFloatProperty getSellingPriceProperty() {
        return sellingPrice;
    }

    public void setSellingPrice(float sellingPrice) {
        (this.sellingPrice).set(sellingPrice);
    }

    /* ---------------------------------------------------------------------------- */

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return this.saleID.get() == ((Sale) obj).saleID.get();
    }
}
