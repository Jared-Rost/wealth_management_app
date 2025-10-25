package com.spenditure.database;

import com.spenditure.object.DateTime;
import com.spenditure.object.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * TransactionPersistence.java
 *
 * COMP3350 SECTION A02
 *
 * @author Jared Rost,
 * @date Feb 7, 2024
 *
 * PURPOSE:
 *  This file provides information on what methods the transaction database handler should implement
 *
 **/

public interface TransactionPersistence {

    // query database
    List<Transaction> getAllTransactionsForUser(int userID);
    boolean addTransaction(Transaction newTransaction);
    boolean modifyTransaction(Transaction targetTransaction);
    boolean deleteTransaction(int transactionID);
    Transaction getTransactionByID(int id);
    ArrayList<Transaction> getTransactionByName(int userID, String name);
    ArrayList<Transaction> getTransactionsByPlace(int userID, String place);
    ArrayList<Transaction> getTransactionsByAmount(int userID, double lower, double upper);
    ArrayList<Transaction> getTransactionsByDateTime(int userID, DateTime lower, DateTime upper);
    ArrayList<Transaction> getTransactionsByCategoryID(int categoryID);

    // sorting
    ArrayList<Transaction> getNewestTransactionsForUser(int userID);
    ArrayList<Transaction> getOldestTransactionsForUser(int userID);

}
