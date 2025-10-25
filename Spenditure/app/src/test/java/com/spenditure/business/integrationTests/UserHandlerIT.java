package com.spenditure.business.integrationTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.spenditure.logic.UserHandler;
import com.spenditure.logic.exceptions.InvalidUserInformationException;
import com.spenditure.utils.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class UserHandlerIT {

    private UserHandler accountManager;
    private File tempDB;

    @Before
    public void setup() throws IOException {
        this.tempDB = TestUtils.copyDB();
        this.accountManager = new UserHandler(false);
    }
    @After
    public void tearDown(){
        UserHandler.cleanup(false);
        this.accountManager = null;
        this.tempDB = null;
    }

    @Test
    public void testLogin() throws NoSuchAlgorithmException {
        int userID = accountManager.login("Me","123");
        assertEquals(1,userID);
        accountManager.logout();

        userID = accountManager.login("He","12345");
        assertEquals(3,userID);
    }
//
    @Test
    public void testGetUserID() throws NoSuchAlgorithmException {
        accountManager.login("Me","123");
        int userID = UserHandler.getUserID();
        assertEquals(1,userID);


    }

    @Test
    public void testGetUsername() throws NoSuchAlgorithmException {
        int userID = accountManager.login("Me","123");
        String username= accountManager.getUserName(userID);
        assertEquals("Me",username);
    }

    @Test
    public void testChangePassword() throws NoSuchAlgorithmException {
        int userID = accountManager.login("Me","123");
        boolean changed;

        try {
            changed = accountManager.changePassword(userID, "123", "ok");
        } catch(InvalidUserInformationException ignore) { changed = false; }

        assertFalse("Invalid password, shouldn't have worked", changed);

        changed = accountManager.changePassword(userID, "123", "Hello123");
        assertTrue(changed);

        accountManager.logout();

        userID = accountManager.login("Me","Hello123");
        assertEquals(1,userID);
    }

    @Test
    public void testRegister() throws NoSuchAlgorithmException {
        int userID = accountManager.register("new user","testpassword123", "test.email@domain.com", "buddy", 1);
        assertEquals(6,userID);
        accountManager.logout();
        userID = accountManager.login("new user","testpassword123");
        assertEquals(6,userID);
    }

//
    @Test
    public void testLogOut() throws NoSuchAlgorithmException {
        accountManager.login("Me","123");
        accountManager.logout();
        boolean isLogout = false;
        try{
            UserHandler.getUserID();
        }catch (InvalidUserInformationException e){
            isLogout= true;
        }

        assertTrue(isLogout);
    }

    @Test
    public void testChangeUsername() throws NoSuchAlgorithmException {
        int userID = accountManager.login("TestingUser1","12345");
        assertEquals(4,userID);
        boolean success = accountManager.changeUsername(userID,"newUsername");
        assertTrue(success);
        assertEquals("newUsername",accountManager.getUserName(userID));
    }

    @Test
    public void testSecurityQuestionAnswer() throws NoSuchAlgorithmException {
        int userID = accountManager.login("TestingUser1","12345");
        assertEquals(4,userID);

        String answer = accountManager.getSecurityQuestionAnswer(4);
        assertEquals("2bc5ec0ae141fa98f0894160bed5fee96dfe037305a418561feac9048ba4e6ed",answer);
    }

    @Test
    public void testSecurityQuestionID() throws NoSuchAlgorithmException {
        int userID = accountManager.login("TestingUser1","12345");
        assertEquals(4,userID);


        int securityQID = accountManager.getSecurityQuestionID(userID);
        assertEquals(1,securityQID);
    }
}
