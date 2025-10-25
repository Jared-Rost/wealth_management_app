package com.spenditure.logic;

import com.spenditure.object.SecurityQuestion;

import java.util.ArrayList;

public interface ISecurityQuestionHandler {


    /*
        getSecurityQuestion
        Given a Security Question ID, retrieves that Security Question (if any).
     */
    SecurityQuestion getSecurityQuestion(int securityQuestionID);

    /*
        getAllSecurityQuestions
        Returns an ArrayList with all the Security Questions.
     */
    ArrayList<SecurityQuestion> getAllSecurityQuestions();
}
