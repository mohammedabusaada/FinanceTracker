package com.example.financetracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.financetracker.R;
import com.example.financetracker.fragments.StatisticsFragment;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private MaterialButton addTransactionBtn, viewTransactionsBtn, viewStatsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setListeners();
    }

    private void initViews() {
        addTransactionBtn = findViewById(R.id.addTransactionBtn_MainActivity);
        viewTransactionsBtn = findViewById(R.id.viewTransactionsBtn_MainActivity);
        viewStatsBtn = findViewById(R.id.viewStatsBtn_MainActivity);
    }

    private void setListeners() {
        addTransactionBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddTransactionActivity.class));
        });

        viewTransactionsBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ViewTransactionsActivity.class));
        });

        viewStatsBtn.setOnClickListener(v -> {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer_MainActivity, new StatisticsFragment())
                    .addToBackStack(null)
                    .commit();
        });
    }
}
