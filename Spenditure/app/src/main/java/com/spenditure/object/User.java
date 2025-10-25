package com.spenditure.object;

/**
 * User class
 * @author Bao Ngo
 * @version 06 Feb 2024
 */
public class User {
    //Attributes
    private String fullName;
    private String username;
    private String email;
    private String securityQuestionAnswer;
    private int securityQuestionID;


    //Constructors
    public User(String username,String fullName, String email){
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.securityQuestionAnswer = "";
        this.securityQuestionID = -1;
    }

    public User(String fullName, String username, String email, String securityQuestionAnswer, int securityQuestionID) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.securityQuestionAnswer = securityQuestionAnswer;
        this.securityQuestionID = securityQuestionID;
    }

    //Business methods
    public String getName() {
        return this.fullName;
    }

    public String getEmail() {
        return this.email;
    }

    public String getUsername() {
        return this.username;
    }

    public String getSecurityQuestionAnswer() {
        return securityQuestionAnswer;
    }

    public int getSecurityQuestionID() {
        return securityQuestionID;
    }

    public void updateName(String newFullName) {
        this.fullName = newFullName;
    }


    public void updateUserName(String newUserName) {
        this.username = newUserName;
    }


    public void updateEmail(String newEmail) {
        this.email = newEmail;
    }

    public void setSecurityQuestionAnswer(String securityQuestionAnswer) {
        this.securityQuestionAnswer = securityQuestionAnswer;
    }

    public void setSecurityQuestionID(int securityQuestionID) {
        this.securityQuestionID = securityQuestionID;
    }
}
