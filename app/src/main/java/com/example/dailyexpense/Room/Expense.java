package com.example.dailyexpense.Room;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "expense_table")
public class Expense {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String date;
    private String category;
    private String product;
    private double quantity;
    private String spinnerValue;
    private double price;

    public Expense(int id, String date, String category, String product, double quantity, String spinnerValue, double price) {
        this.id = id;
        this.date = date;
        this.category = category;
        this.product = product;
        this.quantity = quantity;
        this.spinnerValue = spinnerValue;
        this.price = price;
    }
    @Ignore
    public Expense(String date,String category, String product, double quantity, String spinnerValue, double price) {
        this.date = date;
        this.category = category;
        this.product = product;
        this.quantity = quantity;
        this.spinnerValue = spinnerValue;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getSpinnerValue() {
        return spinnerValue;
    }

    public void setSpinnerValue(String spinnerValue) {
        this.spinnerValue = spinnerValue;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
