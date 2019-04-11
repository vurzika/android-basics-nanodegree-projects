package com.viktorija.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.viktorija.inventoryapp.data.ProductContract;
import com.viktorija.inventoryapp.data.ProductUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Recycle view adapter to display list items in catalog activity
 * following example in https://shellmonger.com/2017/06/28/android-notes-app-content-providers/
 */
public class ProductRecycleViewAdapter extends RecyclerView.Adapter<ProductRecycleViewAdapter.ViewHolder> {

    /**
     * Cursor to products data
     */
    private Cursor mCursor;

    /**
     * Application context for loading resources
     */
    private Context mContext;

    /**
     * Creates new recycle view adapter and stores context and current cursor
     *
     * @param context application context for loading resources
     * @param cursor cursor to products data
     */
    public ProductRecycleViewAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view holder for a view in a list item
        View listItem = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.product_list_item, parent, false);

        return new ViewHolder(listItem);
    }

    // Called every time when recycle view wants to fill data in list item
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Move cursor to provided position to retrieve data
        mCursor.moveToPosition(position);

        // Find the columns of product attributes that we're interested in
        int idColumnIndex = mCursor.getColumnIndex(ProductContract.ProductEntry._ID);
        int nameColumnIndex = mCursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = mCursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE);
        int quantityColumnIndex = mCursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY);
        int imageColumnIndex = mCursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_IMAGE);

        // Read the product attributes from the Cursor for the current product
        Long productId = mCursor.getLong(idColumnIndex);
        String productName = mCursor.getString(nameColumnIndex);
        Double productPrice = mCursor.getDouble(priceColumnIndex);
        final Integer productQuantity = mCursor.getInt(quantityColumnIndex);
        byte[] productImageData = mCursor.getBlob(imageColumnIndex);

        // Store product uri and quantity to use when sales button is clicked within view holder
        final Uri currentProductUri = ContentUris.withAppendedId(ProductContract.ProductEntry.CONTENT_URI, productId);

        // Update view holder ui elements

        // Display product image or placeholder if we don't have actual image
        if (productImageData != null) {
            holder.productImageView.setImageBitmap(ProductUtils.blobToBitmap(productImageData));
        } else {
            holder.productImageView.setImageResource(R.drawable.product_image_placeholder);
        }

        // Update the TextViews with the attributes for the current product
        holder.productNameTextView.setText(productName);
        holder.productPriceTextView.setText(ProductUtils.formatPrice(productPrice));
        holder.productQuantityTextView.setText(mContext.getString(R.string.list_item_available_quantity, productQuantity));

        // Disable Sale button if product quantity is 0
        if (productQuantity == 0) {
            holder.saleButton.setEnabled(false);
        } else {
            holder.saleButton.setEnabled(true);
        }

        holder.saleButton.setOnClickListener(view -> {
            // Decrease product quantity by 1
            ContentValues values = new ContentValues();
            values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, productQuantity-1);

            // Update data
             view.getContext().getContentResolver().update(currentProductUri, values, null, null);
        });

        // Navigate to details activity when row is clicked
        holder.itemView.setOnClickListener(view -> {
            // Create new intent to go to {@link ProductDetailsActivity}
            Intent intent = new Intent(mContext, ProductDetailsActivity.class);

            // Set the URI on the data field of the intent
            intent.setData(currentProductUri);

            // Launch the {@link ProductDetailsActivity} to display the data for the current product.
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return (mCursor == null) ? 0 : mCursor.getCount();
    }

    /**
     * Swaps cursor whenever new data set is received and notifies data change
     *
     * @param cursor new cursor
     * @return old cursor
     */
    public Cursor swapCursor(Cursor cursor) {
        if (mCursor == cursor) {
            return null;
        }

        Cursor oldCursor = mCursor;
        this.mCursor = cursor;

        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }

    /**
     * Class for storing references for UI elements in list item
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.product_thumbnail)
        ImageView productImageView;

        @BindView(R.id.product_name)
        TextView productNameTextView;

        @BindView(R.id.product_price)
        TextView productPriceTextView;

        @BindView(R.id.product_quantity)
        TextView productQuantityTextView;

        @BindView(R.id.product_sale_button)
        Button saleButton;


        public ViewHolder(View listItem) {
            super(listItem);

            ButterKnife.bind(this, listItem);
        }
    }
}
