/**
 * UserHandler.java
 *
 * COMP3350 SECTION A02
 *
 * @author Toran Pillay, 7842389
 * @date March 25, 2024
 *
 * PURPOSE:
 *  This file interprets information from Users sent by the UI layer,
 * sends information to the Database layer, and performs all logic operations
 * pertaining to Users.
 **/

package com.spenditure.logic;

import static com.spenditure.logic.UserValidator.*;
import com.spenditure.application.Services;
import com.spenditure.database.UserPersistence;
import com.spenditure.logic.exceptions.InvalidStringFormat;
import com.spenditure.logic.exceptions.InvalidUserInformationException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserHandler implements IUserHandler {

    private UserPersistence accountPersistence;
    private static int userID = -1; //Store ID of user that are currently using the app

    //Constructor
    public UserHandler(boolean getStubDB){
        accountPersistence = Services.getUserPersistence(getStubDB);

    }

    /*

        login

        Given a username and password as Strings, checks to see if they are valid, and then
        sets the corresponding User ID as the current user.

     */
    public int login(String username, String password) throws InvalidUserInformationException, NoSuchAlgorithmException {

        validateUsernameAndPassword(username, password);

        if(userID < 0) {
            String hashedPassword = hashPassword(password);

            int userIDReturn = accountPersistence.login(username,hashedPassword);

            UserHandler.userID = userIDReturn;

            return userIDReturn;

        } else {
            //Someone is already logged in
            throw new InvalidUserInformationException("Please log out before logging in.");

        }

    } //Returns user id

    /*

        getUserName

        Provides a username for a given user ID.

     */
    public String getUserName(int userID) throws InvalidUserInformationException {

        if (userID < 0)
            throw new InvalidUserInformationException("No users are currently logged in; please log in first.");

        return accountPersistence.getUserName(userID);

    }

    /*

    getSecurityQuestionAnswer

    Provides the securityQuestionAnswer for a given user ID.

 */
    public String getSecurityQuestionAnswer(int userID) {
        if(userID < 0)
            throw new InvalidUserInformationException("No users are currently logged in; please log in first.");
        return accountPersistence.getSecurityQuestionAnswer(userID);
    }

    /*

    getSecurityQuestionID

    Provides the securityQuestion ID for a given user ID.

 */
    public int getSecurityQuestionID(int userID) {
        if(userID < 0)
            throw new InvalidUserInformationException("No users are currently logged in; please log in first.");
        return accountPersistence.getSecurityQuestionID(userID);
    }



    /*

        changePassword

        For a logged in user, enables user to change the account's password to something else.
        Old password is required. Both are taken as Strings

     */
    public boolean changePassword(int userID, String oldPassword, String newPassword) throws InvalidUserInformationException, NoSuchAlgorithmException {

        validatePassword(newPassword);

        if(userID < 0)
            throw new InvalidUserInformationException("No users are logged in; please log in before changing your password.");

        String oldHashedPassword = hashPassword(oldPassword);
        String newHashedPassword = hashPassword(newPassword);

        return accountPersistence.changePassword(userID,oldHashedPassword,newHashedPassword);

    }

    /*

        changeUsername

        For a logged in user, enables user to change the account's username to something else.
        The new username is taken as a string.

     */
    public boolean changeUsername(int providedUserID, String newUsername)
            throws InvalidUserInformationException, InvalidStringFormat {

        validateUsername(newUsername);

        if(providedUserID < 0)
            throw new InvalidUserInformationException("No users are logged in; please log in before changing your username.");

        return accountPersistence.changeUsername(providedUserID,newUsername);

    }


    /*

        register

        Creates a new user account with the given Strings for username and password. Returns the
        new user's User ID.

     */
    public int register(String username, String password, String email, String securityAnswer, int securityQID) throws InvalidStringFormat, NoSuchAlgorithmException {

        validateUsernameAndPassword(username, password);

        validateEmail(email);

        String hashedPassword = hashPassword(password);

        String hashedSecurityAnswer = hashPassword(securityAnswer);

        int newUserID = accountPersistence.register(username,hashedPassword,email, hashedSecurityAnswer, securityQID);

        UserHandler.userID = newUserID;

        return newUserID;

    }

    /*

        logout

        Sets active user ID to -1, signifying that no user is currently logged in.

     */
    public void logout() throws InvalidUserInformationException {

        if (userID >= 0) {
            userID = -1;

        } else {
            throw new InvalidUserInformationException("Cannot log out, as no users are currently logged in. Please log in first.");

        }
    }

    /*

        getUserID

        Returns the logged-in user's User ID.

     */
    public static int getUserID() throws InvalidUserInformationException {

        if(userID >= 0) {
            return userID;

        } else {
            throw new InvalidUserInformationException("No users are logged in. Please log in before requesting a User ID.");

        }
    }

    //For testing purposes
    public static void cleanup(boolean getStubDB){
        Services.restartAccountDB(getStubDB);
        UserHandler.userID = -1;
    }

    /*

        hashPassword
        Given the password, creates and returns a hash

     */
    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes());

        StringBuilder result = new StringBuilder();
        for (byte b : hash) {
            result.append(String.format("%02x", b));
        }

        return result.toString();
    }




}
