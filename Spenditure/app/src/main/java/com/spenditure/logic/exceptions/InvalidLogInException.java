/**
 * InvalidLogInException
 *
 * COMP3350 SECTION A02
 *
 * @author JR,
 * @date Mar 2
 *
 * PURPOSE:
 *  Is thrown if the user ID is not valid and is attemtped to be used
 *
 **/


package com.spenditure.logic.exceptions;

public class InvalidLogInException extends RuntimeException {
    public InvalidLogInException(){
        super("No user is logged in\n");
    }
}
