package com.spenditure.database.hsqldb;

import com.spenditure.database.TransactionPersistence;
import com.spenditure.object.DateTime;
import com.spenditure.object.Transaction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TransactionSQL implements TransactionPersistence {

    private final String dbPath;

    public TransactionSQL(final String dbPath) {
        this.dbPath = dbPath;
    }

    //get connection to DB
    private Connection connection() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:file:" + dbPath + ";shutdown=true", "SA", "");
    }

    //returns Transaction object as result of a query
    private Transaction fromResultSet(final ResultSet rs) throws SQLException {
        final int transactionID = rs.getInt("TRANSACTIONID");
        final int userID = rs.getInt("USERID");
        final String name = rs.getString("NAME");
        final String date = rs.getString("DATE");
        final String place = rs.getString("PLACE");
        final double amount = rs.getDouble("AMOUNT");
        final String comments = rs.getString("COMMENTS");
        final boolean withdrawal = rs.getBoolean("WITHDRAWAL");
        final byte[] image = rs.getBytes("IMAGE");
        final int categoryID = rs.getInt("CATEGORYID");


        DateTime dateTime = new DateTime(date);

        return new Transaction(transactionID, userID, name, dateTime, place, amount, comments, withdrawal, image, categoryID);
    }

    //returns what the next transactionID should be -> current largest TID + 1
    private int getNextTransactionID() {
        int largestTransactionID = 1;
        try(final Connection connection = connection()) {
            final Statement st = connection.createStatement();
            final ResultSet rs = st.executeQuery("SELECT * FROM transactions");
            while (rs.next())
            {
                int currTransactionID = rs.getInt("TRANSACTIONID");
                if(currTransactionID > largestTransactionID) {
                    largestTransactionID = currTransactionID;
                }
            }
            rs.close();
            st.close();

            return largestTransactionID+1;
        }
        catch (final SQLException e) {
            throw new RuntimeException("An error occurred while processing the SQL operation", e);
        }
    }


    //returns list of all Transaction objects associated with a TID in transactions table
    @Override
    public List<Transaction> getAllTransactionsForUser(int userID) {
        List<Transaction> transactions = new ArrayList<>();

        try(final Connection connection = connection()) {
            final PreparedStatement statement = connection.prepareStatement("SELECT * FROM transactions\nWHERE userID=?");
            statement.setInt(1,userID);

            final ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                final Transaction transaction = fromResultSet(resultSet);
                transactions.add(transaction);
            }


            resultSet.close();
            statement.close();

            return transactions;
        }
        catch (final SQLException e) {
            throw new RuntimeException("An error occurred while processing the SQL operation", e);
        }
    }

    //takes a Transaction object and inserts a new entry to transaction table
    @Override
    public boolean addTransaction(Transaction transaction) {

        try(final Connection connection = connection()) {
            final PreparedStatement statement = connection.prepareStatement("INSERT INTO TRANSACTIONS VALUES(?,?,?,?,?,?,?,?,?,?)");

            if(transaction.getTransactionID() == -1) {
                transaction.setTransactionID(getNextTransactionID());
            }
            statement.setInt(1, transaction.getTransactionID());
            statement.setInt(2,transaction.getUserID());
            statement.setString(3,transaction.getName());
            statement.setString(4,transaction.getDateTime().getYearMonthDay());
            statement.setString(5,transaction.getPlace());
            statement.setDouble(6,transaction.getAmount());
            statement.setString(7,transaction.getComments());
            statement.setBoolean(8,transaction.getWithdrawal());
            statement.setBytes(9, transaction.getImage());
            statement.setInt(10,transaction.getCategoryID());

            statement.executeUpdate();

            statement.close();

            return true;
        }
        catch (final SQLException e) {
            throw new RuntimeException("An error occurred while processing the SQL operation " + e.getMessage(), e);
        }
    }

    //takes a Transaction object, updates entry in transaction table with matchin TID to have matching values as the object
    @Override
    public boolean modifyTransaction(Transaction transaction) {
        try (Connection connection = connection()) {

            PreparedStatement statement = connection.prepareStatement("UPDATE TRANSACTIONS SET NAME=?, DATE=?, PLACE=?, AMOUNT=?, COMMENTS=?, WITHDRAWAL=?, IMAGE=?, CATEGORYID=? WHERE transactionID=?");
            statement.setString(1,transaction.getName());
            statement.setString(2,transaction.getDateTime().getYearMonthDay());
            statement.setString(3,transaction.getPlace());
            statement.setDouble(4,transaction.getAmount());
            statement.setString(5,transaction.getComments());
            statement.setBoolean(6,transaction.getWithdrawal());
            statement.setBytes(7, transaction.getImage());
            statement.setInt(8,transaction.getCategoryID());
            statement.setInt(9, transaction.getTransactionID());

            statement.executeUpdate();

            statement.close();

            return true;

        } catch (SQLException e) {
            throw new RuntimeException("An error occurred while processing the SQL operation", e);
        }
    }

    //attempts to delete an entry in the transaction table with associated TID
    //returns true if a row is deleted, false otherwise
    @Override
    public boolean deleteTransaction(int transactionID) {
        try(final Connection connection = connection()) {
            final PreparedStatement statement = connection.prepareStatement("DELETE FROM transactions\nWHERE TRANSACTIONID=?");
            statement.setInt(1,transactionID);

            int row = statement.executeUpdate();

            return row > 0; //true if row number is greater than 0
        }
        catch (final SQLException e) {
            throw new RuntimeException("An error occurred while processing the SQL operation", e);
        }
    }

    //attempts to return a Transaction object with a specific TID, returns false if TID doesnt exist in table
    @Override
    public Transaction getTransactionByID(int transactionID) {
        try(final Connection connection = connection()) {
            final PreparedStatement statement = connection.prepareStatement("SELECT * FROM transactions\nWHERE TRANSACTIONID=?");
            statement.setInt(1,transactionID);

            final ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // If there are rows, retrieve data and create a Transaction object
                final Transaction transaction = fromResultSet(resultSet);

                // Close ResultSet and PreparedStatement
                resultSet.close();
                statement.close();

                return transaction;
            } else {
                return null;
            }

        }
        catch (final SQLException e) {
            throw new RuntimeException("An error occurred while processing the SQL operation", e);
        }
    }

    //returns list of transactions that match a TID and name
    @Override
    public ArrayList<Transaction> getTransactionByName(int userID, String name) {
        final ArrayList<Transaction> transactions = new ArrayList<>();

        try(final Connection connection = connection()) {
            final PreparedStatement statement = connection.prepareStatement("SELECT * FROM transactions\nWHERE NAME=? AND USERID=?");
            statement.setString(1,name);
            statement.setInt(2, userID);

            final ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                final Transaction transaction = fromResultSet(resultSet);
                transactions.add(transaction);
            }

            resultSet.close();
            statement.close();

            return transactions;

        }
        catch (final SQLException e) {
            throw new RuntimeException("An error occurred while processing the SQL operation", e);
        }
    }

    //returns list of transactions that match a TID and place
    @Override
    public ArrayList<Transaction> getTransactionsByPlace(int userID, String place) {
        final ArrayList<Transaction> transactions = new ArrayList<>();

        try(final Connection connection = connection()) {
            final PreparedStatement statement = connection.prepareStatement("SELECT * FROM transactions\nWHERE PLACE=? AND USERID=?");
            statement.setString(1,place);
            statement.setInt(2, userID);

            final ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                final Transaction transaction = fromResultSet(resultSet);
                transactions.add(transaction);
            }

            resultSet.close();
            statement.close();

            return transactions;

        }
        catch (final SQLException e) {
            throw new RuntimeException("An error occurred while processing the SQL operation", e);
        }
    }

    //returns list of transactions that match a TID and is between 2 bounds for it's amount value
    @Override
    public ArrayList<Transaction> getTransactionsByAmount(int userID, double lower, double upper) {
        final ArrayList<Transaction> transactions = new ArrayList<>();

        try(final Connection connection = connection()) {
            final PreparedStatement statement = connection.prepareStatement("SELECT * FROM transactions WHERE USERID=? AND amount >= ? AND amount <= ?");
            statement.setInt(1,userID);
            statement.setDouble(2,lower);
            statement.setDouble(3,upper);

            final ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                final Transaction transaction = fromResultSet(resultSet);
                transactions.add(transaction);
            }

            resultSet.close();
            statement.close();

            return transactions;

        }
        catch (final SQLException e) {
            throw new RuntimeException("An error occurred while processing the SQL operation", e);
        }
    }

    //returns list of transactions that match a TID and is between 2 bounds for it's date value
    @Override
    public ArrayList<Transaction> getTransactionsByDateTime(int userID, DateTime lower, DateTime upper) {
        final ArrayList<Transaction> transactions = new ArrayList<>();

        try(final Connection connection = connection()) {
            final PreparedStatement statement = connection.prepareStatement("SELECT * FROM transactions WHERE userID =? AND date >= ? AND date <= ?");
            statement.setInt(1, userID);
            statement.setString(2,lower.getYearMonthDay());
            statement.setString(3,upper.getYearMonthDay());

            final ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                final Transaction transaction = fromResultSet(resultSet);
                transactions.add(transaction);
            }

            resultSet.close();
            statement.close();

            return transactions;

        }
        catch (final SQLException e) {
            throw new RuntimeException("An error occurred while processing the SQL operation", e);
        }
    }

    // returns a list of Transaction objects with an associated CID
    @Override
    public ArrayList<Transaction> getTransactionsByCategoryID(int categoryID) {
        ArrayList<Transaction> transactions = new ArrayList<>();

        try(final Connection connection = connection()) {
            final PreparedStatement statement = connection.prepareStatement("SELECT * FROM transactions\nWHERE CATEGORYID=?");
            statement.setInt(1,categoryID);

            final ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                final Transaction transaction = fromResultSet(resultSet);
                transactions.add(transaction);
            }

            resultSet.close();
            statement.close();

            return transactions;

        }
        catch (final SQLException e) {
            throw new RuntimeException("An error occurred while processing the SQL operation", e);
        }
    }

    //returns all transactions for a TID, ordered newest -> oldest
    @Override
    public ArrayList<Transaction> getNewestTransactionsForUser(int userID) {
        ArrayList<Transaction> transactions = new ArrayList<>();

        try(final Connection connection = connection()) {
            final PreparedStatement statement = connection.prepareStatement("SELECT * FROM transactions\nWHERE USERID = ?\nORDER BY DATE DESC;");
            statement.setInt(1,userID);

            final ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                final Transaction transaction = fromResultSet(resultSet);
                transactions.add(transaction);
            }

            resultSet.close();
            statement.close();

            return transactions;

        }
        catch (final SQLException e) {
            throw new RuntimeException("An error occurred while processing the SQL operation", e);
        }
    }

    //returns all transactions for a TID, ordered oldest -> newest
    @Override
    public ArrayList<Transaction> getOldestTransactionsForUser(int userID) {
        ArrayList<Transaction> transactions = new ArrayList<>();

        try(final Connection connection = connection()) {
            final PreparedStatement statement = connection.prepareStatement("SELECT * FROM transactions\nWHERE USERID = ?\nORDER BY DATE;");
            statement.setInt(1,userID);

            final ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()) {
                final Transaction transaction = fromResultSet(resultSet);
                transactions.add(transaction);
            }

            resultSet.close();
            statement.close();

            return transactions;

        }
        catch (final SQLException e) {
            throw new RuntimeException("An error occurred while processing the SQL operation", e);
        }
    }

}
