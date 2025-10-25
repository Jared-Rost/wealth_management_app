/**
 * UserValidator.java
 *
 * COMP3350 SECTION A02
 *
 * @author Toran Pillay, 7842389
 * @date March 25, 2024
 *
 * PURPOSE:
 *  This file contains all of the methods necessary to validate the data in a
 * User.
 **/

package com.spenditure.logic;

import static com.spenditure.logic.UserValidatorParameters.*;


import com.spenditure.logic.exceptions.InvalidUserInformationException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator {

    //Other defining variables are in UserValidatorConfig.ini

    //Maximum number of characters in an email, according to Simple Mail Transfer Protocol RFC 3521 4.5.3.1.3
    public static final int MAX_EMAIL_LENGTH = 256;
    //Minimum possible length of an email; a@b.co
    public static final int MIN_EMAIL_LENGTH = 6;

    /*

        validateUsernameAndPassword

        Validates both the username and password conveniently in one call.
        Both are given as strings, and evaluated according to the constraints
        defined by the class constants above.

     */
    public static void validateUsernameAndPassword(String username, String password) throws InvalidUserInformationException {

        validateUsername(username);

        validatePassword(password);

    }

    /*

        validateUsername

        Takes in a String and determines if it's a valid username. If not, throws an appropriate exception.

     */
    public static void validateUsername(String username) throws InvalidUserInformationException {

        new UserValidatorParameters();

        if( username == null || username.trim().isEmpty()
                || username.length() > USERNAME_CHARACTER_LIMIT ) {

            throw new InvalidUserInformationException("Username cannot be blank or over "+USERNAME_CHARACTER_LIMIT
                +" characters long.\nUsername provided: "+username);

        }

    }

    /*

        validatePassword

        Takes in a String a determines if it's a valid password. If not, throws an appropriate exception.

     */
    public static void validatePassword(String password) throws InvalidUserInformationException {

        new UserValidatorParameters();

        if( password == null || password.trim().isEmpty() || password.length() < PASSWORD_MIN_LENGTH
                || password.length() > PASSWORD_CHARACTER_LIMIT ) {

            throw new InvalidUserInformationException("Password cannot be blank, over "+PASSWORD_CHARACTER_LIMIT
                    +" characters long, or less than "+PASSWORD_MIN_LENGTH+" characters long.\nPassword provided: "+password);

        }

        containsNumber(password);

    }

    /*

       containsNumber

       Takes in a String, and ensures that it has the appropriate amount of numerical characters to qualify
       as a password. If not, throws an appropriate exception.

    */
    private static void containsNumber(String password) {

        int numbersPresent = 0;

        //Search the string for instances of char 0-9
        for(int i=0; i<10; i++) {

            if(password.contains(String.valueOf(i)))
                numbersPresent++;

        }

        if(numbersPresent < MIN_PASSWORD_NUMERICAL)
            throw new InvalidUserInformationException("Password must contain at least "+MIN_PASSWORD_NUMERICAL
                    +" numerical characters.\nPassword provided: "+password);

    }

    /*

        validateEmail

        Takes in a String and determines if it is a valid email address.

     */
    public static void validateEmail(String email) throws InvalidUserInformationException {

        if( email == null || email.trim().isEmpty() || email.length() < MIN_EMAIL_LENGTH
                || email.length() > MAX_EMAIL_LENGTH ) {

            throw new InvalidUserInformationException("Email cannot be blank, over "+MAX_EMAIL_LENGTH
                    +" characters long, or less than "+MIN_EMAIL_LENGTH+"characters.\nEmail provided: "+email);

        }

        if(!isValidEmail(email)) {
            throw new InvalidUserInformationException("Provided email is not formatted correctly. Emails must" +
                    "contain exactly one \"@\" symbol, one \".\" symbol, and be in the general format of:" +
                    "\n\"johnny.appleseed@domain.com\".\nEmail provided: "+email);
        }

    }

    //uses a regular expression to enforce email format
    public static boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }


}
