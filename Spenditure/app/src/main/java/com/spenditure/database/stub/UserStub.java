package com.spenditure.database.stub;

import com.spenditure.database.UserPersistence;
import com.spenditure.logic.exceptions.InvalidUserInformationException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


public class UserStub implements UserPersistence {
    private List<UserRow> userTable;
    private int autoIncrementID = 1;

    public UserStub(){
        this.userTable = new ArrayList<>();

        userTable.add(new UserRow("Me","a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3",autoIncrementID++,"aaa@gmail.com", "47798d12ae31ce5a6d9c9dddec7bc9f2a27fffa424b7f2a154fa0e26690972de", 1));
        userTable.add(new UserRow("You","03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4",autoIncrementID++,"bbb@gmail.com", "6d0524cc2710b71be620de123e3f1c143791f31e2f5873e34eac6f180ae9a266", 2));
        userTable.add(new UserRow("He","5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5",autoIncrementID++,"ccc@gmail.com", "ff4b467b7a593047c46682ecdbf6da36b3f3bb4b50d35f08f17f751ef5f15531", 3));

    }



    @Override
    public int login(String username, String password) throws InvalidUserInformationException, NoSuchAlgorithmException {
        for (UserRow user : userTable) {
            if(user.getUsername().equals( username)){
                if(password.equals(user.getPassword())){
                    return user.getUserID();
                }else{
                    throw new InvalidUserInformationException("Wrong username or password");
                }
            }

        }
        throw new InvalidUserInformationException("Wrong password or username");
    }

    @Override
    public String getUserName(int userID) throws InvalidUserInformationException {
        for (UserRow user : userTable) {
            if(user.getUserID() == userID){
                return user.getUsername();
            }
        }
        throw new InvalidUserInformationException("User ID not exist.");
    }

    @Override
    public String getSecurityQuestionAnswer(int userID) {
        for (UserRow user : userTable) {
            if(user.getUserID() == userID){
                return user.getSecurityQuestionAnswer();
            }
        }
        throw new InvalidUserInformationException("User ID not exist.");
    }

    @Override
    public int getSecurityQuestionID(int userID) {
        for (UserRow user : userTable) {
            if(user.getUserID() == userID){
                return user.getSecurityQID();
            }
        }
        throw new InvalidUserInformationException("User ID not exist.");
    }


    @Override
    public boolean changePassword(int userID, String oldPassword, String newPassword) throws InvalidUserInformationException{
        for (UserRow user : userTable) {
            if(user.getUserID() == userID){
                if(user.getPassword().equals(oldPassword)){
                    user.updatePassword(newPassword);
                    return true;
                }else {
                    throw new InvalidUserInformationException("Wrong password");
                }

            }
        }
        throw new InvalidUserInformationException("User ID not exist.");
    }

    @Override
    public boolean changeUsername(int userID, String newUsername) throws InvalidUserInformationException {
        for (UserRow user : userTable) {
            if(user.getUserID() == userID){
                user.updateUserName(newUsername);
                return true;
            }
        }
        throw new InvalidUserInformationException("User ID not exist.");
    }

    @Override
    public int register(String username, String password, String email, String securityQAnswer, int securityQID) throws InvalidUserInformationException {
        for (UserRow user : userTable) {
            if(user.getUsername().equals(username)){
                throw new InvalidUserInformationException("Username already exist");
            }
        }
        int userID = autoIncrementID++;
        userTable.add(new UserRow(username,password,userID, email, securityQAnswer, securityQID));
        return userID;
    }



    private class UserRow{
        private String username;
        private String password;
        private int userID;
        private String email;
        private String securityQuestionAnswer;
        private int securityQID;
        public UserRow(String username,String password,int userID, String email, String securityQuestionAnswer, int securityQID){
            this.username = username;
            this.password = password;
            this.userID = userID;
            this.email = email;
            this.securityQuestionAnswer = securityQuestionAnswer;
            this.securityQID = securityQID;
        }

        private void updateUserName(String newUsername){
            this.username= newUsername;
        }

        private void updatePassword (String newPassword){
            this.password = newPassword;
        }

        private String getPassword(){
            return this.password;
        }

        private String getUsername (){
            return this.username;
        }

        public String getEmail() {
            return email;
        }

        public int getUserID(){
            return this.userID;
        }

        public String getSecurityQuestionAnswer() {
            return securityQuestionAnswer;
        }

        public int getSecurityQID() {
            return securityQID;
        }
    }

}