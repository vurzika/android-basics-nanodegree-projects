/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.viktorija.inventoryapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.viktorija.inventoryapp.data.ProductContract;
import com.viktorija.inventoryapp.data.ProductUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Allows user to create a new product or edit an existing one.
 */
public class ProductEditorActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Identifier for the product data loader
     */
    private static final int EXISTING_PRODUCT_LOADER = 1;

    /**
     * Identifier for selecting a photo from gallery via implicit intent
     */
    private static final int ACTION_SELECT_IMAGE_FROM_GALLERY = 1;

    /**
     * Identifier for requesting permissions to access external storage
     */
    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    /**
     * Content URI for the existing product (null if it's a new product)
     */
    private Uri mCurrentProductUri;

    /**
     * Current image data
     */
    private Bitmap mProductImageData;

    /**
     * Boolean flag that keeps track of whether the product has been edited (true) or not (false)
     */
    private boolean mProductHasChanged = false;

    // Find all relevant views that we will need to read user input from
    @BindView(R.id.edit_product_image)
    ImageView mProductPhotoImageView;

    @BindView(R.id.button_remove_product_image)
    Button mRemoveProductImageButton;

    @BindView(R.id.edit_product_name)
    EditText mProductNameEditText;

    @BindView(R.id.edit_product_price)
    EditText mProductPriceEditText;

    @BindView(R.id.edit_product_quantity)
    EditText mProductQuantityEditText;

    @BindView(R.id.edit_supplier_name)
    EditText mSupplierNameEditText;

    @BindView(R.id.edit_supplier_phone_number)
    EditText mSupplierPhoneEditText;

    /**
     * OnTouchListener that listens for any user touches on a View, implying that they are modifying
     * the view, and we change the mProductHasChanged boolean to true.
     */
    private View.OnTouchListener mTouchListener = (view, motionEvent) -> {
        mProductHasChanged = true;
        return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_editor);

        ButterKnife.bind(this);

        // Examine the intent that was used to launch this activity,
        // in order to figure out if
        // we're creating a new product or editing an existing one.
        Intent intent = getIntent();
        mCurrentProductUri = intent.getData();

        // If the intent DOES NOT contain a product content URI, then we know that we are
        // creating a new product.
        if (mCurrentProductUri == null) {
            // This is a new product, so change the app bar to say "Add a Product"
            setTitle(getString(R.string.editor_activity_title_new_product));

            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete a product that hasn't been created yet.)
            invalidateOptionsMenu();
        } else {
            // Otherwise this is an existing product, so change app bar to say "Edit Product"
            setTitle(getString(R.string.editor_activity_title_edit_product));

            // Initialize a loader to read the product data from the database
            // and display the current values in the editor
            getLoaderManager().initLoader(EXISTING_PRODUCT_LOADER, null, this);
        }

        // Setup OnTouchListeners on all the input fields, so we can determine if the user
        // has touched or modified them. This will let us know if there are unsaved changes
        // or not, if the user tries to leave the editor without saving.
        mProductNameEditText.setOnTouchListener(mTouchListener);
        mProductPriceEditText.setOnTouchListener(mTouchListener);
        mProductQuantityEditText.setOnTouchListener(mTouchListener);
        mSupplierNameEditText.setOnTouchListener(mTouchListener);
        mSupplierPhoneEditText.setOnTouchListener(mTouchListener);

        setProductImageData(null);
    }

    /**
     * Get user input from editor and save product into database.
     *
     * @return true if save was successful
     */
    private boolean saveProduct() {

        // Flag that stores if save was successful or not
        boolean savedSuccessfully = false;

        //Read from input fields
        //Use trim to eliminate leading or tailing white space
        String productName = mProductNameEditText.getText().toString().trim();

        Double productPrice = null;
        try {
            productPrice = Double.valueOf(mProductPriceEditText.getText().toString().trim());
        } catch (NumberFormatException e) {
            // Ignore error, keep value empty
        }

        Integer productQuantity = null;
        try {
            productQuantity = Integer.valueOf(mProductQuantityEditText.getText().toString().trim());
        } catch (NumberFormatException e) {
            // Ignore error, keep value empty
        }

        byte[] productImageData = ProductUtils.bitmapToBlob(mProductImageData);

        String supplierName = mSupplierNameEditText.getText().toString().trim();
        String supplierPhone = mSupplierPhoneEditText.getText().toString().trim();

        // Create a ContentValues object where column names are the keys,
        // and product attributes are the values.
        ContentValues values = new ContentValues();

        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, productName);
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE, productPrice);
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, productQuantity);
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_IMAGE, productImageData);
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME, supplierName);
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER, supplierPhone);

        // Message to display to user after operation was completed
        String resultMessage;

        // Determine if this is a new or existing product by checking if mCurrentProductUri is null or not
        if (mCurrentProductUri == null) {
            // This is a NEW product, so insert a new product into the provider,
            // returning the content URI for the new product.
            try {
                Uri newUri = getContentResolver().insert(ProductContract.ProductEntry.CONTENT_URI, values);

                if (newUri != null) {
                    resultMessage = getString(R.string.editor_insert_product_successful);
                    savedSuccessfully = true;
                } else {
                    resultMessage = getString(R.string.editor_insert_product_failed);
                }
            } catch (Exception e) {
                resultMessage = getString(R.string.editor_insert_product_failed_with_reason, e.getMessage());
            }
        } else {
            // Otherwise this is an EXISTING product, so update the product with content URI: mCurrentProductUri
            // and pass in the new ContentValues. Pass in null for the selection and selection args
            // because mCurrentProductUri will already identify the correct row in the database that
            // we want to modify.
            try {
                int rowsAffected = getContentResolver().update(mCurrentProductUri, values, null, null);

                if (rowsAffected == 0) {
                    // If no rows were affected, then there was an error with the update.
                    resultMessage = getString(R.string.editor_update_product_failed);
                } else {
                    resultMessage = getString(R.string.editor_update_product_successful);
                    savedSuccessfully = true;
                }
            } catch (Exception e) {
                resultMessage = getString(R.string.editor_update_product_failed_with_reason, e.getMessage());
            }
        }

        // Display Toast with result message
        Toast.makeText(this, resultMessage, Toast.LENGTH_LONG).show();

        // Return if we were able to save
        return savedSuccessfully;
    }

    // Menu Related Methods

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save product to the database
                boolean saved = saveProduct();

                if (saved) {
                    //Exit activity only if save was successful
                    finish();
                }
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // If the product hasn't changed, continue with navigating up to previous activity
                if (!mProductHasChanged) {
                    finish();
                    return true;
                }

                // Show a dialog that notifies the user they have unsaved changes
                // User clicked "Discard" button, navigate to previous activity.
                showUnsavedChangesDialog((dialogInterface, i) -> finish());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method is called when the back button is pressed.
     */
    @Override
    public void onBackPressed() {
        // If the product hasn't changed, continue with handling back button press
        if (!mProductHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        // Show dialog that there are unsaved changes
        // User clicked "Discard" button, close the current activity.

        showUnsavedChangesDialog((dialogInterface, i) -> finish());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Since the editor shows all product attributes, define a projection that contains
        // all columns from the product table
        String[] projection = {
                ProductContract.ProductEntry._ID,
                ProductContract.ProductEntry.COLUMN_PRODUCT_NAME,
                ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductContract.ProductEntry.COLUMN_PRODUCT_IMAGE,
                ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME,
                ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentProductUri,             // Query the content URI for the current product
                projection,                     // Columns to include in the resulting Cursor
                null,                  // No selection clause
                null,               // No selection arguments
                null);                 // Default sort order
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
            int productNameColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
            int productPriceColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE);
            int productQuantityColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY);
            int productImageColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_IMAGE);
            int supplierNameColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER);

            // Extract out the value from the Cursor for the given column index
            String productName = cursor.getString(productNameColumnIndex);
            Double productPrice = cursor.getDouble(productPriceColumnIndex);
            Integer productQuantity = cursor.getInt(productQuantityColumnIndex);
            byte[] productImageData = cursor.getBlob(productImageColumnIndex);
            String supplierName = cursor.getString(supplierNameColumnIndex);
            String supplierPhone = cursor.getString(supplierPhoneColumnIndex);

            setProductImageData(ProductUtils.blobToBitmap(productImageData));

            mProductNameEditText.setText(productName);
            mProductPriceEditText.setText(productPrice.toString());
            mProductQuantityEditText.setText(productQuantity.toString());
            mSupplierNameEditText.setText(supplierName);
            mSupplierPhoneEditText.setText(supplierPhone);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        // If the loader is invalidated, clear out all the data from the input fields.
        setProductImageData(null);
        mProductNameEditText.setText("");
        mProductPriceEditText.setText("");
        mProductQuantityEditText.setText("");
        mSupplierNameEditText.setText("");
        mSupplierPhoneEditText.setText("");
    }

    /**
     * Show a dialog that warns the user there are unsaved changes that will be lost
     * if they continue leaving the editor.
     *
     * @param discardButtonClickListener is the click listener for what to do when
     *                                   the user confirms they want to discard their changes
     */
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, (dialog, id) -> {
            // User clicked the "Keep editing" button, so dismiss the dialog
            // and continue editing the product.
            if (dialog != null) {
                dialog.dismiss();
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Opens dialog to select product photo from gallery
     */
    public void selectPhotoFromGallery(View view) {
        // Check if user allowed us to access photo gallery
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Request permissions if not
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            // Create intent to Open Image applications like Gallery, Google Photos
            Intent galleryIntent = new Intent(Intent.ACTION_PICK);
            // Select images only
            galleryIntent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(galleryIntent, ACTION_SELECT_IMAGE_FROM_GALLERY);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // If permission was granted, do the
                    selectPhotoFromGallery(null);
                }
            }
        }
    }

    /**
     * This method is called when we are returning to this activity from external activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // read request code to determine from which activity we are coming back
        switch (requestCode) {
            // returned from select image from gallery
            case ACTION_SELECT_IMAGE_FROM_GALLERY:
                // if was able to retrieve data, update photo
                if (resultCode == RESULT_OK && data != null) {
                    // read image data
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    // update image in editor
                    setProductImageData(BitmapFactory.decodeFile(picturePath));

                    // marked item as changed
                    mProductHasChanged = true;
                }
                break;
        }
    }

    public void removeProductImage(View view) {
        setProductImageData(null);

        // Marked item as changed
        mProductHasChanged = true;
    }

    /**
     * Method to update activity UI based on product image we are setting
     *
     * @param productImageData product image bitmap
     */
    private void setProductImageData(Bitmap productImageData){
        // save product image bitmap in field
        // to be able to save it later to database
        mProductImageData = productImageData;

        // if no image, then hide product remove button and show image placeholder instead
        if (mProductImageData == null){
            mRemoveProductImageButton.setVisibility(View.GONE);
            mProductPhotoImageView.setImageResource(R.drawable.product_image_placeholder);
        } else {
            mRemoveProductImageButton.setVisibility(View.VISIBLE);
            mProductPhotoImageView.setImageBitmap(mProductImageData);
        }
    }
}
