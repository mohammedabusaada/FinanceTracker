package com.example.financetracker.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.financetracker.R;
import com.example.financetracker.activities.TransactionDetailsActivity;
import com.example.financetracker.adapters.TransactionAdapter;
import com.example.financetracker.models.Transaction;
import com.example.financetracker.utils.DatabaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import java.util.List;
import androidx.activity.EdgeToEdge;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
public class ViewTransactionsActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_DETAILS = 1;

    private RecyclerView transactionsRv;
    private TransactionAdapter adapter;
    private List<Transaction> transactionsList;
    private DatabaseHelper dbHelper;
    private String currentUserId; // Current logged-in user's ID

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transactions);

        View mainView = findViewById(R.id.main_ViewTransactionsActivity);
        ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        } else {
            finish();
            return;
        }

        dbHelper = new DatabaseHelper(this);
        transactionsRv = findViewById(R.id.transactionsRV_ViewTransactionsActivity);
        transactionsRv.setLayoutManager(new LinearLayoutManager(this));
        transactionsList = dbHelper.getAllTransactions(currentUserId);
        adapter = new TransactionAdapter(this, transactionsList, this::onTransactionClick);
        transactionsRv.setAdapter(adapter);
    }

    /**
     * Handle item clicks from the RecyclerView.
     * Passes full transaction info to the Details screen.
     */
    private void onTransactionClick(int position) {
        Transaction selectedTransaction = transactionsList.get(position);

        Intent intent = new Intent(ViewTransactionsActivity.this, TransactionDetailsActivity.class);

        // Passing full data to display in the next screen
        intent.putExtra("type", selectedTransaction.getType());
        intent.putExtra("category", selectedTransaction.getCategory());
        intent.putExtra("amount", selectedTransaction.getAmount());
        intent.putExtra("note", selectedTransaction.getNote());
        intent.putExtra("date", selectedTransaction.getDate());

        startActivityForResult(intent, REQUEST_CODE_DETAILS);
    }


    //Refresh the list when returning from the Details Activity (After deletion).
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_DETAILS && resultCode == RESULT_OK) {
            refreshData();
        }
    }


    // Clears and reloads the list from the database for the current user.
    private void refreshData() {
        if (currentUserId != null) {
            transactionsList.clear();
            transactionsList.addAll(dbHelper.getAllTransactions(currentUserId));
            adapter.notifyDataSetChanged();
        }
    }
}