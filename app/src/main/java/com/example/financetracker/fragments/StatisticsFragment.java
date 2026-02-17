package com.example.financetracker.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.financetracker.R;
import com.example.financetracker.models.Transaction;
import com.example.financetracker.utils.DatabaseHelper;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class StatisticsFragment extends Fragment {

    private TextView totalIncomeTV, totalExpenseTV, balanceTV;
    private MaterialButton refreshBtn;
    private PieChart pieChart;
    private DatabaseHelper dbHelper;
    private String currentUserId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        // 1. Initialize Firebase Auth and Database Helper
        dbHelper = new DatabaseHelper(requireContext());
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        // 2. Initialize Views
        totalIncomeTV = view.findViewById(R.id.totalIncomeTV_StatisticsFragment);
        totalExpenseTV = view.findViewById(R.id.totalExpenseTV_StatisticsFragment);
        balanceTV = view.findViewById(R.id.balanceTV_StatisticsFragment);
        refreshBtn = view.findViewById(R.id.refreshStatsBtn_StatisticsFragment);
        pieChart = view.findViewById(R.id.pieChart_StatisticsFragment);

        setupPieChart();
        loadStats();

        refreshBtn.setOnClickListener(v -> loadStats());

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                PieEntry pe = (PieEntry) e;
                Toast.makeText(getActivity(), pe.getLabel() + ": $" + pe.getValue(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() { }
        });

        return view;
    }

    private void setupPieChart() {
        pieChart.getDescription().setEnabled(false);
        pieChart.setUsePercentValues(true);
        pieChart.setDrawEntryLabels(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setTransparentCircleRadius(58f);
        pieChart.setHoleRadius(58f);
        pieChart.getLegend().setEnabled(true);
    }

    private void loadStats() {
        if (currentUserId == null) return;

        // 3. Fetch user-specific data from SQLite
        List<Transaction> userTransactions = dbHelper.getAllTransactions(currentUserId);

        double totalIncome = 0;
        double totalExpense = 0;

        // 4. Calculate totals manually from the list
        for (Transaction t : userTransactions) {
            if ("Income".equalsIgnoreCase(t.getType())) {
                totalIncome += t.getAmount();
            } else if ("Expense".equalsIgnoreCase(t.getType())) {
                totalExpense += t.getAmount();
            }
        }

        double balance = totalIncome - totalExpense;

        // 5. Update UI
        totalIncomeTV.setText(String.format("Total Income: $%.2f", totalIncome));
        totalExpenseTV.setText(String.format("Total Expense: $%.2f", totalExpense));
        balanceTV.setText(String.format("Balance: $%.2f", balance));

        updatePieChart(totalIncome, totalExpense);
    }

    private void updatePieChart(double totalIncome, double totalExpense) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        if (totalIncome > 0) entries.add(new PieEntry((float) totalIncome, "Income"));
        if (totalExpense > 0) entries.add(new PieEntry((float) totalExpense, "Expense"));

        if (entries.isEmpty()) {
            pieChart.clear();
            return;
        }

        PieDataSet dataSet = new PieDataSet(entries, "Financial Overview");

        int incomeColor = ContextCompat.getColor(requireContext(), R.color.incomeColor);
        int expenseColor = ContextCompat.getColor(requireContext(), R.color.expenseColor);
        dataSet.setColors(new int[]{incomeColor, expenseColor});

        dataSet.setValueTextSize(14f);
        dataSet.setValueTextColor(Color.WHITE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(pieChart));

        pieChart.setData(data);
        pieChart.animateY(1000, Easing.EaseInOutCubic);
        pieChart.invalidate();
    }
}