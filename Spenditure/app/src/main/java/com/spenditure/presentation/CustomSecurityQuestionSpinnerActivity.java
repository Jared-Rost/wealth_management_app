/**
 * CustomSecurityQuestionSpinnerActivity.java
 * <p>
 * COMP3350 SECTION A02
 *
 * @author Jillian Friesen, 7889402
 * @date Friday, March 22, 2024
 * <p>
 * PURPOSE:
 * This file contains an adapter that takes in a list of SecurityQuestion objects and generates
 *  a custom UI list of security questions for the user to view.
 **/

package com.spenditure.presentation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.spenditure.R;
import com.spenditure.object.SecurityQuestion;

import java.util.List;

public class CustomSecurityQuestionSpinnerActivity extends BaseAdapter {
    // Instance Variables
    private Context context;
    private List<SecurityQuestion> questions;
    private LayoutInflater inflator;

    public CustomSecurityQuestionSpinnerActivity(List<SecurityQuestion> questions, Context context){
        this.context = context;
        this.questions = questions;
        inflator = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return questions.size();
    }

    @Override
    public SecurityQuestion getItem(int i) {
        return questions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 1;
    }

    public int getPosition(SecurityQuestion question) {
        return questions.indexOf(question);
    }

    // Set up the UI with the information about the Security Question
    @Override
    public View getView(int i, View view, ViewGroup viewGroup){
        view =inflator.inflate(R.layout.activity_custom_security_question_spinner, null);

        TextView question = (TextView) view.findViewById(R.id.textview_security_question);
        question.setText(questions.get(i).getSecurityQuestion());

        return view;
    }
}