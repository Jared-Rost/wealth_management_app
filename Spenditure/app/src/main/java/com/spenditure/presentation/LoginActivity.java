/**
 * LoginActivity.java
 * <p>
 * COMP3350 SECTION A02
 *
 * @author Jillian Friesen, 7889402
 * @date Tuesday, March 5, 2024
 * <p>
 * PURPOSE:
 * This file handles the Login screen and allows users to enter their credentials to access the rest of the app.
 **/

package com.spenditure.presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.spenditure.R;
import com.spenditure.application.Services;
import com.spenditure.database.utils.DBHelper;
import com.spenditure.logic.UserHandler;
import com.spenditure.logic.exceptions.InvalidUserInformationException;
import com.spenditure.presentation.report.ViewReportActivity;

import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {
    private String username;
    private String password;
    private UserHandler userHandler;
    private FloatingActionButton registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!Services.START_UP && !Services.DEVELOPING_STATUS){
            Services.START_UP = true;
            DBHelper.copyDatabaseToDevice(this);
        }

        userHandler = new UserHandler(Services.DEVELOPING_STATUS);
        setContentView(R.layout.activity_login);

        setUpLoginButton();
        setUpRegisterButton();
    }

    // Set up the login button
    private void setUpLoginButton() {
        Button button = findViewById(R.id.button_login);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText user = findViewById(R.id.edittext_username);
                username = user.getText().toString();

                EditText pass = findViewById(R.id.edittext_current_password);
                password = pass.getText().toString();

                try {
                    int userID = userHandler.login(username, password);

                    if (userID != 0){
                        // Login and open the report activity
                        Intent newIntent = new Intent(getApplicationContext(), ViewReportActivity.class);
                        startActivity(newIntent);
                    }
                } catch (InvalidUserInformationException e){
                    Toast.makeText(LoginActivity.this, "Unable to log in: " + e.getMessage(),Toast.LENGTH_LONG).show();
                    user.setText("");
                    pass.setText("");
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    // Set up the registration button to open the registration window
    private void setUpRegisterButton() {
        registerButton = findViewById(R.id.floatingActionButton_register);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open a registration window
                Intent registrationActivity = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(registrationActivity);
            }
        });
    }
}