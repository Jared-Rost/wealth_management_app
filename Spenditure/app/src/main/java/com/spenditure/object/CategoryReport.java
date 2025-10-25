package com.spenditure.object;

public class CategoryReport {

    MainCategory category;

    double totalSpending;

    int numTransactions;

    double averageTransaction;

    double percentage;


    public CategoryReport(MainCategory category, double totalSpending, int numTransactions, double averageTransaction, double percentage)
    {
        this.category = category;
        this.totalSpending = totalSpending;
        this.numTransactions = numTransactions;
        this.averageTransaction = averageTransaction;
        this.percentage = percentage;
    }

    public MainCategory getCategory()
    {
        return category;
    }

    public double getTotalSpending()
    {
        return totalSpending;
    }

    public int getNumTransactions()
    {
        return numTransactions;
    }

    public double getAverageTransactions()
    {
        return averageTransaction;
    }

    public double getPercentage()
    {
        return percentage;
    }
}
