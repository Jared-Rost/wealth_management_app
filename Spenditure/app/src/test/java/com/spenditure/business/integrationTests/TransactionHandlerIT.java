package com.spenditure.business.integrationTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.spenditure.logic.TransactionHandler;
import com.spenditure.logic.UserHandler;
import com.spenditure.logic.exceptions.InvalidTransactionException;
import com.spenditure.object.DateTime;
import com.spenditure.object.Transaction;
import com.spenditure.utils.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class TransactionHandlerIT {

    private TransactionHandler transactionHandler;
    private UserHandler userHandler;

    private File tempDB;

    private static final int EXPECTED_SIZE = 14;

    @Before
    public void setup() throws IOException, NoSuchAlgorithmException {
        this.tempDB = TestUtils.copyDB();
        this.transactionHandler = new TransactionHandler(false);
        userHandler = new UserHandler(false);
        userHandler.login("Me", "123");
        transactionHandler.cleanup(false);
    }
    @After
    public void tearDown(){
        userHandler.logout();
        this.transactionHandler = null;
        this.tempDB = null;
    }

    @Test
    public void testTransactionSet() {

        assertEquals(EXPECTED_SIZE, transactionHandler.getAllTransactions(1).size());

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
        assertEquals("Restaurant bill for friends birthday dinner", transactionHandler.getTransactionByID(13).getName());
        assertEquals("Online course enrollment fee", transactionHandler.getTransactionByID(14).getName());

    }

    @Test
    public void testInsert() {

        assertEquals(transactionHandler.getAllTransactions(1).size(), EXPECTED_SIZE);

        int numInvalidTests = 8;
        Transaction[] invalid = new Transaction[numInvalidTests];

        //invalid ID (obsolete test; ID is handled in TransactionHandler now)
        invalid[0] = new Transaction(0, "2024 BMW M4 Competition Cabriolet", new DateTime(2024, 3, 3, 12, 0, 0), "BMW Dealership", 110200, "", true);
        //ID already exists (obsolete test; ID is handled in TransactionHandler now)
        invalid[1] = new Transaction(1, "2024 Ford Raptor R", new DateTime(2024, 2, 28, 12, 0, 0), "Ford Dealership", 118954, "Includes optional Raptor 37 Performance Package", true);
        //invalid name
        invalid[2] = new Transaction(-1, "", new DateTime(2024, 10, 10, 12, 0, 0), "???", 9000000, "???", true);
        //invalid date
        invalid[3] = new Transaction(-1, "Louisiana", new DateTime(1803, 7, 4, 12, 0, 0), "New Orleans", 358000000, "", true);
        //invalid leap year
        invalid[4] = new Transaction(-1, "2024 Land Rover Range Rover SV P615 Long Wheelbase", new DateTime(2023, 2, 29, 10, 0, 0), "Jaguar Land Rover Dealership", 397224, "Maxed out options and accessories", true);
        //invalid place
        invalid[5] = new Transaction(-1, "2024 Acura TLX Type S", new DateTime(2024, 1, 31, 20, 0, 0), "", 66478.50, "", true);
        //invalid amount
        invalid[6] = new Transaction(-1, "The Moon", new DateTime(2020, 12, 25, 16, 20, 0), "Space", -1398140054810.5082150, "Illegally acquired", true);

        //Invalid comment over character limit
        String invalidComment = "CANTWAIT!!";
        for( int i = 0; i < 350; i++ )
            invalidComment += "!";

        invalid[7] = new Transaction(-1, "2024 Porsche 911 GT3 RS", new DateTime(2024, 5, 8, 12, 12, 0), "Porsche Dealership", 301439, invalidComment, true);

        //Try inserting all invalid tests, all should return false
        for( int i = 2; i < numInvalidTests; i ++ ) {
            try {
                assertFalse(addTransaction(invalid[i]));
            } catch(InvalidTransactionException ignored) {}
        }

        //Should return true, none should have been inserted
        assertEquals(transactionHandler.getAllTransactions(1).size(), EXPECTED_SIZE);

        //Test valid insertion
        Transaction newTransaction = new Transaction(-1, "Tow Truck Fee", new DateTime(2024, 2, 29, 18, 31, 0), "Pembina Highway", 143.59, "Damn BMW", true);
        addTransaction(newTransaction);

        assertEquals(transactionHandler.getAllTransactions(1).size(), EXPECTED_SIZE + 1);

        //Should return true
        assertEquals("Morning Dons", transactionHandler.getTransactionByID(1).getName());

        DateTime testImageDate = new DateTime(2024, 3, 15, 7, 11, 0);
        Transaction testImageTransaction = new Transaction(-1, "test image", testImageDate, "higins and main", 69.69, "lovely service", true);

        byte[] byteArray = new byte[0];
        String fileName = "Image.txt";
        String directoryPath = "src/test/java/com/spenditure/business/integrationTests/";
        String path = directoryPath + fileName;

        try {
            byteArray = readBinaryDataFromFile(path);
            testImageTransaction.setImage(byteArray);
        } catch (IOException e) {
            e.printStackTrace();
        }

        testImageTransaction.setImage(byteArray);
        addTransaction(testImageTransaction);
        assertEquals(transactionHandler.getAllTransactions(1).size(), EXPECTED_SIZE + 2);

    }


    //helper method
    private byte[] readBinaryDataFromFile(String filePath) throws IOException {
        List<Byte> byteList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;

        while ((line = reader.readLine()) != null) {
            // Remove leading/trailing brackets and split by comma and space
            String[] tokens = line.substring(1, line.length() - 1).split(", ");

            // Convert tokens to bytes and add them to the list
            for (String token : tokens) {
                byteList.add(Byte.parseByte(token.trim()));
            }
        }

        // Convert List<Byte> to byte array
        byte[] byteArray = new byte[byteList.size()];
        for (int i = 0; i < byteList.size(); i++) {
            byteArray[i] = byteList.get(i);
        }

        // Close the reader
        reader.close();

        return byteArray;
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
        assertEquals("Restaurant bill for friends birthday dinner", transactionHandler.getTransactionByID(13).getName());
        assertEquals("Online course enrollment fee", transactionHandler.getTransactionByID(14).getName());

        //Ensure requests to delete invalid IDs return false
        Transaction test = new Transaction(-1, "", new DateTime(0, 0, 0, 0, 0, 0), "", 0, "", false);

        try {
            assertFalse(transactionHandler.deleteTransaction(test));
        } catch(InvalidTransactionException ignored) {}

        test = new Transaction(1000, "Quarter Pounder With Cheese", new DateTime(2024, 12, 25, 23, 13, 0), "McDonald's", 11.75, "I'm lovin' it", true);

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

            transactionHandler.addTransaction(1, "2024 Honda Civic Type R", new DateTime(2024, 2, 29, 16, 20, 0), "Winnipeg Honda", 53280.00, "MSRP", true, null, 3);
//            addTransaction(new Transaction(-1,  "2024 Honda Civic Type R", new DateTime(2024, 2, 29, 16, 20, 0), "Winnipeg Honda", 53280.00, "MSRP", true));

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
        list = transactionHandler.getTransactionByAmountBetween(1, -1,199.99);

        //Only 9 elements are strictly less than 200
        assertEquals(9, list.size());

        //Check amountGreaterThan
        list = transactionHandler.getTransactionByAmountBetween(1, 250.51, Integer.MAX_VALUE);

        //Only 2 elements are strictly greater than 250
        assertEquals(list.size(), 2);

        //Check amountBetween, which is inclusive unlike the others
        list = transactionHandler.getTransactionByAmountBetween(1, 80.25, 200.00);

        //7 elements are between 80.25 and 200 (inclusive)
        assertEquals(list.size(),7);

    }


    @Test
    public void testModifyTransaction() {
        Transaction t2 = transactionHandler.getTransactionByID(2);
        t2.setAmount(20.50);

        transactionHandler.modifyTransaction(t2);

        Transaction t2Modified = transactionHandler.getTransactionByID(2);
        assertEquals(20.50, t2Modified.getAmount(),0.1);
    }

    @Test
    public void testGetTransactionByDateTime() {
        DateTime lower = new DateTime(2023, 9, 1);
        DateTime upper = new DateTime(2024, 5, 1);

        ArrayList<Transaction> transactions = transactionHandler.getTransactionByDateTime(1, lower, upper);

        assertEquals(8,transactions.size());
    }

    @Test
    public void testGetNewestOldestTransaction() {
        ArrayList<Transaction> newestTransactions =  transactionHandler.getAllByNewestFirst(1);

        Transaction newest = newestTransactions.get(0);
        assertEquals("Star Wars Rebels merch", newest.getName());

        Transaction oldest = newestTransactions.get(newestTransactions.size()-1);
        assertEquals("Morning Dons", oldest.getName());

        ArrayList<Transaction> oldestTransactions =  transactionHandler.getAllByOldestFirst(1);

        Transaction oldest2 = oldestTransactions.get(0);
        assertEquals("Morning Dons", oldest2.getName());

        Transaction newest2 = oldestTransactions.get(oldestTransactions.size()-1);
        assertEquals("Star Wars Rebels merch", newest2.getName());
    }

    private boolean addTransaction(Transaction t) {
        return transactionHandler.addTransaction(1, t.getName(), t.getDateTime(), t.getPlace(), t.getAmount(), t.getComments(), t.getWithdrawal());
    }
}
