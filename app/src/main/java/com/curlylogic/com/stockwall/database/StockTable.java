package com.curlylogic.com.stockwall.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.JsonReader;
import android.util.Log;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.curlylogic.com.stockwall.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

/**
 * Created by jon on 12/30/17.
 */
public class StockTable {
    private static final String TAG = "DictionaryDatabase";

    //The columns we'll include in the dictionary table
    public static final String COL_SYMBOL = "SYMBOL";
    public static final String COL_DATA = "DATA";

    private static final String DATABASE_NAME = "STOCKS";
    private static final String FTS_VIRTUAL_TABLE = "FTS";
    private static final int DATABASE_VERSION = 1;
    private static String AV_HOST_API;
    private static String AV_API_KEY;

    private final DatabaseOpenHelper mDatabaseOpenHelper;
    private static Context con;

    public StockTable(Context context) {
        mDatabaseOpenHelper = new DatabaseOpenHelper(context);
        con = context;

        Resources res = context.getResources();
        AV_API_KEY = res.getString(R.string.av_api_key);
        AV_HOST_API = res.getString(R.string.av_host_api);

    }

    private static class DatabaseOpenHelper extends SQLiteOpenHelper {

        private final Context mHelperContext;
        private SQLiteDatabase mDatabase;

        private static final String FTS_TABLE_CREATE =
                "CREATE VIRTUAL TABLE " + FTS_VIRTUAL_TABLE +
                        " USING fts3 (" +
                        COL_SYMBOL + ", " +
                        COL_DATA + ")";

        DatabaseOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            mHelperContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            mDatabase = db;
            mDatabase.execSQL(FTS_TABLE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE);
            onCreate(db);
        }

    }


    public long updateSymbol(StockQuote sq) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(COL_SYMBOL, sq.getSymbol());
        StringBuffer sb = new StringBuffer();
        sb.append(sq.getPrice()).append(",").append(sq.getVolume());
        initialValues.put(COL_DATA, sb.toString());

        return mDatabase.insert(FTS_VIRTUAL_TABLE, null, initialValues);
    }
}
