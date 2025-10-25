/**
 * InvalidTransactionAmountException.java
 *
 * COMP3350 SECTION A02
 *
 * @author Bao,
 * @date Feb 7, 2024
 *
 * PURPOSE:
 *  Provide more information on what went wrong
 *
 **/


package com.spenditure.logic.exceptions;

public class InvalidTransactionAmountException extends InvalidTransactionException {

    public InvalidTransactionAmountException(String error) {

        super("\"Amount\" value cannot be a negative number:\n" + error);

    }
}
