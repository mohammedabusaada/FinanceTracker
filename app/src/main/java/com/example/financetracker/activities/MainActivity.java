package com.example.financetracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.financetracker.R;
import com.example.financetracker.fragments.StatisticsFragment;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
public class MainActivity extends AppCompatActivity {

    private MaterialButton addTransactionBtn, viewTransactionsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View mainView = findViewById(R.id.main_MainActivity);
        ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setListeners();
        if (savedInstanceState == null) {
            loadStatisticsFragment();
        }
    }

    private void initViews() {
        addTransactionBtn = findViewById(R.id.addTransactionBtn_MainActivity);
        viewTransactionsBtn = findViewById(R.id.viewTransactionsBtn_MainActivity);
    }

    private void setListeners() {
        // Navigate to Add Transaction screen
        addTransactionBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddTransactionActivity.class));
        });

        // Navigate to View Transactions screen
        viewTransactionsBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ViewTransactionsActivity.class));
        });
    }

    private void loadStatisticsFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer_MainActivity, new StatisticsFragment())
                .commit();
    }

    // --- Options Menu Setup ---

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_currency) {
            startActivity(new Intent(MainActivity.this, CurrencyActivity.class));
            return true;
        } else if (id == R.id.action_logout) {
            // Sign out from Firebase and return to Login screen
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) before allowing access to MainActivity
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            // User not authenticated, redirecting to Login
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
    }
}