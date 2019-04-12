package com.viktorija.inventoryapp.database;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ProductRepository {

    private static ProductRepository ourInstance;

    public LiveData<List<ProductEntity>> productList;

    // declaring an instance of the class ProductDatabase.
    private ProductDatabase db;

    // All Room database operations must be executed on the background thread.
    // Creating a single executor object
    private Executor executor = Executors.newSingleThreadExecutor();

    // Repository - singleton pattern
    public static ProductRepository getInstance(Context context) {
        if (ourInstance == null) {
            // if ourInstance is null - initialize it by calling a private constructor
            ourInstance = new ProductRepository(context);
        }
        return ourInstance;
    }

    public ProductRepository(Context context) {
        db = ProductDatabase.getInstance(context);
        productList = getAllProductItems();
    }

    // when you read information through dao you do not need executor
    private LiveData<List<ProductEntity>> getAllProductItems() {
        // content of the database table
        return db.productDao().getAll();
    }

    public void insertProduct(final ProductEntity productItem) {
        executor.execute(() -> db.productDao().insertProduct(productItem));
    }

    public void insertAllProducts(final List<ProductEntity> productEntities) {
        executor.execute(() -> db.productDao().insertAll(productEntities));
    }

    public void updateProduct(final ProductEntity productItem) {
        executor.execute(() -> db.productDao().updateProduct(productItem));
    }

    public void deleteProduct(final ProductEntity productItem) {
        executor.execute(() -> db.productDao().deleteProduct(productItem));
    }

    public void deleteAllProducts() {
        executor.execute(() -> db.productDao().deleteAll());
    }

    public LiveData<ProductEntity> loadProductById(int productId) {
        return db.productDao().getProductById(productId);
    }
}