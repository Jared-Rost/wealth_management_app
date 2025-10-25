/**
 * GoalValidator.java
 *
 * COMP3350 SECTION A02
 *
 * @author Toran Pillay, 7842389
 * @date March 25, 2024
 *
 * PURPOSE:
 *  This file contains all of the methods necessary to validate the data in a
 * Goal.
 **/

package com.spenditure.logic;

import static com.spenditure.logic.DateTimeValidator.validateDateTime;
import com.spenditure.logic.exceptions.InvalidGoalException;
import com.spenditure.object.Goal;

public class GoalValidator {

    private static final int MIN_GOAL_ID = 1;
    private static final int MIN_USER_ID = 0;
    private static final int MIN_CATEGORY_ID = 1;
    private static final double MIN_SPENDING_GOAL = 0.00;
    private static final int GOAL_NAME_CHARACTER_LIMIT = 256;

    public static void validateGoal(Goal goal) throws InvalidGoalException {

        validateGoalID(goal.getGoalID());

        validateUserID(goal.getUserID());

        validateGoalName(goal.getGoalName());

        validateDateTime(goal.getToBeCompletedBy());

        validateSpendingGoal(goal.getSpendingGoal());

        validateCategoryID(goal.getCategoryID());

    }

    private static void validateGoalID(int goalID) {

        if(goalID < MIN_GOAL_ID)
            throw new InvalidGoalException("Goal ID must be at least "+MIN_GOAL_ID+".");

    }

    private static void validateUserID(int userID) {

        if(userID < MIN_USER_ID)
            throw new InvalidGoalException("User ID provided to a Goal must be at least "+MIN_USER_ID+".");

    }

    private static void validateGoalName(String goalName) {

        if(goalName.trim().isEmpty())
            throw new InvalidGoalException("Goal Name field cannot be empty.");

        if(goalName.length() > GOAL_NAME_CHARACTER_LIMIT)
            throw new InvalidGoalException("Goal Name cannot be more than "+GOAL_NAME_CHARACTER_LIMIT+" characters long.");
    }

    private static void validateSpendingGoal(double spendingGoal) {

        if(spendingGoal < MIN_SPENDING_GOAL)
            throw new InvalidGoalException("Spending goal must be "+MIN_SPENDING_GOAL+" or greater.");

    }

    private static void validateCategoryID(int categoryID) {

        if(categoryID < MIN_CATEGORY_ID)
            throw new InvalidGoalException("Category ID provided to a Goal must be at least "+MIN_CATEGORY_ID+".");

    }

}
