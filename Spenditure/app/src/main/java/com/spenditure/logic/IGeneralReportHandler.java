/**
 * IGeneralReportHandler
 *
 * COMP3350 SECTION A02
 *
 * @author JR,
 * @date Mar 2
 *
 * PURPOSE:
 *  Interface detailing what functions GeneralReportHandler should have
 *
 **/


package com.spenditure.logic;

import com.spenditure.object.CategoryReport;
import com.spenditure.object.MainCategory;

import java.util.ArrayList;
import java.util.List;

public interface IGeneralReportHandler {

    /**
     * numTransactions
     * Returns the number of transactions in a particular category
     */
    public int numTransactions(int userID, int categoryID);

    /**
     * totalSpending
     * Returns the total spending in a particular category
     */
    public double totalSpending(int userID, int categoryID);

    /**
     * averageSpending
     * Returns the average spending in a particular category
     */
    public double averageSpending(int userID, int categoryID);

    /**
     * percentage
     * Returns the percentage of all spending made up by that category
     */
    public double percentage(int userID, int categoryID);

    /**
     * numTransactions
     * Returns the number of transactions in fpr a user
     */
    public int numTransactions(int userID);

    /**
     * totalSpending
     * Returns the total spending in for person logged in
     */
    public double totalSpending(int userID);

    /**
     * averageSpending
     * Returns the average spending in for a user
     */
    public double averageSpending(int userID);

    /**
     * getCategoryReport
     * Returns a category report object with statistics about the selected category
     */
    public CategoryReport getCategoryReport(int userID, int categoryID);

    /**
     * sortByTotal
     * Returns a list of all categories for a user sorted by total number of transactions in each
     */
    public ArrayList<MainCategory> sortByTotal(int userID, boolean descending);

    /**
     * sortByPercent
     * Returns a list of all categories for a user sorted by percentage of total amount spend
     */
    public ArrayList<MainCategory> sortByPercent(int userID, boolean descending);

    /**
     * sortByAverage
     * Returns a list of all categories for a user sorted by average amount spent in that category
     */
    public ArrayList<MainCategory> sortByAverage(int userID, boolean descending);

    /**
     * getAllCategoryReport
     * Returns a list of category report objects
     */
    public List<CategoryReport> getAllCategoryReport(int userID);
}
