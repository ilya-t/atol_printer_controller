package com.atolprinterhelper;

public class CheckItem {
    private String title;
    private int department;
    private double quantity;
    private double price;

    public CheckItem(String title, double quantity, double price) throws IllegalArgumentException{
        this.title = title;
        this.quantity = quantity;
        this.price = price;

        if (quantity <= 0){
            throw new IllegalArgumentException("Quantity must be bigger than zero");
        }

        if (price < 0){
            throw new IllegalArgumentException("Price must be bigger or equal to zero");
        }
    }

    public void setDepartment(int department) {
        this.department = department;
    }

    public String getTitle() {
        return title;
    }

    public int getDepartment() {
        return department;
    }

    public double getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }
}