package com.example.financetracker.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.financetracker.R;
import com.example.financetracker.adapters.TransactionAdapter;
import com.example.financetracker.models.Transaction;
import com.example.financetracker.utils.TransactionManager;

import java.util.List;

public class ViewTransactionsActivity extends Activity {

    private static final int REQUEST_CODE_DETAILS = 1;

    private RecyclerView transactionsRv;
    private TransactionAdapter adapter;
    private List<Transaction> transactions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transactions);

        transactionsRv = findViewById(R.id.transactionsRV_ViewTransactionsActivity);
        transactionsRv.setLayoutManager(new LinearLayoutManager(this));

        transactions = TransactionManager.getAllTransactions(this);
        adapter = new TransactionAdapter(this, transactions, this::onTransactionClick);
        transactionsRv.setAdapter(adapter);
    }

    // Handle transaction click to open details screen
    private void onTransactionClick(int index) {
        Intent intent = new Intent(ViewTransactionsActivity.this, TransactionDetailsActivity.class);
        intent.putExtra("index", index);
        startActivityForResult(intent, REQUEST_CODE_DETAILS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_DETAILS && resultCode == RESULT_OK) {
            // Refresh transaction list after delete
            transactions.clear();
            transactions.addAll(TransactionManager.getAllTransactions(this));
            adapter.notifyDataSetChanged();
        }
    }
}
