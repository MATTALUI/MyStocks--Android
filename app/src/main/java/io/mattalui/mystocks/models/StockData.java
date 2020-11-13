package io.mattalui.mystocks.models;

import android.graphics.Bitmap;

import io.mattalui.mystocks.webservices.StockServices;

public class StockData {
    public String country;
    public String currency;
    public String exchange;
    public String finnhubIndustry;
    public String ipo;
    public String logo;
    public String marketCapitalization;
    public String name;
    public String phone;
    public String ticker;
    public String weburl;
    public float  shareOutstanding;
    public StockQuote quotes;

    public StockData() {
        ticker = "";
        quotes = new StockQuote();
    }

    public String toString(){
        String suffix = (this.name != null && !this.name.equals("")) ? (" -- " + this.name) : "";
        return this.ticker + suffix;
    }

    public String usableName(){
        return this.name == null ? "" : this.name;
    }

    public String usableCurrency(){
        return this.currency == null ? "" : this.currency;
    }

    public float dailyDelta(){
        return quotes.currentPrice - quotes.openingPrice;
    }

    public float percentChange(){
        if (quotes.openingPrice == 0) { return 0; } // Dividing by zero would destroy the world. I think.
        return (Math.abs(dailyDelta() / quotes.openingPrice)) * 100;
    }

    public String diffString() {
        String formattedChange = String.format("%.02f", dailyDelta());
        if (dailyDelta() > 0){
            formattedChange = "+" + formattedChange;
        }
        String formattedPercent = percentChange() > 0 && percentChange() < 0.005 ? ">1" : String.format("%.02f", percentChange());
        String percentString =  " (" + formattedPercent + "%)";
        return formattedChange + percentString;
    }

    public int diffContextColor() {
        float diff = dailyDelta();
        if (diff > 0.0f) {
            return 0xFF148518; // Return Green
        } else if (diff < 0.0f) {
            return 0xFFDF1010; // Return Red
        }else {
            return 0xFF000000; // Return Black
        }
    }

    public boolean hasImage(){
        return this.logo != null && !this.logo.equals("");
    }

    public Bitmap getImageData(){
        if (!hasImage()){ return null; }
        return StockServices.getImageData(this.logo);
    }

    public String formattedOpeningPrice(){
        return this.quotes.formattedOpeningPrice();
    }

    public String formattedDailyHigh(){
        return this.quotes.formattedDailyHigh();
    }

    public String formattedDailyLow(){
        return this.quotes.formattedDailyLow();
    }

    public String formattedPreviousClosingPrice(){
        return this.quotes.formattedPreviousClosingPrice();
    }

    public boolean isSubscribedTo(){
        return State.getState().isSubscribedToStock(this);
    }
}
