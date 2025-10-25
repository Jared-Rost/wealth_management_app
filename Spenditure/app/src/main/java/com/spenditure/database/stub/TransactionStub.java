package com.spenditure.database.stub;

import com.spenditure.database.TransactionPersistence;
import com.spenditure.logic.Comparison.DateNewestFirstComparator;
import com.spenditure.logic.Comparison.DateOldestFirstComparator;
import com.spenditure.object.DateTime;
import com.spenditure.object.Transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * TransactionStub.java
 *
 * COMP3350 SECTION A02
 *
 * @author Jared Rost,
 * @date Feb 7, 2024
 *
 * PURPOSE:
 *  This file stores information using array lists and has some default transactions for testing purposes
 *  It sorts data and returns an array list for use in the logic layer.
 *  It is in the database layer because it is the stub database, where else would it go?
 *
 **/

public class TransactionStub implements TransactionPersistence {

    private ArrayList<Transaction> transactionList;
    private int currentID = 1;

    public TransactionStub()
    {
        transactionList = new ArrayList<>();

        // everything under userID 1

        transactionList.add(new Transaction(generateUniqueID(), 1, "Morning Dons", new DateTime(1,1,1,1,1, 1),
                "Mcdonalds",5.99, "was luke warm today, 2/10", true, null, 2));
        transactionList.add(new Transaction(generateUniqueID(), 1, "Star Wars Rebels merch", new DateTime(2024,2,1,16,59, 59),
                "Toys R Us",500.95, "Sabine looking kinda nice O_o", true, null, 3));
        transactionList.add(new Transaction(generateUniqueID(), 1, "Shopping spree at the mall", new DateTime(2023, 5, 20, 15, 30, 30),
                "Mall", 250.50, "Bought clothes and accessories", true, null, 3));
        transactionList.add(new Transaction(generateUniqueID(), 1, "Dinner at a fancy restaurant", new DateTime(2023, 7, 8, 19, 0, 0),
                "Gourmet Restaurant", 150.75, "Celebrated anniversary", true, null, 2));
        transactionList.add(new Transaction(generateUniqueID(), 1, "Hotel accommodation for business trip", new DateTime(2023, 8, 15, 12, 0, 0),
                "Grand Hotel", 300.0, "Stayed for 3 nights", true, null, 3));
        transactionList.add(new Transaction(generateUniqueID(), 1, "Grocery shopping for the week", new DateTime(2023, 8, 20, 10, 0, 0),
                "Supermarket", 80.25, "Bought fruits, vegetables, and dairy products", true, null, 1));
        transactionList.add(new Transaction(generateUniqueID(), 1, "Utility bill payment", new DateTime(2023, 8, 25, 9, 0, 0),
                "Utility Company", 120.0, "Paid electricity and water bills", true, null, 1));
        transactionList.add(new Transaction(generateUniqueID(), 1, "Concert ticket purchase", new DateTime(2023, 9, 5, 18, 30, 30),
                "Concert Hall", 50.0, "Attended the concert of favorite band", true, null, 3));
        transactionList.add(new Transaction(generateUniqueID(), 1, "Car maintenance service", new DateTime(2023, 9, 10, 8, 0, 0),
                "Auto Service Center", 200.0, "Performed routine maintenance and oil change", true, null, 3));
        transactionList.add(new Transaction(generateUniqueID(), 1, "Online shopping for household items", new DateTime(2023, 9, 15, 16, 0, 0),
                "Online Store", 90.50, "Bought cleaning supplies and kitchenware", true, null, 1));
        transactionList.add(new Transaction(generateUniqueID(), 1, "Movie tickets for family outing", new DateTime(2023, 9, 20, 14, 0, 0),
                "Cinema", 60.0, "Watched latest blockbuster movie with family", true, null, 2));
        transactionList.add(new Transaction(generateUniqueID(), 1, "Gym membership subscription", new DateTime(2023, 9, 25, 17, 0, 0),
                "Fitness Center", 75.0, "Monthly subscription for gym access", true, null, 3));
        transactionList.add(new Transaction(generateUniqueID(), 1, "Restaurant bill for friend's birthday dinner", new DateTime(2023, 9, 30, 20, 0, 0),
                "Fine Dining Restaurant", 200.0, "Celebrated friend's birthday with a fancy dinner", true, null, 2));
        transactionList.add(new Transaction(generateUniqueID(), 1, "Online course enrollment fee", new DateTime(2023, 10, 5, 11, 0, 0),
                "Online Education Platform", 150.0, "Enrolled in a programming course", true, null, 1));

    }

