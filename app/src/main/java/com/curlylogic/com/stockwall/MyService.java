package com.curlylogic.com.stockwall;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.curlylogic.com.stockwall.database.StockDAO;
import com.curlylogic.com.stockwall.database.StockQuote;
import com.curlylogic.com.stockwall.database.StockTable;
import com.curlylogic.com.stockwall.datasource.AlphaVantage;
import com.curlylogic.stockwall.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MyService extends WallpaperService {


    public MyService(){



    }

    @Override
    public Engine onCreateEngine() {
        return new MyWallpaperEngine();
    }

    private class MyWallpaperEngine extends Engine {
        private final Handler handler = new Handler();
        int height;
        private Paint paint = new Paint();
        private int width;
        private boolean visible = true;
        private float textSize = 10f;
        private StockTable st = new StockTable(getApplicationContext());
        private List<StockQuote> currentQuotes = new ArrayList<StockQuote>();
        private StockDAO sDAO = new StockDAO(st);


        private final Runnable drawRunner = new Runnable() {
            @Override
            public void run() {
                draw();
            }

        };

        public MyWallpaperEngine() {
            AlphaVantage av = new AlphaVantage(getApplicationContext());

            Resources res = getResources();
            String[] stocks = res.getStringArray(R.array.stocklistValues);
            av.getData(stocks);

            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);

            //TODO, add this to a fixed schedule
             currentQuotes = sDAO.getQuotes();

            handler.post(drawRunner);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            this.visible = visible;
            if (visible) {
                handler.post(drawRunner);
            } else {
                handler.removeCallbacks(drawRunner);
            }
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            this.visible = false;
            handler.removeCallbacks(drawRunner);
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format,
                                     int width, int height) {
            this.width = width;
            this.height = height;
            super.onSurfaceChanged(holder, format, width, height);
        }

        @Override
        public void onTouchEvent(MotionEvent event) {

        }


        private void draw() {
            SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = null;
            try {
                canvas = holder.lockCanvas();

                if (canvas != null) {
                    canvas.drawColor(Color.WHITE);
                    paint.setTextSize(20);

                    for(int i = 0; i < currentQuotes.size(); i++) {
                        canvas.drawText(currentQuotes.toString(), 100, 200 + (i * textSize), paint);
                    }
                }
            } finally {
                if (canvas != null)
                    holder.unlockCanvasAndPost(canvas);
            }
            handler.removeCallbacks(drawRunner);
            if (visible) {
                handler.postDelayed(drawRunner, 5000);
            }
        }
    }
}