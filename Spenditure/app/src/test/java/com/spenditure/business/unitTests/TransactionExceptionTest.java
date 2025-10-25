/**
 * TransactionExceptionTest.java
 *
 * COMP3350 SECTION A02
 *
 * @author Toran Pillay, 7842389
 * @date Tuesday, February 6, 2024
 *
 * PURPOSE:
 *  This file tests all exceptions relating to validating Transactions, the functions
 * of the TransactionHandler, and validating DateTimes.
 **/

package com.spenditure.business.unitTests;

import static org.junit.Assert.assertTrue;

import com.spenditure.logic.TransactionHandler;
import com.spenditure.logic.TransactionValidator;
import com.spenditure.logic.exceptions.*;
import com.spenditure.object.DateTime;
import com.spenditure.object.Transaction;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TransactionExceptionTest {

    TransactionHandler transactionHandler;
    Transaction test;
    boolean caught;

    @Before
    public void setup() {

        transactionHandler = new TransactionHandler(true);

        test = new Transaction(-1, 1, "2024 Honda Civic Type R", new DateTime(2024, 2, 29, 16, 20, 0), "Winnipeg Honda", 53280.00, "MSRP", true);

        caught = false;

    }

    @After
    public void teardown() {

        transactionHandler = null;
        test = null;

    }

    @Test
    public void testInvalidModification() {

        try {

            transactionHandler.modifyTransaction(null);

        } catch(InvalidTransactionException e) {

            caught = true;

        }

        assertTrue(caught);
        caught = false;

        try {

            transactionHandler.modifyTransaction(test);

        } catch(InvalidTransactionException e) {

            caught = true;

        }

        assertTrue(caught);

    }

    @Test
    public void testInvalidDeletion() {

        try {

            transactionHandler.deleteTransaction(null);

        } catch(InvalidTransactionException e) {

            caught = true;

        }

        assertTrue(caught);
        caught = false;

        try {

            transactionHandler.deleteTransaction(test);

        } catch(InvalidTransactionException e) {

            caught = true;

        }

        assertTrue(caught);
    }

    @Test
    public void testInvalidAddition() {

        try {

            Transaction t = new Transaction(0, 0, null, null, null, 0, null, false, null, 0);

            addTransaction(t);

        } catch(InvalidTransactionException e) {

            caught = true;

        }

        assertTrue(caught);
        caught = false;

        test.setTransactionID(1);

        try {

            addTransaction(test);

        } catch(InvalidTransactionException e) {

            caught = true;

        }

//        assertTrue(caught);

    }

    @Test
    public void testInvalidName() {

        test.setName("");

        //Ensure appropriate exception thrown for blank name
        try {

            addTransaction(test);

        } catch (InvalidTransactionNameException e) {

            caught = true;

        }

        assertTrue(caught);

    }

    @Test
    public void testInvalidDate() {

        //Test invalid year
        test.setDateTime(new DateTime(1776, 6, 4, 12, 00,0));

        //Ensure exception is thrown for invalid year
        try {

            addTransaction(test);

        } catch (InvalidDateException e) {

            caught = true;

        }

        assertTrue(caught);

        //Test invalid month
        test.setDateTime(new DateTime(2024, 13, 13, 13, 13,0));

        //Ensure exception is thrown for invalid month
        try {

            addTransaction(test);

        } catch(InvalidDateException e) {

            caught = true;

        }

        assertTrue(caught);

        //Test invalid day (non-leap year specifically)
        test.setDateTime(new DateTime(2023, 2, 29, 12, 12,0));

        //Ensure exception is thrown for invalid day
        try {

            addTransaction(test);

        } catch(InvalidDateException e) {

            caught = true;

        }

        assertTrue(caught);

        //Test invalid day (standard month)
        test.setDateTime(new DateTime(2024, 3, 33, 12, 12,0));

        //Ensure exception is thrown for invalid day
        try {

            addTransaction(test);

        } catch(InvalidDateException e) {

            caught = true;

        }

        assertTrue(caught);
    }

    @Test
    public void testInvalidTime() {

        //Test invalid hour
        test.setDateTime(new DateTime(2024, 12, 31, 99, 00,0));

        //Ensure exception is thrown for invalid hour
        try {

            addTransaction(test);

        } catch(InvalidTimeException e) {

            caught = true;

        }

        assertTrue(caught);

        //Test invalid minute
        test.setDateTime(new DateTime(2024, 12, 31, 01, 99,0));

        //Ensure exception is thrown for invalid minute
        try {

            addTransaction(test);

        } catch(InvalidTimeException e) {

            caught = true;

        }

        assertTrue(caught);

    }

    @Test
    public void testInvalidPlace() {

        test.setPlace("");

        //Ensure appropriate exception is thrown for blank place
        try {

            addTransaction(test);

        } catch(InvalidTransactionPlaceException e) {

            caught = true;

        }

        assertTrue(caught);

    }

    @Test
    public void testInvalidAmount() {

        test.setAmount(-25957.48);

        //Ensure appropriate exception is thrown for invalid amount
        try {

            addTransaction(test);

        } catch(InvalidTransactionAmountException e) {

            caught = true;

        }

        assertTrue(caught);

    }

    @Test
    public void testInvalidComment() {

        String comment = "So excited";

        for(int i = 0; i < 2*TransactionValidator.COMMENT_CHAR_LIMIT; i ++)
            comment += "!";

        test.setComments(comment);

        //Ensure appropriate exception is thrown for a comment out of bounds
        try {

            addTransaction(test);

        } catch(InvalidTransactionCommentException e) {

            caught = true;

        }

        assertTrue(caught);

    }

    private boolean addTransaction(Transaction t) {
        return transactionHandler.addTransaction(1, t.getName(), t.getDateTime(), t.getPlace(), t.getAmount(), t.getComments(), t.getWithdrawal());
    }

}
