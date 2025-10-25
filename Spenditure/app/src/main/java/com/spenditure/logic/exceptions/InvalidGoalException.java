package com.spenditure.logic.exceptions;

public class InvalidGoalException extends RuntimeException{

    public InvalidGoalException(String error){
        super("Invalid Goal:\n" +  error);
    }
}
