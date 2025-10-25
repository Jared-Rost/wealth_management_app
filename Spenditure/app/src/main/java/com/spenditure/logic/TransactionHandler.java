/**
 * TransactionHandler.java
 *
 * COMP3350 SECTION A02
 *
 * @author Toran Pillay, 7842389
 * @date March 25, 2024
 *
 * PURPOSE:
 *  This file interprets information from Transactions sent by the UI layer,
 * sends information to the Database layer, and performs all logic operations
 * pertaining to Transactions.
 **/

package com.spenditure.logic;

import com.spenditure.application.Services;
import com.spenditure.database.TransactionPersistence;
import com.spenditure.object.DateTime;
import com.spenditure.object.Transaction;
import com.spenditure.logic.exceptions.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
public class TransactionHandler implements ITransactionHandler, Serializable {

    //Handles communication with data layer
    private static TransactionPersistence dataAccessTransaction;
    //Expected ID for new transactions that have not yet been added
    private static final int NEW_TRANSACTION_ID = -1;

    public TransactionHandler(boolean getStubDB) {
        if( dataAccessTransaction == null){
            dataAccessTransaction = Services.getTransactionPersistence(getStubDB);
        }
    }
    /*
        Clean up stub transaction database for testing purpose ( for iteration 1)
     */
    public void cleanup(boolean getStubDB){
        Services.restartTransactionDB(getStubDB);
        dataAccessTransaction = Services.getTransactionPersistence(getStubDB);
    }

    /*
        addTransaction

        Takes all of the values needed to create a new transaction, validates that transaction, and then
        adds it to the database.
     */

    @Override
    public boolean addTransaction(int userID, String whatTheHeck, DateTime date, String place, double amount, String comments, boolean type) throws InvalidTransactionException {

        Transaction t = new Transaction( NEW_TRANSACTION_ID, userID, whatTheHeck, date, place, amount, comments, type );

        if( !checkNewTransactionID(t) )
            throw new InvalidTransactionException("Transaction I.D. of new transaction (I.D.: "
                    + t.getTransactionID() +") is invalid; the transaction may already exist.");

        TransactionValidator.validateTransaction(t);
        return dataAccessTransaction.addTransaction(t);

    }

    /*

        addTransaction

        Alternate constructor

     */
    @Override
    public boolean addTransaction(int userID, String whatTheHeck, DateTime date, String place, double amount, String comments, boolean withdrawal, byte[] image, int categoryID)
        throws InvalidTransactionException {

        Transaction t = new Transaction( NEW_TRANSACTION_ID, userID, whatTheHeck, date, place, amount, comments, withdrawal, image, categoryID );

        TransactionValidator.validateTransaction(t);
        return dataAccessTransaction.addTransaction(t);
    }

    /*
        modifyTransaction

        Checks the ID to make sure it's not a new transaction,
        validates it, then sends it to the data layer to overwrite
        the old one.
     */
    @Override
    public boolean modifyTransaction(Transaction t) throws InvalidTransactionException {

        if( t == null )
            throw new InvalidTransactionException("No transaction was provided to modify!");

        else if(checkNewTransactionID(t))
            throw new InvalidTransactionException("Transaction I.D. of new transaction (I.D.: "
                    + t.getTransactionID() +") is invalid; the transaction may not exist.");

        TransactionValidator.validateTransaction(t);
        return dataAccessTransaction.modifyTransaction(t);
    }

    /*
        deleteTransaction

        Validates the transaction, then sends it to the data layer
        so it can be deleted.
     */
    @Override
    public boolean deleteTransaction(Transaction t) throws InvalidTransactionException {

        if( t == null )
            throw new InvalidTransactionException("No transaction was provided to delete!");

        else if(checkNewTransactionID(t))
            throw new InvalidTransactionException("Transaction I.D. of new transaction (I.D.: "
                    + t.getTransactionID() +") is invalid; the transaction may not exist.");

        return dataAccessTransaction.deleteTransaction(t.getTransactionID());

    }

    /*
        getTransactionByID

        Takes an ID, and returns the corresponding transaction if
        one is found by the data layer.
     */
    @Override
    public Transaction getTransactionByID(int id) {

        return dataAccessTransaction.getTransactionByID(id);

    }

    /*
        getAllTransactions

        Returns all transactions given by the data layer.
     */
    @Override
    public List<Transaction> getAllTransactions(int userID) {

        return dataAccessTransaction.getAllTransactionsForUser(userID);

    }

    /*
        getAllByNewestFirst

        Returns all transactions, sorted by newest first
     */
    @Override
    public ArrayList<Transaction> getAllByNewestFirst(int userID) {

       return dataAccessTransaction.getNewestTransactionsForUser(userID);

    }

    /*
        getAllByOldestFirst

        Returns all transactions, sorted by oldest first
     */
    @Override
    public ArrayList<Transaction> getAllByOldestFirst(int userID) {


        return dataAccessTransaction.getOldestTransactionsForUser(userID);

    }

    /*

        getTransactionByCategoryID

        Returns all transactions associated with the specified
        category.

     */
    @Override
    public ArrayList<Transaction> getTransactionByCategoryID(int categoryID) {

        return dataAccessTransaction.getTransactionsByCategoryID(categoryID);

    }

    /*

        getTransactionByName

        Returns all transactions with names matching the provided String.

     */
    @Override
    public ArrayList<Transaction> getTransactionByName(int userID, String name) {

        return dataAccessTransaction.getTransactionByName(userID,name);

    }

    @Override
    public ArrayList<Transaction> getTransactionByPlace(int userID, String place) {

        return dataAccessTransaction.getTransactionsByPlace(userID, place);

    }



    /*

        getTransactionByAmountBetween

        Returns all transactions with amount values equal to or between the given lower
        and upper bounds.

     */
    @Override
    public ArrayList<Transaction> getTransactionByAmountBetween(int userID, double lower, double upper) {

        return dataAccessTransaction.getTransactionsByAmount(userID, lower, upper);

    }


    /*

        getTransactionByDateTime

        Returns all transactions which occur at the specified date and time

     */
    public ArrayList<Transaction> getTransactionByDateTime(int userID, DateTime lower, DateTime upper) {

        return dataAccessTransaction.getTransactionsByDateTime(userID, lower, upper);

    }



    /*
        checkNewTransactionID

        Checks to see if the transaction has a new ID, meaning it
        hasn't yet been added to the database.
     */
    private boolean checkNewTransactionID(Transaction t) {

        return t.getTransactionID() == NEW_TRANSACTION_ID;

    }




}
