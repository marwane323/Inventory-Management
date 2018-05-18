package inventory.Entity;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Supplier {

    /* ------------------------------- Variables ------------------------------- */

    private SimpleIntegerProperty supplierID;
    private SimpleStringProperty supplierName;
    private SimpleStringProperty supplierAddress;
    private SimpleStringProperty supplierPhone;

    /* ------------------------------- Constructor ------------------------------- */

    public Supplier() {
        //Initializing Variables
        this.supplierID = new SimpleIntegerProperty();
        this.supplierName = new SimpleStringProperty();
        this.supplierAddress = new SimpleStringProperty();
        this.supplierPhone = new SimpleStringProperty();
    }

    /* ------------------------------- Setters & Getters ------------------------------- */


    public int getSupplierID() {
        return supplierID.get();
    }

    public void setSupplierID(int supplierID) {
        (this.supplierID).set(supplierID);
    }

    public String getSupplierName() {
        return supplierName.get();
    }

    public void setSupplierName(String supplierName) {
        (this.supplierName).set(supplierName);
    }

    public String getSupplierAddress() {
        return supplierAddress.get();
    }

    public void setSupplierAddress(String supplierAddress) {
        (this.supplierAddress).set(supplierAddress);
    }

    public String getSupplierPhone() {
        return supplierPhone.get();
    }

    public void setSupplierPhone(String supplierPhone) {
        (this.supplierPhone).set(supplierPhone);
    }

    public SimpleIntegerProperty getSupplierIDProperty() {
        return supplierID;
    }

    public void setSupplierIDProperty(SimpleIntegerProperty supplierID) {
        this.supplierID = supplierID;
    }

    public SimpleStringProperty getSupplierNameProperty() {
        return supplierName;
    }

    public void setSupplierNameProperty(SimpleStringProperty supplierName) {
        this.supplierName = supplierName;
    }


    public SimpleStringProperty getSupplierAddressProperty() {
        return supplierAddress;
    }

    public void setSupplierAddressProperty(SimpleStringProperty supplierAddress) {
        this.supplierAddress = supplierAddress;
    }


    public SimpleStringProperty getSupplierPhoneProperty() {
        return supplierPhone;
    }

    public void setSupplierPhoneProperty(SimpleStringProperty supplierPhone) {
        this.supplierPhone = supplierPhone;
    }


    @Override
    public String toString() {
        return " Supplier's ID: " + supplierID +
               " Supplier's Name: " + supplierName +
               " Supplier's Address: " + supplierAddress +
               " Supplier's Phone: " + supplierPhone;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return this.supplierID.get() == ((Supplier) obj).supplierID.get();
    }
}
