package com.spenditure.database;

import java.security.NoSuchAlgorithmException;

public interface UserPersistence {
    int login(String username, String password) throws NoSuchAlgorithmException; //Return user id
    String getUserName(int userID);
    String getSecurityQuestionAnswer(int userID);
    int getSecurityQuestionID(int userID);
    boolean changePassword(int userID, String oldPassword, String newPassword);
    boolean changeUsername(int userID, String newUsername);
    int register(String username, String password,String email, String securityQAnswer, int securityQID);
}
