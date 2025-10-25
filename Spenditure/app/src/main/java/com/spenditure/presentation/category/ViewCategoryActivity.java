package com.spenditure.presentation.category;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.spenditure.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.spenditure.application.Services;
import com.spenditure.logic.CategoryHandler;
import com.spenditure.logic.ICategoryHandler;
import com.spenditure.logic.UserHandler;
import com.spenditure.presentation.BottomNavigationHandler;


/**
 * Adapter for Category Main activity
 * @author Bao Ngo
 * @version 04 Mar 2024
 */
public class ViewCategoryActivity extends AppCompatActivity {

    private ICategoryHandler categoryHandler = null;
    private RecyclerView recyclerView;
    private FloatingActionButton addButton;
    private FloatingActionButton refreshButton;
    private CategoryAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        categoryHandler = new CategoryHandler(Services.DEVELOPING_STATUS);
        setContentView(R.layout.activity_view_category);
        recyclerView = findViewById(R.id.recyclerview_category);
        addButton = findViewById(R.id.floatingActionButton_category);
        refreshButton = findViewById(R.id.floatingActionButton_refresh);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CategoryAdapter(ViewCategoryActivity.this, categoryHandler.getAllCategory(UserHandler.getUserID()));
        recyclerView.setAdapter(adapter);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter = new CategoryAdapter(ViewCategoryActivity.this, categoryHandler.getAllCategory(UserHandler.getUserID()));
                recyclerView.setAdapter(adapter);
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddNewCategory.newInstance().show(getSupportFragmentManager(),AddNewCategory.TAG);
            }

        });


        navBarHandling();

    }

    private void navBarHandling(){ //Handling input to nav bar to move it to right activity
        BottomNavigationView navView = findViewById(R.id.nav_view);

        BottomNavigationHandler navigationHandler = new BottomNavigationHandler();

        navView.setOnItemSelectedListener((item -> {
            if (item.getItemId() == R.id.navigation_category){
                return false;   //Default setup provided by Android Studio
            }
            Class<? extends AppCompatActivity> newActivity = navigationHandler.select(item.getItemId());
            if(newActivity != null){
                startActivity(new Intent(getApplicationContext(), newActivity));
                return true; //Default setup provided by Android Studio
            }
            return false; //Default setup provided by Android Studio
        }));

        // Set the selected item if needed
        navView.setSelectedItemId(R.id.navigation_category);
    }

    public void refresh()
    {
        adapter = new CategoryAdapter(ViewCategoryActivity.this, categoryHandler.getAllCategory(UserHandler.getUserID()));
        recyclerView.setAdapter(adapter);
    }
}