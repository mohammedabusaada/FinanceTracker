package com.example.financetracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.financetracker.R;
import com.example.financetracker.models.Transaction;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private Context context;
    private List<Transaction> transactions;
    private OnTransactionClickListener listener;

    public interface OnTransactionClickListener {
        void onClick(int index);
    }

    public TransactionAdapter(Context context, List<Transaction> transactions, OnTransactionClickListener listener) {
        this.context = context;
        this.transactions = transactions;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TransactionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionAdapter.ViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        holder.typeTV.setText(transaction.getType());
        holder.amountTV.setText(String.format("$%.2f", transaction.getAmount()));
        holder.dateTV.setText(transaction.getDate());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView typeTV, amountTV, dateTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            typeTV = itemView.findViewById(R.id.typeTV_TransactionItem);
            amountTV = itemView.findViewById(R.id.amountTV_TransactionItem);
            dateTV = itemView.findViewById(R.id.dateTV_TransactionItem);
        }
    }
}
