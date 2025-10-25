/**
 * DateNewestFirstComparator.java
 *
 * COMP3350 SECTION A02
 *
 * @author Jared Rost,
 * @date Feb 7, 2024
 *
 * PURPOSE:
 *  Compare two DateTime objects and decide which is smaller with newer dates being smaller
 *
 **/


package com.spenditure.logic.Comparison;

import com.spenditure.object.Transaction;

import java.util.Comparator;

// smallest first
public class DateNewestFirstComparator implements Comparator<Transaction> {
    @Override
    public int compare(Transaction a, Transaction b) {
        // compare years
        if(a.getDateTime().getYear() - b.getDateTime().getYear() != 0)
        {
            return a.getDateTime().getYear() - b.getDateTime().getYear();
        }
        // compare months
        else if(a.getDateTime().getMonth() - b.getDateTime().getMonth() != 0)
        {
            return a.getDateTime().getMonth() - b.getDateTime().getMonth();
        }
        // compare days
        else if(a.getDateTime().getDay() - b.getDateTime().getDay() != 0)
        {
            return a.getDateTime().getDay() - b.getDateTime().getDay();
        }
        // compare hours
        else if(a.getDateTime().getHour() - b.getDateTime().getHour() != 0)
        {
            return a.getDateTime().getHour() - b.getDateTime().getHour();
        }
        else if(a.getDateTime().getMinute() - b.getDateTime().getMinute() != 0)
        {
            return a.getDateTime().getMinute() - b.getDateTime().getMinute();
        }
        else
        {
            return 0;
        }
    }

}
