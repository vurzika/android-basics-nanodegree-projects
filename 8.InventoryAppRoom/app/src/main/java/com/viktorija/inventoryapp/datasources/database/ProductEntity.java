package com.viktorija.inventoryapp.datasources.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "products_table")
public class ProductEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String productName;

    private Double productPrice;

    private Integer productQuantity;

    private String productSupplierName;

    private String productSupplierPhoneNumber;

    // It is usually not recommended to store image data into the ProductDatabase.
    // But however if it is required for your project then you can do so.
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] productImage;

    public ProductEntity(int id, String productName, Double productPrice, Integer productQuantity, String productSupplierName, String productSupplierPhoneNumber, byte[] productImage) {
        this.id = id;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.productSupplierName = productSupplierName;
        this.productSupplierPhoneNumber = productSupplierPhoneNumber;
        this.productImage = productImage;
    }

    // Constructor to add a new product when you want the integer to be assigned automatically
    @Ignore
    public ProductEntity(String productName, Double productPrice, Integer productQuantity, @NonNull String productSupplierName, @NonNull String productSupplierPhoneNumber, byte[] productImage) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.productSupplierName = productSupplierName;
        this.productSupplierPhoneNumber = productSupplierPhoneNumber;
        this.productImage = productImage;
    }

    @Ignore
    public ProductEntity() {
    }

    // getters

    public int getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public Integer getProductQuantity() {
        return productQuantity;
    }

    public byte[] getProductImage() {
        return productImage;
    }

    public String getProductSupplierName() {
        return productSupplierName;
    }

    public String getProductSupplierPhoneNumber() {
        return productSupplierPhoneNumber;
    }

    // setters
    public void setId(int id) {
        this.id = id;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public void setProductQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
    }

    public void setProductImage(byte[] productImage) {
        this.productImage = productImage;
    }

    public void setProductSupplierName(String productSupplierName) {
        this.productSupplierName = productSupplierName;
    }

    public void setProductSupplierPhoneNumber(String productSupplierPhoneNumber) {
        this.productSupplierPhoneNumber = productSupplierPhoneNumber;
    }
}
