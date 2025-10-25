package com.spenditure.logic;

import static com.spenditure.logic.DateTimeAdjuster.*;
import static com.spenditure.logic.DateTimeValidator.*;
import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

import com.spenditure.application.Services;
import com.spenditure.database.CategoryPersistence;
import com.spenditure.database.TransactionPersistence;
import com.spenditure.logic.exceptions.InvalidCategoryException;
import com.spenditure.object.DateTime;
import com.spenditure.object.Report;
import com.spenditure.object.CategoryStatistics;
import com.spenditure.object.MainCategory;
import com.spenditure.object.Transaction;
import java.util.ArrayList;
import java.util.List;
import java.time.*;

/**
 * ReportManager.java
 *
 * COMP3350 SECTION A02
 *
 * @author Trevor,
 * @date March 25, 2024
 *
 * PURPOSE:
 *  This file handles all logic calculations related to generating reports
 *
 **/


public class TimeBaseReportHandler implements ITimeBaseReportHandler {

    //Constants for time-based reporting
    private static final int DAYS_IN_WEEK = 7;
    private static final int WEEKS_IN_MONTH = 4;

    //instance vars
    private TransactionPersistence dataAccessTransaction;
    private CategoryPersistence dataAccessCategory;

    //Stores all transactions so that the data layer doesn't need to be unnecessarily called
    private List<Transaction> allTransactions;

    public TimeBaseReportHandler(boolean getStubDB) {
        this.dataAccessTransaction = Services.getTransactionPersistence(getStubDB);
        this.dataAccessCategory = Services.getCategoryPersistence(getStubDB);
    }

    /*

    reportBackOneYear

    Creates a Report starting one year before the provided date,
    ending at the provided date.

     */
    public Report reportBackOneYear(int userID, DateTime yearEnd) {

        setEndOfDay(yearEnd);

        validateDateTime(yearEnd);
        assert(userID >= 0);

        // manually set to 1 year ago from provided date
        DateTime yearStart = new DateTime(yearEnd.getYear()-1, yearEnd.getMonth(), yearEnd.getDay());

        return reportOnUserProvidedDates(userID, yearStart, yearEnd);

    }

    /*

    reportBackOnLastYearByMonth

    Creates a list of 12 Reports, one for each month of the year before the current year.
    Reports are ordered from January to December.

     */
    public ArrayList<Report> reportBackOnLastYearByMonth(int userID, DateTime today) {

        setEndOfDay(today);
        validateDateTime(today);
        assert(userID >= 0);

        ArrayList<Report> monthReports = new ArrayList<>();

        List<Transaction> transactions = getTransactions(userID,
                new DateTime(today.getYear()-1, 1, 1),
                new DateTime(today.getYear()-1, MAX_MONTHS, MAX_DAYS));

        for(int i = 1; i <= 12; i ++) {

            List<Transaction> month = getTimeSegmentFromList( transactions,
                    new DateTime(today.getYear()-1, i, 1),
                    new DateTime(today.getYear()-1, i, MAX_DAYS));

            monthReports.add(reportOnProvidedTransactionList(userID, month));

        }

        return monthReports;
    }

    /*

    reportBackOneMonthByWeek

    Creates a report starting 1 month before the provided date,
    ending at the provided date.

     */
    public ArrayList<Report> reportBackOneMonthByWeek(int userID, DateTime end) {

        setEndOfDay(end);
        validateDateTime(end);
        assert(userID >= 0);

        DateTime[] weekDates = new DateTime[WEEKS_IN_MONTH + 1];
        ArrayList<Report> result = new ArrayList<>();

        DateTime start = end.copy();
        start.adjust(0, 0, -DAYS_IN_WEEK * weekDates.length, 0, 0, 0);
        setBeginningOfDay(start);

        List<Transaction> transactions = getTransactions(userID, start, end);

        weekDates[0] = end;

        for( int i = 1; i < weekDates.length; i ++ ) {

            DateTime week = weekDates[i - 1].copy();
            week.adjust(0, 0, -DAYS_IN_WEEK, 0, 0, 0);


            weekDates[i] = week;

            DateTime startPoint = week.copy();
            setBeginningOfDay(startPoint);

            List<Transaction> transactionsInWeek = getTimeSegmentFromList(transactions, startPoint, weekDates[i-1]);

            result.add(reportOnProvidedTransactionList(userID, transactionsInWeek));

        }

        return result;
    }

