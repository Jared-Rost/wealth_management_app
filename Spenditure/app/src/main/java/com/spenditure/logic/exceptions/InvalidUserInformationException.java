package com.spenditure.logic.exceptions;

public class InvalidUserInformationException extends RuntimeException {
    public InvalidUserInformationException(String error){
        super( error);
    }
}
