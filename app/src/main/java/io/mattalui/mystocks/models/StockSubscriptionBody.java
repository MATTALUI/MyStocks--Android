package io.mattalui.mystocks.models;

public class StockSubscriptionBody {
    public String stockId;

    public StockSubscriptionBody(String ticker){
        stockId = ticker;
    }
}
