package inventory.Entity;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Purchase {

    /* ------------------------------- Variables ------------------------------- */

    private SimpleIntegerProperty purchaseID;
    private SimpleIntegerProperty supplierID;
    private SimpleIntegerProperty invoiceID;
    private SimpleIntegerProperty productID;
    private SimpleFloatProperty costPrice;
    private SimpleIntegerProperty quantity;
    private SimpleFloatProperty totalCost;

    /*-------------------------------- UI Variables ---------------------------- */

    private SimpleStringProperty supplierName;
    private SimpleStringProperty productName;

    /* ------------------------------- Constructor ------------------------------- */

    public Purchase() {
        purchaseID = new SimpleIntegerProperty();
        supplierID = new SimpleIntegerProperty();
        invoiceID = new SimpleIntegerProperty();
        productID = new SimpleIntegerProperty();
        costPrice = new SimpleFloatProperty();
        quantity = new SimpleIntegerProperty();
        totalCost = new SimpleFloatProperty();
        supplierName = new SimpleStringProperty();
        productName = new SimpleStringProperty();
    }

    /* ------------------------------- Setters & Getters ------------------------------- */

    public int getPurchaseID() {
        return purchaseID.get();
    }

    public void setPurchaseID(int purchaseID) {
        this.purchaseID.set(purchaseID);
    }

    public int getSupplierID() {
        return supplierID.get();
    }

    public void setSupplierID(int supplierID) {
        this.supplierID.set(supplierID);
    }

    public int getInvoiceID() {
        return invoiceID.get();
    }

    public void setInvoiceID(int invoiceID) {
        this.invoiceID.set(invoiceID);
    }

    public int getProductID() {
        return productID.get();
    }

    public void setProductID(int productID) {
        this.productID.set(productID);
    }

    public float getCostPrice() {
        return costPrice.get();
    }

    public void setCostPrice(float costPrice) {
        this.costPrice.set(costPrice);
    }

    public int getQuantity() {
        return quantity.get();
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public float getTotalCost() {
        return totalCost.get();
    }

    public void setTotalCost(float totalCost) {
        this.totalCost.set(totalCost);
    }

    public SimpleIntegerProperty getPurchaseIDProperty(){
        return purchaseID;
    }

    public SimpleIntegerProperty getSupplierIDProperty() {
        return supplierID;
    }

    public SimpleIntegerProperty getInvoiceIDProperty() {
        return invoiceID;
    }

    public SimpleIntegerProperty getProductIDProperty() {
        return productID;
    }

    public SimpleFloatProperty getCostPriceProperty() {
        return costPrice;
    }

    public SimpleIntegerProperty getQuantityProperty() {
        return quantity;
    }

    public SimpleFloatProperty getTotalCostProperty() {
        return totalCost;
    }

    public void setPurchaseIDProperty(SimpleIntegerProperty purchaseID){
        this.purchaseID = purchaseID;
    }

    public void setSupplierIDProperty(SimpleIntegerProperty supplierID){
        this.supplierID = supplierID;
    }

    public void setInvoiceIDProperty(SimpleIntegerProperty invoiceID){
        this.invoiceID = invoiceID;
    }

    public void setProductIDProperty(SimpleIntegerProperty productID){
        this.productID = productID;
    }

    public void setCostPriceProperty(SimpleFloatProperty costPrice){
        this.costPrice = costPrice;
    }

    public void setQuantityProperty(SimpleIntegerProperty quantity){
        this.quantity = quantity;
    }

    public void setTotalCostProperty(SimpleFloatProperty totalCost){
        this.totalCost = totalCost;
    }

    public String getSupplierName() {
        return supplierName.get();
    }

    public SimpleStringProperty getSupplierNameProperty() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        (this.supplierName).set(supplierName);
    }

    public void setSupplierNameProperty(SimpleStringProperty supplierName){
        this.supplierName = supplierName;
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

    public void setProductNameProperty(SimpleStringProperty productName){
        this.productName = productName;
    }

    /* ---------------------------------------------------------------------------- */

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return this.purchaseID.get() == ((Purchase) obj).purchaseID.get();
    }
}
