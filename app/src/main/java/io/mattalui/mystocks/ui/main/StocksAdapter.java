package io.mattalui.mystocks.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import io.mattalui.mystocks.models.StockData;
import io.mattalui.mystocks.R;

public class StocksAdapter extends ArrayAdapter<StockData> {
    private List<StockData> stocks;
    private Context context;

    public StocksAdapter(List<StockData> data, Context _context) {
        super(_context, R.layout.stock_list_item, data);
        stocks = data;
        context = _context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflator;
            inflator = LayoutInflater.from(context);
            view = inflator.inflate(R.layout.stock_list_item, null);
        }

        StockData stockData = getItem(position);

        if (stockData != null){
            TextView tickerText = view.findViewById(R.id.tickerText2);
            TextView nameText = view.findViewById(R.id.nameText2);
            TextView currentCostText = view.findViewById(R.id.currentCostText2);
            TextView currencyText = view.findViewById(R.id.currencyText2);
            TextView diffText = view.findViewById(R.id.diffText2);

            tickerText.setText(stockData.ticker);
            nameText.setText(stockData.name);
            currentCostText.setText(String.valueOf(stockData.quotes.currentPrice));
            currencyText.setText(stockData.usableCurrency());
            diffText.setText(stockData.diffString());
            diffText.setTextColor(stockData.diffContextColor());
        }

        return view;
    }
//
//    @Override
//    public void onClick(View view) {
//        Intent intent = new Intent(getContext(), ViewVehicleLogs.class);
//        intent.putExtra(getContext().getString(R.string.vehicle_id), view.getTag(R.string.vehicle_id).toString());
//        getContext().startActivity(intent);
//    }
}
