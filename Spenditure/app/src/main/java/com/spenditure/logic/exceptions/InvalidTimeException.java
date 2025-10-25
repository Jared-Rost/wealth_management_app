/**
 * InvalidTimeException.java
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

public class InvalidTimeException extends InvalidDateTimeException {

    public InvalidTimeException(String error) {

        super("The provided time is not valid; ensure it is between 00:00 and 23:59:\n" + error);

    }

}
