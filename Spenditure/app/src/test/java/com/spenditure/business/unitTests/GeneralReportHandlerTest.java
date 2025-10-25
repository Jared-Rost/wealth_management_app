/**
 * GeneralReportHandlerTest
 *
 * COMP3350 SECTION A02
 *
 * @author JR,
 * @date Mar 2
 *
 * PURPOSE:
 *  Testing GeneralReportHandler functions
 *
 **/

package com.spenditure.business.unitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import com.spenditure.logic.GeneralReportHandler;
import com.spenditure.logic.UserHandler;
import com.spenditure.logic.exceptions.InvalidCategoryException;
import com.spenditure.logic.exceptions.InvalidLogInException;
import com.spenditure.object.CategoryReport;
import com.spenditure.object.MainCategory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class GeneralReportHandlerTest {

    private GeneralReportHandler generalReportHandler;

    @Before
    public void setup() {
        this.generalReportHandler = new GeneralReportHandler(true);
    }

    @After
    public void tearDown(){
        UserHandler.cleanup(true);
        this.generalReportHandler = null;
    }

    @Test(expected = InvalidLogInException.class)
    public void testInvalidLogInException() {
        generalReportHandler.numTransactions(-1, 1);
    }

    @Test
    public void testNumTransactions() {
        //get a num of transactions for three categories and one for all categories a user has
        double category1 = generalReportHandler.numTransactions(1, 1);
        double category2 = generalReportHandler.numTransactions(1, 2);
        double category3 = generalReportHandler.numTransactions(1, 5000);
        double allCategories = generalReportHandler.numTransactions(1);

        assertEquals("Expected category 1 to have 4 transactions", category1,4,0.1);
        assertEquals("Expected category 2 to have 4 transactions",category2,4,0.1);
        assertEquals("Expected category 3 to have 0 transactions",category3,0,0.1);
        assertEquals("Expected there to be 14 total transactions for this user",allCategories,14,0.1);
    }


    @Test
    public void testTotalSpending() {
        //get a total spending for two categories and one for all categories a user has
        double category1 = generalReportHandler.totalSpending(1, 1);
        double category2 = generalReportHandler.totalSpending(1, 2);
        double category3 = generalReportHandler.totalSpending(1, 5000);
        double allCategories = generalReportHandler.totalSpending(1);

        assertEquals("Expected category 1 to have a total spending of 440.75", category1,440.75,0.1);
        assertEquals("Expected category 2 to have a total spending of 416.74",category2,416.74,0.1);
        assertEquals("Expected category 3 to have a total spending of 0",category3,0,0.1);
        assertEquals("Expected there to be 14 total transactions for this user",allCategories,2233.94,0.1);
    }


    @Test
    public void testAverageSpending() {
        //get a total spending for two categories and one for all categories a user has
        double category1 = generalReportHandler.averageSpending(1, 1);
        double category2 = generalReportHandler.averageSpending(1, 2);
        double category3 = generalReportHandler.averageSpending(1, 5000);
        double allCategories = generalReportHandler.averageSpending(1);

        assertEquals("Expected category 1 to have a average spending of 110.1875", category1,110.1875,0.1);
        assertEquals("Expected category 2 to have a average spending of 104.185",category2,104.185,0.1);
        assertEquals("Expected category 3 to have a average spending of 0",category3,0,0.1);
        assertEquals("Expected there to be average of 159.57 for this user",allCategories,159.57,0.1);
    }

    @Test
    public void testPercentage() {
        //get a total spending for two categories and one for all categories a user has
        double category1 = generalReportHandler.percentage(1, 1);
        double category2 = generalReportHandler.percentage(1, 2);
        double category3 = generalReportHandler.percentage(1, 5000);

        assertEquals("Expected category 1 to have a percentage of 19.73", category1,19.73,0.1);
        assertEquals("Expected category 2 to have a percentage of 18.65",category2,18.65,0.1);
        assertEquals("Expected category 3 to have a percentage of 0",category3,0,0.1);
    }


    @Test
    public void testCategoryReport() {
        //test two different categories to see if report is correct
        CategoryReport category1 = generalReportHandler.getCategoryReport(1, 1);
        CategoryReport category2 = generalReportHandler.getCategoryReport(1, 2);


        // testing objects
        assertNotEquals("Expected categoryReport objects to not be null", category1, null);
        assertNotEquals("Expected categoryReport objects to not be null", category2, null);
        assertNotEquals("Expected categoryReport category objects to not be null", category1.getCategory(), null);
        assertNotEquals("Expected categoryReport category objects to not be null", category2.getCategory(), null);


        // testing category 1
        assertEquals("Expected category 1 to have a main category of 1", category1.getCategory().getCategoryID(),1,0.1);
        assertEquals("Expected category 1 to have total spending of 440", category1.getTotalSpending(),440.75,0.1);
        assertEquals("Expected category 1 to have num transactions of 4", category1.getNumTransactions(),4,0.1);
        assertEquals("Expected category 1 to have average transactions of 110.1875", category1.getAverageTransactions(),110.1875,0.1);
        assertEquals("Expected category 1 to have percentage of 19.73", category1.getPercentage(),19.73,0.1);


        // testing category 2
        assertEquals("Expected category 2 to have a main category of 2", category2.getCategory().getCategoryID(),2,0.1);
        assertEquals("Expected category 2 to have total spending of 416.74", category2.getTotalSpending(),416.74,0.1);
        assertEquals("Expected category 2 to have num transactions of 4", category2.getNumTransactions(),4,0.1);
        assertEquals("Expected category 2 to have average transactions of 104.185", category2.getAverageTransactions(),104.185,0.1);
        assertEquals("Expected category 2 to have percentage of 18.65", category2.getPercentage(),18.65,0.1);
    }



    @Test
    public void testSortByTotal()
    {
        ArrayList<MainCategory> test = generalReportHandler.sortByTotal(1, true);


        assertEquals("Testing 1st position of sort by total", test.get(0).getCategoryID(), 3,0.1);
        assertEquals("Testing 2nd position of sort by total", test.get(1).getCategoryID(), 1,0.1);
    }

    @Test
    public void testSortByPercent()
    {
        ArrayList<MainCategory> test = generalReportHandler.sortByPercent(1, true);


        assertEquals("Testing 1st position of sort by percent", test.get(0).getCategoryID(), 3,0.1);
        assertEquals("Testing 2nd position of sort by percent", test.get(1).getCategoryID(), 1,0.1);
    }


    @Test
    public void testSortByAverage()
    {
        ArrayList<MainCategory> test = generalReportHandler.sortByAverage(1, true);


        assertEquals("Testing 1st position of sort by average", test.get(0).getCategoryID(), 3,0.1);
        assertEquals("Testing 2nd position of sort by average", test.get(1).getCategoryID(), 1,0.1);
    }


    @Test(expected = InvalidCategoryException.class)
    public void testInvalidCategoryException() {
        generalReportHandler.getCategoryReport(1, -1);
    }


}
