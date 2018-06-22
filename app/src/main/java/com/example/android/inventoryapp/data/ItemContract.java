package com.example.android.inventoryapp.data;

import android.provider.BaseColumns;

/**
 * API Contract for the Inventory app.
 */
public final class ItemContract {

    /**
     * Empty constructor to prevent from instantiating the contract class.
     */
    private ItemContract() {}

    public static final class ItemEntry implements BaseColumns {

        /**
         * Name of database table for items
         */
        public final static String TABLE_NAME = "items";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_ITEM_NAME = "product_name";
        public final static String COLUMN_ITEM_PRICE = "product_price";
        public final static String COLUMN_ITEM_QUANTITY = "product_quantity";
        public final static String COLUMN_ITEM_SUPPLIER_NAME = "supplier_name";
        public final static String COLUMN_ITEM_SUPPLIER_PHONE = "supplier_phone";
        public final static String COLUMN_ITEM_IMAGE = "product_image";

    }

}