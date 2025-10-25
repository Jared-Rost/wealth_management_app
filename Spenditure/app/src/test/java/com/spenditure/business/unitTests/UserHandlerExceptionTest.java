package com.spenditure.business.unitTests;


import com.spenditure.logic.UserHandler;
import com.spenditure.logic.UserValidator;
import com.spenditure.logic.exceptions.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import static org.junit.Assert.*;

import java.security.NoSuchAlgorithmException;

public class UserHandlerExceptionTest {

    private UserHandler userHandler;
    private boolean caught;

    @Before
    public void setup(){
        userHandler = new UserHandler(true);
    }

    @After
    public void teardown(){
        UserHandler.cleanup(true);
        this.userHandler = null;
    }

    @Test
    public void testLogin() throws NoSuchAlgorithmException {
        caught = false;
        try{
            userHandler.login(null,"123");
        }catch (InvalidUserInformationException e){
            caught = true;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        assertTrue(caught);

        caught = false;
        try{
            userHandler.login("null",null);
        }catch (InvalidUserInformationException | NoSuchAlgorithmException e){
            caught = true;
        }
        assertTrue(caught);

        caught = false;
        try{
            userHandler.login("","123");
        }catch (InvalidUserInformationException | NoSuchAlgorithmException e){
            caught = true;
        }
        assertTrue(caught);

        caught = false;
        try{
            userHandler.login("Me","");
        }catch (InvalidUserInformationException | NoSuchAlgorithmException e){
            caught = true;
        }
        assertTrue(caught);

        caught = false;
        try{
            userHandler.login("Hehe","123");
        }catch (InvalidUserInformationException | NoSuchAlgorithmException e){
            caught = true;
        }
        assertTrue(caught);

        caught = false;
        try{
            userHandler.login("Me","12233");
        }catch (InvalidUserInformationException | NoSuchAlgorithmException e){
            caught = true;
        }
        assertTrue(caught);

        userHandler.login("Me","123");
        caught = false;
        try{
            userHandler.login("Me","123");
        }catch (InvalidUserInformationException e){
            caught = true;
        }
        assertTrue(caught);
    }

    @Test
    public void testGetUserID(){
        caught = false;
        try {
            UserHandler.getUserID();
        }catch (InvalidUserInformationException e){
            caught = true;
        }
        assertTrue(caught);

    }

    @Test
    public void testGetUsername(){
        caught = false;
        try {
            userHandler.getUserName(-1);
        }catch (InvalidUserInformationException e){
            caught = true;
        }
        assertTrue(caught);
    }

    @Test
    public void testChangePassword() throws NoSuchAlgorithmException {
        int userID = userHandler.login("Me","123");


        caught = false;
        try{
            userHandler.changePassword(userID,null,"hihi");
        }catch (InvalidUserInformationException e){
            caught = true;
        }
        assertTrue(caught);

        caught = false;
        try{
            userHandler.changePassword(userID,"null",null);
        }catch (InvalidUserInformationException e){
            caught = true;
        }
        assertTrue(caught);

        caught = false;
        try{
            userHandler.changePassword(userID,"","null");
        }catch (InvalidUserInformationException e){
            caught = true;
        }
        assertTrue(caught);

        caught = false;
        try{
            userHandler.changePassword(userID,"null","");
        }catch (InvalidUserInformationException e){
            caught = true;
        }
        assertTrue(caught);

        caught = false;
        try{
            userHandler.changePassword(-1,"null","asd");
        }catch (InvalidUserInformationException e){
            caught = true;
        }
        assertTrue(caught);
    }

    @Test
    public void testRegister(){
        caught = false;
        try{
           userHandler.register(null,"null", "", "", -1);
        }catch (InvalidUserInformationException e){
            caught = true;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        assertTrue(caught);

        caught = false;
        try{
            userHandler.register("null",null, null, null, 0);
        }catch (InvalidUserInformationException e){
            caught = true;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        assertTrue(caught);

        caught = false;
        try{
            userHandler.register("null","", "null", "null", 0);
        }catch (InvalidUserInformationException | NoSuchAlgorithmException e){
            caught = true;
        }
        assertTrue(caught);

        caught = false;
        try{
            userHandler.register("","aaa", "", "anser", 1);
        }catch (InvalidUserInformationException | NoSuchAlgorithmException e){
            caught = true;
        }
        assertTrue(caught);

        caught = false;
        try{
            userHandler.register("Aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa","aaa", "aaaaaaaaaa", "aaaaaaa", 1);
        }catch (InvalidUserInformationException | NoSuchAlgorithmException e){
            caught = true;
        }
        assertTrue(caught);
    }

    @Test
    public void testLogOut(){
        caught = false;
        try{
            userHandler.logout();
        }catch (InvalidUserInformationException e){
            caught = true;
        }
        assertTrue(caught);
    }

    @Test
    public void testChangeUsername() throws NoSuchAlgorithmException {
        int userID = userHandler.login("Me","123");
        caught = false;
        try{
            userHandler.changeUsername(-1,"me");
        }catch (InvalidUserInformationException e){
            caught = true;
        }
        assertTrue(caught);

        caught = false;
        try{
            userHandler.changeUsername(userID,"");
        }catch (InvalidUserInformationException e){
            caught = true;
        }
        assertTrue(caught);

        caught = false;
        try{
            userHandler.changeUsername(userID,null);
        }catch (InvalidUserInformationException e){
            caught = true;
        }
        assertTrue(caught);

        caught = false;
        try{
            userHandler.changeUsername(userID,"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        }catch (InvalidUserInformationException e){
            caught = true;
        }
        assertTrue(caught);
    }

    @Test
    public void testValidateEmail() {

        boolean caught = false;

        try {
            UserValidator.validateEmail("john.doe@domain.com");
        } catch(InvalidUserInformationException e) {
            caught = true;
        }

        assertFalse(caught);

        try {
            UserValidator.validateEmail("john@doe@domain@com");
        } catch(InvalidUserInformationException e) {
            caught = true;
        }

        assertTrue(caught);

        try {
            UserValidator.validateEmail("john.doe@hahahagotcha");
        } catch(InvalidUserInformationException e) {
            caught = true;
        }

        assertTrue(caught);

        try {
            UserValidator.validateEmail("john.doe@sneaky.trickster.co.uk");
        } catch(InvalidUserInformationException e) {
            caught = true;
        }

        assertTrue(caught);

        try {
            UserValidator.validateEmail("AAAASSAEFQJEPQIJAKLSJDCALNV1-1'4F;MFQA;SDXCÆÍSÍÍÎÅÍ˜Ω≈Œ∏´∑");
        } catch(InvalidUserInformationException e) {
            caught = true;
        }

        assertTrue(caught);
    }

}
