package com.spenditure.business.unitTests;

import static org.junit.Assert.assertEquals;

import com.spenditure.object.DateTime;

import org.junit.Test;

public class DateTimeAdjusterTest {

    @Test
    public void testFixDateTime() {

        DateTime dateTime = new DateTime(2024, 01, 01, 00, 00, 00);

        //Go back a week, should go back to Christmas Day
        dateTime.adjust(0, 0, -7, 0, 0, 0);
        assertEquals(2023, dateTime.getYear());
        assertEquals(12, dateTime.getMonth());
        assertEquals(25, dateTime.getDay());

        //Restart at first day of 2001, go back 1 day to New Year's Eve 2000
        dateTime = new DateTime(2001, 01, 01);
        dateTime.adjust(0, 0, -1, 0, 0, 0);
        assertEquals(2000, dateTime.getYear());
        assertEquals(12, dateTime.getMonth());
        assertEquals(31, dateTime.getDay());

        //Restart at first second of 2010, go back 1 second to last second of 2009
        dateTime = new DateTime(2010, 01, 01, 00, 00, 00);
        dateTime.adjust(0, 0, 0, 0, 0, -1);
        assertEquals(2009, dateTime.getYear());
        assertEquals(12, dateTime.getMonth());
        assertEquals(31, dateTime.getDay());
        assertEquals(23, dateTime.getHour());
        assertEquals(59, dateTime.getMinute());
        assertEquals(59, dateTime.getSeconds());

        //Restart at last second of 2023. Add 1 second to go to first second of 2024
        dateTime = new DateTime(2023, 12, 31, 23, 59, 59);
        dateTime.adjust(0, 0, 0, 0, 0, 1);
        assertEquals(2024, dateTime.getYear());
        assertEquals(01, dateTime.getMonth());
        assertEquals(01, dateTime.getDay());
        assertEquals(00, dateTime.getHour());
        assertEquals(00, dateTime.getMinute());
        assertEquals(00, dateTime.getSeconds());

        //Start at first day of March. Go back 1 day to last day of Feb (leap year)
        dateTime = new DateTime(2024, 03, 01);
        dateTime.adjust(0, 0, -1, 0, 0, 0);
        assertEquals(2024, dateTime.getYear());
        assertEquals(02, dateTime.getMonth());
        assertEquals(29, dateTime.getDay());

        //Do it again, but this time in 2023 (not a leap year)
        dateTime = new DateTime(2023, 03, 01);
        dateTime.adjust(0, 0, -1, 0, 0, 0);
        assertEquals(2023, dateTime.getYear());
        assertEquals(02, dateTime.getMonth());
        assertEquals(28, dateTime.getDay());

        //Go to first day of 2001. Go forward 100 days to April
        dateTime = new DateTime(2001, 01, 01);
        dateTime.adjust(0, 0, 100, 0, 0, 0);
        assertEquals(2001, dateTime.getYear());
        assertEquals(04, dateTime.getMonth());

        //Go backwards 100 days to January
        dateTime.adjust(0, 0, -100, 0, 0, 0);
        assertEquals(2001, dateTime.getYear());
        assertEquals(01, dateTime.getMonth());

    }

}
