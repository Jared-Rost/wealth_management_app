/**
 * EditTransactionActivity.java
 *
 * COMP3350 SECTION A02
 *
 * @author Jillian Friesen, 7889402
 * @date Tuesday, February 7, 2024
 *
 * PURPOSE:
 *  This file contains all the event handlers and UI management for the Edit Transaction
 *  activity where users can modify and save an existing transaction.
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

import com.spenditure.R;
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
import com.spenditure.object.Transaction;
import com.spenditure.presentation.BottomNavigationHandler;
import com.spenditure.presentation.category.CustomCategorySpinnerAdapter;

public class EditTransactionActivity extends AppCompatActivity {
    private Transaction givenTransaction;
    private ITransactionHandler transactionHandler;
    private ICategoryHandler categoryHandler;
    private ActivityResultLauncher<Intent> getImageCaptureResult;
    private byte[] imageBytes;
    private Button viewImageButton;
    private CustomCategorySpinnerAdapter adapter;
    private DateTime selectedDate;
    private int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_transaction);
        this.userID = UserHandler.getUserID();
        categoryHandler = new CategoryHandler(Services.DEVELOPING_STATUS);

        int givenID = -1;

        // Get the passed transaction ID
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            givenID = extras.getInt("selectedTransaction");
        }

        // Get the transaction
        transactionHandler = new TransactionHandler(Services.DEVELOPING_STATUS);
        givenTransaction = transactionHandler.getTransactionByID(givenID);

        setUpCategories();

        // Populate the UI fields
        populateTransactionFields(givenTransaction);

        setUpEditButton();
        setUpDatePicker();
        setUpImageCaptureButton();
        setUpViewImageButton();
        navBarHandling();
    }

    // Set up the Edit Transaction button
    private void setUpEditButton() {
        Button button = findViewById(R.id.button_edit_transaction);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try {
                    // Call helper method & modify the Transaction
                    Transaction updatedTransaction = editTransaction();
                    transactionHandler.modifyTransaction(updatedTransaction);

                    startActivity(new Intent(getApplicationContext(), ViewTransactionsActivity.class));
                } catch (InvalidTransactionException e) {
                    Toast.makeText(EditTransactionActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // Set up the category drop down menu
    private void setUpCategories() {
        Spinner categories = findViewById(R.id.spinner_categories);

        try {
            // Create adapter to display the categories
            adapter = new CustomCategorySpinnerAdapter(categoryHandler.getAllCategory(userID), EditTransactionActivity.this);
            categories.setAdapter(adapter);
        } catch (Exception e) {
            Toast.makeText(EditTransactionActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    // Set up the date picker
    private void setUpDatePicker() {
        EditText dateField = findViewById(R.id.edittext_date);

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
                        EditTransactionActivity.this,
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

        viewImageButton.setOnClickListener(view -> {
            // Open the Image View activity and pass in the image bytes
            Intent imageViewActivity = new Intent(getApplicationContext(), ImageViewActivity.class);
            imageViewActivity.putExtra("imageBytes", imageBytes);
            startActivity(imageViewActivity);
        });
    }

    // Set up the image capture button
    private void setUpImageCaptureButton() {
        // Initialize the ActivityResultLauncher
        getImageCaptureResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Handle the result here
                        Intent data = result.getData();
                        if (data != null) {
                            Bundle received = data.getExtras();
                            imageBytes = received.getByteArray("imageBytes");

                            // Enable the view image button now
                            viewImageButton.setEnabled(true);
                        }
                    }
                }
        );

        // Create event for when Image Capture button is clicked
        ImageButton button = findViewById(R.id.button_take_image);
        button.setOnClickListener(view -> {
            // Open the Image Capture activity to take the image
            Intent imageCaptureActivity = new Intent(getApplicationContext(), ImageCaptureActivity.class);
            getImageCaptureResult.launch(imageCaptureActivity);
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

    // Populate the fields on the UI using the given transaction
    private void populateTransactionFields(Transaction transaction){
        EditText whatTheHeck = findViewById(R.id.edittext_what_the_heck);
        whatTheHeck.setText(transaction.getName());

        EditText dateField = findViewById(R.id.edittext_date);
        selectedDate = new DateTime(
                transaction.getDateTime().getYear(),
                transaction.getDateTime().getMonth(),
                transaction.getDateTime().getDay()
        );
        dateField.setText(selectedDate.toString());

        EditText place = findViewById(R.id.edittext_place);
        place.setText(transaction.getPlace());

        EditText amount = findViewById(R.id.edittext_amount);
        amount.setText(Double.toString(transaction.getAmount()));

        EditText comments = findViewById(R.id.edittext_comments);
        comments.setText(transaction.getComments());

        AppCompatToggleButton type = findViewById(R.id.togglebutton_type);
        type.setChecked(transaction.getWithdrawal());

        try {
            // Get and select the category
            Spinner category = findViewById(R.id.spinner_categories);
            MainCategory cat = categoryHandler.getCategoryByID(transaction.getCategoryID());
            category.setSelection(adapter.getPosition(cat));
        } catch(Exception e) {
            Toast.makeText(EditTransactionActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        imageBytes = transaction.getImage();

        // If there was an image saved, enable the View Image button
        if (imageBytes != null) {
            Button button = findViewById(R.id.button_view_image);
            button.setEnabled(true);
        }
    }

    // Helper method: return the updated Transaction object made from user-entered info
    private Transaction editTransaction() {
        // Parse all the user fields
        EditText whatTheHeck = findViewById(R.id.edittext_what_the_heck);
        EditText place = findViewById(R.id.edittext_place);
        EditText amount = findViewById(R.id.edittext_amount);
        EditText comments = findViewById(R.id.edittext_comments);
        AppCompatToggleButton type = findViewById(R.id.togglebutton_type);
        Spinner category = findViewById(R.id.spinner_categories);

        // Create the new transaction object
        Transaction updatedTransaction = new Transaction(
                givenTransaction.getTransactionID(),
                UserHandler.getUserID(),
                whatTheHeck.getText().toString(),
                selectedDate,
                place.getText().toString(),
                Double.parseDouble(amount.getText().toString()),
                comments.getText().toString(),
                type.isChecked()
        );

        // Get the selected category & update the transaction
        MainCategory cat = adapter.getItem(category.getSelectedItemPosition());
        updatedTransaction.setCategoryID(cat.getCategoryID());

        // Only add image if it was taken
        if (imageBytes != null) {
            updatedTransaction.setImage(imageBytes);
        }

        return updatedTransaction;
    }
}