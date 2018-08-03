package com.example.android.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.inventoryapp.data.ItemContract.ItemEntry;


    // Display list of items stored in the database.
    public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ITEM_LOADER = 0;

    ItemCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // Find the listview which will populate the data
        ListView itemListView = (ListView) findViewById(R.id.list);

        // find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        itemListView.setEmptyView(emptyView);

        // setup an Adapter to create a list item for each row of data in the Cursor.
        mCursorAdapter = new ItemCursorAdapter(this, null);
        itemListView.setAdapter(mCursorAdapter);

        // Setup item click listener
        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this, EditorActivity.class);

                Uri currentItemUri = ContentUris.withAppendedId(ItemEntry.CONTENT_URI, id);

                intent.setData(currentItemUri);

                startActivity(intent);
            }
        });

        // Kick off the loader
        getLoaderManager().initLoader(ITEM_LOADER, null, this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    // Helper method to populate the database with sample dummy data for demo purposes.
    private void insertItem() {

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ItemEntry.COLUMN_ITEM_NAME, "Samsung Galaxy Book W620");
        values.put(ItemEntry.COLUMN_ITEM_PRICE, 499.99);
        values.put(ItemEntry.COLUMN_ITEM_QUANTITY, 10);
        values.put(ItemEntry.COLUMN_ITEM_SUPPLIER_NAME, "Samsung Store");
        values.put(ItemEntry.COLUMN_ITEM_SUPPLIER_PHONE, "123-123-1234");
        values.put(ItemEntry.COLUMN_ITEM_IMAGE, "android.resource://com.example.android.inventoryapp/drawable/laptop2");
        Uri newUri = getContentResolver().insert(ItemEntry.CONTENT_URI, values);

        ContentValues values2 = new ContentValues();
        values2.put(ItemEntry.COLUMN_ITEM_NAME, "Samsung NX1000");
        values2.put(ItemEntry.COLUMN_ITEM_PRICE, 249.99);
        values2.put(ItemEntry.COLUMN_ITEM_QUANTITY, 25);
        values2.put(ItemEntry.COLUMN_ITEM_SUPPLIER_NAME, "Buy More");
        values2.put(ItemEntry.COLUMN_ITEM_SUPPLIER_PHONE, "770-123-1234");
        values2.put(ItemEntry.COLUMN_ITEM_IMAGE, "android.resource://com.example.android.inventoryapp/drawable/camera");
        Uri newUri2 = getContentResolver().insert(ItemEntry.CONTENT_URI, values2);

        ContentValues values3 = new ContentValues();
        values3.put(ItemEntry.COLUMN_ITEM_NAME, "Apple Iphone 8");
        values3.put(ItemEntry.COLUMN_ITEM_PRICE, 799.99);
        values3.put(ItemEntry.COLUMN_ITEM_QUANTITY, 1);
        values3.put(ItemEntry.COLUMN_ITEM_SUPPLIER_NAME, "Apple Online");
        values3.put(ItemEntry.COLUMN_ITEM_SUPPLIER_PHONE, "312-123-1234");
        values3.put(ItemEntry.COLUMN_ITEM_IMAGE, "android.resource://com.example.android.inventoryapp/drawable/iphone");
        Uri newUri3 = getContentResolver().insert(ItemEntry.CONTENT_URI, values3);

        ContentValues values4 = new ContentValues();
        values4.put(ItemEntry.COLUMN_ITEM_NAME, "Samsung Galaxy S7");
        values4.put(ItemEntry.COLUMN_ITEM_PRICE, 599.99);
        values4.put(ItemEntry.COLUMN_ITEM_QUANTITY, 30);
        values4.put(ItemEntry.COLUMN_ITEM_SUPPLIER_NAME, "Apple Online");
        values4.put(ItemEntry.COLUMN_ITEM_SUPPLIER_PHONE, "312-123-1234");
        values4.put(ItemEntry.COLUMN_ITEM_IMAGE, "android.resource://com.example.android.inventoryapp/drawable/samsung");
        Uri newUri4 = getContentResolver().insert(ItemEntry.CONTENT_URI, values4);

        ContentValues values5 = new ContentValues();
        values5.put(ItemEntry.COLUMN_ITEM_NAME, "Sony LED-TV XD-100");
        values5.put(ItemEntry.COLUMN_ITEM_PRICE, 700);
        values5.put(ItemEntry.COLUMN_ITEM_QUANTITY, 15);
        values5.put(ItemEntry.COLUMN_ITEM_SUPPLIER_NAME, "Mall of America");
        values5.put(ItemEntry.COLUMN_ITEM_SUPPLIER_PHONE, "312-123-1234");
        Uri newUri5 = getContentResolver().insert(ItemEntry.CONTENT_URI, values5);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertItem();

                return true;
            // Respond to a click on the "Delete all entries" menu option

            case R.id.action_delete_all_entries:
                showDeleteConfirmationDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                ItemEntry._ID,
                ItemEntry.COLUMN_ITEM_NAME,
                ItemEntry.COLUMN_ITEM_PRICE,
                ItemEntry.COLUMN_ITEM_QUANTITY,
                ItemEntry.COLUMN_ITEM_IMAGE};

        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(this,
                ItemEntry.CONTENT_URI,
                projection,
                null, null, null);
        }

        @Override
        public void onLoadFinished (Loader < Cursor > loader, Cursor data){
            // Update Adapter with this new cursor containing updated data.
            mCursorAdapter.swapCursor(data);

        }

        @Override
        public void onLoaderReset (Loader < Cursor > loader) {
            mCursorAdapter.swapCursor(null);
        }

        // Helpermethod to delete all items.
        private void deleteAllItems () {
            int rowsDeleted = getContentResolver().delete(ItemEntry.CONTENT_URI, null, null);
            Log.v("MainActivity", rowsDeleted + " rows deleted from database");
        }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_all_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteAllItems();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }



}