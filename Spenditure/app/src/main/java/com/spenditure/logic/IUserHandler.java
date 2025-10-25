package com.spenditure.logic;

import java.security.NoSuchAlgorithmException;

public interface IUserHandler {

    /*
        login
        Given a username and password as Strings, checks to see if they are valid, and then
        sets the corresponding User ID as the current user.
     */
    public int login(String username, String password) throws NoSuchAlgorithmException;

    /*
        getUserName
        Provides a username for a given user ID.
     */
    public String getUserName(int userID);

    /*
        changePassword
        For a logged in user, enables user to change the account's password to something else.
        Old password is required. Both are taken as Strings

     */
    public boolean changePassword(int userID, String oldPassword, String newPassword) throws NoSuchAlgorithmException;

    /*
        changeUsername
        For a logged in user, enables user to change the account's username to something else.
        The new username is taken as a string.
     */
    public boolean changeUsername(int providedUserID, String newUsername);

    /*
       register
       Creates a new user account with the given Strings for username and password. Returns the
       new user's User ID.
    */
    public int register(String username, String password, String email, String securityAnswer, int securityQuestionID)
            throws NoSuchAlgorithmException;

    /*
        logout
        Sets active user ID to -1, signifying that no user is currently logged in.
     */
    public void logout();

}
