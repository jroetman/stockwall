package com.curlylogic.com.stockwall.datasource;

import android.util.JsonReader;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.curlylogic.com.stockwall.database.StockQuote;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by jon on 12/31/17.
 */
public class AlphaVantage {

    public StockQuote readQuote(JsonReader reader){
        StockQuote sq = new StockQuote();
        try {
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

    public void getData(StockTable::) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(con);
        String query = "query?function=BATCH_STOCK_QUOTES&symbols=MCD,TWTR&apikey="+ AV_API_KEY;

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
                                if (name.equals("Stock Quotes")) {
                                    StockQuote sq = readQuote(reader);
                                    updateSymbol(sq);
                                }
                            }
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
