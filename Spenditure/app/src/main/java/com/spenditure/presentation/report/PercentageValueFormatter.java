package com.spenditure.presentation.report;

import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;

public class PercentageValueFormatter extends ValueFormatter {
    private DecimalFormat format;

    public PercentageValueFormatter() {
        format = new DecimalFormat("###,###,##0.0");
    }

    @Override
    public String getFormattedValue(float value) {
        return format.format(value) + "%";
    }
}
