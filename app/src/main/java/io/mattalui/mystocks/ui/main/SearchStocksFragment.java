package io.mattalui.mystocks.ui.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import io.mattalui.mystocks.R;
import io.mattalui.mystocks.models.State;
import io.mattalui.mystocks.models.StockData;
import io.mattalui.mystocks.webservices.StockServices;

public class SearchStocksFragment extends Fragment {
    ProgressBar spinner;
    EditText stockSearchInput;
    TextView tickerText;
    TextView nameText;
    TextView currentCostText;
    TextView currencyText;
    TextView diffText;
    TextView openingPriceText;
    TextView previousClosingPriceText;
    TextView dailyHighText;
    TextView dailyLowText;
    ImageView logoImage;
    Button stockSearchButton;
    Button subscriptionButton;
    StockData stockData;
    Bitmap imageData;
    ConstraintLayout stockDataContainer;

    public static SearchStocksFragment newInstance() {
        SearchStocksFragment fragment = new SearchStocksFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_stocks_main, container, false);
        spinner = (ProgressBar) view.findViewById(R.id.loader);
        stockSearchInput = (EditText) view.findViewById(R.id.stockSearchInput);
        stockSearchButton = (Button) view.findViewById(R.id.stockSearchButton);
        subscriptionButton = (Button) view.findViewById(R.id.subscriptionButton);
        stockDataContainer = (ConstraintLayout) view.findViewById(R.id.stockDataContainer);
        tickerText = (TextView) view.findViewById(R.id.tickerText);
        nameText = (TextView) view.findViewById(R.id.nameText);
        currentCostText = (TextView) view.findViewById(R.id.currentCostText);
        currencyText = (TextView) view.findViewById(R.id.currencyText);
        diffText = (TextView) view.findViewById(R.id.diffText);
        logoImage = (ImageView) view.findViewById(R.id.logoImage);
        openingPriceText = (TextView) view.findViewById(R.id.openingPriceText);
        previousClosingPriceText = (TextView) view.findViewById(R.id.previousClosingPriceText);
        dailyHighText = (TextView) view.findViewById(R.id.dailyHighText);
        dailyLowText = (TextView) view.findViewById(R.id.dailyLowText);

        imageData = null;
        final SearchStocksFragment that = this;

        stockSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                that.searchStocks();
            }
        });

        subscriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                that.toggleStockSubscription();
            }
        });

        return view;
    }

    public void hideSpinner(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                spinner.setVisibility(View.GONE);
            }
        });
    }

    public void showSpinner(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                spinner.setVisibility(View.VISIBLE);
            }
        });
    }

    public void hideStockData() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                stockDataContainer.setVisibility(View.GONE);
            }
        });
    }

    public void displayStockData() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tickerText.setText(stockData.ticker);
                nameText.setText(stockData.usableName());
                currentCostText.setText(String.valueOf(stockData.quotes.currentPrice));
                currencyText.setText(stockData.usableCurrency());
                diffText.setText(stockData.diffString());
                openingPriceText.setText(stockData.formattedOpeningPrice());
                previousClosingPriceText.setText(stockData.formattedPreviousClosingPrice());
                dailyHighText.setText(stockData.formattedDailyHigh());
                dailyLowText.setText(stockData.formattedDailyLow());
                diffText.setTextColor(stockData.diffContextColor());
                if(imageData != null){
                    logoImage.setVisibility(View.VISIBLE);
                    logoImage.setImageBitmap(imageData);
                } else {
                    logoImage.setVisibility(View.GONE);
                }
                if(stockData.isSubscribedTo()){
                    subscriptionButton.setText("UNSUBSCRIBE TO THIS STOCK");
                }else {
                    subscriptionButton.setText("SUBSCRIBE TO THIS STOCK");
                }

                stockDataContainer.setVisibility(View.VISIBLE);
            }
        });
    }

    public void toggleStockSubscription(){
        if(stockData.isSubscribedTo()){
            unsubscribeToStock();
        }else{
            subscribeToStock();
        }
    }

    public void subscribeToStock(){
        new Thread(new Runnable(){
            @Override
            public void run(){
                try{
                    StockServices.subscribeToStock(stockData.ticker);
                    State.getState().subscribeStockData(stockData);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            subscriptionButton.setText("UNSUBSCRIBE TO THIS STOCK");
                        }
                    });
                } catch (Exception e) {
                }
            }
        }).start();
    }

    public void unsubscribeToStock(){
        new Thread(new Runnable(){
            @Override
            public void run(){
                try{
                    StockServices.unsubscribeToStock(stockData.ticker);
                    State.getState().unsubscribeStockData(stockData);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            subscriptionButton.setText("SUBSCRIBE TO THIS STOCK");
                        }
                    });
                } catch (Exception e) {
                }
            }
        }).start();
    }

    public void searchStocks(){
        final SearchStocksFragment that = this;
        showSpinner();
        hideStockData();
        new Thread(new Runnable(){
            @Override
            public void run(){
                try{
                    stockData = StockServices.getStockData(stockSearchInput.getText().toString());
                    imageData = stockData.getImageData();
//                    Thread.sleep(7000);

                    hideSpinner();
                    displayStockData();
                } catch (Exception e) {
                    hideSpinner();
                }
            }
        }).start();
    }
}
