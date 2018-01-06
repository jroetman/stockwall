package com.curlylogic.com.stockwall;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static org.junit.Assert.*;


import com.curlylogic.com.stockwall.database.StockQuote;
import com.curlylogic.com.stockwall.database.StockTable;
import com.curlylogic.com.stockwall.datasource.AlphaVantage;

import java.util.ArrayList;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class StockTableTest {

    StockTable st;

    @Before
    public void setup(){
        st = new StockTable(RuntimeEnvironment.application);
    }

    @Test
    public void getDbConnection() throws Exception {
        st.getDatabaseName();
        assertNotNull(st);
    }

    @Test
    public void getDatabaseName() throws Exception {
        assertEquals("STOCKWALL", st.getDatabaseName());
    }

    @Test
    public void insertRow() throws Exception {
        ContentValues cv = new ContentValues();
        cv.put(StockTable.COL_SYMBOL, "test");
        cv.put(StockTable.COL_DATA, "test");
        String[] cols = new String[1];
        cols[0] = "*";

        st.getWritableDatabase().insert("STOCKS",null,cv);
        Cursor c = st.getReadableDatabase().query("STOCKS", cols, null,null, null,null,null,"1");
        c.moveToNext();
        assertEquals("test", c.getString(0));
    }


    @Test
    public void upsertSymbol() throws Exception {
        AlphaVantage av = new AlphaVantage(RuntimeEnvironment.application);
        StockQuote sq = new StockQuote();
        sq.setSymbol("mcd");
        sq.setPrice(21.54f);
        sq.setVolume(1000);
        sq.setTimestamp("2018-01-01T00:00:00");
        av.upsertSymbol(sq);
        String[] cols = new String[1];
        cols[0] = "*";

        Cursor c = st.getReadableDatabase().query("STOCKS", cols, null,null, null,null,null,"1");
        c.moveToNext();
        assertEquals(sq.getSymbol(),c.getString(0));

    }

}