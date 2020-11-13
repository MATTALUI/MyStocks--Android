package io.mattalui.mystocks.webservices;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import io.mattalui.mystocks.models.StockData;
import io.mattalui.mystocks.models.StockSubscriptionBody;

import java.io.InputStream;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class StockServices {
    public static final String baseUrl = "http://miscapi.herokuapp.com/my-stocks/stocks/";

    public static List<StockData> getMyStockData(){
        String data = new QuickHTTP().get(baseUrl);
        return new Gson().fromJson(data, new TypeToken<List<StockData>>(){}.getType());
    }

    public static StockData getStockData(String ticker){
        String data = new QuickHTTP().get(baseUrl + ticker);
        return new Gson().fromJson(data, StockData.class);
    }

    public static StockData subscribeToStock(String ticker){
        StockSubscriptionBody body = new StockSubscriptionBody(ticker);
        String json = new Gson().toJson(body);
        String data = new QuickHTTP().post(baseUrl, json);
        return new Gson().fromJson(data, StockData.class);
    }

    public static Bitmap getImageData(String url){
        InputStream in;
        try {
             in = new java.net.URL(url).openStream();
        }catch(Exception e) {
            return null;
        }
        return BitmapFactory.decodeStream(in);
    }

    public static void unsubscribeToStock(String ticker){
        new QuickHTTP().delete(baseUrl + ticker);
    }
}
