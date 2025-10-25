/**
 * DateTimeAdjuster.java
 *
 * COMP3350 SECTION A02
 *
 * @author Toran Pillay, 7842389
 * @date March 25, 2024
 *
 * PURPOSE:
 *  This file contains all of the methods necessary to ensure that adjusted DateTimes
 * remain valid, guaranteeing that dates always make sense whenever you modify them.
 **/

package com.spenditure.logic;

import static com.spenditure.logic.DateTimeValidator.*;
import static java.lang.Math.abs;
import com.spenditure.logic.exceptions.*;
import com.spenditure.object.DateTime;

public class DateTimeAdjuster {

    /*

    correctDateTime

    Given a DateTime, ensures that it's valid. If not, it goes through the DateTime's
    elements one by one and corrects them until the DateTime is valid.

     */
    public static DateTime correctDateTime(DateTime toFix) {

        //Keeps track of the status of the DateTime
        boolean fixed = false;
        //Copy the original to operate on
        DateTime result = toFix.copy();

        while(!fixed) {

            try {
                //Use existing validator in DateTimeValidator to see if any work needs to be done.
                validateDateTime(result);

                fixed = true;

            } catch(InvalidDateTimeException ignore) {
                //Go through and fix each element of the date time, then try again until it's fixed
                fixSeconds(result);

                fixMinutes(result);

                fixHours(result);

                fixDays(result);

                fixMonths(result);

                checkYears(result);

            }

        }

        return result;

    }

    /*

        setEndOfDay

        Given a DateTime, sets that DateTime to the last second of that day (23h, 59m, 59s).

     */
    public static void setEndOfDay(DateTime dateTime) {

        int h = dateTime.getHour();
        int m = dateTime.getMinute();
        int s = dateTime.getSeconds();

        dateTime.adjust(0, 0, 0,
                -h + MAX_HOURS-1, -m + MAX_MINUTES-1, -s + MAX_SECONDS-1);

    }

    /*

        setBeginningOfDay

        Given a DateTime, sets that DateTime to the first second of that day (0h, 0m, 0s).

     */
    public static void setBeginningOfDay(DateTime dateTime) {

        int h = dateTime.getHour();
        int m = dateTime.getMinute();
        int s = dateTime.getSeconds();

        dateTime.adjust( 0, 0, 0,
                -h, -m, -s);

    }

    /*

    fixSeconds

    Given a DateTime, ensures its seconds value is between 0 and 59. If not, adjusts the minute value
    by the appropriate amount.

     */
   private static void fixSeconds(DateTime dateTime) {

        int seconds = dateTime.getSeconds();

        while(seconds < 0 || seconds >= MAX_SECONDS) {

            if (seconds < 0) {
                dateTime.absoluteAdjust(0, 0, 0, 0, -1, MAX_SECONDS);

            } else {
                dateTime.absoluteAdjust(0, 0, 0, 0, 1, -MAX_SECONDS);
            }

            seconds = dateTime.getSeconds();
        }
   }

   /*

   fixMinutes

   Given a DateTime, ensures its minute value is between 0 and 59.
   If not, adjusts the hour value by the appropriate amount.

    */
   private static void fixMinutes(DateTime dateTime) {

       int minutes = dateTime.getMinute();

       while(minutes < 0 || minutes >= MAX_MINUTES) {

           if (minutes < 0) {
               dateTime.absoluteAdjust(0, 0, 0, -1, MAX_MINUTES, 0);

           } else {
               dateTime.absoluteAdjust(0, 0, 0, 1, -MAX_MINUTES, 0);
           }
           minutes = dateTime.getMinute();

       }
   }

   /*

   fixHours

   Given a DateTime, ensures the hours value is between 0 and 23.
   If not, adjusts the days value by the appropriate amount.

    */
   private static void fixHours(DateTime dateTime) {

        int hours = dateTime.getHour();

        while(hours < 0 || hours >= MAX_HOURS) {

            if (hours < 0) {
                dateTime.absoluteAdjust(0, 0, -1, MAX_HOURS, 0, 0);

            } else {
                dateTime.absoluteAdjust(0, 0, 1, -MAX_HOURS, 0, 0);
            }

            hours = dateTime.getHour();
        }
   }

   /*

   fixDays

   Given a DateTime, ensures the day value is between 0 and 28, 29, 30, or 31, depending on the month.
   If not, adjusts the month value by the appropriate amount.

    */
   private static void fixDays(DateTime dateTime) {

        int days = dateTime.getDay();
        int month = dateTime.getMonth();
        int year = dateTime.getYear();
        int monthLimit = getMonthLimit(month, year);

        while(days <= 0 || days > monthLimit) {

            if (days <= 0) {
                month = month - 1;

                if(month <= 0)
                    monthLimit = getMonthLimit((abs(month)+12)%13, year);
                else
                    monthLimit = getMonthLimit(month, year);

                dateTime.absoluteAdjust(0, -1, monthLimit, 0, 0, 0);

            } else {
                month = month + 1;

                if(month >= 13)
                    monthLimit = getMonthLimit(month%12, year);
                else
                    monthLimit = getMonthLimit(month, year);

                dateTime.absoluteAdjust(0, 1, -monthLimit, 0, 0, 0);
            }

            days = dateTime.getDay();
        }
   }

   /*

   fixMonths

   Given a DateTime, ensures the month value is between 1 and 12.
   If not, adjusts the year value by the appropriate amount.

    */
   private static void fixMonths(DateTime dateTime) {

        int months = dateTime.getMonth();

        while(months <= 0 || months > MAX_MONTHS) {

            if(months <= 0) {
                dateTime.absoluteAdjust(-1, MAX_MONTHS, 0, 0, 0, 0);

            } else {
                dateTime.absoluteAdjust(1, -MAX_MONTHS, 0, 0, 0, 0);
            }

            months = dateTime.getMonth();
        }
   }

   /*

   checkYears

   Ensures years is within the bounds defined by DateTimeValidator. If not, throws an exception,
   as the adjustment is invalid.

    */
   private static void checkYears(DateTime dateTime) throws InvalidDateTimeException {

        int years = dateTime.getYear();

        if(years < MIN_YEAR) {
            throw new InvalidDateException("The requested adjustment goes beyond the minimum permitted year ("+MIN_YEAR+").");
        } else if(years > MAX_YEAR) {
            throw new InvalidDateException("The requested adjustment goes beyond the maximum permitted year ("+MAX_YEAR+").");
        }

   }

   /*

   getMonthLimit

   Finds the last day of a given month and year.

    */
    private static int getMonthLimit(int month, int year) {

        int result = 28;
        boolean hitLimit = false;

        DateTime test = new DateTime(year, month, result);

        while(!hitLimit && result <= 31) {

            try {
                //Use existing DateTimeValidator to see if the date is valid.
                validateDateTime(test);

                result ++;
                test = new DateTime(year, month, result);

            } catch(InvalidDateException ignore) {
                //The date is invalid, meaning the previous value was the last day of the month
                hitLimit = true;
            }
        }

        return result - 1;

    }

}