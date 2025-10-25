/**
 * ImageTest
 *
 * COMP3350 SECTION A02
 *
 * @author JR
 * @date Mar 23
 *
 * PURPOSE:
 *  System test for adding image (feature 5)
 *
 **/


package com.spenditure;



import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
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
import com.spenditure.logic.ICategoryHandler;
import com.spenditure.logic.ITransactionHandler;
import com.spenditure.logic.TransactionHandler;
import com.spenditure.logic.UserHandler;
import com.spenditure.presentation.LoginActivity;
import com.spenditure.utility.TestUtility;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ImageTest {

    private final int sleepTime = 1000;
    private ITransactionHandler transactionHandler;
    private ICategoryHandler categoryHandler;

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
        TestUtility.setUpEnvirForReportTest(categoryHandler,transactionHandler,4);

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
        TestUtility.cleanUpEnvir(categoryHandler,transactionHandler,4);
    }

    /**
     * newTransactionWithImage
     *
     * Creates a new transaction and adds an image, then views that transaction and verifies that the image is there
     * @returns void - NA
     */
    @Test
    public void newTransactionWithImage()
    {
        // this moves us from the home page to "Create transaction" page
        onView(withId(R.id.navigation_create_transaction)).perform(click());

        // fill in transaction name
        onView(withId(R.id.edittext_what_the_heck)).perform(
                typeText("Testing image"),
                ViewActions.closeSoftKeyboard()
        );

        // fill in date
        onView(withId(R.id.edittext_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2024, 5, 13));
        onView(withId(android.R.id.button1)).perform(click());

        // fill in place
        onView(withId(R.id.edittext_place)).perform(
                typeText("Trevor's closet"),
                ViewActions.closeSoftKeyboard()
        );

        // fill in amount
        onView(withId(R.id.edittext_amount)).perform(
                typeText("69"),
                ViewActions.closeSoftKeyboard()
        );

        // take picture
        onView(withId(R.id.button_take_image)).perform(click());

        // press capture
        onView(withId(R.id.button_take_image)).perform(click());

        // confirm
        onView(withId(R.id.button_confirm)).perform(click());

        // save transaction
        onView(withId(R.id.button_create_transaction)).perform(
                click()
        );

        // now we click over to view transactions
        onView(withId(R.id.navigation_view_transactions)).perform(click());

        // check database that it image is not null
        assert(transactionHandler.getTransactionByName(UserHandler.getUserID(), "Testing image").get(0).getImage() != null);

        // press on button to sort by newest first
        onView(withId(R.id.togglebutton_transaction_date_sort)).perform(click());

        // select top transaction
        onData(anything()).inAdapterView(withId(R.id.listview_transactions)).atPosition(0).perform(click());

        // click edit
        onView(withId(R.id.button_edit)).perform(click());

        // view image
        onView(withId(R.id.button_view_image)).perform(click());

        // exit image button_back
        onView(withId(R.id.button_back)).perform(click());

        // exit transaction edit screen
        onView(withId(R.id.navigation_create_transaction)).perform(click());


    }

    /**
     * addImageToTransaction
     *
     * Attempts to add an image to an existing transaction, then verifies that the image was added
     * @returns void - NA
     */
    @Test
    public void addImageToTransaction()
    {
        // this moves us from the home page to "Create transaction" page
        onView(withId(R.id.navigation_view_transactions)).perform(click());

        // select top transaction
        onData(anything()).inAdapterView(withId(R.id.listview_transactions)).atPosition(0).perform(click());

        // click edit
        onView(withId(R.id.button_edit)).perform(click());

        // take picture
        onView(withId(R.id.button_take_image)).perform(click());

        // press capture
        onView(withId(R.id.button_take_image)).perform(click());

        // confirm
        onView(withId(R.id.button_confirm)).perform(click());

        // view image
        onView(withId(R.id.button_view_image)).perform(click());

        // exit image button_back
        onView(withId(R.id.button_back)).perform(click());

        // press save button
        onView(withId(R.id.button_edit_transaction)).perform(click());

        // verify an image has been added
        assert(transactionHandler.getTransactionByName(UserHandler.getUserID(), "Morning Dons").get(0).getImage() != null);

        // now we click over to view transactions
        onView(withId(R.id.navigation_view_transactions)).perform(click());

        // select top transaction
        onData(anything()).inAdapterView(withId(R.id.listview_transactions)).atPosition(0).perform(click());

        // click edit
        onView(withId(R.id.button_edit)).perform(click());

        // view image
        onView(withId(R.id.button_view_image)).perform(click());

        // exit image button_back
        onView(withId(R.id.button_back)).perform(click());

        // exit transaction edit screen
        onView(withId(R.id.navigation_create_transaction)).perform(click());

    }
}
