package com.curlylogic.com.stockwall.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.JsonReader;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.curlylogic.com.stockwall.database.StockQuote;
import com.curlylogic.com.stockwall.database.StockTable;
import com.curlylogic.stockwall.R;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jon on 12/31/17.
 */
public class AlphaVantage {
    private StockTable dbHelper;
    public RequestQueue queue;
    private static String AV_HOST_API;
    private static String AV_API_KEY;

    public AlphaVantage(Context context){
        dbHelper = new StockTable(context);
        queue    = Volley.newRequestQueue(context);

        AV_API_KEY =context.getString(R.string.av_api_key);
        AV_HOST_API =context.getString(R.string.av_host_api);
    }

    public StockQuote readQuote(JsonReader reader){
        StockQuote sq = new StockQuote();
        try {
            reader.beginArray();
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("1. symbol")) {
                    sq.setSymbol(reader.nextString());

                } else if(name.equals("2. price")) {
                    sq.setPrice(Float.parseFloat(reader.nextString()));

                } else if(name.equals("3. volume")) {
                    sq.setVolume(Double.parseDouble(reader.nextString()));

                } else if(name.equals("4. timestamp")) {
                    sq.setTimestamp(reader.nextString());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return sq;
    }

    public void getData(String[] symbols) {
        String symbolString = android.text.TextUtils.join(",", symbols);
        String query = "query?function=BATCH_STOCK_QUOTES&symbols=" + symbolString + "&apikey="+ AV_API_KEY;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AV_HOST_API + query,
                new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    JsonReader reader = new JsonReader(new StringReader(response));
                    try {
                        reader.beginObject();

                        while (reader.hasNext()) {
                            String name = reader.nextName();
                            if (!name.equals("Stock Quotes")) {
                                reader.skipValue();
                              //  reader.endObject();

                            } else {
                                StockQuote sq = readQuote(reader);
                                upsertSymbol(sq);
                            }


                        }
                        reader.endObject();


                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
               System.out.println(error);
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    public long upsertSymbol(StockQuote sq) {
        System.out.println(sq.toString());
        ContentValues initialValues = new ContentValues();
        initialValues.put(StockTable.COL_SYMBOL, sq.getSymbol());
        StringBuffer sb = new StringBuffer();
        sb.append(sq.getPrice()).append(",").append(sq.getVolume());
        initialValues.put(StockTable.COL_DATA, sb.toString());

        return dbHelper.getReadableDatabase().insert("STOCKS", null, initialValues);
    }
}
