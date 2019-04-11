package com.viktorija.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.viktorija.inventoryapp.data.ProductContract.ProductEntry;
import com.viktorija.inventoryapp.data.ProductUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Application main activity containing product list
 */
public class ProductCatalogActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Identifier for the product data loader
     */
    private static final int PRODUCT_LOADER = 0;

    /**
     * Adapter for the ListView
     */
    private ProductRecycleViewAdapter mCursorAdapter;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    // View to display when there are no products in the list
    @BindView(R.id.empty_view)
    RelativeLayout mEmptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_catalog);

        ButterKnife.bind(this);

        // Configure recycler view to use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        // and item dividers
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                linearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        // Setup an Adapter to create a list item for each row of product data in the Cursor.
        // There is no product data yet (until the loader finishes) so pass in null for the Cursor.
        mCursorAdapter = new ProductRecycleViewAdapter(this, null);
        mRecyclerView.setAdapter(mCursorAdapter);

        mEmptyView.setVisibility(View.GONE);

        // Register data observer to detect when number of products in the list have changed
        // in order to show empty view when number of items is 0
        mCursorAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                if (mCursorAdapter.getItemCount() == 0) {
                    mEmptyView.setVisibility(View.VISIBLE);
                } else {
                    mEmptyView.setVisibility(View.GONE);
                }
            }
        });

        // Setup FAB to open EditorActivity for adding new item
        FloatingActionButton fab = findViewById(R.id.fab_add_product);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(ProductCatalogActivity.this, ProductEditorActivity.class);
            startActivity(intent);
        });

        // Kick of the loader
        getLoaderManager().initLoader(PRODUCT_LOADER, null, this);
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
                insertData();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllProducts();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Loader Manager Related Methods

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductEntry.COLUMN_PRODUCT_IMAGE};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,     // Parent activity context
                ProductEntry.CONTENT_URI,         // Provider content URI to query
                projection,                       // Columns to include in the resulting Cursor
                null,                    // No selection clause
                null,                 // No selection arguments
                null);                   // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update {@link ProductCursorAdapter} with this new cursor containing updated product data
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    // Methods for creating dummy data

    /**
     * Helper method to insert hardcoded product data into the database. For debugging purposes only.
     */
    private void insertData() {
        insertProduct("Skateboard", 15.00, 1, "Amazon", "925-346-7083", BitmapFactory.decodeResource(getResources(), R.drawable.example_skateboard));
        insertProduct("Pen", 3.40, 2, "Staples", "925-346-7083", null);
        insertProduct("Notebook", 4.25, 5, "Target", "415-878-9922", BitmapFactory.decodeResource(getResources(), R.drawable.example_notebook));
    }

    /**
     * Insert a single product into a database
     *
     * @param name                - name of the product
     * @param price               - price of the product
     * @param quantity            - quantity of the product
     * @param supplierName        - name of the supplier
     * @param supplierPhoneNumber - phone number of the supplier
     * @param productImageBitmap  - image to use for displaying a product
     */
    private void insertProduct(String name, double price, int quantity,
                               String supplierName, String supplierPhoneNumber, Bitmap productImageBitmap) {
        // Create a ContentValues object where column names are the keys,
        // and product attributes are the values.
        ContentValues values = new ContentValues();

        values.put(ProductEntry.COLUMN_PRODUCT_NAME, name);
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, price);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity);
        values.put(ProductEntry.COLUMN_PRODUCT_IMAGE, ProductUtils.bitmapToBlob(productImageBitmap));
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME, supplierName);
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER, supplierPhoneNumber);

        // Insert a new row into the provider using the ContentResolver
        try {
            getContentResolver().insert(ProductEntry.CONTENT_URI, values);
        } catch (Exception e) {
            // Ignore error as it shouldn't happen because we are trying to import our own dummy data
        }

    }

    /**
     * Helper method to delete all products in the database.
     */
    private void deleteAllProducts() {
        getContentResolver().delete(ProductEntry.CONTENT_URI, null, null);
    }
}