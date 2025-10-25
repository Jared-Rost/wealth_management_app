package com.spenditure.business.unitTests;

import static org.junit.Assert.*;

import com.spenditure.logic.SecurityQuestionHandler;
import com.spenditure.object.SecurityQuestion;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class SecurityQuestionHandlerTest {

    private SecurityQuestionHandler securityQuestionHandler;

    @Before
    public void setup() {

        securityQuestionHandler = new SecurityQuestionHandler(true);
        securityQuestionHandler.cleanup(true);

    }

    @After
    public void tearDown() {

        securityQuestionHandler = null;

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
