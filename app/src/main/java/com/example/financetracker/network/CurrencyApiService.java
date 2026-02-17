package com.example.financetracker.network;

import com.example.financetracker.models.CurrencyResponse;
import retrofit2.Call;
import retrofit2.http.GET;

public interface CurrencyApiService {
    // End point to get latest rates relative to USD
    @GET("latest?base=USD")
    Call<CurrencyResponse> getLatestRates();
}