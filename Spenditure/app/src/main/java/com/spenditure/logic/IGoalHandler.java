package com.spenditure.logic;

import com.spenditure.object.DateTime;
import com.spenditure.object.Goal;

import java.util.List;

public interface IGoalHandler {


    /*
        createGoal
        Takes in all of the necessary parameters to build a Goal, validates it, and then sends it to
        the data layer to add to the database.
     */
    void createGoal(int userID, String name, DateTime completeBy, int goalAmount, int categoryID);

    /*
        getGoalByID
        Passes the Goal ID to the data layer and returns the resulting Goal.
     */
    Goal getGoalByID(int goalID);

    /*
        getGoalsForUserID
        Passes the User ID to the data layer, and returns all goals linked to that User ID.
     */
    List<Goal> getGoalsForUserID(int userID);

    /*
        deleteGoal
        Passes the Goal ID to the data layer, which will delete any matching goal.
     */
    void deleteGoal(int goalID);
}
