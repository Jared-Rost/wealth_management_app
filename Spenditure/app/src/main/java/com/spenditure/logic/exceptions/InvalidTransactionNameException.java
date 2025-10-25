/**
 * InvalidTransactionNameException.java
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

public class InvalidTransactionNameException extends InvalidTransactionException {

    public InvalidTransactionNameException(String error) {

        super("\"Title\" field must not be left empty:\n" + error);

    }
}
