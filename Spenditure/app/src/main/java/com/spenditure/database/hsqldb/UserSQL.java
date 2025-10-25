package com.spenditure.database.hsqldb;

import android.annotation.SuppressLint;

import com.spenditure.database.UserPersistence;
import com.spenditure.logic.exceptions.InvalidUserInformationException;
import com.spenditure.object.MainCategory;
import com.spenditure.object.Transaction;
import com.spenditure.object.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserSQL implements UserPersistence {

    private final String dbPath;


    public UserSQL(final String dbPath) {
        this.dbPath = dbPath;
    }

    //returns connection to DB
    private Connection connection() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:file:" + dbPath + ";shutdown=true", "SA", "");
    }

    //returns username for associated UID
    @Override
    public String getUserName(int userID) {
        String userName = "";
        try(final Connection connection = connection()) {
            final PreparedStatement statement = connection.prepareStatement("SELECT * FROM users\nWHERE userID=?");
            statement.setInt(1,userID);

            final ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                userName = resultSet.getString("USERNAME");
            }

            resultSet.close();
            statement.close();

            return userName;
        }
        catch (final SQLException e) {
            throw new RuntimeException("An error occurred while processing the SQL operation", e);   
        }

    }

    //returns the SecurityQuestion answer for a given userID
    @Override
    public String getSecurityQuestionAnswer(int userID) {
        String securityQuestionAnswer = "";
        try(final Connection connection = connection()) {
            final PreparedStatement statement = connection.prepareStatement("SELECT * FROM users\nWHERE userID=?");
            statement.setInt(1,userID);

            final ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                securityQuestionAnswer = resultSet.getString("SECURITYQUESTIONANSWER");
            }

            resultSet.close();
            statement.close();

            return securityQuestionAnswer;
        }
        catch (final SQLException e) {
            throw new RuntimeException("An error occurred while processing the SQL operation", e);
        }
    }

    //returns the sid for the given userID
    @Override
    public int getSecurityQuestionID(int userID) {
        int securityQuestionID = 0;
        try(final Connection connection = connection()) {
            final PreparedStatement statement = connection.prepareStatement("SELECT * FROM users\nWHERE userID=?");
            statement.setInt(1,userID);

            final ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                securityQuestionID = resultSet.getInt("SECURITYQUESTIONID");
            }

            resultSet.close();
            statement.close();

            if(securityQuestionID > 0) {
                return securityQuestionID;
            }
            else {
                throw new InvalidUserInformationException("Invalid securityQuestionID");
            }
        }
        catch (final SQLException e) {
            throw new RuntimeException("An error occurred while processing the SQL operation", e);
        }
    }

    //returns number of users in user table
    private int getNumberOfUsers() {
        int count = 0;
        try(final Connection connection = connection()) {
            final Statement st = connection.createStatement();
            final ResultSet rs = st.executeQuery("SELECT * FROM users");
            while (rs.next())
            {
                count++;
            }
            rs.close();
            st.close();

            return count;

        }
        catch (final SQLException e) {
            throw new RuntimeException("An error occurred while processing the SQL operation", e);   
        }
    }

    //if username + password combo exists in db -> return userID
    @Override
    public int login(String username, String password) {

        try(final Connection connection = connection()) {
            final PreparedStatement statement = connection.prepareStatement("SELECT * FROM users\nWHERE USERNAME=? AND PASSWORD=?;");
            statement.setString(1, username);
            statement.setString(2, password);

            final ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                return resultSet.getInt("USERID");
            }
            else {
                throw new InvalidUserInformationException("Wrong username or password");
            }

        }
        catch (final SQLException e) {
            throw new RuntimeException("An error occurred while processing the SQL operation", e);   
        }

    }

    //takes UID, current password, new password to change it to
    //attempts to update password in table for entry with associated UID
    //returns true on success, false on failure to update
    @Override
    public boolean changePassword(int userID, String oldPassword, String newPassword) {
        try(final Connection connection = connection()) {
            final PreparedStatement selectStatement  = connection.prepareStatement("SELECT * FROM users\nWHERE USERID=? AND PASSWORD=?;");
            selectStatement .setInt(1,userID);
            selectStatement .setString(2, oldPassword);

            final ResultSet resultSet = selectStatement .executeQuery();

            if(resultSet.next()) {
                final PreparedStatement updateStatement = connection.prepareStatement("UPDATE users\nSET PASSWORD=?\nWHERE USERID=?");
                updateStatement.setString(1, newPassword);
                updateStatement.setInt(2,userID);

                int rowsAffected = updateStatement.executeUpdate();

                if (rowsAffected == 1) {
                    return true; // Password updated successfully
                } else {
                    throw new RuntimeException("Failed to update password.");
                }

            }
            else {
                throw new InvalidUserInformationException("UserID and password combination does not exist.");
            }
        }
        catch (final SQLException e) {
            throw new RuntimeException("An error occurred while processing the SQL operation", e);   
        }

    }

    //takes a UID and a new username, attempts to update username in table for entry with associated UID
    //returns true on success, false on failure to update
    @Override
    public boolean changeUsername(int userID, String newUsername) {
        try(final Connection connection = connection()) {
            final PreparedStatement updateStatement = connection.prepareStatement("UPDATE users\nSET USERNAME=?\nWHERE USERID=?");
            updateStatement.setString(1, newUsername);
            updateStatement.setInt(2, userID);

            int rowsAffected = updateStatement.executeUpdate();
            if (rowsAffected == 1) {
                return true; // Username updated successfully
            } else {
                throw new RuntimeException("Failed to update username.");
            }

        }
        catch (final SQLException e) {
            throw new RuntimeException("An error occurred while processing the SQL operation", e);   
        }

    }

    //takes username, password, email
    //attempts to insert new entry into user table
    //returns UID on success, throws exception on failure
    @Override
    public int register( String username, String password,String email, String securityQAnswer, int securityQID) {
        try(final Connection connection = connection()) {

            String insertQuery = "INSERT INTO users (userid, username, password, email, securityquestionanswer, securityquestionid) VALUES (?, ?, ?, ?, ?, ?)";

            int newUserID = getNumberOfUsers() + 1;
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setInt(1, newUserID);
                preparedStatement.setString(2, username);
                preparedStatement.setString(3, password);
                preparedStatement.setString(4, email);
                preparedStatement.setString(5, securityQAnswer);
                preparedStatement.setInt(6, securityQID);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    return newUserID;
                }
                else {
                    throw new RuntimeException("Register failed.");
                }
            }
        }
        catch (final SQLException e) {
            throw new RuntimeException("An error occurred while processing the SQL operation", e);   
        }

    }


}
