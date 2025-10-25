package com.spenditure;



import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AccountManagementTest.class,
        CategoryTest.class,
        GeneralReportTest.class,
        GoalsTest.class,
        IllustrativeReportTest.class,
        ImageTest.class,
        TimeBaseReportTest.class,
        TransactionTest.class
})

public class AllSystemTests {


}
