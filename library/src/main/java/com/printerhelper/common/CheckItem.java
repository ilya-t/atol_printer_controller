package com.printerhelper.common;

import java.util.List;

public class CheckItem {
    private List<String> headers;
    private String title;
    private int vatAmount = 0;
    private int department;
    private double quantity;
    private double price;
    private float discount = 0f;

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

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public int getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(int vatAmount) {
        this.vatAmount = vatAmount;
    }
}
