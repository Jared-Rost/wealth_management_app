package com.spenditure.presentation;

public abstract class UIUtility {
    private static int LIMIT_NUM_TRANSACTION = 1000;
    private static String CURRENCY_UNIT = "CAD";
    public static String cleanTransactionNumberString(int number){
        String uiOutput = "";
        if(number > LIMIT_NUM_TRANSACTION){
            uiOutput = "> " + LIMIT_NUM_TRANSACTION + " transactions";
        }else{
            uiOutput = number + " transactions";
        }
        return uiOutput;
    }

    public static String cleanTotalString(double total) {
        return "$" + roundUp(total) + " " + CURRENCY_UNIT;
    }

    public static String cleanAverageString(double average){
        return "$" + roundUp(average) +  " " + CURRENCY_UNIT;
    }

    public static String cleanPercentageString(double percentage){
        return roundUp(percentage) +"%";
    }


    private static double roundUp (double input){
        return Math.ceil(input * 100) / 100;
    }
}
