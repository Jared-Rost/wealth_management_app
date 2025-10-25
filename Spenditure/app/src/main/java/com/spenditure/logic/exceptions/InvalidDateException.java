/**
 * InvalidDateException.java
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

public class InvalidDateException extends InvalidDateTimeException {

    public InvalidDateException(String error) {

        super("The provided date is not a valid date: \n" + error);

    }
}
