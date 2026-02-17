package com.example.financetracker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.financetracker.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder> {

    private List<String> currencyKeys;
    private Map<String, Double> currencyMap;

    public CurrencyAdapter(Map<String, Double> currencyMap) {
        this.currencyMap = currencyMap;
        // Convert Map keys to a list so we can access them by position (index)
        this.currencyKeys = new ArrayList<>(currencyMap.keySet());
    }

    @NonNull
    @Override
    public CurrencyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_currency, parent, false);
        return new CurrencyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyViewHolder holder, int position) {
        String key = currencyKeys.get(position);
        Double value = currencyMap.get(key);

        holder.nameTV.setText(key);
        holder.valueTV.setText(String.format("%.2f", value));
    }

    @Override
    public int getItemCount() {
        return currencyKeys.size();
    }

    static class CurrencyViewHolder extends RecyclerView.ViewHolder {
        TextView nameTV, valueTV;
        public CurrencyViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.currencyNameTV);
            valueTV = itemView.findViewById(R.id.currencyValueTV);
        }
    }
}