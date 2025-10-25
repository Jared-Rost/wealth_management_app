/**
 * TransactionHandlerTest.java
 *
 * COMP3350 SECTION A02
 *
 * @author Toran Pillay, 7842389
 * @date Tuesday, February 6, 2024
 *
 * PURPOSE:
 *  This file tests for expected behavior in the Transaction Handler.
 **/

package com.spenditure.business.unitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.spenditure.logic.TransactionHandler;
import com.spenditure.logic.exceptions.InvalidDateTimeException;
import com.spenditure.logic.exceptions.InvalidTransactionAmountException;
import com.spenditure.logic.exceptions.InvalidTransactionException;
import com.spenditure.logic.exceptions.InvalidTransactionNameException;
import com.spenditure.logic.exceptions.InvalidTransactionPlaceException;
import com.spenditure.object.DateTime;
import com.spenditure.object.Transaction;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class TransactionHandlerTest {

    private TransactionHandler transactionHandler;
    private static final int EXPECTED_SIZE = 14;

    @Before
    public void setup() {
        this.transactionHandler = new TransactionHandler(true);
        this.transactionHandler.cleanup(true);
    }

    @After
    public void tearDown() {
        this.transactionHandler = null;

    }

    @Test
    public void testTransactionSet() {

        assertEquals(transactionHandler.getAllTransactions(1).size(), EXPECTED_SIZE);

        //Tests to make sure all transactions are returned. All should return true
        assertEquals("Morning Dons", transactionHandler.getTransactionByID(1).getName());
        assertEquals("Star Wars Rebels merch", transactionHandler.getTransactionByID(2).getName());
        assertEquals("Shopping spree at the mall", transactionHandler.getTransactionByID(3).getName());
        assertEquals("Dinner at a fancy restaurant", transactionHandler.getTransactionByID(4).getName());
        assertEquals("Hotel accommodation for business trip", transactionHandler.getTransactionByID(5).getName());
        assertEquals("Grocery shopping for the week", transactionHandler.getTransactionByID(6).getName());
        assertEquals("Utility bill payment", transactionHandler.getTransactionByID(7).getName());
        assertEquals("Concert ticket purchase", transactionHandler.getTransactionByID(8).getName());
        assertEquals("Car maintenance service", transactionHandler.getTransactionByID(9).getName());
        assertEquals("Online shopping for household items", transactionHandler.getTransactionByID(10).getName());
        assertEquals("Movie tickets for family outing", transactionHandler.getTransactionByID(11).getName());
        assertEquals("Gym membership subscription", transactionHandler.getTransactionByID(12).getName());
        assertEquals("Restaurant bill for friend's birthday dinner", transactionHandler.getTransactionByID(13).getName());
        assertEquals("Online course enrollment fee", transactionHandler.getTransactionByID(14).getName());



    }

    @Test
    public void testInsert() {

        assertEquals(transactionHandler.getAllTransactions(1).size(), EXPECTED_SIZE);

        int numInvalidTests = 8;
        Transaction[] invalid = new Transaction[numInvalidTests];

        //invalid ID (no longer needed now that TransactionHandler builds transactions)
        invalid[0] = new Transaction(0, 1, "2024 BMW M4 Competition Cabriolet", new DateTime(2024, 3, 3, 12, 00, 0), "BMW Dealership", 110200, "", true);
        //ID already exists (no longer needed now that TransactionHandler builds transactions)
        invalid[1] = new Transaction(1, 1, "2024 Ford Raptor R", new DateTime(2024, 2, 28, 12, 00, 0), "Ford Dealership", 118954, "Includes optional Raptor 37 Performance Package", true);
        //invalid name
        invalid[2] = new Transaction(-1, 1, "", new DateTime(2024, 10, 10, 12, 00,0), "???", 9000000, "???", true);
        //invalid date
        invalid[3] = new Transaction(-1, 1, "Louisiana", new DateTime(1803, 7, 4, 12, 00, 0), "New Orleans", 358000000, "", true);
        //invalid leap year
        invalid[4] = new Transaction(-1, 1, "2024 Land Rover Range Rover SV P615 Long Wheelbase", new DateTime(2023, 2, 29, 10, 00,0 ), "Jaguar Land Rover Dealership", 397224, "Maxed out options and accessories", true);
        //invalid place
        invalid[5] = new Transaction(-1, 1, "2024 Acura TLX Type S", new DateTime(2024, 1, 31, 20, 00, 0), "", 66478.50, "", true);
        //invalid amount
        invalid[6] = new Transaction(-1, 1, "The Moon", new DateTime(2020, 12, 25, 16, 20, 0), "Space", -1398140054810.5082150, "Illegally acquired", true);

        //Invalid comment over character limit
        String invalidComment = "CANTWAIT!!";
        for( int i = 0; i < 350; i++ )
            invalidComment += "!";

        invalid[7] = new Transaction(-1, 1, "2024 Porsche 911 GT3 RS", new DateTime(2024, 5, 8, 12, 12, 0), "Porsche Dealership", 301439, invalidComment, true);

        //Try inserting all invalid tests, all should return false
        for( int i = 2; i < numInvalidTests; i ++ ) {
            try {
                assertFalse(addTransaction(invalid[i]));
            } catch(InvalidTransactionException ignored) {}
        }

        //Should return true, none should have been inserted
        assertEquals(transactionHandler.getAllTransactions(1).size(), EXPECTED_SIZE);

        //Test valid insertion
        Transaction newTransaction = new Transaction(-1, 1, "Tow Truck Fee", new DateTime(2024, 2, 29, 18, 31, 0), "Pembina Highway", 143.59, "Damn BMW", true);
        addTransaction(newTransaction);

        assertEquals(transactionHandler.getAllTransactions(1).size(), EXPECTED_SIZE + 1);

        //Should return true
        assertEquals("Morning Dons", transactionHandler.getTransactionByID(1).getName());

        assertEquals("Tow Truck Fee", transactionHandler.getTransactionByID(EXPECTED_SIZE + 1).getName());




    }

    @Test
    public void testDelete() {

        assertEquals(transactionHandler.getAllTransactions(1).size(), EXPECTED_SIZE);

        //Make sure all items are in the list
        assertEquals("Morning Dons", transactionHandler.getTransactionByID(1).getName());
        assertEquals("Star Wars Rebels merch", transactionHandler.getTransactionByID(2).getName());
        assertEquals("Shopping spree at the mall", transactionHandler.getTransactionByID(3).getName());
        assertEquals("Dinner at a fancy restaurant", transactionHandler.getTransactionByID(4).getName());
        assertEquals("Hotel accommodation for business trip", transactionHandler.getTransactionByID(5).getName());
        assertEquals("Grocery shopping for the week", transactionHandler.getTransactionByID(6).getName());
        assertEquals("Utility bill payment", transactionHandler.getTransactionByID(7).getName());
        assertEquals("Concert ticket purchase", transactionHandler.getTransactionByID(8).getName());
        assertEquals("Car maintenance service", transactionHandler.getTransactionByID(9).getName());
        assertEquals("Online shopping for household items", transactionHandler.getTransactionByID(10).getName());
        assertEquals("Movie tickets for family outing", transactionHandler.getTransactionByID(11).getName());
        assertEquals("Gym membership subscription", transactionHandler.getTransactionByID(12).getName());
        assertEquals("Restaurant bill for friend's birthday dinner", transactionHandler.getTransactionByID(13).getName());
        assertEquals("Online course enrollment fee", transactionHandler.getTransactionByID(14).getName());

        //Ensure requests to delete invalid IDs return false
        Transaction test = new Transaction(-1, 1, "", new DateTime(0, 0, 0, 0, 0, 0), "", 0, "", false);

        try {
            assertFalse(transactionHandler.deleteTransaction(test));
        } catch(InvalidTransactionException ignored) {}

        test = new Transaction(1000, 1, "Quarter Pounder With Cheese", new DateTime(2024, 12, 25, 23, 13, 0), "McDonald's", 11.75, "I'm lovin' it", true);

        assertFalse(transactionHandler.deleteTransaction(test));

        //Delete every odd transaction ID
        for( int i = 0; i <= EXPECTED_SIZE/2; i ++ ) {
            Transaction toDelete = transactionHandler.getTransactionByID((2* i + 1));

            try {
                transactionHandler.deleteTransaction(toDelete);
            } catch(InvalidTransactionException ignored) {}
        }

        //Should return true, list should be half the size
        assertEquals(EXPECTED_SIZE/2, transactionHandler.getAllTransactions(1).size());

        //Ensure the correct transactions were deleted
        assertNull(transactionHandler.getTransactionByID(1));
        assertEquals("Star Wars Rebels merch", transactionHandler.getTransactionByID(2).getName());
        assertNull(transactionHandler.getTransactionByID(3));
        assertEquals("Dinner at a fancy restaurant", transactionHandler.getTransactionByID(4).getName());
        assertNull(transactionHandler.getTransactionByID(5));
        assertEquals("Grocery shopping for the week", transactionHandler.getTransactionByID(6).getName());
        assertNull(transactionHandler.getTransactionByID(7));
        assertEquals("Concert ticket purchase", transactionHandler.getTransactionByID(8).getName());
        assertNull(transactionHandler.getTransactionByID(9));
        assertEquals("Online shopping for household items", transactionHandler.getTransactionByID(10).getName());
        assertNull(transactionHandler.getTransactionByID(11));
        assertEquals("Gym membership subscription", transactionHandler.getTransactionByID(12).getName());
        assertNull(transactionHandler.getTransactionByID(13));
        assertEquals("Online course enrollment fee", transactionHandler.getTransactionByID(14).getName());


    }

    @Test
    public void testModify() {

        assertEquals(transactionHandler.getAllTransactions(1).size(), EXPECTED_SIZE);

        Transaction toModify = transactionHandler.getTransactionByID(2);

        //Modifying name to invalid value, should return false
        toModify.setName("");

        try {
            assertFalse(transactionHandler.modifyTransaction(toModify));
        } catch(InvalidTransactionNameException ignored) {}

        //Modifying name to valid value, should return true
        toModify.setName("Paycheck");
        assertTrue(transactionHandler.modifyTransaction(toModify));
        assertEquals(transactionHandler.getTransactionByID(2).getName(), "Paycheck");

        //Modifying date/time to invalid value, should return false
        toModify.setDateTime(new DateTime(159025, 1048, 1058, 159810, 15970135, 0));

        //Modifying date/time to valid value, should return true
        try {
            assertFalse(transactionHandler.modifyTransaction(toModify));
        } catch(InvalidDateTimeException ignored) {}

        toModify.setDateTime(new DateTime(2024, 4, 15, 00, 00, 0));
        assertTrue(transactionHandler.modifyTransaction(toModify));

        //Modifying place to invalid value, should return false
        toModify.setPlace("");

        try {
            assertFalse(transactionHandler.modifyTransaction(toModify));
        } catch(InvalidTransactionPlaceException ignored) {}

        //Modifying place to valid value, should return true
        toModify.setPlace("Work");
        assertTrue(transactionHandler.modifyTransaction(toModify));
        assertEquals(transactionHandler.getTransactionByID(2).getPlace(), "Work");

        //Modifying amount to invalid value, should return false
        toModify.setAmount(-1358154093.91399);

        try {
            assertFalse(transactionHandler.modifyTransaction(toModify));
        } catch(InvalidTransactionAmountException ignored) {}

        //Modifying amount to valid value, should return true
        toModify.setAmount(1500.00);
        assertTrue(transactionHandler.modifyTransaction(toModify));
        assertEquals(transactionHandler.getTransactionByID(2).getAmount(), 1500.00, 1);

        //Modifying comment
        toModify.setComments("");
        assertTrue(transactionHandler.modifyTransaction(toModify));

        //Modifying type
        toModify.setWithdrawal(false);
        assertTrue(transactionHandler.modifyTransaction(toModify));
        assertFalse(transactionHandler.getTransactionByID(2).getWithdrawal());

        assertEquals(transactionHandler.getAllTransactions(1).size(), EXPECTED_SIZE);

    }

    @Test
    public void testGetOldestFirst() {

        ArrayList<Transaction> list = transactionHandler.getAllByOldestFirst(1);

        //Test specific names that should have gone to the front/back; should return true
        assertEquals(list.get(0).getName(), transactionHandler.getTransactionByID(2).getName());
        assertEquals(list.get(EXPECTED_SIZE-1).getName(), transactionHandler.getTransactionByID(1).getName());

    }

    @Test
    public void testGetNewestFirst() {

        ArrayList<Transaction> list = transactionHandler.getAllByNewestFirst(1);

        //Test specific names that should have gone to the front/back; should return true
        assertEquals(list.get(0).getName(), transactionHandler.getTransactionByID(1).getName());
        assertEquals(list.get(EXPECTED_SIZE-1).getName(), transactionHandler.getTransactionByID(2).getName());
    }

    @Test
    public void testGetTransactionByCategoryID() {

        //Should only contain transactions with "grocery" category
        ArrayList<Transaction> list = transactionHandler.getTransactionByCategoryID(1);

        //Should only be 4 items
        assertEquals(list.size(), 4);

        //Check items, all should be true
        assertEquals(list.get(0).getName(), "Grocery shopping for the week");
        assertEquals(list.get(1).getName(), "Utility bill payment");
        assertEquals(list.get(2).getName(), "Online shopping for household items");
        assertEquals(list.get(3).getName(), "Online course enrollment fee");

    }

    @Test
    public void testGetTransactionByNameAndPlace() {

        int numInserts = 3;

        //Search for existing item
        ArrayList<Transaction> nameList = transactionHandler.getTransactionByName(1, "Morning Dons");
        ArrayList<Transaction> placeList = transactionHandler.getTransactionByPlace(1, "Mcdonalds");

        //Should only be one
        assertEquals(nameList.size(), 1);
        assertEquals(placeList.size(), 1);

        //Get multiple transactions with the same name
        for(int i = 0; i < numInserts; i ++) {

            addTransaction(new Transaction(-1, 1,  "2024 Honda Civic Type R", new DateTime(2024, 2, 29, 16, 20, 0), "Winnipeg Honda", 53280.00, "MSRP", true));

        }

        nameList = transactionHandler.getTransactionByName(1, "2024 Honda Civic Type R");
        placeList = transactionHandler.getTransactionByPlace(1, "Winnipeg Honda");

        //Should return true, list should have the specified number of inserts
        assertEquals(nameList.size(), numInserts);
        assertEquals(placeList.size(), numInserts);

    }

    @Test
    public void testGetByAmount() {

        //Check getByAmount
        ArrayList<Transaction> list = transactionHandler.getTransactionByAmountBetween(1, 200.00, 200.00);

        //There are 2 values that are exactly 200
        assertEquals(list.size(), 2);

        //Check amountLessThan
        list = transactionHandler.getTransactionByAmountBetween(1, -1,200);

        //Only 11 elements are less than 200
        assertEquals(list.size(), 11);

        //Check amountGreaterThan
        list = transactionHandler.getTransactionByAmountBetween(1, 250.50, Integer.MAX_VALUE);

        //Only 3 elements are greater than 250
        assertEquals(list.size(), 3);

        //Check amountBetween, which is inclusive
        list = transactionHandler.getTransactionByAmountBetween(1,80.25, 200.00);

        //7 elements are between 80.25 and 200 (inclusive)
        assertEquals(list.size(),7);

    }

    @Test
    public void testGetByDate() {

        DateTime date = new DateTime(2023, 9, 15, 16, 0, 0);
        //Test retrieval of specific item by date
        ArrayList<Transaction> list = transactionHandler.getTransactionByDateTime(1, date, date);

        //Both should be true
        assertEquals(list.size(), 1);
        assertEquals(list.get(0).getName(), "Online shopping for household items");

        //Test retrieval of items between specified dates
        list = transactionHandler.getTransactionByDateTime(1,
                new DateTime(2023, 9, 1, 00, 00, 0),
                new DateTime(2023, 9, 31, 23, 59, 0));
        assertEquals(list.size(), 6);



    }

    private boolean addTransaction(Transaction t) {
        return transactionHandler.addTransaction(1, t.getName(), t.getDateTime(), t.getPlace(), t.getAmount(), t.getComments(), t.getWithdrawal());
    }

}
