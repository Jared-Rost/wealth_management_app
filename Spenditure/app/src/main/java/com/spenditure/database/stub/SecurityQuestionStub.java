package com.spenditure.database.stub;

import com.spenditure.database.SecurityQuestionPersistence;
import com.spenditure.object.Goal;
import com.spenditure.object.SecurityQuestion;

import java.util.ArrayList;

public class SecurityQuestionStub implements SecurityQuestionPersistence {

    private ArrayList<SecurityQuestion> securityQuestionList;

    public SecurityQuestionStub() {
        this.securityQuestionList = new ArrayList<>();
        securityQuestionList.add(new SecurityQuestion(1, "name of childhood pet"));
        securityQuestionList.add(new SecurityQuestion(2, "fathers bachelors name"));
        securityQuestionList.add(new SecurityQuestion(3, "best comp class"));
    }

    @Override
    public SecurityQuestion getSecurityQuestion(int sid) {
        for (SecurityQuestion question : securityQuestionList) {
            if(question.getSid() == sid) {
                return question;
            }
        }
        return null;
    }

    @Override
    public ArrayList<SecurityQuestion> getAllSecurityQuestions() {
        return securityQuestionList;
    }


}