    public ArrayList<Transaction> getAllTransactions(int userID)
    {
        ArrayList<Transaction> transactions = new ArrayList<>();
        for (int i = 0; i < transactionList.size(); i++) {
            if(transactionList.get(i).getUserID() == userID) {
                transactions.add(transactionList.get(i));
            }
        }
        return transactions;
    }

    public List<Transaction> getAllTransactionsForUser(int userID) {
        List<Transaction> transactions = new ArrayList<>();
        for(Transaction transaction : this.transactionList) {
            if(transaction.getUserID() == userID) {
                transactions.add(transaction);
            }
        }
        return transactions;
    }

    public boolean addTransaction(Transaction newTransaction)
    {
        for(int i = 0; i < transactionList.size(); i++)
        {
            if(transactionList.get(i).getTransactionID() == newTransaction.getTransactionID())
            {
                return false;
            }
        }

        newTransaction.setTransactionID(generateUniqueID());
        transactionList.add(newTransaction);
        return true;
    }

    public boolean modifyTransaction(Transaction targetTransaction)
    {
        for(int i = 0; i < transactionList.size(); i++)
        {
            if(transactionList.get(i).getTransactionID() == targetTransaction.getTransactionID())
            {
                transactionList.set(i, targetTransaction);
                return true;
            }
        }

        return false;
    }

    public boolean deleteTransaction(int transactionID)
    {
        boolean foundTransaction = false;

        Iterator<Transaction> itr = transactionList.iterator();
        while (itr.hasNext())
        {
            Transaction current = itr.next();
            if (current.getTransactionID() == transactionID)
            {
                foundTransaction = true;
                itr.remove();
            }
        }

        return foundTransaction;
    }

    public Transaction getTransactionByID(int id)
    {
        for(Transaction transaction : this.transactionList)
        {
            if(transaction.getTransactionID() == id){
                return transaction;
            }
        }

        return null;
    }

    public ArrayList<Transaction> getTransactionByName(int userID, String name) {

        ArrayList<Transaction> allTransactionsWithName = new ArrayList<>();

        for(Transaction transaction : this.transactionList) {

            if(transaction.getName().equalsIgnoreCase(name) && transaction.getUserID() == userID)
                allTransactionsWithName.add(transaction);

        }

        return allTransactionsWithName;

    }

    public ArrayList<Transaction> getTransactionsByPlace(int userID, String place) {

        ArrayList<Transaction> allTransactionsWithPlace = new ArrayList<>();

        for(Transaction transaction : this.transactionList) {

            if(transaction.getPlace().equalsIgnoreCase(place) && transaction.getUserID() == userID)
                allTransactionsWithPlace.add(transaction);

        }

        return allTransactionsWithPlace;

    }

    public ArrayList<Transaction> getTransactionsByAmount(int userID, double lower, double upper) {

        ArrayList<Transaction> allTransactionsInBounds = new ArrayList<>();

        for(Transaction transaction : this.transactionList) {

            double amount = transaction.getAmount();

            if( lower <= amount && amount <= upper && transaction.getUserID() == userID)
                allTransactionsInBounds.add(transaction);

        }

        return allTransactionsInBounds;

    }

    public ArrayList<Transaction> getTransactionsByDateTime(int userID, DateTime lower, DateTime upper) {

        ArrayList<Transaction> allTransactionsInBounds = new ArrayList<>();

        for(Transaction transaction : this.transactionList) {

            int lowerBound = transaction.getDateTime().compare(lower);
            int upperBound = transaction.getDateTime().compare(upper);

            if( 0 <= lowerBound && upperBound <= 0 && transaction.getUserID() == userID) {
                allTransactionsInBounds.add(transaction);
            }
        }
        return allTransactionsInBounds;
    }

    public int generateUniqueID()
    {
        return currentID++;
    }

    public ArrayList<Transaction> getTransactionsByCategoryID(int categoryID) {
        ArrayList<Transaction> allTransactionsWithID = new ArrayList<Transaction>();

        for(Transaction t : this.transactionList){
            if(t.getCategoryID() == categoryID){
                allTransactionsWithID.add(t);
            }
        }
        return allTransactionsWithID;
    }

    @Override
    public ArrayList<Transaction> getNewestTransactionsForUser(int userID)
    {
        Collections.sort(transactionList, new DateNewestFirstComparator());
        return transactionList;
    }

    @Override
    public ArrayList<Transaction> getOldestTransactionsForUser(int userID)
    {
        Collections.sort(transactionList, new DateOldestFirstComparator());
        return transactionList;
    }

}
