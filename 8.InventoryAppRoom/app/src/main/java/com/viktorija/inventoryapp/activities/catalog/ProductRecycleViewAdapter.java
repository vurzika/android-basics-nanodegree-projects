package com.viktorija.inventoryapp.activities.catalog;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.viktorija.inventoryapp.R;
import com.viktorija.inventoryapp.activities.details.ProductDetailsActivity;
import com.viktorija.inventoryapp.database.ProductEntity;
import com.viktorija.inventoryapp.utilities.ProductUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.viktorija.inventoryapp.activities.details.ProductDetailsActivity.EXTRA_PRODUCT_ID;

public class ProductRecycleViewAdapter extends RecyclerView.Adapter<ProductRecycleViewAdapter.ViewHolder> {

    private final List<ProductEntity> productList;

    private Context context;

    private CatalogActivityViewModel viewModel;

    // constructor
    public ProductRecycleViewAdapter(Context context, List<ProductEntity> productList, CatalogActivityViewModel viewModel) {
        this.context = context;
        this.productList = productList;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        // Create a new view holder for a view in a list item
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.product_list_item, parent, false);

        return new ViewHolder(itemView);
    }

    // Called every time when recycle view wants to fill data in list item
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ProductEntity productEntity = productList.get(position);

        holder.productNameTextView.setText(productEntity.getProductName());
        holder.productPriceTextView.setText(ProductUtils.formatPrice(productEntity.getProductPrice()));
        holder.productQuantityTextView.setText(context.getString(R.string.list_item_available_quantity, productEntity.getProductQuantity()));

        byte[] productImageData = productEntity.getProductImage();
        // Display product image or placeholder if we don't have actual image
        if (productImageData != null) {
            holder.productImageView.setImageBitmap(ProductUtils.blobToBitmap(productImageData));
        } else {
            holder.productImageView.setImageResource(R.drawable.product_image_placeholder);
        }

        // Disable Sale button if product quantity is 0
        if (productEntity.getProductQuantity() == 0) {
            holder.saleButton.setEnabled(false);
        } else {
            holder.saleButton.setEnabled(true);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    /**
     * Class for storing references for UI elements in list item
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

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

            // Navigate to details activity when row is clicked
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();

                ProductEntity productEntity = productList.get(position);

                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra(EXTRA_PRODUCT_ID, productEntity.getId());
                context.startActivity(intent);
            });

            // Decrease product quantity when Sale button is pressed
            saleButton.setOnClickListener(view -> {
                int position = getAdapterPosition();
                ProductEntity productEntity = productList.get(position);
                productEntity.setProductQuantity(productEntity.getProductQuantity() - 1);
                viewModel.updateProduct(productEntity);
            });
        }
    }
}
