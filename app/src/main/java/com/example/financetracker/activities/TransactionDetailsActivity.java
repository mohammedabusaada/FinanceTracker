package com.example.financetracker.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AlertDialog;

import com.example.financetracker.R;
import com.example.financetracker.models.Transaction;
import com.example.financetracker.utils.TransactionManager;
import com.google.android.material.button.MaterialButton;

public class TransactionDetailsActivity extends Activity {

    TextView typeTV, categoryTV, amountTV, noteTV, dateTV;
    MaterialButton deleteBtn;

    int index;
    Transaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);

        typeTV = findViewById(R.id.typeTV_TransactionDetailsActivity);
        categoryTV = findViewById(R.id.categoryTV_TransactionDetailsActivity);
        amountTV = findViewById(R.id.amountTV_TransactionDetailsActivity);
        noteTV = findViewById(R.id.noteTV_TransactionDetailsActivity);
        dateTV = findViewById(R.id.dateTV_TransactionDetailsActivity);
        deleteBtn = findViewById(R.id.deleteBtn_TransactionDetailsActivity);

        // Get index from intent
        index = getIntent().getIntExtra("index", -1);
        transaction = TransactionManager.getTransaction(index);

        if (transaction == null) {
            Toast.makeText(this, "Transaction not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        typeTV.setText("Type: " + transaction.getType());
        categoryTV.setText("Category: " + transaction.getCategory());
        amountTV.setText("Amount: $" + transaction.getAmount());
        noteTV.setText("Note: " + transaction.getNote());
        dateTV.setText("Date: " + transaction.getDate());

        deleteBtn.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Transaction")
                    .setMessage("Are you sure you want to delete this transaction?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        TransactionManager.deleteTransaction(index, this);
                        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();

                        // Send result to previous activity
                        setResult(RESULT_OK);
                        finish();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }
}
