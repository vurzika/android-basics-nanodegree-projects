package com.viktorija.inventoryapp.activities.details;

import android.Manifest;
import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import com.viktorija.inventoryapp.R;
import com.viktorija.inventoryapp.activities.editor.ProductEditorActivity;
import com.viktorija.inventoryapp.datasources.database.ProductEntity;
import com.viktorija.inventoryapp.utilities.ProductUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_PRODUCT_ID = "EXTRA_PRODUCT_ID";

    private static final int PERMISSIONS_REQUEST_CALL_PHONE = 1;

    private DetailsActivityViewModel viewModel;

    private ProductEntity selectedProduct;

    @BindView(R.id.details_product_image)
    ImageView productPhotoImageView;

    @BindView(R.id.details_product_price)
    TextView productPriceTextView;

    @BindView(R.id.details_product_quantity)
    TextView productQuantityTextView;

    @BindView(R.id.details_supplier_name)
    TextView supplierNameTextView;

    @BindView(R.id.details_supplier_phone)
    TextView supplierPhoneTextView;

    @BindView(R.id.details_button_product_quantity_decrease)
    ImageButton decreaseProductQuantityButton;

    @BindView(R.id.product_details_collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.details_supplier_phone_image_button)
    ImageButton supplierPhoneImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.product_details_toolbar);
        setSupportActionBar(toolbar);

        // Display back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        supplierPhoneImageButton.setOnClickListener(v -> contactSupplier());


        viewModel = ViewModelProviders.of(this).get(DetailsActivityViewModel.class);

        Bundle extras = getIntent().getExtras();
        int productId = extras.getInt(EXTRA_PRODUCT_ID);

        viewModel.loadProductById(productId).observe(this, productEntity -> {
            selectedProduct = productEntity;
            updateUi();
        });
    }

    private void contactSupplier() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // Request permissions if not
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    PERMISSIONS_REQUEST_CALL_PHONE);
        } else {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + selectedProduct.getProductSupplierPhoneNumber())));
        }
    }


    // Update the views on the screen with the values from the database
    private void updateUi() {
        if (selectedProduct != null) {
            // Update activity title in toolbar to contain product name
            collapsingToolbarLayout.setTitle(selectedProduct.getProductName());

            productQuantityTextView.setText("" + selectedProduct.getProductQuantity());
            if (selectedProduct.getProductQuantity() == 0) {
                decreaseProductQuantityButton.setEnabled(false);
            } else {
                decreaseProductQuantityButton.setEnabled(true);
            }

            productPriceTextView.setText(ProductUtils.formatPrice(selectedProduct.getProductPrice()));

            byte[] productImageData = selectedProduct.getProductImage();
            // Display product image or placeholder if we don't have actual image
            if (productImageData != null) {
                productPhotoImageView.setVisibility(View.VISIBLE);
                productPhotoImageView.setImageBitmap(ProductUtils.blobToBitmap(productImageData));
            } else {
                productPhotoImageView.setVisibility(View.GONE);
            }

            supplierNameTextView.setText(selectedProduct.getProductSupplierName());
            supplierPhoneTextView.setText(selectedProduct.getProductSupplierPhoneNumber());
        } else {
            productPhotoImageView.setVisibility(View.GONE);
            productPriceTextView.setText("");
            productQuantityTextView.setText("");
            supplierNameTextView.setText("");
            supplierPhoneTextView.setText("");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //Decreases product quantity by 1
    public void decreaseProductQuantity(View view) {
        selectedProduct.setProductQuantity(selectedProduct.getProductQuantity() - 1);
        viewModel.updateProduct(selectedProduct);
    }

    //Increases product quantity by 1
    public void increaseProductQuantity(View view) {
        selectedProduct.setProductQuantity(selectedProduct.getProductQuantity() + 1);
        viewModel.updateProduct(selectedProduct);
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
        switch (item.getItemId()) {
            case R.id.action_edit:
                // Open ProductEditor Activity
                Intent intent = new Intent(ProductDetailsActivity.this, ProductEditorActivity.class);
                intent.putExtra(EXTRA_PRODUCT_ID, selectedProduct.getId());
                startActivity(intent);
                return true;

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

    // Delete the product from the database
    private void deleteProduct() {
        viewModel.deleteProduct(selectedProduct);

        Toast.makeText(this, getString(R.string.editor_delete_product_successful),
                Toast.LENGTH_SHORT).show();
        // Exit Activity
        finish();
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
                    contactSupplier();
                }
            }
        }
    }
}

