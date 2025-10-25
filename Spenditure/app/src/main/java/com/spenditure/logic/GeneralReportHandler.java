/**
 * GeneralReportHandler
 *
 * COMP3350 SECTION A02
 *
 * @author JR,
 * @date Mar 25
 *
 * PURPOSE:
 *  Handles all reporting related to statistics that are not dependant on a time frame.
 *  Namely: summation statistics for categories, summation statistics for all transactions,
 *  and sorting categories by these statistics
 *
 **/

package com.spenditure.logic;

import com.spenditure.application.Services;
import com.spenditure.database.CategoryPersistence;
import com.spenditure.database.TransactionPersistence;
import com.spenditure.object.CategoryReport;
import com.spenditure.object.MainCategory;
import com.spenditure.object.Transaction;
import com.spenditure.logic.exceptions.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GeneralReportHandler implements IGeneralReportHandler {
    private TransactionPersistence dataAccessTransaction;
    private CategoryPersistence dataAccessCategory;

    //Store a save state of the Transaction and Category tables
    private List<Transaction> allTransactions = null;
    private List<MainCategory> allCategories = null;

    /**
     * constructor
     *
     * initializes needed databases
     * @param boolean getStubDB - if true then we use stub not sql
     * @returns NA
     */
    public GeneralReportHandler(boolean getStubDB) {

        this.dataAccessTransaction = Services.getTransactionPersistence(getStubDB);

        this.dataAccessCategory = Services.getCategoryPersistence(getStubDB);

    }


    /**
     * numTransactions
     *
     * Returns the number of transactions in a particular category
     * @param int userID - userID of person logged in
     * @param int categoryID - categoryID of category to be accessed
     * @returns int - number of transactions
     */
    public int numTransactions(int userID, int categoryID) throws InvalidLogInException, InvalidCategoryException
    {
        checkSaveState(userID);

        if(categoryID < 0)
            throw new InvalidCategoryException("Could not get Transactions from the Category; the provided Category ID ("+categoryID+") is not associated with any existing Categories.");

        return getTransactionsByCategoryID(categoryID).size();
    }

    /**
     * totalSpending
     *
     * Returns the total spending in a particular category
     * @param int userID - userID of person logged in
     * @param int categoryID - categoryID of category to be accessed
     * @returns double - total spending
     */
    public double totalSpending(int userID, int categoryID) throws InvalidLogInException, InvalidCategoryException
    {
        checkSaveState(userID);

        List<Transaction> transactions;

        if(categoryID < 0)
            throw new InvalidCategoryException("Could not calculate statistics for the Category; the provided Category ID ("+categoryID+") is not associated with any existing Categories.");

        double total = 0;

        transactions = getTransactionsByCategoryID(categoryID);

        // calculate total
        for(int i = 0; i < transactions.size(); i++)
        {
            total += transactions.get(i).getAmount();
        }

        return total;

    }

    /**
     * averageSpending
     *
     * Returns the average spending in a particular category
     * @param int userID - userID of person logged in
     * @param int categoryID - categoryID of category to be accessed
     * @returns double - average spending
     */
    public double averageSpending(int userID, int categoryID) throws InvalidLogInException, InvalidCategoryException
    {
        checkSaveState(userID);

        double average;

        if(categoryID < 0)
            throw new InvalidCategoryException("Could not calculate statistics for the Category; the provided Category ID ("+categoryID+") is not associated with any existing Categories.");

        // to avoid dividing by 0 check if there are no transactions
        if(numTransactions(userID, categoryID) == 0)
        {
            average = 0;
        }
        else
        {
            average = totalSpending(userID, categoryID) / numTransactions(userID, categoryID);
        }

        return average;

    }

    /**
     * percentage
     *
     * Returns the percentage of all spending made up by that category
     * @param int userID - userID of person logged in
     * @param int categoryID - categoryID of category to be accessed
     * @returns double - percentage
     */
    public double percentage(int userID, int categoryID) throws InvalidLogInException, InvalidCategoryException
    {
        checkSaveState(userID);

        double percentage;

        if(categoryID < 0)
            throw new InvalidCategoryException("Could not calculate statistics for the Category; the provided Category ID ("+categoryID+") is not associated with any existing Categories.");

        // to avoid dividing by 0 check if there are no transactions
        if(numTransactions(userID, categoryID) == 0)
        {
            percentage = 0;
        }
        else
        {
            percentage = (totalSpending(userID, categoryID)/totalSpending(userID)) * 100;
        }

        return percentage;

    }

    /**
     * numTransactions
     *
     * Returns the number of transactions in fpr a user
     * @param int userID - userID of person logged in
     * @returns int - number of transactions
     */
    public int numTransactions(int userID) throws InvalidLogInException
    {
        checkSaveState(userID);

        return allTransactions.size();
    }

    /**
     * totalSpending
     *
     * Returns the total spending in for person logged in
     * @param int userID - userID of person logged in
     * @returns double - total spending
     */
    public double totalSpending(int userID) throws InvalidLogInException
    {
        checkSaveState(userID);

        double total = 0;

        // calculate total
        for(int i = 0; i < allTransactions.size(); i++)
        {
            total += allTransactions.get(i).getAmount();
        }

        return total;

    }

    /**
     * averageSpending
     *
     * Returns the average spending in for a user
     * @param int userID - userID of person logged in
     * @returns double - average spending
     */
    public double averageSpending(int userID) throws InvalidLogInException
    {
        checkSaveState(userID);

        double average;

        // to avoid dividing by 0 check if there are no transactions
        if(numTransactions(userID) == 0)
        {
            average = 0;
        }
        else
        {
            average = totalSpending(userID) / numTransactions(userID);
        }

        return average;

    }


    /**
     * getCategoryReport
     *
     * Returns a category report object with statistics about the selected category
     * @param int userID - userID of person logged in
     * @param int categoryID - categoryID of category to be reported on
     * @returns CategoryReport - report object
     */
    public CategoryReport getCategoryReport(int userID, int categoryID) throws InvalidLogInException, InvalidCategoryException
    {
        checkSaveState(userID);

        double totalSpending;
        int numTransactions;
        double average;
        double percentage;

        if(categoryID < 0)
            throw new InvalidCategoryException("Could not provide a Report for the Category; the provided Category ID ("+categoryID+") is not associated with any existing Categories.");

        totalSpending = totalSpending(userID, categoryID);
        numTransactions = numTransactions(userID, categoryID);
        average = averageSpending(userID, categoryID);
        percentage = percentage(userID, categoryID);

        return new CategoryReport(getCategoryByID(categoryID), totalSpending, numTransactions, average, percentage);
    }

    /**
     * getAllCategoryReport
     *
     * Returns a list of category report objects
     * @param int userID - userID of person logged in
     * @returns List<CategoryReport> - list of CategoryReports
     */
    public List<CategoryReport> getAllCategoryReport(int userID) {

        checkSaveState(userID);

        List<CategoryReport> categoryReportList = new ArrayList<>();
        List<MainCategory> mainCategoryList = allCategories;

        for (MainCategory mainCategory : mainCategoryList) {
            categoryReportList.add(getCategoryReport(userID,mainCategory.getCategoryID()));
        }

        return categoryReportList;

    }



    /**
     * sortByTotal
     *
     * Returns a list of all categories for a user sorted by total number of transactions in each
     * @param int userID - userID of person logged in
     * @param boolean descending - if true then sorted in descending order, else ascending
     * @returns ArrayList<MainCategory> - sorted array list
     */
    public ArrayList<MainCategory> sortByTotal(int userID, boolean descending) throws InvalidLogInException
    {
        checkSaveState(userID);

        ArrayList<CategoryReport> categoryReportList = new ArrayList<>();
        ArrayList<MainCategory> sortedCategories = new ArrayList<>();

        // generate report for each category user has
        for(MainCategory node : allCategories)
        {
            categoryReportList.add(getCategoryReport(userID, node.getCategoryID()));
        }

        // sort categories by total
        Collections.sort(categoryReportList, (node1, node2) -> {
            // Compare based on the 'total' attribute
            if(descending) {
                return Double.compare(node2.getTotalSpending(), node1.getTotalSpending());
            }
            else {
                return Double.compare(node1.getTotalSpending(), node2.getTotalSpending());
            }

        });

        // take out category objects from sorted list of category reports
        for (CategoryReport node : categoryReportList) {
            sortedCategories.add(node.getCategory());
        }

        return sortedCategories;
    }

    /**
     * sortByPercent
     *
     * Returns a list of all categories for a user sorted by percentage of total amount spend
     * @param int userID - userID of person logged in
     * @param boolean descending - if true then sorted in descending order, else ascending
     * @returns ArrayList<IMainCategory> - sorted array list
     */
    public ArrayList<MainCategory> sortByPercent(int userID, boolean descending) throws InvalidLogInException
    {
        checkSaveState(userID);

        ArrayList<CategoryReport> categoryReportList = new ArrayList<>();
        ArrayList<MainCategory> sortedCategories = new ArrayList<>();

        // generate report for each category user has
        for(MainCategory node : allCategories)
        {
            categoryReportList.add(getCategoryReport(userID, node.getCategoryID()));
        }

        // sort categories by percentage
        Collections.sort(categoryReportList, (node1, node2) -> {
            // Compare based on the 'percentage' attribute
            if(descending) {
                return Double.compare(node2.getPercentage(), node1.getPercentage());
            }
            else {
                return Double.compare(node1.getPercentage(), node2.getPercentage());
            }
            // =D, you found the secret smiley face, have a nice day!
        });

        // take out category objects from sorted list of category reports
        for (CategoryReport node : categoryReportList) {
            sortedCategories.add(node.getCategory());
        }

        return sortedCategories;
    }

    /**
     * sortByAverage
     *
     * Returns a list of all categories for a user sorted by average amount spent in that category
     * @param int userID - userID of person logged in
     * @param boolean descending - if true then sorted in descending order, else ascending
     * @returns ArrayList<IMainCategory> - sorted array list
     */
    public ArrayList<MainCategory> sortByAverage(int userID, boolean descending) throws InvalidLogInException
    {
        checkSaveState(userID);

        ArrayList<CategoryReport> categoryReportList = new ArrayList<>();
        ArrayList<MainCategory> sortedCategories = new ArrayList<>();

        // generate report for each category user has
        for(MainCategory node : allCategories)
        {
            categoryReportList.add(getCategoryReport(userID, node.getCategoryID()));
        }

        // sort categories by average
        Collections.sort(categoryReportList, (node1, node2) -> {
            // Compare based on the 'average' attribute
            if(descending) {
                return Double.compare(node2.getAverageTransactions(), node1.getAverageTransactions());
            }
            else {
                return Double.compare(node1.getTotalSpending(), node2.getTotalSpending());
            }

        });

        // take out category objects from sorted list of category reports
        for (CategoryReport node : categoryReportList) {
            sortedCategories.add(node.getCategory());
        }

        return sortedCategories;
    }

    /**
     * checkSaveState
     *
     * Checks the User ID's validity, then checks to ensure that there is a valid save state (not null).
     *
     * @param userID
     * @throws InvalidLogInException
     */
    private void checkSaveState(int userID) throws InvalidLogInException{

        if(userID < 0)
            throw new InvalidLogInException();

        if(allTransactions == null) {

            allTransactions = dataAccessTransaction.getAllTransactionsForUser(userID);
            allCategories = dataAccessCategory.getAllCategory(userID);

        }

    }

    /**
     * getTransactionsByCategoryID
     *
     * Given a Category ID, returns a List of all Transactions in the allTransactions
     * List which are associated with that Category ID.
     *
     * @param categoryID
     * @return List of Transactions
     */
    private List<Transaction> getTransactionsByCategoryID(int categoryID) {

        List<Transaction> result = new ArrayList<>();

        for(int i = 0; i < allTransactions.size(); i++) {

            if(allTransactions.get(i).getCategoryID() == categoryID) {

                result.add(allTransactions.get(i));

            }

        }

        return result;

    }

    /**
     *
     * getCategoryByID
     *
     * Given a Category ID, returns the corresponding Category in the allCategories List.
     *
     * @param categoryID
     * @return MainCategory
     */
    private MainCategory getCategoryByID(int categoryID) {

        MainCategory result = null;

        for(int i = 0; i < allCategories.size(); i++ ) {

            if(allCategories.get(i).getCategoryID() == categoryID) {

                result = allCategories.get(i);

                break;
            }

        }

        if(result == null)
            throw new InvalidCategoryException("Could not fetch category with ID of "+categoryID+", because no such category exists.");

        return result;
    }


}
