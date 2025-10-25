package com.spenditure.business.unitTests;

import static com.spenditure.logic.GoalValidator.validateGoal;

import static org.junit.Assert.assertTrue;

import com.spenditure.logic.exceptions.InvalidGoalException;
import com.spenditure.object.Goal;
import com.spenditure.object.DateTime;

import org.junit.Before;
import org.junit.Test;

public class GoalExceptionTest {

    boolean caught;
    DateTime dateTime;

    @Before
    public void setup() {

        caught = false;
        dateTime = new DateTime(2001, 01, 01);

    }

    @Test
    public void testInvalidGoalID() {

        try {

            validateGoal(new Goal(-1, 1, "Name", dateTime, 1, 1));

        } catch(InvalidGoalException ignored) {

            caught = true;

        }

        assertTrue(caught);

    }

    @Test
    public void testInvalidUserID() {

        try {

            validateGoal(new Goal(1, -1, "Name", dateTime, 1, 1));

        } catch(InvalidGoalException ignored) {

            caught = true;

        }

        assertTrue(caught);

    }

    @Test
    public void testGoalNameOverLimit() {

        String overLimit = ".";

        for(int i = 0; i < 256; i ++) {
            overLimit += ".";
        }

        try {

            validateGoal(new Goal(1, 1, overLimit, dateTime, 1, 1));

        } catch(InvalidGoalException ignored) {

            caught = true;

        }

        assertTrue(caught);

    }

    @Test
    public void testGoalNameEmpty() {

        try {

            validateGoal(new Goal(1, 1, "   ", dateTime, 1, 1));

        } catch (InvalidGoalException ignored) {

            caught = true;

        }

        assertTrue(caught);

    }

    @Test
    public void testInvalidSpendingGoal() {

        try {

            validateGoal(new Goal(1, 1, "Name", dateTime, -200000, 1));

        } catch (InvalidGoalException ignored) {

            caught = true;

        }

        assertTrue(caught);

    }

    @Test
    public void testInvalidCategoryID() {

        try {

            validateGoal(new Goal(1, 1, "Name", dateTime, 1, -1093));

        } catch (InvalidGoalException ignored) {

            caught = true;

        }

        assertTrue(caught);

    }
}
