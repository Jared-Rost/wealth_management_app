package com.spenditure;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

import android.os.SystemClock;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.spenditure.application.Services;
import com.spenditure.logic.CategoryHandler;
import com.spenditure.logic.ICategoryHandler;
import com.spenditure.logic.ITransactionHandler;
import com.spenditure.logic.TransactionHandler;
import com.spenditure.logic.UserHandler;
import com.spenditure.presentation.LoginActivity;
import com.spenditure.utility.TestUtility;

import org.junit.After;
import org.junit.Before;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * General report system test (Feature 3)
 * @author Bao Ngo
 * @version 22 Mar 2024
 */

@RunWith(AndroidJUnit4.class)
public class GeneralReportTest {
    private final int sleepTime = 2000;
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
    public void testGeneralReport() {
        SystemClock.sleep(sleepTime);

        assertEquals(4, UserHandler.getUserID());
        onView(withId(R.id.textview_general_total_spending)).check(matches(isDisplayed()));
        onView(withId(R.id.textview_summary_num_trans)).check(matches(isDisplayed()));

        onView(withId(R.id.textview_general_total_spending)).check(matches(withText(containsString("757.44"))));
        onView(withId(R.id.textview_summary_num_trans)).check(matches(withText(containsString("3"))));


    }

    @Test
    public void testCustomGenerral(){
        SystemClock.sleep(sleepTime);

        onView(withId(R.id.textview_custom_report_most)).check(matches(withText(containsString("Food"))));
        onView(withId(R.id.textview_custom_report_least)).check(matches(withText(containsString("Grocery"))));

        onView(withId(R.id.spinner_report)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Report by total"))).perform(click());
        SystemClock.sleep(sleepTime);
        onView(withId(R.id.textview_custom_report_most)).check(matches(withText(containsString("Food"))));
        onView(withId(R.id.textview_custom_report_least)).check(matches(withText(containsString("Grocery"))));

        onView(withId(R.id.spinner_report)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Report by percentage"))).perform(click());
        SystemClock.sleep(sleepTime);
        onView(withId(R.id.textview_custom_report_most)).check(matches(withText(containsString("Food"))));
        onView(withId(R.id.textview_custom_report_least)).check(matches(withText(containsString("Grocery"))));

    }

    @Test
    public void testCategoriesReport(){
        onView(withId(R.id.viewpager_report)).perform(scrollTo());
        onView(withId(R.id.viewpager_report)).check(matches(isDisplayed()));

        //Check the first item of viewpager
        TestUtility.testSlideCard(R.id.viewpager_report,"Grocery","1 transactions","$5.99 CAD","$5.99 CAD","0.8%",0);

        //Check the second item of viewpager
        onView(withId(R.id.viewpager_report)).check(matches(isDisplayed()));
        onView(withId(R.id.viewpager_report)).perform(swipeLeft());
        SystemClock.sleep(sleepTime);

        TestUtility.testSlideCard(R.id.viewpager_report,"Food","1 transactions","$500.95 CAD","$500.95 CAD","66.14%",1);

        onView(withId(R.id.viewpager_report)).check(matches(isDisplayed()));
        onView(withId(R.id.viewpager_report)).perform(swipeLeft());
        SystemClock.sleep(sleepTime);

    }


}
