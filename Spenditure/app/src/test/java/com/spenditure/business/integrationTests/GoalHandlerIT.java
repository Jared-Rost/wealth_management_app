package com.spenditure.business.integrationTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.spenditure.logic.GoalHandler;
import com.spenditure.logic.UserHandler;
import com.spenditure.logic.exceptions.InvalidGoalException;
import com.spenditure.object.DateTime;
import com.spenditure.object.Goal;
import com.spenditure.utils.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class GoalHandlerIT {

    private GoalHandler goalHandler;
    private UserHandler userHandler;

    private File tempDB;

    @Before
    public void setup() throws IOException, NoSuchAlgorithmException {
        this.tempDB = TestUtils.copyDB();
        this.goalHandler = new GoalHandler(false);
        userHandler = new UserHandler(false);
        userHandler.login("Me", "123");
        goalHandler.cleanup(false);
    }
    @After
    public void tearDown(){
        userHandler.logout();
        this.goalHandler = null;
        this.tempDB = null;
    }

    @Test
    public void testGetByGoalIDValidCase() {

        Goal test = goalHandler.getGoalByID(1);

        assertEquals("Check 1st goal in Stub DB, 'eat less fast food'", "eat less fast food", test.getGoalName());

    }

    @Test
    public void testByGoalIDInvalidCase() {

        boolean caught = false;

        try {

            Goal test = goalHandler.getGoalByID(999999);

        } catch (InvalidGoalException ignored) {

            caught = true;

        }

        assertTrue(caught);

    }

    @Test
    public void testCreateGoalValidCase() {

        goalHandler.createGoal(1, "Buy less rare minerals online", new DateTime(2024, 12, 31), 500, 3);

        assertEquals("Goal ID should be 1 greater than the last in the stub DB", "Buy less rare minerals online", goalHandler.getGoalByID(4).getGoalName());

    }

    @Test
    public void testCreateGoalInvalidCase() {

        boolean caught = false;

        try {

            goalHandler.createGoal(1, "", new DateTime(2025, 12, 31), -100, 1);

        } catch(InvalidGoalException ignored) {

            caught = true;

        }

        assertTrue(caught);

    }

    @Test
    public void deleteGoalValidCase() {

        goalHandler.createGoal(1, "Buy less rare minerals online", new DateTime(2024, 12, 31), 500, 3);

        Goal test = goalHandler.getGoalByID(4);

        assertNotNull(test);

        goalHandler.deleteGoal(4);

        boolean caught = false;

        try {

            goalHandler.getGoalByID(4);

        } catch (InvalidGoalException ignored) {

            caught = true;

        }

        assertTrue(caught);

    }

    @Test
    public void deleteGoalInvalidCase() {

        boolean caught = false;

        try {

            Goal test = goalHandler.getGoalByID(99999);

        } catch(InvalidGoalException ignored) {

            caught = true;

        }

        //Ensure no User ID "99999" in database
        assertTrue(caught);

        goalHandler.deleteGoal(99999);

        //No exceptions should be thrown
    }

    @Test
    public void testGoalsForUserIDValidCase() {

        List<Goal> list = goalHandler.getGoalsForUserID(1);

        assertEquals("There should be 3 items in the list", 3, list.size());

    }

    @Test
    public void testGoalsForUserIDInvalidCase() {

        List<Goal> list = goalHandler.getGoalsForUserID(999);

        assert(list.isEmpty());

    }


}
