package com.spenditure.database;

import com.spenditure.object.SecurityQuestion;

import java.util.ArrayList;

public interface SecurityQuestionPersistence {

    SecurityQuestion getSecurityQuestion(int sid);
    ArrayList<SecurityQuestion> getAllSecurityQuestions();
}
