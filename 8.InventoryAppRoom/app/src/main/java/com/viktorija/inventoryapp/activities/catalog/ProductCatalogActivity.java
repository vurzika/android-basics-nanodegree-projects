package com.viktorija.inventoryapp.activities.catalog;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.AdapterDataObserver;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.viktorija.inventoryapp.R;
import com.viktorija.inventoryapp.activities.editor.ProductEditorActivity;
import com.viktorija.inventoryapp.database.ProductEntity;
import com.viktorija.inventoryapp.utilities.SampleData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Application main activity containing product list
 */
public class ProductCatalogActivity extends AppCompatActivity {

    private ProductRecycleViewAdapter adapter;
    private CatalogActivityViewModel viewModel;
    private List<ProductEntity> productData = new ArrayList<>();

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    // View to display when there are no products in the list
    @BindView(R.id.empty_view)
    RelativeLayout emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_catalog);

        ButterKnife.bind(this);

        initViewModel();

        initRecyclerView();

        // Setup FAB to open EditorActivity for adding new item
        FloatingActionButton fab = findViewById(R.id.fab_add_product);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(ProductCatalogActivity.this, ProductEditorActivity.class);
            startActivity(intent);
        });
    }

    private void initRecyclerView() {

        // Configure recycler view to use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        // Setup an Adapter to create a list item for each row of product data in the Cursor.
        // There is no product data yet (until the loader finishes) so pass in null for the Cursor.
        adapter = new ProductRecycleViewAdapter(this, productData, viewModel);
        recyclerView.setAdapter(adapter);

        // and item dividers
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        emptyView.setVisibility(View.GONE);

        // Register data observer to detect when number of products in the list have changed
        // in order to show empty view when number of items is 0
        adapter.registerAdapterDataObserver(new AdapterDataObserver() {
            @Override
            public void onChanged() {
                if (adapter.getItemCount() == 0) {
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    emptyView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initViewModel() {
        // Reference to the ViewModel
        viewModel = ViewModelProviders.of(this).get(CatalogActivityViewModel.class);

        //Adding sample data automatically
        viewModel.getProductList().observe(ProductCatalogActivity.this, productEntities -> {
            productData.clear();
            if (productEntities != null) {
                productData.addAll(productEntities);
            }
            adapter.notifyDataSetChanged();
        });
    }

    // Menu Related Methods

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertSampleData();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // ACTIONS

    private void insertSampleData() {
        viewModel.insertAllProducts(SampleData.getProductItems(this));
    }

    private void deleteData() {
        viewModel.deleteAllProducts();
    }
}