package com.curlylogic.com.stockwall;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import com.curlylogic.com.stockwall.database.StockQuote;
import com.curlylogic.com.stockwall.database.StockTable;
import com.curlylogic.com.stockwall.datasource.AlphaVantage;

@RunWith(MockitoJUnitRunner.class)
public class StockTableTest {

    @Mock
    Context mMockContext;

    @Before
    public void setup(){
        when(mMockContext.getString(R.string.av_api_key)).thenReturn("");
        when(mMockContext.getString(R.string.av_host_api)).thenReturn("");

    }
    @Test
    public void getData() throws Exception {

    }

    @Test
    public void upsertSymbol() throws Exception {
        AlphaVantage av = new AlphaVantage(mMockContext);
        StockQuote sq = new StockQuote();
        sq.setSymbol("mcd");
        sq.setPrice(21.54f);
        sq.setVolume(1000);
        sq.setTimestamp("2018-01-01T00:00:00");

        av.upsertSymbol(sq);
    }
}