package com.example.financetracker.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.financetracker.R;
import com.example.financetracker.adapters.CurrencyAdapter;
import com.example.financetracker.models.CurrencyResponse;
import com.example.financetracker.network.CurrencyApiService;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import androidx.activity.EdgeToEdge;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
public class CurrencyActivity extends AppCompatActivity {

    private RecyclerView currencyRv;
    private ProgressBar progressBar;
    private Retrofit retrofit;
    private static final String BASE_URL = "https://api.exchangerate-api.com/v6/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);

        View mainView = findViewById(R.id.main_CurrencyActivity);
        ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        currencyRv = findViewById(R.id.currencyRV_CurrencyActivity);
        progressBar = findViewById(R.id.progressBar_CurrencyActivity);
        currencyRv.setLayoutManager(new LinearLayoutManager(this));

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        fetchCurrencyData();
    }

    private void fetchCurrencyData() {
        // Show loading
        progressBar.setVisibility(View.VISIBLE);

        CurrencyApiService apiService = retrofit.create(CurrencyApiService.class);
        Call<CurrencyResponse> call = apiService.getLatestRates();

        call.enqueue(new Callback<CurrencyResponse>() {
            @Override
            public void onResponse(Call<CurrencyResponse> call, Response<CurrencyResponse> response) {
                progressBar.setVisibility(View.GONE); // Hide loading

                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Double> ratesMap = response.body().getRates();

                    // Create and set the adapter
                    CurrencyAdapter adapter = new CurrencyAdapter(ratesMap);
                    currencyRv.setAdapter(adapter);
                } else {
                    Toast.makeText(CurrencyActivity.this, "Failed to get data from server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CurrencyResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE); // Hide loading
                Toast.makeText(CurrencyActivity.this, "Network error: Check your internet", Toast.LENGTH_LONG).show();
            }
        });
    }
}