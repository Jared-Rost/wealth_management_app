/**
 * CreateTransactionActivity.java
 * <p>
 * COMP3350 SECTION A02
 *
 * @author Jillian Friesen, 7889402
 * @date Tuesday, February 7, 2024
 * <p>
 * PURPOSE:
 * This file contains all the event handlers and UI management for the Create Transaction
 * activity where users can create and save a new transaction.
 **/

package com.spenditure.presentation.transaction;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatToggleButton;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.spenditure.application.Services;
import com.spenditure.logic.CategoryHandler;
import com.spenditure.logic.ICategoryHandler;
import com.spenditure.logic.ITransactionHandler;
import com.spenditure.logic.TransactionHandler;
import com.spenditure.logic.UserHandler;
import com.spenditure.logic.exceptions.InvalidTransactionException;
import com.spenditure.object.DateTime;
import com.spenditure.object.MainCategory;

import com.spenditure.R;
import com.spenditure.presentation.BottomNavigationHandler;
import com.spenditure.presentation.category.CustomCategorySpinnerAdapter;
import com.spenditure.presentation.report.ViewReportActivity;

import java.time.LocalDateTime;

public class CreateTransactionActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> getImageCaptureResult;
    private byte[] imageBytes;  // Holds the bytes of a captured image
    private Button viewImageButton;
    private CustomCategorySpinnerAdapter adapter;   // Adaptor to display the Categories in a drop down menu
    private DateTime selectedDate;
    private int userID;
    private ITransactionHandler transactionHandler;
    private ICategoryHandler categoryHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_transaction);
        this.userID = UserHandler.getUserID();

        transactionHandler = new TransactionHandler(Services.DEVELOPING_STATUS);

        setUpCreateButton();
        setUpCategories();
        setUpDatePicker();
        setUpImageCaptureButton();
        setUpViewImageButton();
        navBarHandling();
    }

    // Set up the Create Transaction button
    private void setUpCreateButton() {
        Button button = findViewById(R.id.button_create_transaction);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    // Call helper method
                    createTransaction();

                    // Return to the View Report activity
                    startActivity(new Intent(getApplicationContext(), ViewReportActivity.class));
                } catch (InvalidTransactionException e) {
                    Toast.makeText(CreateTransactionActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(CreateTransactionActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // Set up the category drop down menu
    private void setUpCategories() {
        categoryHandler = new CategoryHandler(Services.DEVELOPING_STATUS);

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
                        CreateTransactionActivity.this,
                        date,
                        selectedDate.getYear(),
                        selectedDate.getMonth(),
                        selectedDate.getDay()
                );

                dialog.show();
            }
        });
    }

    // Set up the View Image button
    private void setUpViewImageButton(){
        viewImageButton = findViewById(R.id.button_view_image);

        // Create handler for button click
        viewImageButton.setOnClickListener(view -> {
            // Open the activity and pass it any existing image
            Intent imageViewActivity = new Intent(getApplicationContext(), ImageViewActivity.class);
            imageViewActivity.putExtra("imageBytes", imageBytes);
            startActivity(imageViewActivity);
        });
    }

    // Set up the image capture button
    private void setUpImageCaptureButton() {
        // Initialize the ActivityResultLauncher so when it returns, we can get the bytes from the image
        getImageCaptureResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Handle the result here
                        Intent data = result.getData();
                        if (data != null) {
                            Bundle received = data.getExtras();
                            imageBytes = received.getByteArray("imageBytes");

                            // Enable the View Image button now
                            viewImageButton.setEnabled(true);
                        }
                    }
                }
        );

        // Create handler for button click
        ImageButton button = findViewById(R.id.button_take_image);
        button.setOnClickListener(view -> {
            Intent imageCaptureActivity = new Intent(getApplicationContext(), ImageCaptureActivity.class);
            getImageCaptureResult.launch(imageCaptureActivity);
        });
    }

    // Handle the bottom navigation bar
    private void navBarHandling(){
        BottomNavigationView navView = findViewById(R.id.nav_view);

        BottomNavigationHandler navigationHandler = new BottomNavigationHandler();

        navView.setOnItemSelectedListener((item -> {
            if (item.getItemId() == R.id.navigation_create_transaction){
                return false;
            }
            Class<? extends AppCompatActivity> newActivity = navigationHandler.select(item.getItemId());
            if(newActivity != null){
                startActivity(new Intent(getApplicationContext(), newActivity));
                return true;
            }
            return false;
        }));

        // Set the selected item if needed
        navView.setSelectedItemId(R.id.navigation_create_transaction);
    }

    // Helper method: create and return new Transaction object made from user-entered info
    private void createTransaction() {
        // Parse all the user fields
        EditText whatTheHeck = findViewById(R.id.edittext_what_the_heck);
        EditText place = findViewById(R.id.edittext_place);
        EditText amount = findViewById(R.id.edittext_amount);
        EditText comments = findViewById(R.id.edittext_comments);
        AppCompatToggleButton type = findViewById(R.id.togglebutton_type);
        Spinner category = findViewById(R.id.spinner_categories);

        // this has to be checked in UI because otherwise it crashes on "MainCategory cat = adapter.getItem(category.getSelectedItemPosition());"
        if(category.getSelectedItemPosition() == -1)
        {
            throw new InvalidTransactionException("Transactions must have a category.");
        }
        else
        {
            MainCategory cat = adapter.getItem(category.getSelectedItemPosition());
            // Create the new transaction object
            transactionHandler.addTransaction(UserHandler.getUserID(),whatTheHeck.getText().toString(),selectedDate,place.getText().toString(),
                    Double.parseDouble(amount.getText().toString()),comments.getText().toString(),type.isChecked(),imageBytes,cat.getCategoryID());
        }



    }
}