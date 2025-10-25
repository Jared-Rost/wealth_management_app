/**
 * ITransactionHandler.java
 *
 * COMP3350 SECTION A02
 *
 * @author Toran Pillay, 7842389
 * @date Tuesday, February 6, 2024
 *
 * PURPOSE:
 *  Interface file for the Transaction Handler.
 **/

package com.spenditure.logic;

import com.spenditure.logic.exceptions.InvalidTransactionException;
import com.spenditure.object.DateTime;
import com.spenditure.object.Transaction;

import java.util.ArrayList;
import java.util.List;


public interface ITransactionHandler {

    //Takes all of the values needed to create a new transaction, validates that transaction, and then adds it to the database.
    boolean addTransaction(int userID, String whatTheHeck, DateTime date, String place, double amount, String comments, boolean type);

    //Takes all of the values needed to create a new transaction, validates that transaction, and then adds it to the database.
    boolean addTransaction(int userID, String whatTheHeck, DateTime date, String place, double amount, String comments, boolean withdrawal, byte[] image, int categoryID)
        throws InvalidTransactionException;

    //Given an existing transaction, overwrites that transaction in the database.
    boolean modifyTransaction(Transaction t);

    //Given an existing transaction, removes that transaction from the database.
    boolean deleteTransaction(Transaction t);

    //Given a Transaction ID, returns that transaction.
    Transaction getTransactionByID(int id);

    //Returns a List of all Transactions associated with a User ID.
    List<Transaction> getAllTransactions(int userID);

    //Returns a list of all a User's transaction in order of date, newest first.
    ArrayList<Transaction> getAllByNewestFirst(int userID);

    //Returns a list of all a User's transactions in order of date, oldest first.
    ArrayList<Transaction> getAllByOldestFirst(int userID);

    //Returns a list of all transactions associated with a given Category ID.
    ArrayList<Transaction> getTransactionByCategoryID(int categoryID);

    //Returns a list of all a User's transactions containing the specified name.
    ArrayList<Transaction> getTransactionByName(int userID, String name);

    //Returns a list of all a User's transactions containing the specified place.
    ArrayList<Transaction> getTransactionByPlace(int userID, String place);

    //Returns a list of all a User's transactions with an amount between the specified bounding values.
    ArrayList<Transaction> getTransactionByAmountBetween(int userID, double lower, double upper);

    //Returns a list of all a User's transactions with a matching DateTime.
    ArrayList<Transaction> getTransactionByDateTime(int userID, DateTime lower, DateTime upper);

}
