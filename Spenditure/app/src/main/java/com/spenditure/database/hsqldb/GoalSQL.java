package com.spenditure.database.hsqldb;

import com.spenditure.database.GoalPersistence;
import com.spenditure.logic.exceptions.InvalidGoalException;
import com.spenditure.logic.exceptions.InvalidUserInformationException;
import com.spenditure.object.DateTime;
import com.spenditure.object.Goal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GoalSQL implements GoalPersistence {

    private final String dbPath;

    public GoalSQL(final String dbPath) {
        this.dbPath = dbPath;
    }

    private Connection connection() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:file:" + dbPath + ";shutdown=true", "SA", "");
    }

    //builds Goal object from query result data
    private Goal fromResultSet(final ResultSet rs) throws SQLException {
        final int goalID = rs.getInt("GOALID");
        final int userID = rs.getInt("USERID");
        final String goalName = rs.getString("GOALNAME");
        final String date = rs.getString("TOBECOMPLETEDBY");
        final int spendingGoal = rs.getInt("SPENDINGGOAL");
        final int categoryID = rs.getInt("CATEGORYID");


        DateTime dateTime = new DateTime(date);
        return new Goal(goalID, userID, goalName, dateTime, spendingGoal, categoryID);
    }

    //adds goal to db, throws exception if error
    @Override
    public void addGoal(Goal goal) {
        try(final Connection connection = connection()) {
            final PreparedStatement statement = connection.prepareStatement("INSERT INTO GOALS VALUES(?, ?, ?, ?, ?, ?)");
            statement.setInt(1, goal.getGoalID());
            statement.setInt(2, goal.getUserID());
            statement.setString(3, goal.getGoalName());
            statement.setString(4,goal.getToBeCompletedBy().getYearMonthDay());
            statement.setInt(5, goal.getSpendingGoal());
            statement.setInt(6, goal.getCategoryID());
            statement.executeUpdate();

        }
        catch (final SQLException e) {
            throw new RuntimeException("An error occurred while processing the SQL operation " + e.getMessage(), e);
        }
    }

    //returns List of goals associated with a specific userID
    @Override
    public List<Goal> getGoalsForUser(int userID) {
        List<Goal> goalsForUser = new ArrayList<>();

        try(final Connection connection = connection()) {
            final PreparedStatement statement = connection.prepareStatement("SELECT * FROM goals\nWHERE userID=?");
            statement.setInt(1,userID);

            final ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                final Goal goal = fromResultSet(resultSet);
                goalsForUser.add(goal);
            }

            resultSet.close();
            statement.close();

            return goalsForUser;
        }
        catch (final SQLException e) {
            throw new RuntimeException("An error occurred while processing the SQL operation " + e.getMessage(), e);
        }
    }

    //deletes a goal based on goalID, returns true if success, false if no changes made, exception thrown if error
    @Override
    public boolean deleteGoal(int goalID) {
        try(final Connection connection = connection()) {
            final PreparedStatement statement = connection.prepareStatement("DELETE FROM goals\nWHERE GOALID=?");
            statement.setInt(1,goalID);

            int row = statement.executeUpdate();

            return row > 0; //true if row number is greater than 0
        }
        catch (final SQLException e) {
            throw new RuntimeException("An error occurred while processing the SQL operation " + e.getMessage(), e);
        }
    }

    //returns Goal based on goalID, throws exception if it does not exist or error with query
    @Override
    public Goal getGoalByID(int goalID) {

        try(final Connection connection = connection()) {
            final PreparedStatement statement = connection.prepareStatement("SELECT * FROM goals\nWHERE GOALID=?");
            statement.setInt(1,goalID);

            final ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                return fromResultSet(resultSet);
            }
            else {
                throw new InvalidGoalException("No goal with id: " + goalID);
            }

        }
        catch (final SQLException e) {
            throw new RuntimeException("An error occurred while processing the SQL operation " + e.getMessage(), e);
        }

    }

    //gets the next sequential goalID to assign
    public int generateUniqueID() {

        int largestTransactionID = 1;
        try(final Connection connection = connection()) {
            final Statement st = connection.createStatement();
            final ResultSet rs = st.executeQuery("SELECT * FROM goals");
            while (rs.next())
            {

                int currTransactionID = rs.getInt("GOALID");
                if(currTransactionID > largestTransactionID) {
                    largestTransactionID = currTransactionID;
                }
            }
            rs.close();
            st.close();

            return largestTransactionID+1;

        }
        catch (final SQLException e) {
            throw new RuntimeException("An error occurred while processing the SQL operation " + e.getMessage(), e);
        }

    }
}
