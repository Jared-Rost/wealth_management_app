/**
 * CreateGoalActivity.java
 * <p>
 * COMP3350 SECTION A02
 *
 * @author Jillian Friesen, 7889402
 * @date Friday, March 22, 2024
 * <p>
 * PURPOSE:
 * This file handles the the Create Goal screen where users can create a new financial goal.
 **/

package com.spenditure.presentation.Goals;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.spenditure.R;
import com.spenditure.application.Services;
import com.spenditure.logic.CategoryHandler;
import com.spenditure.logic.GoalHandler;
import com.spenditure.logic.ICategoryHandler;
import com.spenditure.logic.IGoalHandler;
import com.spenditure.logic.UserHandler;
import com.spenditure.logic.exceptions.InvalidGoalException;
import com.spenditure.object.DateTime;
import com.spenditure.object.MainCategory;
import com.spenditure.presentation.BottomNavigationHandler;
import com.spenditure.presentation.category.CustomCategorySpinnerAdapter;

import java.time.LocalDateTime;

public class CreateGoalActivity extends AppCompatActivity {
    private CustomCategorySpinnerAdapter adapter;   // Adaptor to display the Categories in a drop down menu
    private DateTime selectedDate;
    private int userID;
    private IGoalHandler goalHandler;
    private ICategoryHandler categoryHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_goal);

        this.userID = UserHandler.getUserID();

        goalHandler = new GoalHandler(Services.DEVELOPING_STATUS);
        categoryHandler = new CategoryHandler(Services.DEVELOPING_STATUS);

        setUpCreateButton();
        setUpCategories();
        setUpDatePicker();
        setupBackButton();
        navBarHandling();
    }

    // Set up the Create Goal button
    private void setUpCreateButton() {
        Button button = findViewById(R.id.button_create_goal);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    // Call helper method
                    createGoal();

                    // Return to the View Goals window
                    startActivity(new Intent(getApplicationContext(), ViewGoalsActivity.class));
                } catch (InvalidGoalException e) {
                    Toast.makeText(CreateGoalActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(CreateGoalActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // Helper method: Gathers user input and sends it to Logic to create a new Goal
    private void createGoal() {
        // Parse all the user fields
        EditText goalName = findViewById(R.id.edittext_goal_name);
        EditText amount = findViewById(R.id.edittext_amount);
        int intAmount = (amount.getText().length() == 0) ? -1 : Integer.parseInt(amount.getText().toString());
        Spinner category = findViewById(R.id.spinner_categories);
        MainCategory cat = adapter.getItem(category.getSelectedItemPosition());

        // Create the new goal object
        goalHandler.createGoal(
                userID,
                goalName.getText().toString(),
                selectedDate,
                intAmount,
                cat.getCategoryID());
    }

    // Set up the drop down menu that displays the Categories
    private void setUpCategories() {
        Spinner categories = findViewById(R.id.spinner_categories);

        // Create adapter to display the categories
        adapter = new CustomCategorySpinnerAdapter(categoryHandler.getAllCategory(userID), this);
        categories.setAdapter(adapter);
    }

    // Set up the date picker
    private void setUpDatePicker() {
        EditText dateField = findViewById(R.id.edittext_date);
        // Default to today's date
        LocalDateTime today = LocalDateTime.now();
        selectedDate = new DateTime(today.getYear(), today.getMonthValue()-1, today.getDayOfMonth());

        // Create event for when a new date is selected
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                selectedDate = new DateTime(year, month+1, day);
                dateField.setText(selectedDate.toString());
            }
        };

        // Create event for when the date field is selected
        dateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(
                        CreateGoalActivity.this,
                        date,
                        selectedDate.getYear(),
                        selectedDate.getMonth(),
                        selectedDate.getDay()
                );

                dialog.show();
            }
        });
    }

    // Set up the Back button to return to the previous activity
    private void setupBackButton() {
        FloatingActionButton backButton = findViewById(R.id.floatingActionButton_back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Return to the calling activity
                finish();
            }
        });
    }

    // Handle the bottom navigation bar
    private void navBarHandling(){
        BottomNavigationView navView = findViewById(R.id.nav_view);

        BottomNavigationHandler navigationHandler = new BottomNavigationHandler();

        navView.setOnItemSelectedListener((item -> {
            Class<? extends AppCompatActivity> newActivity = navigationHandler.select(item.getItemId());
            if(newActivity != null){
                startActivity(new Intent(getApplicationContext(), newActivity));
                return true;
            }
            return false;
        }));


    }
}