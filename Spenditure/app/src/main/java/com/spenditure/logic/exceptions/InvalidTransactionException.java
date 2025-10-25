/**
 * InvalidTransactionException.java
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

public class InvalidTransactionException extends RuntimeException {

    public InvalidTransactionException(String error) {

        super("Transaction data is invalid:\n" + error);

    }

}
