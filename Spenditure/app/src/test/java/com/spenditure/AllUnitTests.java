package com.spenditure;

import com.spenditure.business.unitTests.GoalExceptionTest;
import com.spenditure.business.unitTests.GoalHandlerTest;
import com.spenditure.business.unitTests.SecurityQuestionHandlerTest;
import com.spenditure.business.unitTests.UserHandlerExceptionTest;
import com.spenditure.business.unitTests.UserHandlerTest;
import com.spenditure.business.unitTests.CategoryHandlerTest;
import com.spenditure.business.unitTests.CategoryExceptionTest;

import com.spenditure.business.unitTests.DateTimeAdjusterTest;
import com.spenditure.business.unitTests.GeneralReportHandlerTest;
import com.spenditure.business.unitTests.TimeBaseReportHandlerTest;
import com.spenditure.business.unitTests.TransactionExceptionTest;
import com.spenditure.business.unitTests.TransactionHandlerTest;
//import com.spenditure.business.unitTests.UserHandlerExceptionTest;
import com.spenditure.objects.MainCategoryTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * All unit tests
 * @author Bao Ngo
 * @version 06 Feb 2024
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        CategoryHandlerTest.class,
        MainCategoryTest.class,
        CategoryExceptionTest.class,
        TransactionExceptionTest.class,
        TransactionHandlerTest.class,
        TimeBaseReportHandlerTest.class,
        UserHandlerTest.class,
        GeneralReportHandlerTest.class,
        UserHandlerExceptionTest.class,
        DateTimeAdjusterTest.class,
        GoalExceptionTest.class,
        GoalHandlerTest.class,
        SecurityQuestionHandlerTest.class
})
public class AllUnitTests {
}
