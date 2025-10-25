package com.spenditure.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.junit.Test;
import static org.junit.Assert.*;
public class TestHashing {

    @Test
    public void testHash() throws NoSuchAlgorithmException {

        String input = "Guderian";

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes());

        StringBuilder result = new StringBuilder();
        for (byte b : hash) {
            result.append(String.format("%02x", b));
        }

        String finalForm = result.toString();


        System.out.println(finalForm);

    }

}
