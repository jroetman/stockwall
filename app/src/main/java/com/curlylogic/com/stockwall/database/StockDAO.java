package com.curlylogic.com.stockwall.database;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jon on 1/5/18.
 */

public class StockDAO {

    private StockTable st;

    public StockDAO(StockTable stockTable) {
        st = stockTable;
    }

    public List<StockQuote> getQuotes(){
        List<StockQuote> result = new ArrayList<StockQuote>();

        String[] cols = new String[1];
        cols[0] = "*";
        StockQuote sq;
        Cursor c = st.getReadableDatabase().query("STOCKS", cols, null,null, null,null,null,"1");
        while(c.moveToNext()) {
            if(!c.isNull(0)){
                sq = new StockQuote();
                sq.setSymbol(c.getString(0));
                sq.setTimestamp(c.getString(1));
                result.add(sq);
            }

        }

        return result;
    }
}
