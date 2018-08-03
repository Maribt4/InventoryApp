package com.example.android.inventoryapp;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp.data.ItemContract.ItemEntry;

public class ItemCursorAdapter extends CursorAdapter {

    // constructs a new itemcursoradapter.
    public ItemCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    // Makes a new blank list item view.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    // This method binds the  data (in the current row pointed to by cursor) to the given list item layout.
    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_main);
        TextView priceTextView = (TextView) view.findViewById(R.id.list_price);

        // Find the columns of  attributes that we're interested in
        int itemIdColumnIndex = cursor.getColumnIndex(ItemEntry._ID);
        int nameColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_NAME);
        int quantityColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_QUANTITY);
        int imageColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_IMAGE);
        int priceColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_PRICE);

        // Read the attributes from the Cursor
        final int itemId = cursor.getInt(itemIdColumnIndex);
        String itemName = cursor.getString(nameColumnIndex);
        String itemQuantity = cursor.getString(quantityColumnIndex);
        String itemImage = cursor.getString(imageColumnIndex);
        String itemPrice = cursor.getString(priceColumnIndex);

        // Update the TextViews with the attributes
        nameTextView.setText(itemName);
        quantityTextView.setText(itemQuantity);
        final int currentQuantity = Integer.parseInt(itemQuantity);
        priceTextView.setText(itemPrice);

        if (itemImage != null) {
            Uri mUri = Uri.parse(itemImage);
            imageView.setImageURI(mUri);
            Log.d("Marius", "this is the URI" + mUri.toString());
        } else {
            imageView.setImageResource(R.drawable.baseline_image_black_18);
            imageView.setScaleType(ImageView.ScaleType.CENTER);
        }

        Button mSaleButton = (Button) view.findViewById(R.id.sale_button);
        mSaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentResolver resolver = view.getContext().getContentResolver();
                ContentValues values = new ContentValues();
                if (currentQuantity > 0) {
                    int quantityValue = currentQuantity;

                    values.put(ItemEntry.COLUMN_ITEM_QUANTITY, --quantityValue);

                    Uri uri = ContentUris.withAppendedId(ItemEntry.CONTENT_URI, itemId);
                    resolver.update(
                            uri,
                            values,
                            null,
                            null);
                } else {
                    Toast.makeText(context, R.string.negative_quantity, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
