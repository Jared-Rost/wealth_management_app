/**
 * DateTime.java
 *
 * COMP3350 SECTION A02
 *
 * @author Jared Rost,
 * @date Feb 7, 2024
 *
 * PURPOSE:
 *  Store all info related to datetime object
 *
 **/

package com.spenditure.object;

import static com.spenditure.logic.DateTimeAdjuster.correctDateTime;

import android.annotation.SuppressLint;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTime {

    private int year;

    private int month;

    private int day;

    private int hour;

    private int minute;

    private int seconds;

    //Relates month names to month value; names correlate to month-1
    public static final String[] MONTHS = {
            "Jan",
            "Feb",
            "Mar",
            "Apr",
            "May",
            "Jun",
            "Jul",
            "Aug",
            "Sep",
            "Oct",
            "Nov",
            "Dec"
    };

    public static final String [] WEEKS = {
            "1st Week",
            "2nd Week",
            "3rd Week",
            "4th Week"
    };

    public DateTime(int year, int month, int day, int hour, int minute, int seconds)
    {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.seconds = seconds;
    }

    public DateTime(int year) {

        this.year = year;
        this.month = 0;
        this.day = 0;
        hour = 0;
        minute = 0;
    }

    public DateTime(int year, int month) {

        this.year = year;
        this.month = month;
        this.day = 0;
        hour = 0;
        minute = 0;
    }

    public DateTime(int year, int month, int day) {

        this.year = year;
        this.month = month;
        this.day = day;
        hour = 0;
        minute = 0;
        seconds = 0;
    }

    public DateTime(String dateString) {

        String[] parts = dateString.split("-");

        if (parts.length == 3) {
            try {
                // Parse each part to integers and assign them to the attributes
                this.year = Integer.parseInt(parts[0]);
                this.month = Integer.parseInt(parts[1]);
                this.day = Integer.parseInt(parts[2]);
            } catch (NumberFormatException e) {
                // Handle if the parsing fails (e.g., invalid format)
                System.err.println("Invalid date format: " + dateString);
            }
        } else {
            // Handle if the dateString doesn't have three parts
            System.err.println("Invalid date format: " + dateString);
        }
    }

    public String toString() {

        String adjustMinutesPrefix = "";
        String adjustMinutesSuffix = "";

        //Correct minute to write "??:00" instead of "??:0"
        if(minute == 0) adjustMinutesSuffix = "0";
        //Correct minute to write ??:0X" instead of "??:X"
        if(minute < 10) adjustMinutesPrefix = "0";

        return MONTHS[month - 1] + " " + day + " " + year + ", "
                + hour + ":" + adjustMinutesPrefix + minute + adjustMinutesSuffix;

    }


    public String getYearMonthDay() {
        @SuppressLint("DefaultLocale") String result = String.format("%d-%d-%d", this.year, this.month, this.day);
        return result;
    }

    /*
        compare()

        Compares this DateTime to another DateTime. Return value will be:
        - POSITIVE if this DateTime is more recent than the other DateTime;
        - NEGATIVE if this DateTime is older than the other DateTime;
        - ZERO if the DateTimes are the same.
     */
    public int compare(DateTime other) {

        if( year - other.getYear() != 0 ) {
            return year - other.getYear();

        } else if( month - other.getMonth() != 0 ) {
            return month - other.getMonth();

        } else if( day - other.getDay() != 0 ) {
            return day - other.getDay();

        } else if( hour - other.getHour() != 0 ) {
            return hour - other.getHour();

        } else if( minute - other.getMinute() != 0 ) {
            return minute - other.getMinute();

        } else if(seconds - other.getSeconds() != 0) {
            return seconds - other.getSeconds();
        } else
            return 0;

    }

    public void adjust( int changeYear, int changeMonth, int changeDay, int changeHour, int changeMinute, int changeSecond ) {

        DateTime temp = new DateTime(
        year + changeYear,
        month + changeMonth,
        day + changeDay,
        hour + changeHour,
        minute + changeMinute,
        seconds + changeSecond );

        DateTime adjusted = correctDateTime(temp);

        year = adjusted.getYear();

        month = adjusted.getMonth();

        day = adjusted.getDay();

        hour = adjusted.getHour();

        minute = adjusted.getMinute();

        seconds = adjusted.getSeconds();

    }

    public void absoluteAdjust(int changeYear, int changeMonth, int changeDay, int changeHour, int changeMinute, int changeSecond ) {

        year += changeYear;

        month += changeMonth;

        day += changeDay;

        hour += changeHour;

        minute += changeMinute;

        seconds += changeSecond;

    }

    public DateTime copy() {

        return new DateTime(year, month, day, hour, minute,seconds);

    }

    public int getYear()
    {
        return year;
    }

    public int getMonth()
    {
        return month;
    }

    public int getDay()
    {
        return day;
    }

    public int getHour()
    {
        return hour;
    }

    public int getMinute()
    {
        return minute;
    }

    public int getSeconds() {return seconds; }
}
