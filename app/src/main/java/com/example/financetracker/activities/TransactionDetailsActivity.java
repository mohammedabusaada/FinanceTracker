package com.example.financetracker.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.financetracker.R;
import com.example.financetracker.utils.DatabaseHelper;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
public class TransactionDetailsActivity extends AppCompatActivity {

    private TextView typeTV, categoryTV, amountTV, noteTV, dateTV;
    private MaterialButton deleteBtn;
    private DatabaseHelper dbHelper;
    private String currentUserId;
    private String note, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);

        View mainView = findViewById(R.id.main_TransactionDetailsActivity);
        ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        dbHelper = new DatabaseHelper(this);
        initViews();
        note = getIntent().getStringExtra("note");
        date = getIntent().getStringExtra("date");
        displayData();
        deleteBtn.setOnClickListener(v -> showDeleteConfirmation());
    }

    private void initViews() {
        typeTV = findViewById(R.id.typeTV_TransactionDetailsActivity);
        categoryTV = findViewById(R.id.categoryTV_TransactionDetailsActivity);
        amountTV = findViewById(R.id.amountTV_TransactionDetailsActivity);
        noteTV = findViewById(R.id.noteTV_TransactionDetailsActivity);
        dateTV = findViewById(R.id.dateTV_TransactionDetailsActivity);
        deleteBtn = findViewById(R.id.deleteBtn_TransactionDetailsActivity);
    }

    private void displayData() {
        typeTV.setText("Type: " + getIntent().getStringExtra("type"));
        categoryTV.setText("Category: " + getIntent().getStringExtra("category"));

        double amount = getIntent().getDoubleExtra("amount", 0.0);
        amountTV.setText(String.format("Amount: $%.2f", amount));

        noteTV.setText("Note: " + note);
        dateTV.setText("Date: " + date);
    }

    private void showDeleteConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Transaction")
                .setMessage("Are you sure you want to delete this record from your device?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    if (currentUserId != null) {
                        // Delete from SQLite database using user identity and unique fields
                        dbHelper.deleteTransaction(currentUserId, note, date);

                        Toast.makeText(this, "Deleted successfully", Toast.LENGTH_SHORT).show();

                        // Return to previous activity and refresh list
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(this, "Error: User not authenticated", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}