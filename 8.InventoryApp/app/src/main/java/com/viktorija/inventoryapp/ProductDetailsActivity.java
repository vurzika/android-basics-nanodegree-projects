package com.viktorija.inventoryapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.viktorija.inventoryapp.data.ProductContract;
import com.viktorija.inventoryapp.data.ProductUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductDetailsActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Identifier for the product data loader
     */
    private static final int EXISTING_PRODUCT_LOADER = 2;
    private static final int PERMISSIONS_REQUEST_CALL_PHONE = 1;

    /**
     * Content URI for the existing product (null if it's a new product)
     */
    private Uri mCurrentProductUri;

    /**
     * Field for storing current product quantity (used for updating it)
     */
    private Integer mProductQuantity;

    /**
     * Field for storing current supplier phone number
     */
    private String mSupplierPhone;


    @BindView(R.id.details_product_image)
    ImageView mProductPhotoImageView;

    @BindView(R.id.details_product_price)
    TextView mProductPriceTextView ;

    @BindView(R.id.details_product_quantity)
    TextView mProductQuantityTextView ;

    @BindView(R.id.details_supplier_name)
    TextView mSupplierNameTextView ;

    @BindView(R.id.details_supplier_phone)
    TextView mSupplierPhoneTextView ;

    @BindView(R.id.details_button_product_quantity_decrease)
    ImageButton mDecreaseProductQuantityButton ;

    @BindView(R.id.product_details_collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Toolbar toolbar = findViewById(R.id.product_details_toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        // Display back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Examine the intent that was used to launch this activity,
        // in order to figure out if
        // we're creating a new product or editing an existing one.
        Intent intent = getIntent();
        mCurrentProductUri = intent.getData();

        // Initialize a loader to read the product data from the database
        // and display the current values in the editor
        getLoaderManager().initLoader(EXISTING_PRODUCT_LOADER, null, this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    // Menu Related Methods

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Edit" menu option
            case R.id.action_edit:
                // Open ProductEditor Activity
                Intent intent = new Intent(ProductDetailsActivity.this, ProductEditorActivity.class);
                // Set the URI on the data field of the intent
                intent.setData(mCurrentProductUri);
                startActivity(intent);
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Pop up confirmation dialog for deletion
                showDeleteConfirmationDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Prompt the user to confirm that they want to delete this product.
     */
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);

        // User clicked the "Delete" button, so delete the product.
        builder.setPositiveButton(R.string.delete, (dialog, id) -> deleteProduct());

        // User clicked the "Cancel" button, so dismiss the dialog
        // and continue editing the product.
        builder.setNegativeButton(R.string.cancel, (dialog, id) -> {
            if (dialog != null) {
                dialog.dismiss();
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Perform the deletion of the product in the database.
     */
    private void deleteProduct() {
        // Call the ContentResolver to delete the product at the given content URI.
        // Pass in null for the selection and selection args because the mCurrentProductUri
        // content URI already identifies the product that we want.
        int rowsDeleted = getContentResolver().delete(mCurrentProductUri, null, null);
        // Show a toast message depending on whether or not the delete was successful.
        if (rowsDeleted == 0) {
            // If no rows were deleted, then there was an error with the delete.
            Toast.makeText(this, getString(R.string.editor_delete_product_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the delete was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.editor_delete_product_successful),
                    Toast.LENGTH_SHORT).show();
            // Exit Activity
            finish();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Since the editor shows all product attributes, define a projection that contains
        // all columns from the product table
        String[] projection = {
                ProductContract.ProductEntry._ID,
                ProductContract.ProductEntry.COLUMN_PRODUCT_IMAGE,
                ProductContract.ProductEntry.COLUMN_PRODUCT_NAME,
                ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME,
                ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentProductUri,             // Query the content URI for the current product
                projection,                     // Columns to include in the resulting Cursor
                null,                  // No selection clause
                null,              // No selection arguments
                null);                // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of product attributes that we're interested in
            int productImageColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_IMAGE);
            int productNameColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
            int productPriceColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE);
            int productQuantityColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER);

            // Extract out the value from the Cursor for the given column index
            String productName = cursor.getString(productNameColumnIndex);
            Double productPrice = cursor.getDouble(productPriceColumnIndex);
            // Save to field to be able to use this value when increase / decrease quantity
            mProductQuantity = cursor.getInt(productQuantityColumnIndex);
            byte[] productImageData = cursor.getBlob(productImageColumnIndex);
            String supplierName = cursor.getString(supplierNameColumnIndex);
            mSupplierPhone = cursor.getString(supplierPhoneColumnIndex);

            // Update the views on the screen with the values from the database

            // Update activity title in toolbar to contain product name
            mCollapsingToolbarLayout.setTitle(productName);

            if (productImageData != null) {
                mProductPhotoImageView.setVisibility(View.VISIBLE);
                mProductPhotoImageView.setImageBitmap(ProductUtils.blobToBitmap(productImageData));
            } else {
                mProductPhotoImageView.setVisibility(View.GONE);
            }

            // Update other fields in content
            mProductPriceTextView.setText(ProductUtils.formatPrice(productPrice));
            mProductQuantityTextView.setText(mProductQuantity.toString());
            if (mProductQuantity == null || mProductQuantity == 0) {
                mDecreaseProductQuantityButton.setEnabled(false);
            } else {
                mDecreaseProductQuantityButton.setEnabled(true);
            }
            mSupplierNameTextView.setText(supplierName);
            mSupplierPhoneTextView.setText(mSupplierPhone);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        // If the loader is invalidated, clear out all the data from the input fields.
        mProductPhotoImageView.setVisibility(View.GONE);
        mProductPriceTextView.setText("");
        mProductQuantityTextView.setText("");
        mSupplierNameTextView.setText("");
        mSupplierPhoneTextView.setText("");
    }

    /**
     * Decreases product quantity by 1
     *
     * @param view view
     */
    public void decreaseProductQuantity(View view) {
        updateProductQuantity(mProductQuantity - 1);
    }

    /**
     * Increases product quantity by 1
     *
     * @param view view
     */
    public void increaseProductQuantity(View view) {
        updateProductQuantity(mProductQuantity + 1);
    }

    /**
     * Updates product quantity in database using provided value
     *
     * @param updatedProductQuantity product quantity to set
     */
    private void updateProductQuantity(Integer updatedProductQuantity) {
        // Prepare values to update
        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, updatedProductQuantity);

        // update data
        getContentResolver().update(mCurrentProductUri, values, null, null);
    }

    public void contactSupplier(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // Request permissions if not
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    PERMISSIONS_REQUEST_CALL_PHONE);
        } else {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mSupplierPhone)));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // If permission was granted, do the
                    contactSupplier(null);
                }
            }
        }
    }
}

