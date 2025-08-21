package com.example.financetracker.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.example.financetracker.R;
import com.example.financetracker.models.Transaction;
import com.example.financetracker.utils.TransactionManager;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddTransactionActivity extends Activity {

    EditText amountEdT, noteEdT;
    Spinner typeSp, categorySp;
    MaterialButton saveBtn;

    String[] typeOptions = {"Income", "Expense"};
    String[] categoryOptions = {"Food", "Transport", "Bills", "Shopping", "Other"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        amountEdT = findViewById(R.id.amountEdT_AddTransactionActivity);
        noteEdT = findViewById(R.id.noteEdT_AddTransactionActivity);
        typeSp = findViewById(R.id.typeSpinner_AddTransactionActivity);
        categorySp = findViewById(R.id.categorySpinner_AddTransactionActivity);
        saveBtn = findViewById(R.id.saveBtn_AddTransactionActivity);

        // Setup spinners
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, typeOptions);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSp.setAdapter(typeAdapter);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryOptions);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySp.setAdapter(categoryAdapter);

        saveBtn.setOnClickListener(v -> {
            String amountStr = amountEdT.getText().toString().trim();
            String note = noteEdT.getText().toString().trim();
            String type = typeSp.getSelectedItem().toString();
            String category = categorySp.getSelectedItem().toString();

            if (amountStr.isEmpty()) {
                Toast.makeText(this, "Please enter an amount", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
                return;
            }

            if (amount <= 0) {
                Toast.makeText(this, "Amount must be positive", Toast.LENGTH_SHORT).show();
                return;
            }

            String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

            Transaction transaction = new Transaction(type, category, amount, note, date);
            TransactionManager.addTransaction(transaction, this);

            Toast.makeText(this, "Transaction saved!", Toast.LENGTH_SHORT).show();
            finish();
        });

    }
}
