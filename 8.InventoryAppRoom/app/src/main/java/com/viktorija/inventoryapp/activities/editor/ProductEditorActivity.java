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
package com.viktorija.inventoryapp.activities.editor;

import android.Manifest;
import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.viktorija.inventoryapp.R;
import com.viktorija.inventoryapp.datasources.database.ProductEntity;
import com.viktorija.inventoryapp.utilities.ProductUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Allows user to create a new product or edit an existing one.
 */
public class ProductEditorActivity extends AppCompatActivity {

    public static final String EXTRA_PRODUCT_ID = "EXTRA_PRODUCT_ID";

    private EditorActivityViewModel viewModel;

    private ProductEntity selectedProduct;

    //Identifier for selecting a photo from gallery via implicit intent
    private static final int ACTION_SELECT_IMAGE_FROM_GALLERY = 1;

    //Identifier for requesting permissions to access external storage
    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    //Current image data
    private Bitmap productImageData;

    //Boolean flag that keeps track of whether the product has been edited (true) or not (false)
    private boolean productHasChanged = false;

    // Find all relevant views that we will need to read user input from
    @BindView(R.id.edit_product_image)
    ImageView productPhotoImageView;

    @BindView(R.id.button_remove_product_image)
    Button removeProductImageButton;

    @BindView(R.id.edit_product_name)
    EditText productNameEditText;

    @BindView(R.id.edit_product_price)
    EditText productPriceEditText;

    @BindView(R.id.edit_product_quantity)
    EditText productQuantityEditText;

    @BindView(R.id.edit_supplier_name)
    EditText supplierNameEditText;

    @BindView(R.id.edit_supplier_phone_number)
    EditText supplierPhoneEditText;

    /**
     * OnTouchListener that listens for any user touches on a View, implying that they are modifying
     * the view, and we change the productHasChanged boolean to true.
     */
    private View.OnTouchListener mTouchListener = (view, motionEvent) -> {
        productHasChanged = true;
        return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_editor);

        ButterKnife.bind(this);

        viewModel = ViewModelProviders.of(this).get(EditorActivityViewModel.class);

        // Setup OnTouchListeners on all the input fields, so we can determine if the user
        // has touched or modified them. This will let us know if there are unsaved changes
        // or not, if the user tries to leave the editor without saving.
        productNameEditText.setOnTouchListener(mTouchListener);
        productPriceEditText.setOnTouchListener(mTouchListener);
        productQuantityEditText.setOnTouchListener(mTouchListener);
        supplierNameEditText.setOnTouchListener(mTouchListener);
        supplierPhoneEditText.setOnTouchListener(mTouchListener);
        supplierPhoneEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        removeProductImageButton.setOnClickListener(v -> {
            setProductImageData(null);

            // Marked item as changed
            productHasChanged = true;
        });

        productPhotoImageView.setOnClickListener(v -> selectPhotoFromGallery());

        // Examine the intent that was used to launch this activity,
        // in order to figure out if
        // we're creating a new product or editing an existing one.

