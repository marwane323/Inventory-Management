package inventory.Entity;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Invoice {

    private java.util.Date dt = new java.util.Date();
    private java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private SimpleIntegerProperty invoiceID;
    private SimpleStringProperty date;

    public Invoice() {
        invoiceID = new SimpleIntegerProperty();
        date = new SimpleStringProperty(sdf.format(dt));
    }

    public int getInvoiceID() {
        return invoiceID.get();
    }

    public void setInvoiceID(int invoiceID) {
        (this.invoiceID).set(invoiceID);
    }

    public String getDate() {
        return date.get();
    }

    public void setDate(String date) {
        (this.date).set(date);
    }

    public SimpleIntegerProperty getInvoiceIDProperty() {
        return this.invoiceID;
    }

    public void setInvoiceIDProperty(SimpleIntegerProperty invoiceID){
        this.invoiceID = invoiceID;
    }

    public SimpleStringProperty getDateProperty() {
        return this.date;
    }

    public void setDateProperty(SimpleStringProperty date){
        this.date = date;
    }

    @Override
    public String toString() {
        return " Invoice ID: " + invoiceID +
               " Date: " + date;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return this.invoiceID.get() == ((Invoice) obj).invoiceID.get();
    }
}
