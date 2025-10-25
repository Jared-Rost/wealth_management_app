/**
 * InvalidTransactionCommentException.java
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

import com.spenditure.logic.TransactionValidator;

public class InvalidTransactionCommentException extends InvalidTransactionException {

    public InvalidTransactionCommentException(String error) {

        super("Transaction comments may not be more than "
                + TransactionValidator.COMMENT_CHAR_LIMIT
                + " characters long.\n" + error);

    }

}
