package com.spenditure.logic.exceptions;

public class InvalidStringFormat extends RuntimeException{
    public InvalidStringFormat(String error) {

        super("Invalid string format: " + error);

    }
}
