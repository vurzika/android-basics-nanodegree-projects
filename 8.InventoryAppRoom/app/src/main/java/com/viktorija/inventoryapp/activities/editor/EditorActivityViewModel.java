package com.viktorija.inventoryapp.activities.editor;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.viktorija.inventoryapp.database.ProductEntity;
import com.viktorija.inventoryapp.database.ProductRepository;

public class EditorActivityViewModel extends AndroidViewModel {

    private ProductRepository repository;

    public EditorActivityViewModel(@NonNull Application application) {
        super(application);
        repository = new ProductRepository(application);
    }

    public LiveData<ProductEntity> loadProductById(int productId) {
        return repository.loadProductById(productId);
    }

    // Updating product
    public void insertProduct(ProductEntity productEntity) {
        repository.insertProduct(productEntity);
    }
}
