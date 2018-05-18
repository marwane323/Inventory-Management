package inventory.Entity;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Brand {

    /* ------------------------------- Variables ------------------------------- */

    private SimpleIntegerProperty brandID;
    private SimpleStringProperty brandName;
    private FileInputStream brandPicture;

    /* ------------------------------- Constructor ------------------------------- */

    public Brand() {
        brandID = new SimpleIntegerProperty();
        brandName = new SimpleStringProperty();
    }

    /* ------------------------------- Setters & Getters ------------------------------- */

    public int getBrandID() {
        return brandID.get();
    }

    public void setBrandID(int brandID) {
        (this.brandID).set(brandID);
    }

    public String getBrandName() {
        return brandName.get();
    }

    public void setBrandName(String brandName) {
        (this.brandName).set(brandName);
    }

    public SimpleIntegerProperty getBrandIDProperty() {
        return brandID;
    }

    public void setBrandIDProperty(SimpleIntegerProperty brandID) {
        this.brandID = brandID;
    }

    public SimpleStringProperty getBrandNameProperty() {
        return brandName;
    }

    public void setBrandNameProperty(SimpleStringProperty brandName) {
        this.brandName = brandName;
    }

    public FileInputStream getBrandPicture() {
        return brandPicture;
    }

    public void setBrandPicture(String path) {
        try {
            File image = new File(path);
            this.brandPicture = new FileInputStream(image);
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found Exception: - " + e.getMessage());
        }
    }

    /* ---------------------------------------------------------------------------- */

    //Overriding toString() this way to get used by the boxBrand ComboBox
    @Override
    public String toString() {
        return getBrandName();
    }

    //Overriding equals method to use it in the SelectionModel.select(Brand brand) in the boxBrand ComboBox
    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return this.brandID.get() == ((Brand) obj).brandID.get();
    }

    @Override
    public int hashCode() {
        return 7 + 5 * brandID.get();
    }
}
