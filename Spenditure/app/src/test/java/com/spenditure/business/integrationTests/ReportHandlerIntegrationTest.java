package com.spenditure.business.integrationTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.spenditure.application.Services;
import com.spenditure.database.CategoryPersistence;
import com.spenditure.logic.TimeBaseReportHandler;
import com.spenditure.logic.UserHandler;
import com.spenditure.object.CategoryStatistics;
import com.spenditure.object.DateTime;
import com.spenditure.object.MainCategory;
import com.spenditure.object.Report;
import com.spenditure.utils.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class ReportHandlerIntegrationTest {

    private TimeBaseReportHandler reportManager;
    private UserHandler userHandler;
    private File tempDB;

    @Before
    public void setup() throws IOException, NoSuchAlgorithmException {

        this.tempDB = TestUtils.copyDB();
        this.reportManager = new TimeBaseReportHandler(false);
        userHandler = new UserHandler(false);
        userHandler.login("Me", "123");

        assertNotNull(this.reportManager);
    }

    @After
    public void teardown() {
        userHandler.logout();
        reportManager = null;
        tempDB = null;
    }



    @Test
    public void testCategoryTable() {
        CategoryPersistence dataAccessCategory = Services.getCategoryPersistence(false);

        List<MainCategory> categories = dataAccessCategory.getAllCategory(1);

        int expected = 3;

        for(int i=0; i<categories.size(); i++) {
            if(categories.get(i).getName().equalsIgnoreCase("bills"))
                expected = 4;
        }

        assertEquals(expected, categories.size());
    }


    @Test
    public void testReportOnLastYear() {

        Report report = reportManager.reportBackOneYear(1, new DateTime(2024, 03, 04));

        assertEquals("13 transactions since the beginning of 2023", 13, report.getNumTrans());
        assertEquals("Average is 171.38", 171.38, report.getAvgTransSize(), 0.1);
        assertEquals("Standard deviation is 120.2", 120.2, report.getStdDev(), 0.1);

        List<CategoryStatistics> list = report.getCategoryStatisticsList();

        CategoryStatistics categoryStatistics = list.get(0);
        assertEquals("Category 1 should be first", 1, categoryStatistics.getCategory().getCategoryID());
        assertEquals("Total of items should be 440.75", 440.75, categoryStatistics.getTotal(), 0.1);
        assertEquals("Average should be 110.12", 110.12, categoryStatistics.getAverage(), 0.1);
        assertEquals("19.8% of total amount", 19.8, categoryStatistics.getPercent(), 0.1);

        categoryStatistics = list.get(1);
        assertEquals("Category 2 should be next", 2, categoryStatistics.getCategory().getCategoryID());
        assertEquals("Total of items should be 410.75", 410.75, categoryStatistics.getTotal(), 0.1);
        assertEquals("Average should be 136.92", 136.92, categoryStatistics.getAverage(), 0.1);
        assertEquals("18.4% of total amount", 18.4, categoryStatistics.getPercent(), 0.1);

        categoryStatistics = list.get(2);
        assertEquals("Category 3 should be last", 3, categoryStatistics.getCategory().getCategoryID());
        assertEquals("Total of items should be 1,376.45", 1376.45, categoryStatistics.getTotal(), 0.1);
        assertEquals("Average should be 229.41", 229.41, categoryStatistics.getAverage(), 0.1);
        assertEquals("61.8% of total amount", 61.8, categoryStatistics.getPercent(), 0.1);
    }


    //Tests: reportOnUserProvidedDates
    @Test
    public void testReportOnUserProvidedDates() {

        DateTime start = new DateTime(2023, 9, 5);
        DateTime end = new DateTime(2024, 3, 4);

        Report report = reportManager.reportOnUserProvidedDates(1, start, end);

        assertEquals("Should only be 8 reports between Sept. 5, 2023 and Mar. 1, 2024", 8, report.getNumTrans());
        assertEquals("Average of all transactions should be 165.8", 165.8, report.getAvgTransSize(), 0.1);
        assertEquals("Standard deviation should be 138.5", 138.5, report.getStdDev(), 0.1);
        assertEquals("59.3% of all transactions in db", 59.3, report.getPercent(), 0.1);

        List<CategoryStatistics> list = report.getCategoryStatisticsList();

        CategoryStatistics categoryStatistics = list.get(0);

        assertEquals("Category 1 should be first", 1, categoryStatistics.getCategory().getCategoryID());
        assertEquals("Total of 2 items should be 240.5", 240.5, categoryStatistics.getTotal(), 0.1);
        assertEquals("Average should be 120.25", 120.25, categoryStatistics.getAverage(), 0.1);
        assertEquals("18.1% of total amount", 18.1, categoryStatistics.getPercent(), 0.1);

        categoryStatistics = list.get(1);
        assertEquals("Category 2 should be next", 2, categoryStatistics.getCategory().getCategoryID());
        assertEquals("Total of 2 items should be 260", 260, categoryStatistics.getTotal(), 0.1);
        assertEquals("Average should be 130", 130, categoryStatistics.getAverage(), 0.1);
        assertEquals("19.6% of total amount", 19.6, categoryStatistics.getPercent(), 0.1);

        categoryStatistics = list.get(2);
        assertEquals("Category 3 should be last", 3, categoryStatistics.getCategory().getCategoryID());
        assertEquals("Total of 4 items should be 825.95", 825.95, categoryStatistics.getTotal(), 0.1);
        assertEquals("Average should be 206.49", 206.49, categoryStatistics.getAverage(), 0.1);
        assertEquals("62.3% of total amount", 62.3, categoryStatistics.getPercent(), 0.1);

    }

    //Tests: reportOnLastMonthByWeek
    @Test
    public void testReportOnLastMonthByWeek() {

        ArrayList<Report> reports = reportManager.reportBackOneMonthByWeek(1, new DateTime(2023, 10, 06));

        assertEquals("There should be 4 reports, for 4 weeks in a month", 4, reports.size());

        Report week = reports.get(0);
        assertEquals("There should only be 2 transactions", 2, week.getNumTrans());
        assertEquals("The average is 175 for those two", 175, week.getAvgTransSize(), 0.1);

        week = reports.get(1);
        assertEquals("There is only 1 transaction for this week", 1, week.getNumTrans());
        assertEquals("It was $75", 75, week.getAvgTransSize(), 0.1);

        week = reports.get(2);
        assertEquals("There are 2 here since all transactions are at 00H00M", 2, week.getNumTrans());
        assertEquals("Average between these 2 is 75.25", 75.25, week.getAvgTransSize(), 0.1);

        week = reports.get(3);
        assertEquals("There are 2 here", 2, week.getNumTrans());
        assertEquals("Average was 145.25", 145.25, week.getAvgTransSize(), 0.1);
    }

    //Tests: reportOnLastYearByMonth
    @Test
    public void testReportOnLastYearByMonth() {

        ArrayList<Report> reports = reportManager.reportBackOnLastYearByMonth(1, new DateTime(2024, 03, 04));

        assertEquals("There should be 12 reports, one per month", 12, reports.size());

        Report month = reports.get(4);
        CategoryStatistics categoryStatistics = month.getCategoryStatisticsList().get(2);

        assertEquals("Only 1 transaction should be present", 1, month.getNumTrans());
        assertEquals("Amount was 250.50", 250.50, categoryStatistics.getTotal(), 0.1);

        month = reports.get(8);
        categoryStatistics = month.getCategoryStatisticsList().get(2);
        assertEquals("6 transactions in Sept. 2023", 6, month.getNumTrans());
        assertEquals("Total for cat. 3 transactions is 325", 325, categoryStatistics.getTotal(), 0.1);

    }

}
