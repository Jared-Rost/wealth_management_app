package com.spenditure.database.hsqldb;

import com.spenditure.database.SecurityQuestionPersistence;
import com.spenditure.logic.exceptions.InvalidGoalException;
import com.spenditure.object.MainCategory;
import com.spenditure.object.SecurityQuestion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SecurityQuestionSQL implements SecurityQuestionPersistence {

    private final String dbPath;

    public SecurityQuestionSQL(final String dbPath) {
        this.dbPath = dbPath;
    }

    private Connection connection() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:file:" + dbPath + ";shutdown=true", "SA", "");
    }

    //creates a SecurityQuestion from query result
    private SecurityQuestion fromResultSet(final ResultSet rs) throws SQLException {
        final String securityQuestion = rs.getString("SECURITYQUESTION");
        final int sid = rs.getInt("SECURITYQID");
        return new SecurityQuestion(sid, securityQuestion);
    }

    //returns SecurityQuestion object for associated sid,
    @Override
    public SecurityQuestion getSecurityQuestion(int sid) {
        try(final Connection connection = connection()) {
            final PreparedStatement statement = connection.prepareStatement("SELECT * FROM securityquestions\nWHERE SECURITYQID=?");
            statement.setInt(1,sid);

            final ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                return fromResultSet(resultSet);
            }
            else {
                return null;
            }

        }
        catch (final SQLException e) {
            throw new RuntimeException("An error occurred while processing the SQL operation5", e);
        }
    }

    //returns ArrayList of all SecurityQuestions in the table
    @Override
    public ArrayList<SecurityQuestion> getAllSecurityQuestions() {

        ArrayList<SecurityQuestion> questions = new ArrayList<>();
        SecurityQuestion question;
        try(final Connection connection = connection()) {
            final Statement st = connection.createStatement();
            final ResultSet rs = st.executeQuery("SELECT * FROM securityquestions");


            while(rs.next()) {
                question = fromResultSet(rs);
                questions.add(question);
            }

            return questions;

        }
        catch (final SQLException e) {
            throw new RuntimeException("An error occurred while processing the SQL operation5", e);
        }
    }
}
