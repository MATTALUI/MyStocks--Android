package io.mattalui.mystocks.models;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class State {
    private static State instance = null;

    private HashMap<String, StockData> subscribedStocks;
    private PropertyChangeSupport support;

    private State() {
        support = new PropertyChangeSupport(this);
        subscribedStocks = new HashMap<String, StockData>();
    }

    public static State getState() {
        if (instance == null) {
            instance = new State();
        }
        return instance;
    }

    public HashMap<String, StockData> getSubscribedStocks(){
        return subscribedStocks;
    }

    public List<StockData> getSubscribedStocksArray(){
        List<StockData> subscribedStocksArray = new ArrayList<StockData>();
        for (StockData stockData : subscribedStocks.values()){
            subscribedStocksArray.add(stockData);
        }
        return subscribedStocksArray;
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        System.out.println("Event Listener added");
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    public void subscribeMultipleStockData(List<StockData> stocksData) {
        for (StockData stockData : stocksData){
            this.subscribedStocks.put(stockData.ticker, stockData);
        }
        System.out.println("Firing change!");
        support.firePropertyChange("subscribedStocks", "Meow", subscribedStocks);
    }

    public void subscribeStockData(StockData stockData){
        this.subscribedStocks.put(stockData.ticker, stockData);
        support.firePropertyChange("subscribedStocks", "Meow", subscribedStocks);
    }

    public void unsubscribeStockData(StockData stockData){
        this.subscribedStocks.remove(stockData.ticker);
        support.firePropertyChange("subscribedStocks", "Meow", subscribedStocks);
    }

    public boolean isSubscribedToStock(StockData stockData){
        return this.subscribedStocks.containsKey(stockData.ticker);
    }
}
