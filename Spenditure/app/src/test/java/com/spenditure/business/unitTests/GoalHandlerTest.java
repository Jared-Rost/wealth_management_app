package com.spenditure.business.unitTests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.spenditure.database.GoalPersistence;
import com.spenditure.logic.GoalHandler;
import com.spenditure.logic.exceptions.InvalidGoalException;
import com.spenditure.object.DateTime;
import com.spenditure.object.Goal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class GoalHandlerTest {

    private GoalPersistence goalPersistence;
    private GoalHandler goalHandler;

    @Before
    public void setup() {

        goalPersistence = mock(GoalPersistence.class);
        goalHandler = new GoalHandler(goalPersistence);

    }

    @After
    public void tearDown() {

        goalHandler = null;

    }

    @Test
    public void testGetByGoalIDValidCase() {

        Goal goal2 = new Goal(1, 1, "goal2", new DateTime(2001, 01, 02), 2, 1);

        goalPersistence.addGoal(goal2);

        when(goalPersistence.getGoalByID(1)).thenReturn(goal2);

        Goal result = goalHandler.getGoalByID(1);

        assertNotNull(result);
        assertEquals(goal2.getGoalName(), result.getGoalName());
    }

    @Test
    public void testByGoalIDInvalidCase() {

        Goal goal2 = new Goal(1, 1, "goal2", new DateTime(2001, 01, 02), 2, 1);

        goalPersistence.addGoal(goal2);

        when(goalPersistence.getGoalByID(1)).thenReturn(goal2);

        Goal result = goalHandler.getGoalByID(2);

        assertNull(result);

    }

    @Test
    public void testCreateGoalValidCase() {

        boolean caught = false;

        try {

            goalHandler.createGoal(1, "", new DateTime(2000, 1, 1), -195481208, 1);

        } catch (InvalidGoalException ignored) {

            caught = true;

        }

        assertTrue(caught);

    }

    @Test
    public void mockCreateGoalValidCase() {

        Goal newGoal = new Goal(1, 1, "name", new DateTime(2001, 1, 1), 1, 1);

        List<Goal> list = new ArrayList<>();
        list.add(newGoal);

        goalHandler.createGoal(1, "name", new DateTime(2001, 1, 1), 1, 1);

        when(goalPersistence.getGoalsForUser(1)).thenReturn(list);

        List<Goal> goals = goalHandler.getGoalsForUserID(1);

        boolean found = false;

        for(int i=0; i<goals.size(); i++) {

            if(goals.get(i).getGoalName().equals("name"))
                found = true;

        }

        assertTrue(found);

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

        Goal goal = new Goal(4, 1, "Buy less rare minerals online", new DateTime(2024, 12, 31), 500, 3);

        goalHandler.createGoal(1, "Buy less rare minerals online", new DateTime(2024, 12, 31), 500, 3);

        when(goalPersistence.getGoalByID(4)).thenReturn(goal);

        Goal test = goalHandler.getGoalByID(4);

        assertNotNull(test);

        goalHandler.deleteGoal(4);

        when(goalPersistence.getGoalByID(4)).thenReturn(null);

        test = goalHandler.getGoalByID(4);

        assertNull(test);

    }

    @Test
    public void deleteGoalInvalidCase() {

        Goal test = goalHandler.getGoalByID(99999);

        assertNull(test);

        goalHandler.deleteGoal(99999);

        //No exceptions should be thrown
    }

    @Test
    public void testGoalsForUserIDValidCase() {

        List<Goal> db = new ArrayList<>();
        db.add(new Goal(0, 1, "", null, 1, 1));
        db.add(new Goal(1, 1, "", null, 1, 1));
        db.add(new Goal(2, 1, "", null, 1, 1));

        when(goalPersistence.getGoalsForUser(1)).thenReturn(db);

        List<Goal> list = goalHandler.getGoalsForUserID(1);

        assertEquals("There should be 3 items in the list", 3, list.size());

    }

    @Test
    public void testGoalsForUserIDInvalidCase() {

        List<Goal> db = new ArrayList<>();
        db.add(new Goal(0, 1, "", null, 1, 1));
        db.add(new Goal(1, 1, "", null, 1, 1));
        db.add(new Goal(2, 1, "", null, 1, 1));

        when(goalPersistence.getGoalsForUser(1)).thenReturn(db);

        List<Goal> list = goalHandler.getGoalsForUserID(999);

        assert(list.isEmpty());

    }

}
