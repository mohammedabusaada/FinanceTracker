package com.example.financetracker.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.example.financetracker.R;
import com.example.financetracker.utils.TransactionManager;

public class MainActivity extends AppCompatActivity {

    private Button addTransactionBtn, viewTransactionsBtn;
    private TextView balanceTV, incomeTV, expenseTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TransactionManager.loadTransactions(this);
        initViews();
        setListeners();
        updateSummary();
    }


    private void initViews() {
        addTransactionBtn = findViewById(R.id.addTransactionBtn_MainActivity);
        viewTransactionsBtn = findViewById(R.id.viewTransactionsBtn_MainActivity);
        balanceTV = findViewById(R.id.balanceTV_MainActivity);
        incomeTV = findViewById(R.id.incomeTV_MainActivity);
        expenseTV = findViewById(R.id.expenseTV_MainActivity);
    }

    private void setListeners() {
        addTransactionBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTransactionActivity.class);
            startActivity(intent);
        });

        viewTransactionsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ViewTransactionsActivity.class);
            startActivity(intent);
        });
    }

    private void updateSummary() {
        double balance = TransactionManager.getBalance();
        double income = TransactionManager.getTotalIncome();
        double expense = TransactionManager.getTotalExpense();

        balanceTV.setText(String.format("Balance: $%.2f", balance));
        incomeTV.setText(String.format("Total Income: $%.2f", income));
        expenseTV.setText(String.format("Total Expense: $%.2f", expense));
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateSummary(); // Refresh values after returning from Add screen
    }
}
