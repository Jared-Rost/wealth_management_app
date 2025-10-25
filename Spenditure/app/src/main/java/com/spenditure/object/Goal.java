package com.spenditure.object;

public class Goal {

    //instance variables
    private int goalID;
    private int userID;

    private String goalName;
    private DateTime toBeCompletedBy;
    private int spendingGoal;
    private int categoryID;

    public Goal(int goalID, int userID, String goalName, DateTime toBeCompletedBy, int spendingGoal, int categoryID) {
        this.goalID = goalID;
        this.userID = userID;
        this.goalName = goalName;
        this.toBeCompletedBy = toBeCompletedBy;
        this.spendingGoal = spendingGoal;
        this.categoryID = categoryID;
    }

    public int getGoalID() {
        return goalID;
    }

    public int getUserID() {
        return userID;
    }

    public String getGoalName() {
        return goalName;
    }

    public DateTime getToBeCompletedBy() {
        return toBeCompletedBy;
    }

    public int getSpendingGoal() {
        return spendingGoal;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setGoalID(int newID) { goalID = newID; }
}
