package com.curlylogic.com.stockwall;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.JsonReader;

import com.android.volley.BuildConfig;
import com.android.volley.Cache;
import com.android.volley.toolbox.StringRequest;
import com.curlylogic.com.stockwall.database.StockQuote;
import com.curlylogic.com.stockwall.database.StockTable;
import com.curlylogic.com.stockwall.datasource.AlphaVantage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class AlphaVantagetTest {

    StockTable st;
    final ExecutorService ex = Executors.newSingleThreadExecutor();


    @Before
    public void setup(){
        st = new StockTable(RuntimeEnvironment.application);
    }


    @Test
    public void readQuote() throws Exception {
        AlphaVantage av = new AlphaVantage(RuntimeEnvironment.application);

        Reader sr = new StringReader("{\"1. symbol\" : \"mcd\"}");
        JsonReader jr  = new JsonReader(sr);
        StockQuote sq = av.readQuote(jr);
        assertEquals("mcd", sq.getSymbol());

    }



}