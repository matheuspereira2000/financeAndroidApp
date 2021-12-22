package com.example.android.effectivenavigation;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SavingGoals extends Fragment {


    public PieChart pieChart;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.saving_goals_fragment, container, false);
        pieChart = (PieChart)view.findViewById(R.id.PieChart);
        setupPieChart();
        makeEmptyChart();
        return view;
    }
    public void onViewCreated(View view, Bundle savedInstanceState){
        EditText editTextGoal = getView().findViewById(R.id.SavingsGoalAmount);
        EditText editTextBank = getView().findViewById(R.id.SavingsAmountInBank);
        EditText editTextMonth = getView().findViewById(R.id.SavingsMonthlyContribution);
        TextView textView = getView().findViewById(R.id.savingsTextView);
        Button button = getView().findViewById(R.id.savings_button);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String goal = editTextGoal.getText().toString();
                String bank = editTextBank.getText().toString();
                String month = editTextMonth.getText().toString();

                if(editTextGoal.getText().toString().isEmpty() &&
                        editTextBank.getText().toString().isEmpty() &&
                        editTextMonth.getText().toString().isEmpty())
                {
                    textView.setText("Please enter all entries. ");
                    return;
                }

                loadPieChartData(goal, bank, month);
                pieChart.invalidate();

            }
        });
    }

    private void setupPieChart() {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(18);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("This Year's Goal!");
        pieChart.setCenterTextSize(24);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setTextSize(20);
    }

    private void makeEmptyChart(){
        pieChart.setCenterText("This Year's Goal!");
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(0, "Enter Data Below to Start"));
        PieDataSet dataSet = new PieDataSet(entries, "~");
        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(18f);
        data.setValueTextColor(Color.BLACK);
        pieChart.setData(data);
        pieChart.invalidate();
    }


    private void loadPieChartData(String goal, String bank, String month) {
        TextView textView = getView().findViewById(R.id.savingsTextView);
        pieChart.setCenterText("Goal: $" + goal);
        ArrayList<PieEntry> entries = new ArrayList<>();

        float bankFloat = Float.parseFloat(bank);
        float goalFloat = Float.parseFloat(goal);
        float monthFloat = Float.parseFloat(month);
        float toSaveStill = goalFloat - bankFloat;

        entries.add(new PieEntry(bankFloat, "In da Bank"));
        entries.add(new PieEntry(toSaveStill, "2 Save Still"));
        float monthsToGoal = toSaveStill/monthFloat;

        String foo = String.format("%.2f", monthsToGoal);
        textView.setText("Months 'Til Goal: " + foo);

        ArrayList<Integer> colors = new ArrayList<>();
        for (int color: ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }
        for (int color: ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(18f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate();

        pieChart.animateY(1400, Easing.EaseInOutQuad);
    }




}