    /*

    reportOnUserProvidedDates

    Creates a report starting and ending at user-specified times.

     */
    public Report reportOnUserProvidedDates(int userID, DateTime start, DateTime end) {

        setEndOfDay(end);
        validateDateTime(start);
        validateDateTime(end);
        assert(userID >= 0);

        List<Transaction> transactions = getTransactions(userID, start, end);

        return reportOnProvidedTransactionList(userID, transactions);

    }

    /*

    getCurrentDate

    Returns a DateTime of the format:
    (currentYear, currentMonth, currentDay, 00H, 00M, 00S)

    Especially useful with the above functions for making Reports around the current day

     */
    public static DateTime getCurrentDate() {
        LocalDate javaDateObject = LocalDate.now(); // Create a date object

        return new DateTime(javaDateObject.getYear(), javaDateObject.getMonthValue(), javaDateObject.getDayOfMonth());
    }

    // ################ PRIVATE METHODS #################
    /*

    reportOnProvidedTransactionList

    Generates a Report for the given User ID and pre-provided list of Transactions.

     */
    private Report reportOnProvidedTransactionList(int userID, List<Transaction> transactions) {

        double averageTransactionSize = getAverageTransactionSizeByDate(transactions);
        int numTransactions = transactions.size();
        double standardDeviation = getStandardDeviationByDate(transactions);
        double percent = getPercentForReport(userID, transactions);

        ArrayList<CategoryStatistics> categoryStatistics = buildCategoryList(userID, transactions);

        return new Report( averageTransactionSize, numTransactions, standardDeviation, percent, categoryStatistics );

    }

    /*

    getTimeSegmentFromList

    Given a list of Transactions and an upper and lower bound, returns a list containing only the Transactions
    which occur within those bounds (inclusive).

     */
    private List<Transaction> getTimeSegmentFromList(List<Transaction> transactions, DateTime lower, DateTime upper) {

        List<Transaction> result = new ArrayList<>();

        for(int i = 0; i < transactions.size(); i ++) {

            DateTime timestamp = transactions.get(i).getDateTime();

            if(timestamp.compare(lower) >= 0 && timestamp.compare(upper) <= 0) {

                result.add(transactions.get(i));

            }

        }

        return result;

    }

    /*

    getTransactions

    Updates the allTransactions variable with the database, then selects the specified portion according
    to the given DateTimes and returns all Transactions within that.

     */
    private List<Transaction> getTransactions(int userID, DateTime start, DateTime end) {

        allTransactions = dataAccessTransaction.getAllTransactionsForUser(userID);

        return getTimeSegmentFromList(allTransactions, start, end);

    }

    /*

    getCategories

    Returns a list of all categories associeated with a provided userID.

     */
    private List<MainCategory> getCategories(int userID) {

        return dataAccessCategory.getAllCategory(userID);

    }

    /*

    getAverageTransactionSizeByDate

    Returns the average transaction size between the two provided dates.

     */
    private double getAverageTransactionSizeByDate(List<Transaction> transactions) {

        double total = 0.00;

        for( int i = 0; i < transactions.size(); i ++ ) {

            total += transactions.get(i).getAmount();

        }

        if(!transactions.isEmpty())
            return total / transactions.size();
        else
            return 0;

    }

    /*

    getStandardDeviationByDate

    Returns the standard deviation value for transactions between the two specified dates.

     */
    private double getStandardDeviationByDate(List<Transaction> transactions) {

        double mean = getAverageTransactionSizeByDate(transactions);
        double sum = 0.00;
        double variance;
        double standardDeviation;

        for( int i = 0; i < transactions.size(); i ++ ) {

            double current = transactions.get(i).getAmount();

            current = abs(current - mean);
            current *= current;

            sum += current;
        }

        variance = sum / (transactions.size());

        standardDeviation = sqrt(variance);

        if(!transactions.isEmpty())
            return standardDeviation;
        else
            return 0;

    }