        int productId = -1;

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            productId = extras.getInt(EXTRA_PRODUCT_ID);
        }

        // If the intent DOES NOT contain a product id, then we know that we are
        // creating a new product.
        if (productId == -1) {
            // This is a new product, so change the app bar to say "Add a Product"
            setTitle(getString(R.string.editor_activity_title_new_product));

            selectedProduct = new ProductEntity();

            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete a product that hasn't been created yet.)
            invalidateOptionsMenu();
        } else {
            // Otherwise this is an existing product, so change app bar to say "Edit Product"
            setTitle(getString(R.string.editor_activity_title_edit_product));

            viewModel.loadProductById(productId).observe(this, productEntity -> {
                selectedProduct = productEntity;
                updateUI();
            });
        }

        // initialize UI
        updateUI();
    }

    private void updateUI() {
        if (selectedProduct != null) {
            setProductImageData(selectedProduct.getProductImage());
            productNameEditText.setText(selectedProduct.getProductName());
            if (selectedProduct.getProductPrice() != null) {
                productPriceEditText.setText("" + selectedProduct.getProductPrice());
            } else {
                productPriceEditText.setText("");
            }
            if (selectedProduct.getProductQuantity() != null) {
                productQuantityEditText.setText("" + selectedProduct.getProductQuantity());
            } else {
                productQuantityEditText.setText("");
            }
            supplierNameEditText.setText(selectedProduct.getProductSupplierName());
            supplierPhoneEditText.setText(selectedProduct.getProductSupplierPhoneNumber());
        } else {
            setProductImageData(null);
            productNameEditText.setText("");
            productPriceEditText.setText("");
            productQuantityEditText.setText("");
            supplierNameEditText.setText("");
            supplierPhoneEditText.setText("");
        }
    }

    private void selectPhotoFromGallery() {
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
            galleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(galleryIntent, ACTION_SELECT_IMAGE_FROM_GALLERY);
        }
    }

    /**
     * Method to update activity UI based on product image we are setting
     *
     * @param productImageData product image bitmap
     */
    private void setProductImageData(byte[] productImageData) {
        // save product image bitmap in field
        // to be able to save it later to ProductDatabase
        if (productImageData != null) {
            this.productImageData = ProductUtils.blobToBitmap(productImageData);
        } else {
            this.productImageData = null;
        }

        // if no image, then hide product remove button and show image placeholder instead
        if (this.productImageData == null) {
            removeProductImageButton.setVisibility(View.GONE);
            productPhotoImageView.setImageResource(R.drawable.product_image_placeholder);
        } else {
            removeProductImageButton.setVisibility(View.VISIBLE);
            productPhotoImageView.setImageBitmap(this.productImageData);
        }
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
                // Save product to the ProductDatabase
                boolean saved = saveProduct();

                if (saved) {
                    //Exit activity only if save was successful
                    finish();
                }
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // If the product hasn't changed, continue with navigating up to previous activity
                if (!productHasChanged) {
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
     * Get user input from editor and save product into ProductDatabase.
     *
     * @return true if save was successful
     */
    private boolean saveProduct() {

        //Read from input fields
        //Use trim to eliminate leading or tailing white space
        String productName = productNameEditText.getText().toString().trim();
        if (productName.isEmpty()) {
            Toast.makeText(this, R.string.error_product_name_required, Toast.LENGTH_LONG).show();
            return false;
        }

        double productPrice;
        try {
            productPrice = Double.parseDouble(productPriceEditText.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.error_product_price_required, Toast.LENGTH_LONG).show();
            return false;
        }

        int productQuantity;
        try {
            productQuantity = Integer.parseInt(productQuantityEditText.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.error_product_quantity_required, Toast.LENGTH_LONG).show();
            return false;
        }


        byte[] productImageData = ProductUtils.bitmapToBlob(this.productImageData);

        String supplierName = supplierNameEditText.getText().toString().trim();
        if (supplierName.isEmpty()) {
            Toast.makeText(this, R.string.error_supplier_name_required, Toast.LENGTH_LONG).show();
            return false;
        }

        String supplierPhone = supplierPhoneEditText.getText().toString().trim();
        if (supplierPhone.isEmpty()) {
            Toast.makeText(this, R.string.error_supplier_phone_number_required, Toast.LENGTH_LONG).show();
            return false;
        }

        selectedProduct.setProductName(productName);
        selectedProduct.setProductQuantity(productQuantity);
        selectedProduct.setProductPrice(productPrice);
        selectedProduct.setProductSupplierName(supplierName);
        selectedProduct.setProductSupplierPhoneNumber(supplierPhone);
        selectedProduct.setProductImage(productImageData);

        viewModel.insertProduct(selectedProduct);

        // Display Toast with result message
        Toast.makeText(this, getString(R.string.editor_update_product_successful), Toast.LENGTH_LONG).show();

        // Return if we were able to save
        return true;
    }


    /**
     * This method is called when the back button is pressed.
     */
    @Override
    public void onBackPressed() {
        // If the product hasn't changed, continue with handling back button press
        if (!productHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        // Show dialog that there are unsaved changes
        // User clicked "Discard" button, close the current activity.

        showUnsavedChangesDialog((dialogInterface, i) -> finish());
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // If permission was granted, do the
                selectPhotoFromGallery();
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
        // returned from select image from gallery
        if (requestCode == ACTION_SELECT_IMAGE_FROM_GALLERY) {// if was able to retrieve data, update photo
            if (resultCode == RESULT_OK && data != null) {
                // read image data
                Uri selectedImage = data.getData();
                if (selectedImage != null) {
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();

                        // update image in editor
                        setProductImageData(ProductUtils.bitmapToBlob(BitmapFactory.decodeFile(picturePath)));

                        // marked item as changed
                        productHasChanged = true;
                    }
                }
            }
        }
    }
}
