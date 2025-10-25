package com.spenditure.business.integrationTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.spenditure.logic.SecurityQuestionHandler;
import com.spenditure.object.SecurityQuestion;
import com.spenditure.utils.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SecurityQuestionHandlerIT {

    private SecurityQuestionHandler securityQuestionHandler;
    private File tempDB;

    @Before
    public void setup() throws IOException {
        this.tempDB = TestUtils.copyDB();
        this.securityQuestionHandler = new SecurityQuestionHandler(false);
    }

    @After
    public void tearDown(){
        this.securityQuestionHandler = null;
        this.tempDB = null;
    }

    @Test
    public void testGetSecurityQuestionValidCase() {

        SecurityQuestion securityQuestion = securityQuestionHandler.getSecurityQuestion(1);

        assertEquals("name of childhood pet", securityQuestion.getSecurityQuestion());

        securityQuestion = securityQuestionHandler.getSecurityQuestion(2);

        assertEquals("fathers bachelors name", securityQuestion.getSecurityQuestion());

        securityQuestion = securityQuestionHandler.getSecurityQuestion(3);

        assertEquals("best comp class", securityQuestion.getSecurityQuestion());

    }

    @Test
    public void testGetSecurityQuestionInvalidCase() {

        boolean caught = false;
        SecurityQuestion securityQuestion;

        try {

            securityQuestion = securityQuestionHandler.getSecurityQuestion(0);

        } catch (AssertionError ignored) {

            caught = true;

        }

        assertTrue(caught);

        securityQuestion = securityQuestionHandler.getSecurityQuestion(90999);

        assertNull(securityQuestion);

    }

    @Test
    public void testGetAllSecurityQuestions() {

        ArrayList<SecurityQuestion> questions = securityQuestionHandler.getAllSecurityQuestions();

        assert(questions.size() == 3);

        assertEquals("name of childhood pet", questions.get(0).getSecurityQuestion());
        assertEquals("fathers bachelors name", questions.get(1).getSecurityQuestion());
        assertEquals("best comp class", questions.get(2).getSecurityQuestion());

    }

}
