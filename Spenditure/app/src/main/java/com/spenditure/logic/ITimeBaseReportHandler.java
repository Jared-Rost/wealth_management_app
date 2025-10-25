package com.spenditure.logic;

import com.spenditure.object.DateTime;
import com.spenditure.object.Report;

import java.util.ArrayList;

public interface ITimeBaseReportHandler {

    public Report reportBackOneYear(int userID, DateTime yearEnd);

    public ArrayList<Report> reportBackOnLastYearByMonth(int userID, DateTime today);

    public ArrayList<Report> reportBackOneMonthByWeek(int userID, DateTime start);

    public Report reportOnUserProvidedDates(int userID, DateTime start, DateTime end);

}
