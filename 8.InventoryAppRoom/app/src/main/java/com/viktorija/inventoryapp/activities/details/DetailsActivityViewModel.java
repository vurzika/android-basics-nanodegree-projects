package com.viktorija.inventoryapp.activities.details;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.viktorija.inventoryapp.datasources.database.ProductEntity;
import com.viktorija.inventoryapp.datasources.ProductRepository;

public class DetailsActivityViewModel extends AndroidViewModel {

    private ProductRepository repository;

    public DetailsActivityViewModel(@NonNull Application application) {
        super(application);
        repository = new ProductRepository(application);
    }

    public LiveData<ProductEntity> loadProductById(int productId) {
        return repository.loadProductById(productId);
    }

    public void deleteProduct(ProductEntity productEntity) {
        repository.deleteProduct(productEntity);
    }

    public void updateProduct(ProductEntity productEntity) {
        repository.updateProduct(productEntity);
    }
}
