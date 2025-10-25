package com.spenditure.utility;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;

import android.os.SystemClock;
import android.widget.DatePicker;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.matcher.ViewMatchers;

import com.spenditure.R;
import com.spenditure.logic.ICategoryHandler;
import com.spenditure.logic.ITransactionHandler;
import com.spenditure.object.DateTime;
import com.spenditure.object.MainCategory;
import com.spenditure.object.Transaction;

import java.util.List;

/**
 * Description: Testing utility
 * @author Bao Ngo
 * @version 22 Mar 2024
 */

public abstract class TestUtility {
    public static void login(String username,String password){
        onView(ViewMatchers.withId(R.id.edittext_username)).perform(
                typeText(username),
                ViewActions.closeSoftKeyboard()
        );
        onView(withId(R.id.edittext_current_password)).perform(
                typeText(password),
                ViewActions.closeSoftKeyboard()
        );
        onView(withId(R.id.button_login)).perform(click());
        SystemClock.sleep(10000);
    }

    public static void logout(){
        onView(withId(R.id.navigation_user)).perform(click());
        onView(withId(R.id.button_logout)).perform(click());
    }

    public static void setDate(int buttonStartDatePicker, int year, int monthOfYear, int dayOfMonth) {
        onView(withId(buttonStartDatePicker)).perform(click());
        SystemClock.sleep(1000);
        onView(isAssignableFrom(DatePicker.class))
                .perform(PickerActions.setDate(year, monthOfYear, dayOfMonth));
        onView(withId(android.R.id.button1)).perform(click());
    }

    public static void setUpEnvirForReportTest(ICategoryHandler categoryHandler, ITransactionHandler transactionHandler, int userID){
        //Refresh this user for testing purpose
        List<Transaction>transactionList = transactionHandler.getAllTransactions(4);
        for (Transaction currTra : transactionList){
            transactionHandler.deleteTransaction(currTra);
        }

        List<MainCategory> mainCategoryList = categoryHandler.getAllCategory(userID);
        for (MainCategory currCat : mainCategoryList){
            categoryHandler.deleteCategory(currCat.getCategoryID());

        }
        //Populating data for testing
        MainCategory grocery = categoryHandler.addCategory("Grocery",4);
        MainCategory food  = categoryHandler.addCategory("Food",4);
        MainCategory hangOut = categoryHandler.addCategory("Hang out",4);

        transactionHandler.addTransaction( 4, "Morning Dons", new DateTime(2000,1,1,1,1, 1),
                "Mcdonalds",5.99, "was luke warm today, 2/10", true, null, grocery.getCategoryID());
        transactionHandler.addTransaction( 4, "Star Wars Rebels merch", new DateTime(2024,2,1,16,59, 59),
                "Toys R Us",500.95, "Sabine looking kinda nice O_o", true, null, food.getCategoryID());
        transactionHandler.addTransaction(4, "Shopping spree at the mall", new DateTime(2023, 5, 20, 15, 30, 30),
                "Mall", 250.50, "Bought clothes and accessories", true, null, hangOut.getCategoryID());


    }

    public static void cleanUpEnvir(ICategoryHandler categoryHandler, ITransactionHandler transactionHandler, int userID){
        //Clean-up testing data
        List<MainCategory> mainCategoryList = categoryHandler.getAllCategory(userID);
        for (MainCategory currCat : mainCategoryList){
            categoryHandler.deleteCategory(currCat.getCategoryID());

        }
        List<Transaction> transactionList = transactionHandler.getAllTransactions(4);

        for (Transaction currTra : transactionList){
            transactionHandler.deleteTransaction(currTra);

        }

        //Log-out to refresh the application
        logout();
    }

    public static void testSlideCard(int parent,String title,String transCount,String total,String average, String percentage,int index){
        onView(allOf(withId(R.id.slide_tittle), isDescendantOfA(ViewPagerSupporter.getChildOf(withId(parent),index))))
                .check(matches(withText(title)));
        onView(allOf(withId(R.id.textview_catReport_transactionsCount), isDescendantOfA(ViewPagerSupporter.getChildOf(withId(parent),index))))
                .check(matches(withText(transCount)));

        onView(allOf(withId(R.id.textview_catReport_total), isDescendantOfA(ViewPagerSupporter.getChildOf(withId(parent),index))))
                .check(matches(withText(total)));

        onView(allOf(withId(R.id.textview_catReport_percentage), isDescendantOfA(ViewPagerSupporter.getChildOf(withId(parent),index))))
                .check(matches(withText(percentage)));

        onView(allOf(withId(R.id.textview_catReport_average), isDescendantOfA(ViewPagerSupporter.getChildOf(withId(parent),index))))
                .check(matches(withText(average)));

    }
}
