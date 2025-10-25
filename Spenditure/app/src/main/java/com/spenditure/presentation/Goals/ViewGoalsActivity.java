/**
 * ViewGoalsActivity.java
 *
 * COMP3350 SECTION A02
 *
 * @author Jillian Friesen, 7889402
 * @date Friday, March 22, 2024
 *
 * PURPOSE:
 *  This file contains all the event handlers and UI management for the View Goals
 *  activity where users can view a list of all their goals. The UI allows users
 *  to create or delete goals.
 **/

package com.spenditure.presentation.Goals;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.spenditure.R;
import com.spenditure.application.Services;
import com.spenditure.logic.GoalHandler;
import com.spenditure.logic.IGoalHandler;
import com.spenditure.logic.UserHandler;
import com.spenditure.logic.exceptions.InvalidGoalException;
import com.spenditure.object.Goal;
import com.spenditure.presentation.BottomNavigationHandler;

import java.util.List;

public class ViewGoalsActivity extends AppCompatActivity {
    private static IGoalHandler goalHandler = null;
    private List<Goal> goals;
    private int currentIdSelected = -1;
    private CustomGoalAdapter adaptor;  // Adaptor to display the Goals in a list
    private ListView goalsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_goals);

        if (goalHandler == null) {
            goalHandler = new GoalHandler(Services.DEVELOPING_STATUS);
        }
//
        goals = goalHandler.getGoalsForUserID(UserHandler.getUserID());

        // Create an adaptor to format transactions in the list view
        goalsListView = findViewById(R.id.listview_goals);
        adaptor = new CustomGoalAdapter(goals, this);
        goalsListView.setAdapter(adaptor);

        // Trigger when list item is selected, save the ID of the selected item
        goalsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentIdSelected = (int) id;
            }
        });

        setUpAddButton();
        setUpDeleteButton();
        setupBackButton();
        navBarHandling();
    }

    // Set up the Add Goal button
    private void setUpAddButton() {
        FloatingActionButton button = findViewById(R.id.floatingActionButton_new_goal);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    // Open the window to create a new Goal
                    Intent newIntent = new Intent(getApplicationContext(), CreateGoalActivity.class);
                    startActivity(newIntent);
                } catch (InvalidGoalException e){
                    Toast.makeText(ViewGoalsActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // Set up the Delete Goal button
    private void setUpDeleteButton() {
        FloatingActionButton button = findViewById(R.id.floatingActionButton_delete);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (currentIdSelected < 0) {
                    // No item selected
                    Toast.makeText(ViewGoalsActivity.this, "No Goal selected",Toast.LENGTH_LONG).show();
                    return;
                }

                // Deselect any items
                goalsListView.clearChoices();

                // Get and delete the chosen goal
                Goal toDelete = goalHandler.getGoalByID(currentIdSelected);
                goalHandler.deleteGoal(toDelete.getGoalID());

                // Refresh the list view
                adaptor = new CustomGoalAdapter(goalHandler.getGoalsForUserID(UserHandler.getUserID()), getBaseContext());
                goalsListView.setAdapter(adaptor);

                currentIdSelected = -1;
            }
        });
    }

    // Set up the Back button
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