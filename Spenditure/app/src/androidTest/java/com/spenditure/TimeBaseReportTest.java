package com.spenditure;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.spenditure.application.Services;
import com.spenditure.logic.CategoryHandler;
import com.spenditure.logic.ICategoryHandler;
import com.spenditure.logic.ITransactionHandler;
import com.spenditure.logic.TransactionHandler;
import com.spenditure.presentation.LoginActivity;
import com.spenditure.presentation.report.ViewReportActivity;
import com.spenditure.utility.TestUtility;
import com.spenditure.utility.ViewPagerSupporter;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Timebase report system test (Feature 4)
 * @author Bao Ngo
 * @version 22 Mar 2024
 */

@RunWith(AndroidJUnit4.class)
public class TimeBaseReportTest {
    private final int sleepTime = 5000;
    private ITransactionHandler transactionHandler;
    private ICategoryHandler categoryHandler;

    @Before
    public void setup(){
        ActivityScenario.launch(LoginActivity.class);
        SystemClock.sleep(sleepTime);

        transactionHandler = new TransactionHandler(Services.DEVELOPING_STATUS);
        categoryHandler = new CategoryHandler(Services.DEVELOPING_STATUS);
        TestUtility.setUpEnvirForReportTest(categoryHandler,transactionHandler,4);

        TestUtility.login("TestingUser1","12345");
    }

    @After
    public void teardown(){
        TestUtility.cleanUpEnvir(categoryHandler,transactionHandler,4);
    }


    @Test
    public void testSelectDate(){
        SystemClock.sleep(sleepTime);
        onView(withId(R.id.textview_customTime_percentage)).perform(ViewActions.scrollTo());
        onView(withId(R.id.button_report_from)).check(matches(isDisplayed()));

        TestUtility.setDate(R.id.button_report_from,2023,9,5);
        onView(withId(R.id.button_report_from)).check(matches(withText(containsString("9/5/2023"))));

        SystemClock.sleep(sleepTime);

        onView(withId(R.id.button_report_to)).check(matches(isDisplayed()));
        TestUtility.setDate(R.id.button_report_to,2024,3,4);
        onView(withId(R.id.button_report_to)).check(matches(withText(containsString("3/4/2024"))));

        onView(withId(R.id.button_get_report)).check(matches(isDisplayed()));
        onView(withId(R.id.button_get_report)).perform(click());



        SystemClock.sleep(sleepTime);

        onView(withId(R.id.textview_customTime_totalAmount)).check(matches(withText(containsString("500.95"))));
        onView(withId(R.id.textview_customTime_percentage)).check(matches(withText(containsString("66.14"))));
        onView(withId(R.id.textview_customTime_totalTrans)).check(matches(withText(containsString("1"))));
        onView(withId(R.id.textview_customTime_average)).check(matches(withText(containsString("500.95"))));

    }
    @Test
    public void testLastYearReport(){
        onView(withId(R.id.report_last_year)).perform(ViewActions.scrollTo());
        SystemClock.sleep(sleepTime);

        onView(withId(R.id.textview_lastYear_total)).check(matches(withText(containsString("751.4"))));
        onView(withId(R.id.textview_lastYear_percentage)).check(matches(withText(containsString("99.21"))));
        onView(withId(R.id.textview_lastYear_transactionsCount)).check(matches(withText(containsString("2"))));
        onView(withId(R.id.textview_lastYear_average)).check(matches(withText(containsString("375.7"))));
    }

    @Test
    public void testTimeBaseReport(){
        SystemClock.sleep(3000);
        onView(withId(R.id.gridlayout_timebase_report)).perform(ViewActions.scrollTo());
        onView(withId(R.id.gridlayout_timebase_report)).check(matches(isDisplayed()));

//        Test with report on last year breaking down into 12 months
        for(int i = 0; i < 4 ; i ++){
            onView(withId(R.id.gridlayout_timebase_report)).check(matches(isDisplayed()));
            onView(withId(R.id.gridlayout_timebase_report)).perform(swipeLeft());

        }

        //Check the report of May
        TestUtility.testSlideCard(R.id.gridlayout_timebase_report,"May","1 transactions","$250.5 CAD","$250.5 CAD","33.08%",2);
        //Check whether is all zero report of June
        onView(withId(R.id.gridlayout_timebase_report)).perform(swipeLeft());
        TestUtility.testSlideCard(R.id.gridlayout_timebase_report,"Jun","0 transactions","$0.0 CAD","$0.0 CAD","0.0%",2);


        //Test with report on last month breaking down into 4 weeks
        onView(withId(R.id.spinner_timebase_report)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(ViewReportActivity.TIME_BASE_OPTION[1]))).perform(click());
        SystemClock.sleep(5000);

        TestUtility.testSlideCard(R.id.gridlayout_timebase_report,"1st Week","0 transactions","$0.0 CAD","$0.0 CAD","0.0%",0);
        onView(withId(R.id.gridlayout_timebase_report)).perform(swipeLeft());
        TestUtility.testSlideCard(R.id.gridlayout_timebase_report,"2nd Week","0 transactions","$0.0 CAD","$0.0 CAD","0.0%",1);
        onView(withId(R.id.gridlayout_timebase_report)).perform(swipeLeft());
        TestUtility.testSlideCard(R.id.gridlayout_timebase_report,"3rd Week","0 transactions","$0.0 CAD","$0.0 CAD","0.0%",2);

    }
}
