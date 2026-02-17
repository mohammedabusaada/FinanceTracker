package com.example.financetracker.models;

import java.util.Map;

public class CurrencyResponse {
    private String base;
    private Map<String, Double> rates; // This will hold the currency name and its value

    public Map<String, Double> getRates() {
        return rates;
    }
}
