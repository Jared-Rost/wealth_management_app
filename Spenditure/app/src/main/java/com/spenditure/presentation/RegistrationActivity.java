/**
 * RegistrationActivity.java
 * <p>
 * COMP3350 SECTION A02
 *
 * @author Jillian Friesen, 7889402
 * @date Monday, March 18, 2024
 * <p>
 * PURPOSE:
 * This file contains all the event handlers and UI management for the Registration
 * activity where users can create a new account.
 **/

package com.spenditure.presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.spenditure.R;
import com.spenditure.application.Services;
import com.spenditure.logic.ISecurityQuestionHandler;
import com.spenditure.logic.IUserHandler;

import com.spenditure.logic.SecurityQuestionHandler;
import com.spenditure.logic.UserHandler;
import com.spenditure.logic.exceptions.InvalidUserInformationException;
import com.spenditure.object.SecurityQuestion;
import com.spenditure.presentation.report.ViewReportActivity;

import java.security.NoSuchAlgorithmException;

public class RegistrationActivity extends AppCompatActivity {
    private IUserHandler userManager;
    private ISecurityQuestionHandler securityQuestionHandler;
    private FloatingActionButton backButton;
    private Button registerButton;
    private CustomSecurityQuestionSpinnerActivity adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        userManager = new UserHandler(Services.DEVELOPING_STATUS);
        securityQuestionHandler = new SecurityQuestionHandler(Services.DEVELOPING_STATUS);

        setupSecurityQuestions();
        setupRegisterButton();
        setupBackButton();
    }

    // Setup the drop down menu to display the available security questions
    private void setupSecurityQuestions() {
        Spinner questions = findViewById(R.id.spinner_security_question);

        // Create adapter to display the categories
        adapter = new CustomSecurityQuestionSpinnerActivity(securityQuestionHandler.getAllSecurityQuestions(), this);
        questions.setAdapter(adapter);
    }

    // Set up the Registration button
    private void setupRegisterButton() {
        registerButton = findViewById(R.id.button_register);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Attempt to register the new user
                if (registerNewUser()) {
                    Intent newIntent = new Intent(getApplicationContext(), ViewReportActivity.class);
                    startActivity(newIntent);
                }
            }
        });
    }

    // Set up the Back button
    private void setupBackButton() {
        backButton = findViewById(R.id.floatingActionButton_back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Return to the calling activity
                finish();
            }
        });
    }

    // Collect all the new user information and pass it to the user handler
    private Boolean registerNewUser() {
        Boolean successful = false;

        EditText username = findViewById(R.id.edittext_username);
        EditText password = findViewById(R.id.edittext_current_password);
        EditText email = findViewById(R.id.edittext_email);
        Spinner question = findViewById(R.id.spinner_security_question);
        SecurityQuestion securityQuestion = adapter.getItem(question.getSelectedItemPosition());
        EditText questionAnswer = findViewById(R.id.edittext_question_answer);

        try {
            userManager.register(
                    username.getText().toString(),
                    password.getText().toString(),
                    email.getText().toString(),
                    questionAnswer.getText().toString(),
                    securityQuestion.getSid());
            successful = true;
        } catch (InvalidUserInformationException e) {
            Toast.makeText(RegistrationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (NoSuchAlgorithmException e) {
            Toast.makeText(RegistrationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return successful;
    }
}