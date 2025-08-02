package com.example.financetracker.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.financetracker.models.Transaction;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TransactionManager {
    private static final String PREFS_NAME = "finance_prefs";
    private static final String KEY_TRANSACTIONS = "transactions";

    private static List<Transaction> transactions = new ArrayList<>();

    public static void addTransaction(Transaction transaction, Context context) {
        transactions.add(transaction);
        saveTransactions(context);
    }

    public static List<Transaction> getAllTransactions(Context context) {
        loadTransactions(context);
        return transactions;
    }

    public static Transaction getTransaction(int index) {
        if (index >= 0 && index < transactions.size()) {
            return transactions.get(index);
        }
        return null;
    }

    public static void deleteTransaction(int index, Context context) {
        if (index >= 0 && index < transactions.size()) {
            transactions.remove(index);
            saveTransactions(context);
        }
    }

    public static double getTotalIncome() {
        double total = 0;
        for (Transaction t : transactions) {
            if ("Income".equalsIgnoreCase(t.getType())) {
                total += t.getAmount();
            }
        }
        return total;
    }

    public static double getTotalExpense() {
        double total = 0;
        for (Transaction t : transactions) {
            if ("Expense".equalsIgnoreCase(t.getType())) {
                total += t.getAmount();
            }
        }
        return total;
    }

    public static double getBalance() {
        return getTotalIncome() - getTotalExpense();
    }

    public static void loadTransactions(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_TRANSACTIONS, null);
        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Transaction>>() {}.getType();
            transactions = gson.fromJson(json, type);
        } else {
            transactions = new ArrayList<>();
        }
    }

    private static void saveTransactions(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(transactions);
        editor.putString(KEY_TRANSACTIONS, json);
        editor.apply();
    }
}
