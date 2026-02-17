package com.example.financetracker.models;

public class Transaction {
    private String userId;    // Firebase UID to associate data with a user
    private String type;      // "Income" or "Expense"
    private String category;
    private double amount;
    private String note;
    private String date;

    public Transaction(String userId, String type, String category, double amount, String note, String date) {
        this.userId = userId;
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.note = note;
        this.date = date;
    }

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}