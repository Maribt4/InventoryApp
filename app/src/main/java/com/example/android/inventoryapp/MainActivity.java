package com.example.android.inventoryapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.inventoryapp.data.ItemDbHelper;
import com.example.android.inventoryapp.data.ItemContract.ItemEntry;


public class MainActivity extends AppCompatActivity {

    private ItemDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //find the view that is the button for adding dummy data
        Button dummyData = findViewById(R.id.button);

        //set a click listener on that view
        dummyData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertItem();
                readDatabaseInfo();
            }
        });

        mDbHelper = new ItemDbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        readDatabaseInfo();
    }

    /**
     * Helper method to insert hardcoded data into the database. For debugging purposes only.
     */
    private void insertItem() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ItemEntry.COLUMN_ITEM_NAME, "Mobile Phone");
        values.put(ItemEntry.COLUMN_ITEM_PRICE, 100);
        values.put(ItemEntry.COLUMN_ITEM_QUANTITY, 25);
        values.put(ItemEntry.COLUMN_ITEM_SUPPLIER_NAME, "Buy More");
        values.put(ItemEntry.COLUMN_ITEM_SUPPLIER_PHONE, "123-123-1234");

        long newRowId = db.insert(ItemEntry.TABLE_NAME, null, values);

        Log.d("Marius", "New Row ID" + newRowId);
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */
    private void readDatabaseInfo() {
        ItemDbHelper mDbHelper = new ItemDbHelper(this);

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                ItemEntry._ID,
                ItemEntry.COLUMN_ITEM_NAME,
                ItemEntry.COLUMN_ITEM_PRICE,
                ItemEntry.COLUMN_ITEM_QUANTITY
        };

        Cursor cursor = db.query(ItemEntry.TABLE_NAME, projection,
                null, null,
                null, null, null);
        TextView displayView = (TextView) findViewById(R.id.display_data);

        try {
            displayView.setText("The table contains " + cursor.getCount() + " items.\n\n");
            displayView.append(ItemEntry._ID + " - " +
                    ItemEntry.COLUMN_ITEM_NAME + " - " +
                    ItemEntry.COLUMN_ITEM_PRICE + " - " +
                    ItemEntry.COLUMN_ITEM_QUANTITY + "\n");

            int idColumnIndex = cursor.getColumnIndex(ItemEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_NAME);
            int priceColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_QUANTITY);

            while (cursor.moveToNext()) {
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                int currentPrice = cursor.getInt(priceColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                displayView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentPrice + " - " +
                        currentQuantity));
            }
        } finally {
            cursor.close();
        }
    }
}