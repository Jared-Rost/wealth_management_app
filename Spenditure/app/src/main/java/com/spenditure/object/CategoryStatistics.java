package com.spenditure.object;

//CategoryStatistics class used for internal ReportManager purposes
//Stores each Category with associated total, average and percent values
public class CategoryStatistics {
    //instance vars
    private MainCategory category;
    private double total;
    private double average;
    private double percent;

    public CategoryStatistics(MainCategory category, double total, double average, double percent) {
        this.category = category;
        this.total = total;
        this.average = average;
        this.percent = percent;
    }

    public MainCategory getCategory() {
        return category;
    }

    public double getTotal() {
        return total;
    }

    public double getAverage() {
        return average;
    }

    public double getPercent() {
        return percent;
    }
}