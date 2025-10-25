package com.spenditure.logic;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

public class UserValidatorParameters {

    protected static int USERNAME_CHARACTER_LIMIT;

    protected static int PASSWORD_CHARACTER_LIMIT;

    protected static int PASSWORD_MIN_LENGTH;

    protected static int MIN_PASSWORD_NUMERICAL;

    private static final int FALLBACK_USERNAME_LIMIT = 32;

    private static final int FALLBACK_PASSWORD_MAX = 32;

    private static final int FALLBACK_PASSWORD_MIN = 3;

    private static final int FALLBACK_PASSWORD_NUMERICAL = 3;

    public UserValidatorParameters() {

        HashMap<String, String> map = new HashMap<>();
        File input = new File("src/main/java/com/spenditure/logic/UserValidatorConfig.ini");

        try {

            FileInputStream inputStream = new FileInputStream(input.getAbsolutePath());
            Scanner scanner = new Scanner(inputStream);

            String line = scanner.nextLine();

            while (scanner.hasNext() && line.contains("#")) {
                line = scanner.nextLine();
            }

            while (scanner.hasNext()) {

                map.put(scanner.next(), scanner.next());

            }

            inputStream.close();
            scanner.close();

        } catch (FileNotFoundException e) {

            System.out.print("The file \"UserValidatorConfig.ini\" was not found. User parameters were set to defaults.\n");

        } catch (IOException e) {

            throw new RuntimeException(e);

        }

        setValues(map);
        validateMinimums();

    }

    private void setValues(HashMap<String, String> map) {

        USERNAME_CHARACTER_LIMIT = setIndividualValue(FALLBACK_USERNAME_LIMIT, map.get("USERNAME_CHARACTER_LIMIT"));

        PASSWORD_CHARACTER_LIMIT = setIndividualValue(FALLBACK_PASSWORD_MAX, map.get("PASSWORD_CHARACTER_LIMIT"));

        PASSWORD_MIN_LENGTH = setIndividualValue(FALLBACK_PASSWORD_MIN, map.get("PASSWORD_MIN_LENGTH"));

        MIN_PASSWORD_NUMERICAL = setIndividualValue(FALLBACK_PASSWORD_NUMERICAL, map.get("MIN_PASSWORD_NUMERICAL"));

    }

    private int setIndividualValue(int fallback, String setTo) {

        int result;

        if(setTo != null && !setTo.isEmpty()) {

            try {

                 result = Integer.parseInt(setTo);

            } catch(NumberFormatException e) {

                result = fallback;

                System.out.print("An error occured trying to convert a value in \"UserValidatorConfig.ini\" to a number." +
                        " Check to ensure the file is formatted correctly. The default value has been set instead.\n");

            }

        } else {

            result = fallback;

        }

        return result;
    }

    private void validateMinimums() {

        assert(MIN_PASSWORD_NUMERICAL >= 0);

        assert(PASSWORD_MIN_LENGTH >= 0);

    }

}
