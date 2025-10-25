/**
 * ViewProfileActivity.java
 * <p>
 * COMP3350 SECTION A02
 *
 * @author Jillian Friesen, 7889402
 * @date Tuesday, March 5, 2024
 * <p>
 * PURPOSE:
 * This file handles the user information and allows users to change their credentials and logout.
 **/

package com.spenditure.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.spenditure.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.spenditure.application.Services;
import com.spenditure.logic.UserHandler;
import com.spenditure.logic.exceptions.InvalidUserInformationException;
import com.spenditure.presentation.Goals.ViewGoalsActivity;

import java.security.NoSuchAlgorithmException;

public class ViewProfileActivity extends AppCompatActivity {
    private String username;
    private String currentPassword;
    private String newPassword;
    private UserHandler userHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userHandler = new UserHandler(Services.DEVELOPING_STATUS);
        setContentView(R.layout.activity_view_profile);

        setUpFields();
        setUpSaveButton();
        setUpLogoutButton();
        setUpViewGoalsButton();
        navBarHandling();
    }

    // Populate the information fields
    private void setUpFields(){
        EditText usernameField = findViewById(R.id.edittext_username);
        usernameField.setText(userHandler.getUserName(UserHandler.getUserID()));

        EditText currentPasswordField = findViewById(R.id.edittext_current_password);
        currentPasswordField.setText("123");
    }

    // Set up the Save Changes button
    private void setUpSaveButton(){
        Button button = findViewById(R.id.button_save_changes);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText usernameField = findViewById(R.id.edittext_username);
                username = usernameField.getText().toString();

                EditText currentPasswordField = findViewById(R.id.edittext_current_password);
                currentPassword = currentPasswordField.getText().toString();

                EditText newPasswordField = findViewById(R.id.edittext_new_password);
                newPassword = newPasswordField.getText().toString();

                try {
                    // Try to change both values
                    userHandler.changeUsername(userHandler.getUserID(), username);
                    userHandler.changePassword(userHandler.getUserID(), currentPassword, newPassword);

                    Toast.makeText(ViewProfileActivity.this, "Changes saved successfully.",Toast.LENGTH_LONG).show();
                } catch (InvalidUserInformationException e){
                    Toast.makeText(ViewProfileActivity.this, "Unable to update credentials." + e.getMessage(),Toast.LENGTH_LONG).show();
                    usernameField.setText("");
                    currentPasswordField.setText("");
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    // Set up the logout button
    private void setUpLogoutButton(){
        Button button = findViewById(R.id.button_logout);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    userHandler.logout();

                    Intent newIntent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(newIntent);
                } catch (InvalidUserInformationException e){
                    Toast.makeText(ViewProfileActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // Handle the bottom nav bar
    private void navBarHandling(){
        BottomNavigationView navView = findViewById(R.id.nav_view);

        BottomNavigationHandler navigationHandler = new BottomNavigationHandler();

        navView.setOnItemSelectedListener((item -> {
            if (item.getItemId() == R.id.navigation_user){
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
        navView.setSelectedItemId(R.id.navigation_user);
    }

    // Set up the View Goals button
    private void setUpViewGoalsButton(){
        Button button = findViewById(R.id.button_goals);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    // Open the View Goals activity
                    Intent newIntent = new Intent(getApplicationContext(), ViewGoalsActivity.class);
                    startActivity(newIntent);
                } catch (InvalidUserInformationException e){
                    Toast.makeText(ViewProfileActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
