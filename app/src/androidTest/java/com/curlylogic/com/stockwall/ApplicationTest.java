package com.curlylogic.com.stockwall;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import com.curlylogic.com.stockwall.datasource.AlphaVantage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class ApplicationTest {
    private Context ctx;
    public static final String TEST_STRING = "This is a string";
    public static final long TEST_LONG = 12345678L;

    public ApplicationTest(){
        ctx = InstrumentationRegistry.getTargetContext();
    }

    @Before
    public void createLogHistory() {

    }


    @Test
    public void testGetData() throws Exception {
        AlphaVantage av = new AlphaVantage(this.ctx);
        String[] symbols = new String[1];
        symbols[0] = "mcd";
        av.getData(symbols);


    }

}
