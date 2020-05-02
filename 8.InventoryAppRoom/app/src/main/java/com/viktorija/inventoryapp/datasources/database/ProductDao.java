package com.viktorija.inventoryapp.datasources.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProduct(ProductEntity productEntity);

    @Insert
    void insertAll(List<ProductEntity> productList);

    @Delete
    void deleteProduct(ProductEntity productEntity);

    @Query("SELECT * FROM products_table WHERE id = :id")
    LiveData<ProductEntity> getProductById(int id);

    @Query("SELECT * FROM products_table ORDER BY productName ASC")
    LiveData<List<ProductEntity>> getAll();

    // delete all items
    @Query("DELETE FROM products_table")
    void deleteAll();

    @Update
    void updateProduct(ProductEntity productEntity);
}
