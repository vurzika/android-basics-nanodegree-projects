package com.viktorija.inventoryapp.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.util.Currency;

/**
 * Set of helper methods for formatting product fields
 * that we use across all activities
 */
public class ProductUtils {

    /**
     * Formats double value as money for displaying
     *
     * @param price price
     * @return formatted price (in USD currency)
     */
    public static String formatPrice(Double price) {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setCurrency(Currency.getInstance("USD"));
        return format.format(price);
    }

    /**
     * Converts image bitmap to byte array for storing it in ProductDatabase
     *
     * @param bitmap image bitmap
     * @return byte array representing image
     */
    public static byte[] bitmapToBlob(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    /**
     * Converts array of bytes (blob) to Bitmap to display data retrieved from ProductDatabase
     *
     * @param imageData data retrieved from ProductDatabase
     * @return image bitmap
     */
    public static Bitmap blobToBitmap(byte[] imageData) {
        if (imageData == null) {
            return null;
        }
        return BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
    }

    /**
     * Allows reading image from resources as bytes
     *
     * @param context         context to get resources
     * @param imageResourceId image resource id
     * @return image resource as bytes[]
     */
    public static byte[] imageAsBytes(Context context, int imageResourceId) {
        return bitmapToBlob(BitmapFactory.decodeResource(context.getResources(), imageResourceId));
    }
}