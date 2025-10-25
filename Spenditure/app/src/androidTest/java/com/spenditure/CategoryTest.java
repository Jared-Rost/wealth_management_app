package com.spenditure;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.spenditure.application.Services;
import com.spenditure.logic.CategoryHandler;
import com.spenditure.logic.ITransactionHandler;
import com.spenditure.logic.TransactionHandler;
import com.spenditure.presentation.LoginActivity;
import com.spenditure.utility.TestUtility;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Category handler system test (Feature 2)
 * @author Bao Ngo
 * @version 22 Mar 2024
 */
@RunWith(AndroidJUnit4.class)
public class CategoryTest {
    private CategoryHandler categoryHandler;
    private ITransactionHandler transactionHandler;
    @Before
    public void setup(){

        ActivityScenario.launch(LoginActivity.class);
        categoryHandler = new CategoryHandler(Services.DEVELOPING_STATUS);
        transactionHandler = new TransactionHandler(Services.DEVELOPING_STATUS);
        TestUtility.setUpEnvirForReportTest(categoryHandler,transactionHandler,4);

        TestUtility.login("TestingUser1","12345");
        onView(withId(R.id.navigation_category)).perform(click());

    }
    @Test
    public void createCategory(){
        onView(withId(R.id.floatingActionButton_category)).perform(click());
        onView(withId(R.id.category_edittext)).perform(
                typeText("New test category"),
                ViewActions.closeSoftKeyboard()
        );
        onView(withId(R.id.category_save_btn)).perform(click());
        onView(withId(R.id.floatingActionButton_refresh)).perform(click());

        onView(withId(R.id.recyclerview_category)).perform(RecyclerViewActions.scrollToPosition(4));
        //Check if already added category displayed on screen
        onView(withId(R.id.recyclerview_category)).perform(RecyclerViewActions.actionOnItem(hasDescendant(withText("Food")), click()));
        onView(withId(R.id.recyclerview_category)).perform(RecyclerViewActions.actionOnItem(hasDescendant(withText("Grocery")), click()));
        onView(withId(R.id.recyclerview_category)).perform(RecyclerViewActions.actionOnItem(hasDescendant(withText("Hang out")), click()));
        //Check if the new category is added
        onView(withId(R.id.recyclerview_category)).perform(RecyclerViewActions.actionOnItem(hasDescendant(withText("New test category")), click()));

        TestUtility.cleanUpEnvir(categoryHandler,transactionHandler,4);
    }


}
