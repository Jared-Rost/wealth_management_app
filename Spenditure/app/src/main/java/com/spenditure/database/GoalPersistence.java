package com.spenditure.database;

import com.spenditure.object.Goal;

import java.util.List;

public interface GoalPersistence {

    void addGoal(Goal goal);

    List<Goal> getGoalsForUser(int userID);

    boolean deleteGoal(int goalID);

    Goal getGoalByID(int goalID);

    int generateUniqueID();
}
