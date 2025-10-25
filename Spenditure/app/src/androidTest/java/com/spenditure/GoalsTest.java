/**
 * GoalsTest
 *
 * COMP3350 SECTION A02
 *
 * @author JR
 * @date Mar 23
 *
 * PURPOSE:
 *  System test for financial goal tracker (feature 7)
 *
 **/

package com.spenditure;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.anything;

import android.os.SystemClock;
import android.widget.DatePicker;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;


import com.spenditure.application.Services;
import com.spenditure.logic.CategoryHandler;
import com.spenditure.logic.GoalHandler;
import com.spenditure.logic.ICategoryHandler;
import com.spenditure.logic.IGoalHandler;
import com.spenditure.logic.ITransactionHandler;
import com.spenditure.logic.TransactionHandler;
import com.spenditure.object.Goal;
import com.spenditure.presentation.LoginActivity;
import com.spenditure.utility.TestUtility;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class GoalsTest {

    private final int sleepTime = 1000;
    private ITransactionHandler transactionHandler;
    private ICategoryHandler categoryHandler;
    private IGoalHandler goalHandler;

    /**
     * setup
     *
     * Sets up the test user account by resetting the category/transaction database, launching the log in screen, and logging in
     * @returns void - NA
     */
    @Before
    public void setup(){
        ActivityScenario.launch(LoginActivity.class);
        SystemClock.sleep(sleepTime);

        transactionHandler = new TransactionHandler(Services.DEVELOPING_STATUS);
        categoryHandler = new CategoryHandler(Services.DEVELOPING_STATUS);
        goalHandler = new GoalHandler(Services.DEVELOPING_STATUS);
        TestUtility.setUpEnvirForReportTest(categoryHandler,transactionHandler,4);

        // remove all goals in db
        List<Goal> goalList = goalHandler.getGoalsForUserID(4);
        for (Goal currGoal : goalList){
            goalHandler.deleteGoal(currGoal.getGoalID());
        }

        TestUtility.login("TestingUser1","12345");
    }

    /**
     * teardown
     *
     * Resets the test user account by deleting everything in the database and logging out
     * @returns void - NA
     */
    @After
    public void teardown(){
        // remove all goals in db
        List<Goal> goalList = goalHandler.getGoalsForUserID(4);
        for (Goal currGoal : goalList){
            goalHandler.deleteGoal(currGoal.getGoalID());
        }

        TestUtility.cleanUpEnvir(categoryHandler,transactionHandler,4);

    }

    /**
     * createGoal
     *
     * Attempts to create a new goal and then verifies that goal was created
     * @returns void - NA
     */
    @Test
    public void createGoal()
    {
        // this moves us from the home page to user profile page
        onView(withId(R.id.navigation_user)).perform(click());

        // press button to move to goals page
        onView(withId(R.id.button_goals)).perform(click());

        // press button to create a goal
        onView(withId(R.id.floatingActionButton_new_goal)).perform(click());

        // fill in name
        onView(withId(R.id.edittext_goal_name)).perform(
                typeText("Spend less on needy gf"),
                ViewActions.closeSoftKeyboard()
        );

        // fill in date
        onView(withId(R.id.edittext_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2024, 1, 13));
        onView(withId(android.R.id.button1)).perform(click());

        // fill in amount
        onView(withId(R.id.edittext_amount)).perform(
                typeText("6900"),
                ViewActions.closeSoftKeyboard()
        );

        // fill in category
        onView(withId(R.id.spinner_categories)).perform(click());
        onView(allOf(withText("Hang out"))).perform(click());

        // press button to create the goal
        onView(withId(R.id.button_create_goal)).perform(click());

        // check that it is displayed
        onData(anything()).inAdapterView(withId(R.id.listview_goals)).atPosition(0).
                onChildView(withId(R.id.textview_goal_name)).
                check(matches(withText("Spend less on needy gf")));

        onData(anything()).inAdapterView(withId(R.id.listview_goals)).atPosition(0).
                onChildView(withId(R.id.textview_amount)).
                check(matches(withText("$6900")));

        onData(anything()).inAdapterView(withId(R.id.listview_goals)).atPosition(0).
                onChildView(withId(R.id.textview_category)).
                check(matches(withText("Hang out")));

        onData(anything()).inAdapterView(withId(R.id.listview_goals)).atPosition(0).
                onChildView(withId(R.id.textview_completion_date)).
                check(matches(withText("Complete by: Jan 13 2024, 0:000")));

        // verify that it exists in the database
        assert(goalHandler.getGoalsForUserID(4).get(0).getGoalName().equals("Spend less on needy gf"));
        assert(goalHandler.getGoalsForUserID(4).get(0).getSpendingGoal() == 6900);

    }

    /**
     * deleteGoal
     *
     * Creates 2 goals, deletes one of them, then verifies that it was deleted and the UI was updated
     * @returns void - NA
     */
    @Test
    public void deleteGoal()
    {
        // this moves us from the home page to user profile page
        onView(withId(R.id.navigation_user)).perform(click());

        // press button to move to goals page
        onView(withId(R.id.button_goals)).perform(click());



        // create two goals

        // press button to create a goal
        onView(withId(R.id.floatingActionButton_new_goal)).perform(click());

        // fill in name
        onView(withId(R.id.edittext_goal_name)).perform(
                typeText("Goal1"),
                ViewActions.closeSoftKeyboard()
        );

        // fill in date
        onView(withId(R.id.edittext_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2024, 1, 13));
        onView(withId(android.R.id.button1)).perform(click());

        // fill in amount
        onView(withId(R.id.edittext_amount)).perform(
                typeText("111"),
                ViewActions.closeSoftKeyboard()
        );

        // fill in category
        onView(withId(R.id.spinner_categories)).perform(click());
        onView(allOf(withText("Hang out"))).perform(click());

        // press button to create the goal
        onView(withId(R.id.button_create_goal)).perform(click());


        // press button to create a goal
        onView(withId(R.id.floatingActionButton_new_goal)).perform(click());

        // fill in name
        onView(withId(R.id.edittext_goal_name)).perform(
                typeText("Goal2"),
                ViewActions.closeSoftKeyboard()
        );

        // fill in date
        onView(withId(R.id.edittext_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2024, 1, 13));
        onView(withId(android.R.id.button1)).perform(click());

        // fill in amount
        onView(withId(R.id.edittext_amount)).perform(
                typeText("222"),
                ViewActions.closeSoftKeyboard()
        );

        // fill in category
        onView(withId(R.id.spinner_categories)).perform(click());
        onView(allOf(withText("Food"))).perform(click());

        // press button to create the goal
        onView(withId(R.id.button_create_goal)).perform(click());


        // delete first goal

        // click on first goal
        onData(anything()).inAdapterView(withId(R.id.listview_goals)).atPosition(0).perform(click());

        // delete first goal
        onView(withId(R.id.floatingActionButton_delete)).perform(click());


        // check to see whether new top goal is what we expect

        onData(anything()).inAdapterView(withId(R.id.listview_goals)).atPosition(0).
                onChildView(withId(R.id.textview_goal_name)).
                check(matches(withText("Goal2")));

        onData(anything()).inAdapterView(withId(R.id.listview_goals)).atPosition(0).
                onChildView(withId(R.id.textview_amount)).
                check(matches(withText("$222")));

        onData(anything()).inAdapterView(withId(R.id.listview_goals)).atPosition(0).
                onChildView(withId(R.id.textview_category)).
                check(matches(withText("Food")));

        onData(anything()).inAdapterView(withId(R.id.listview_goals)).atPosition(0).
                onChildView(withId(R.id.textview_completion_date)).
                check(matches(withText("Complete by: Jan 13 2024, 0:000")));


        // check in db to see if one was deleted
        assert(goalHandler.getGoalsForUserID(4).size() == 1);


    }

}
