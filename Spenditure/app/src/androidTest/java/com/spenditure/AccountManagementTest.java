/**
 * AccountManagementTest
 *
 * COMP3350 SECTION A02
 *
 * @author JR
 * @date Mar 23
 *
 * PURPOSE:
 *  System test for account management (feature 6)
 *
 **/


package com.spenditure;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.os.SystemClock;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.spenditure.application.Services;
import com.spenditure.logic.CategoryHandler;
import com.spenditure.logic.ICategoryHandler;
import com.spenditure.logic.ITransactionHandler;
import com.spenditure.logic.TransactionHandler;
import com.spenditure.presentation.LoginActivity;
import com.spenditure.utility.TestUtility;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AccountManagementTest
{
    private final int sleepTime = 10000;
    private ITransactionHandler transactionHandler;
    private ICategoryHandler categoryHandler;

    /**
     * setup
     *
     * Sets up the test user account by resetting the category/transaction database, and launching the log in screen
     * @returns void - NA
     */
    @Before
    public void setup(){
        ActivityScenario.launch(LoginActivity.class);
        SystemClock.sleep(sleepTime);

        transactionHandler = new TransactionHandler(Services.DEVELOPING_STATUS);
        categoryHandler = new CategoryHandler(Services.DEVELOPING_STATUS);
        TestUtility.setUpEnvirForReportTest(categoryHandler,transactionHandler,4);

    }

    /**
     * teardown
     *
     * Resets the test user account by deleting everything in the database and logging out
     * @returns void - NA
     */
    @After
    public void teardown(){
        TestUtility.cleanUpEnvir(categoryHandler,transactionHandler,4);
    }

    /**
     * changePassword
     *
     * Logs in and attempts to change the username and password of current user. Then it logs out, logs back in, and changes it back to what it was before
     * @returns void - NA
     */
    @Test
    public void changePassword()
    {
        // login
        TestUtility.login("TestingUser1","12345");

        // move to profile management section
        onView(withId(R.id.navigation_user)).perform(click());

        // edit username
        onView(withId(R.id.edittext_username)).perform(
                replaceText("TestingUser1New"),
                ViewActions.closeSoftKeyboard()
        );

        // enter old password
        onView(withId(R.id.edittext_current_password)).perform(
                replaceText("12345"),
                ViewActions.closeSoftKeyboard()
        );

        // enter new password
        onView(withId(R.id.edittext_new_password)).perform(
                typeText("54321"),
                ViewActions.closeSoftKeyboard()
        );

        // save changes
        onView(withId(R.id.button_save_changes)).perform(click());

        // log out
        onView(withId(R.id.button_logout)).perform(click());

        // log back in
        onView(withId(R.id.edittext_username)).perform(
                typeText("TestingUser1New"),
                ViewActions.closeSoftKeyboard()
        );
        onView(withId(R.id.edittext_current_password)).perform(
                typeText("54321"),
                ViewActions.closeSoftKeyboard()
        );
        onView(withId(R.id.button_login)).perform(click());


        // now change password back to what it was before


        // move to profile management section
        onView(withId(R.id.navigation_user)).perform(click());

        // edit username
        onView(withId(R.id.edittext_username)).perform(
                replaceText("TestingUser1"),
                ViewActions.closeSoftKeyboard()
        );

        // enter old password
        onView(withId(R.id.edittext_current_password)).perform(
                replaceText("54321"),
                ViewActions.closeSoftKeyboard()
        );

        // enter new password
        onView(withId(R.id.edittext_new_password)).perform(
                typeText("12345"),
                ViewActions.closeSoftKeyboard()
        );

        // save changes
        onView(withId(R.id.button_save_changes)).perform(click());


    }

    /**
     * newAccount
     *
     * Attempts to create a new account from log in page then log in with that account
     * @returns void - NA
     */
    @Test
    public void newAccount()
    {
        // create new account
        onView(withId(R.id.floatingActionButton_register)).perform(click());

        SystemClock.sleep(sleepTime);

        // edit username
        onView(withId(R.id.edittext_username)).perform(
                typeText("NewUserName"),
                ViewActions.closeSoftKeyboard()
        );

        // enter new password
        onView(withId(R.id.edittext_current_password)).perform(
                typeText("NewPassword123"),
                ViewActions.closeSoftKeyboard()
        );

        // enter new email
        onView(withId(R.id.edittext_email)).perform(
                typeText("johnny.appleseed@domain.com"),
                ViewActions.closeSoftKeyboard()
        );

        // select question
        onView(withId(R.id.spinner_security_question)).perform(click());
        onView(allOf(withText("best comp class"))).perform(click());

        // enter question answer
        onView(withId(R.id.edittext_question_answer)).perform(
                typeText("Rob's class"),
                ViewActions.closeSoftKeyboard()
        );

        // save changes
        onView(withId(R.id.button_register)).perform(click());

        // we are now logged in

        // move to profile management section
        onView(withId(R.id.navigation_user)).perform(click());

        // log out
        onView(withId(R.id.button_logout)).perform(click());

        // log back in
        onView(withId(R.id.edittext_username)).perform(
                typeText("NewUserName"),
                ViewActions.closeSoftKeyboard()
        );
        onView(withId(R.id.edittext_current_password)).perform(
                typeText("NewPassword123"),
                ViewActions.closeSoftKeyboard()
        );
        onView(withId(R.id.button_login)).perform(click());

    }




}
