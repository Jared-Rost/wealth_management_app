/**
 * SecurityQuestionHandler.java
 *
 * COMP3350 SECTION A02
 *
 * @author Toran Pillay, 7842389
 * @date March 25, 2024
 *
 * PURPOSE:
 *  This file interprets information from Security Questions sent by the UI layer,
 * sends information to the Database layer, and performs all logic operations
 * pertaining to Security Questions.
 **/

package com.spenditure.logic;

import com.spenditure.application.Services;
import com.spenditure.database.SecurityQuestionPersistence;
import com.spenditure.object.SecurityQuestion;
import java.util.ArrayList;

public class SecurityQuestionHandler implements ISecurityQuestionHandler {

    private SecurityQuestionPersistence dataAccessSecurityQuestion;

    public SecurityQuestionHandler(boolean getStubDB) {

        dataAccessSecurityQuestion = Services.getSecurityQuestionPersistence(getStubDB);

    }

    public void cleanup(boolean getStubDB) {

        Services.restartSecurityQuestionDB(getStubDB);

        dataAccessSecurityQuestion = Services.getSecurityQuestionPersistence(getStubDB);

    }

    /*

        getSecurityQuestion

        Given a Security Question ID, retrieves that Security Question (if any).

     */
    @Override
    public SecurityQuestion getSecurityQuestion(int securityQuestionID) {

        assert( securityQuestionID > 0 );

        return dataAccessSecurityQuestion.getSecurityQuestion(securityQuestionID);

    }

    /*

        getAllSecurityQuestions

        Returns an ArrayList with all the Security Questions.

     */
    @Override
    public ArrayList<SecurityQuestion> getAllSecurityQuestions() {

        return dataAccessSecurityQuestion.getAllSecurityQuestions();

    }

}
