package io.mattalui.mystocks.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import io.mattalui.mystocks.R;
import io.mattalui.mystocks.models.State;
import io.mattalui.mystocks.models.StockData;
import io.mattalui.mystocks.webservices.StockServices;

public class SubscribedStocksFragment extends Fragment implements PropertyChangeListener {
    ListView subscribedStocksContainer;

    public static SubscribedStocksFragment newInstance() {
        SubscribedStocksFragment fragment = new SubscribedStocksFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        State.getState().addPropertyChangeListener(this);
        fetchSubscribedStocks();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.subscribed_stocks_main, container, false);

        subscribedStocksContainer = (ListView) view.findViewById(R.id.subscribedStocksContainer);

        return view;
    }

    public void fetchSubscribedStocks() {
        new Thread(new Runnable(){
            @Override
            public void run(){
                try {
                    List<StockData> stocksData = StockServices.getMyStockData();
                    State.getState().subscribeMultipleStockData(stocksData);
                } catch (Exception e) {
                }
            }
        }).start();
    }

    public void propertyChange(PropertyChangeEvent evt) {
        buildContentFromState();
    }

    protected void buildContentFromState() {
        List<StockData> subscribedStocks = State.getState().getSubscribedStocksArray();
        final StocksAdapter adapter = new StocksAdapter(subscribedStocks, getActivity());
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                subscribedStocksContainer.setAdapter(adapter);
            }
        });
    }
}
