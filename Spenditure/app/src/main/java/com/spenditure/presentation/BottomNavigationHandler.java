package com.spenditure.presentation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.spenditure.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.spenditure.presentation.category.ViewCategoryActivity;
import com.spenditure.presentation.report.ViewReportActivity;
import com.spenditure.presentation.transaction.CreateTransactionActivity;
import com.spenditure.presentation.transaction.ViewTransactionsActivity;

import java.util.HashMap;
import java.util.Map;

public class BottomNavigationHandler {

    private final Map<Integer, Class<? extends AppCompatActivity>> activityMap;

    public BottomNavigationHandler() {
        this.activityMap = NavigationConfig.ACTIVITY_MAP;
    }


    public Class<? extends AppCompatActivity> select(int itemId){
        Class<? extends AppCompatActivity> activityClass = activityMap.get(itemId);
        return activityClass;
    }
}

class NavigationConfig {
    public static final Map<Integer, Class<? extends AppCompatActivity>> ACTIVITY_MAP = new HashMap<>();

    static {
        ACTIVITY_MAP.put(R.id.navigation_home, ViewReportActivity.class);
        ACTIVITY_MAP.put(R.id.navigation_create_transaction, CreateTransactionActivity.class);
        ACTIVITY_MAP.put(R.id.navigation_view_transactions, ViewTransactionsActivity.class);
        ACTIVITY_MAP.put(R.id.navigation_category, ViewCategoryActivity.class);
        ACTIVITY_MAP.put(R.id.navigation_user, ViewProfileActivity.class);
        // Add other mappings as needed
    }
}