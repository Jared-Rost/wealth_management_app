/**
 * Transaction.java
 *
 * COMP3350 SECTION A02
 *
 * @author Jared Rost,
 * @date Feb 7, 2024
 *
 * PURPOSE:
 *  Store all infromation relavent to a single transaction
 *
 **/

package com.spenditure.object;

public class Transaction {

    private int transactionID;

    private int userID;

    private String name;

    private DateTime dateTime;

    private String place;

    private double amount;

    private String comments;

    private boolean withdrawal;

    private byte[] image;
    
    private int categoryID;

    public Transaction(int transactionID, int userID, String name, DateTime dateTime,
                       String place, double amount, String comments,
                       boolean withdrawal, byte[] image, int categoryID) {
        this.transactionID = transactionID;

        this.userID = userID;

        this.name = name;

        this.dateTime = dateTime;

        this.place = place;

        this.amount = amount;

        this.comments = comments;

        this.withdrawal = withdrawal;

        this.image = image;

        this.categoryID = categoryID;

    }

    public Transaction(int transactionID, int userID, String name, DateTime dateTime,
                       String place, double amount, String comments,
                       boolean withdrawal, byte[] image) {

        this(transactionID, userID, name, dateTime, place, amount, comments, withdrawal);

        this.image = image;

        this.categoryID = 0;    //represents not having an associated category
    }

    public Transaction(int transactionID, int userID, String name, DateTime dateTime,
                       String place, double amount, String comments, boolean withdrawal) {

        this.transactionID = transactionID;
        this.userID = userID;
        this.name = name;
        this.dateTime = dateTime;

        this.place = place;

        this.amount = amount;

        this.comments = comments;

        this.withdrawal = withdrawal;

        this.image = null; //no image
        this.categoryID = 0; //no associated category


    }

    public Transaction(int transactionID, String name, DateTime dateTime,
                       String place, double amount, String comments,
                       boolean withdrawal) {
        this.transactionID = transactionID;

        this.name = name;

        this.dateTime = dateTime;

        this.place = place;

        this.amount = amount;

        this.comments = comments;

        this.withdrawal = withdrawal;

        this.image = null;

        this.categoryID = 0;    //represents not having an associated category
    }

    //getters
    public int getTransactionID()
    {
        return transactionID;
    }

    public int getUserID() { return userID;}

    public String getName()
    {
        return name;
    }

    public DateTime getDateTime()
    {
        return dateTime;
    }

    public String getPlace()
    {
        return place;
    }

    public double getAmount()
    {
        return amount;
    }

    public String getComments()
    {
        return comments;
    }

    public boolean getWithdrawal()
    {
        return withdrawal;
    }

    public byte[] getImage() {
        return image;
    }

    public int getCategoryID() {
        return categoryID;
    }


    // setters
    public void setTransactionID(int transactionID)
    {
        this.transactionID = transactionID;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setDateTime(DateTime dateTime)
    {
        this.dateTime = dateTime;
    }

    public void setPlace(String place)
    {
        this.place = place;
    }

    public void setAmount(double amount)
    {
        this.amount = amount;
    }

    public void setComments(String comments)
    {
        this.comments = comments;
    }

    public void setWithdrawal(boolean withdrawal)
    {
        this.withdrawal = withdrawal;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

}
