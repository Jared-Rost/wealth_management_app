/**
 * TransactionValidator.java
 *
 * COMP3350 SECTION A02
 *
 * @author Toran Pillay, 7842389
 * @date March 25, 2024
 *
 * PURPOSE:
 *  This file contains all of the methods necessary to validate the data in a
 * Transaction.
 **/

package com.spenditure.logic;

import com.spenditure.object.Transaction;
import com.spenditure.logic.exceptions.*;

public class TransactionValidator {

    //Max number of characters allowed in a comment
    public static final int COMMENT_CHAR_LIMIT = 300;

    /*
        validateTransaction

        throws exception if not valid
     */
    public static void validateTransaction(Transaction t) throws InvalidTransactionException {

        if(t != null) {
            validateName(t.getName());
            DateTimeValidator.validateDateTime(t.getDateTime());
            validatePlace(t.getPlace());
            validateAmount(t.getAmount());
            checkComment(t.getComments());
            //Type is a boolean, so it can't be invalid

        }

    }

    /*
        validateName

        ensures the name is not null and at least 1 character long
     */
    private static void validateName(String name) throws InvalidTransactionNameException {

        if( name == null || name.isEmpty())
            throw new InvalidTransactionNameException("Provided name was blank.");

    }


    /*
        validatePlace

        ensures the place is not null and at least 1 character long
     */
    private static void validatePlace(String place) throws InvalidTransactionPlaceException {

        if( place == null || place.isEmpty())
            throw new InvalidTransactionPlaceException("Provided place was blank.");


    }

    /*
        validateAmount

        ensures the amount is at least 0 and fits the minimum increment value
     */
    private static void validateAmount(double amount) throws InvalidTransactionAmountException {

        if(amount < 0)
            throw new InvalidTransactionAmountException("Provided amount "+amount+" is a negative number.");


    }

    /*
        checkComment

        ensures the comment meets the character limit, if a comment
        exists
     */
    private static void checkComment(String comment) throws InvalidTransactionCommentException {

        boolean withinLimit = true;

        if( comment != null )
            withinLimit = comment.length() <= COMMENT_CHAR_LIMIT;

        if( !withinLimit )
        {
            int overflow = comment.length() - COMMENT_CHAR_LIMIT;
            throw new InvalidTransactionCommentException("Provided comment was " + overflow + " characters too long.");
        }

    }



}
