/**
 * InvalidCategoryException.java
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

public class InvalidCategoryException extends RuntimeException {
    //Comment
    public InvalidCategoryException(String error){
        super("Fail to read Category data:\n" +  error);
    }
}
