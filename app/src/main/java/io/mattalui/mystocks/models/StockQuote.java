package io.mattalui.mystocks.models;

public class StockQuote {
    public float openingPrice;
    public float dailyHigh;
    public float dailyLow;
    public float currentPrice;
    public float previousClosingPrice;

    public StockQuote() {
        openingPrice = 0.0f;
        dailyHigh = 0.0f;
        dailyLow = 0.0f;
        currentPrice = 0.0f;
        previousClosingPrice = 0.0f;
    }

    public String formattedOpeningPrice(){
        return String.format("$%.02f", this.openingPrice);
    }

    public String formattedDailyHigh(){
        return String.format("$%.02f", this.dailyHigh);
    }

    public String formattedDailyLow(){
        return String.format("$%.02f", this.dailyLow);
    }

    public String formattedPreviousClosingPrice(){
        return String.format("$%.02f", this.previousClosingPrice);
    }
}
