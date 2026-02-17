package com.example.financetracker.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.example.financetracker.R;
import com.example.financetracker.models.Transaction;
import com.example.financetracker.utils.DatabaseHelper;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddTransactionActivity extends AppCompatActivity {

    private EditText amountEdT, noteEdT;
    private Spinner typeSp, categorySp;
    private MaterialButton saveBtn;
    private DatabaseHelper dbHelper;
    private FirebaseAuth mAuth;

    private String[] typeOptions = {"Income", "Expense"};
    private String[] categoryOptions = {"Food", "Transport", "Bills", "Shopping", "Other"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        View mainView = findViewById(R.id.main_AddTransactionActivity);
        ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        initViews();
        dbHelper = new DatabaseHelper(this);
        setupSpinners();
        saveBtn.setOnClickListener(v -> saveTransaction());
    }

    private void initViews() {
        amountEdT = findViewById(R.id.amountEdT_AddTransactionActivity);
        noteEdT = findViewById(R.id.noteEdT_AddTransactionActivity);
        typeSp = findViewById(R.id.typeSpinner_AddTransactionActivity);
        categorySp = findViewById(R.id.categorySpinner_AddTransactionActivity);
        saveBtn = findViewById(R.id.saveBtn_AddTransactionActivity);
    }

    private void setupSpinners() {
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, typeOptions);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSp.setAdapter(typeAdapter);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryOptions);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySp.setAdapter(categoryAdapter);
    }

    private void saveTransaction() {
        // 1. Get Current Firebase User UID to link the record
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated!", Toast.LENGTH_SHORT).show();
            return;
        }
        String userId = currentUser.getUid();

        // 2. Get data from input fields
        String amountStr = amountEdT.getText().toString().trim();
        String note = noteEdT.getText().toString().trim();
        String type = typeSp.getSelectedItem().toString();
        String category = categorySp.getSelectedItem().toString();

        // 3. Validation
        if (amountStr.isEmpty()) {
            Toast.makeText(this, "Please enter an amount", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid amount format", Toast.LENGTH_SHORT).show();
            return;
        }

        if (amount <= 0) {
            Toast.makeText(this, "Amount must be positive", Toast.LENGTH_SHORT).show();
            return;
        }

        // 4. Generate current date
        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        // 5. Create new Transaction object
        Transaction transaction = new Transaction(userId, type, category, amount, note, date);

        // 6. Save to Local SQLite Database
        long result = dbHelper.addTransaction(transaction);

        if (result != -1) {
            Toast.makeText(this, "Transaction saved successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error saving data to database", Toast.LENGTH_SHORT).show();
        }
    }
}