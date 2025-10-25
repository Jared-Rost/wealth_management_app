/**
 * GoalListViewActivity.java
 *
 * COMP3350 SECTION A02
 *
 * @author Jillian Friesen, 7889402
 * @date Friday, March 22, 2024
 *
 * PURPOSE:
 *  This file changes the layout when a new Goal is created.
 **/

package com.spenditure.presentation.Goals;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.spenditure.R;

public class GoalListViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_list_view);
    }
}