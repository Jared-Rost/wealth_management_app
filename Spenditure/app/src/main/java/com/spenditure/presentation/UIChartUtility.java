package com.spenditure.presentation;

import android.graphics.Color;
import android.graphics.Typeface;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.spenditure.R;
import com.spenditure.presentation.report.PercentageValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class UIChartUtility {
    public static void configPieChar(PieChart pieChart, List<PieEntry> entries){
        Typeface boldTF = Typeface.defaultFromStyle(Typeface.BOLD);
        PieDataSet pieDataSet = new PieDataSet(entries,"");
        pieDataSet.setValueFormatter(new PercentageValueFormatter());

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);


        //Styling pie chart
        pieDataSet.setValueTextSize(20); // Set size of value text
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        pieDataSet.setValueTypeface(boldTF);

        pieChart.getDescription().setEnabled(false);
        pieChart.animateY(1000);

        pieChart.setHoleColor(R.drawable.background_dark_blue);
        pieChart.setEntryLabelTextSize(0);
        pieChart.setCenterTextColor(Color.WHITE);

        Legend legend = pieChart.getLegend();
        legend.setTextSize(40); // Set text size of legend
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setTextColor(Color.WHITE);
        legend.setFormSize(20);
        legend.setTypeface(boldTF);


    }

    public static void configLineChar(LineChart lineChart, List<Entry> entries,List<String> labels){
        //Config line chart
        lineChart.animateXY(3000, 3000);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getAxisRight().setDrawGridLines(false);

        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getLegend().setEnabled(false);

        //Config X-Y axis
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setLabelCount(4);
        xAxis.setGranularity(1f);
        xAxis.setGridColor(Color.WHITE);
        xAxis.setAxisLineColor(Color.WHITE);
        xAxis.setTextSize(16f);
        xAxis.setAxisLineWidth(5f);
        xAxis.setTextColor(Color.WHITE);

        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setAxisMinimum(0f);
        yAxis.setMaxWidth(100f);
        yAxis.setAxisLineWidth(2f);
        yAxis.setZeroLineColor(Color.WHITE);
        yAxis.setLabelCount(10);
        yAxis.setAxisLineColor(Color.WHITE);
        yAxis.setTextSize(20f);
        yAxis.setAxisLineWidth(5f);
        yAxis.setTextColor(Color.WHITE);

        //Fill chart with data
        LineDataSet dataSet = new LineDataSet(entries,"");

        dataSet.setColors(Color.WHITE);
        dataSet.setLineWidth(5f);
        dataSet.setCircleRadius(5f);
        dataSet.setValueTextSize(20f);
        dataSet.setValueTextColor(Color.WHITE);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);

    }
}