    /*

    countTransactionsByCategoryByDate

    Returns the number of transactions of a given category that occurred between the given dates.

     */
    private int countTransactionsByCategoryByDate(int categoryID, List<Transaction> transactionsInTimeframe) {

        ArrayList<Transaction> categoryTransactions = new ArrayList<>();

        for(int i = 0; i < transactionsInTimeframe.size(); i ++) {

            if( transactionsInTimeframe.get(i).getCategoryID() == categoryID )
                categoryTransactions.add(transactionsInTimeframe.get(i));

        }

        return categoryTransactions.size();
    }

    /*

    getTotalForAllTransactionsByDate

    Returns the total value of all the transactions that occurred between the given dates.

     */
    private double getTotalForAllTransactionsByDate(List<Transaction> transactions) {

        double total = 0.0;

        for(Transaction element : transactions) {
            total += element.getAmount();
        }

        return total;
    }

    /*

    getTotalForCategoryByDate

    Returns the total value of all transactions in a category over a given time.

     */
    private double getTotalForCategoryByDate(int categoryID, List<Transaction> transactionsInTimeframe) {

        double total = 0.0;

        for( int i = 0; i < transactionsInTimeframe.size(); i++ ) {

            Transaction element = transactionsInTimeframe.get(i);

            if(element.getCategoryID() == categoryID)
                //for each CT, access its transaction, add the transaction value to total
                total += element.getAmount();

        }

        return total;
    }

    /*

    getAverageForCategoryByDate

    Returns the average value of each transaction in a given category for given dates.

     */
    private double getAverageForCategoryByDate(int categoryID, List<Transaction> transactions) {

        double total = getTotalForCategoryByDate(categoryID, transactions);

        int count = countTransactionsByCategoryByDate(categoryID, transactions);

        if(count > 0)
            return (total / count);
        else
            return 0;
    }

    /*

    getPercentForCategoryByDate

    Returns the percentage of transactions in a given category out of all transactions, for given dates

     */
    private double getPercentForCategoryByDate(int categoryID, List<Transaction> transactions) {

        double totalAllTransactions = getTotalForAllTransactionsByDate(transactions);

        double totalForCategory = getTotalForCategoryByDate(categoryID, transactions);

        if(totalAllTransactions > 0)
            return ((totalForCategory / totalAllTransactions) * 100);
        else
            return 0;
    }

    /*

    getPercentForReport

    Returns the percentage of all transactions in the report out of all transactions in total

     */
    private double getPercentForReport(int userID, List<Transaction> transactions) {

        double allTotal = getTotalForAllTransactionsByDate(allTransactions);

        double reportTotal = getTotalForAllTransactionsByDate(transactions);

        return (reportTotal / allTotal) * 100;

    }

    /*

    buildCategoryList

    For each category, gets the statistics and packages them into items in a list. Returns a list
    of CategoryStatistics items which contain the stats on each category, and a link to the category
    itself.

     */
    private ArrayList<CategoryStatistics> buildCategoryList(int userID, List<Transaction> transactions) {

        ArrayList<CategoryStatistics> categoryList = new ArrayList<>();
        List<MainCategory> categories = getCategories(userID);

        for(int i = 1; i < categories.size() + 1; i++) {

            //for each Category calculate -> total, average, %
            MainCategory category = getCategoryByID(categories, categories.get(i-1).getCategoryID());

            double total = getTotalForCategoryByDate(i, transactions);
            double average = getAverageForCategoryByDate(i, transactions);
            double percent = getPercentForCategoryByDate(i, transactions);

            CategoryStatistics node = new CategoryStatistics(category,total,average,percent);
            categoryList.add(node);

        }

        return categoryList;

    }

    /*

    getCategoryByID

    Given a list of Categories, searches it for a Category with an ID matching that which was provided,
    and returns it.

     */
    private MainCategory getCategoryByID(List<MainCategory> categories, int id) throws InvalidCategoryException {

        MainCategory result = null;

        for(int i = 0; i < categories.size(); i ++) {

            if(categories.get(i).getCategoryID() == id) {

                result = categories.get(i);

                break;

            }

        }

        if(result == null)
            throw new InvalidCategoryException("Cannot produce a report for Category "+id+" because no such category exists.");

        return result;

    }

}
