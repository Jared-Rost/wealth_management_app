/**
 * TransactionsListViewActivity.java
 *
 * COMP3350 SECTION A02
 *
 * @author Jillian Friesen, 7889402
 * @date Tuesday, February 7, 2024
 *
 * PURPOSE:
 *  This file changes the layout when a new transaction is created
 **/

package com.spenditure.presentation.transaction;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.spenditure.R;

public class TransactionsListViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions_list_view);
    }
}