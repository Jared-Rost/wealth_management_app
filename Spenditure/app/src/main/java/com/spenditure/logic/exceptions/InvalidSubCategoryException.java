/**
 * InvalidSubCategoryException.java
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

public class InvalidSubCategoryException extends RuntimeException {
    public InvalidSubCategoryException(String error){
        super("Fail to read Sub-Category data:\n" +  error);
    }
}
