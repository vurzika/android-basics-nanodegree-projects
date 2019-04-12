package com.viktorija.inventoryapp.activities.catalog;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.viktorija.inventoryapp.database.ProductEntity;
import com.viktorija.inventoryapp.database.ProductRepository;

import java.util.List;

public class CatalogActivityViewModel extends AndroidViewModel {

    public LiveData<List<ProductEntity>> productList;

    private ProductRepository repository;

    public CatalogActivityViewModel(@NonNull Application application) {
        super(application);

        repository = ProductRepository.getInstance(application.getApplicationContext());
        productList = repository.productList;
    }

    public LiveData<List<ProductEntity>> getProductList() {
        return productList;
    }

    public void updateProduct(ProductEntity productEntity) {
        repository.updateProduct(productEntity);
    }

    public void deleteAllProducts() {
        repository.deleteAllProducts();
    }

    public void insertAllProducts(List<ProductEntity> productEntities) {
        repository.insertAllProducts(productEntities);
    }
}
