package com.viktorija.inventoryapp.utilities;

import android.content.Context;

import com.viktorija.inventoryapp.R;
import com.viktorija.inventoryapp.datasources.database.ProductEntity;

import java.util.ArrayList;
import java.util.List;

public class SampleData {

    public static List<ProductEntity> getProductItems(Context context) {
        List<ProductEntity> productList = new ArrayList<>();
        productList.add(
                new ProductEntity(
                        "Skateboard",
                        15.00,
                        1,
                        "Amazon",
                        "925-346-7083",
                        ProductUtils.imageAsBytes(context, R.drawable.example_skateboard)));
        productList.add(
                new ProductEntity(
                        "Pen",
                        3.40,
                        2,
                        "Staples",
                        "925-346-7083",
                        ProductUtils.imageAsBytes(context, R.drawable.product_image_placeholder)));
        productList.add(
                new ProductEntity(
                        "Notebook",
                        4.25,
                        5,
                        "Target",
                        "415-878-9922",
                        ProductUtils.imageAsBytes(context, R.drawable.example_notebook)));
        return productList;
    }
}