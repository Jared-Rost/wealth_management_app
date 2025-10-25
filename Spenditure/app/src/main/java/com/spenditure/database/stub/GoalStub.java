package com.spenditure.database.stub;

import com.spenditure.database.GoalPersistence;
import com.spenditure.logic.exceptions.InvalidCategoryException;
import com.spenditure.logic.exceptions.InvalidGoalException;
import com.spenditure.object.DateTime;
import com.spenditure.object.Goal;
import com.spenditure.object.MainCategory;
import com.spenditure.object.Transaction;

import java.util.ArrayList;
import java.util.List;

public class GoalStub implements GoalPersistence {

    private ArrayList<Goal> goalList;
    int currentID = 1;

    //init GoalStub + adds 3 Goals to stub
    public GoalStub(){
        this.goalList = new ArrayList<>();
        this.goalList.add(new Goal(generateUniqueID(),1,"eat less fast food", new DateTime(2024,11,5), 500, 2));
        this.goalList.add(new Goal(generateUniqueID(),1,"less gems in clash of clans", new DateTime(2024,6,12), 200, 3));
        this.goalList.add(new Goal(generateUniqueID(),1,"less partying", new DateTime(2025,1,1), 750, 3));
    }

    //adds goal to goalList or throws exception if error
    @Override
    public void addGoal(Goal goal) {
        if(goal != null) {
            goalList.add(goal);
        }
        else {
            throw new InvalidGoalException("Could not add goal.");
        }
    }

    //returns List of Goals based on userID
    @Override
    public List<Goal> getGoalsForUser(int targetUID) {
        List<Goal> goalsForUser = new ArrayList<>();
        for (Goal goal : goalList) {
            if(goal.getUserID() == targetUID) {
                goalsForUser.add(goal);
            }
        }
        return goalsForUser;
    }

    //deletes goal from goalList with target goalID, returns true if success, false if goalID does not exist
    @Override
    public boolean deleteGoal(int targetGID) {

        for (Goal goal : goalList) {
            if(goal.getGoalID() == targetGID) {
                goalList.remove(goal);
                return true;
            }
        }

        return false;
    }

    //returns Goal based on goalID, null if Goal does not exist
    @Override
    public Goal getGoalByID(int targetGID) {

        for (Goal goal : goalList) {
            if(goal.getGoalID() == targetGID) {
                return goal;
            }
        }

        return null;
    }

    public int generateUniqueID()
    {
        return currentID++;
    }

}
