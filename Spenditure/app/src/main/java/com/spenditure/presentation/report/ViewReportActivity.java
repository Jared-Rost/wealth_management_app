package com.spenditure.presentation.report;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;
import com.spenditure.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import com.spenditure.application.Services;
import com.spenditure.logic.CategoryHandler;
import com.spenditure.logic.GeneralReportHandler;
import com.spenditure.logic.ICategoryHandler;
import com.spenditure.logic.IGeneralReportHandler;
import com.spenditure.logic.ITimeBaseReportHandler;
import com.spenditure.logic.ITransactionHandler;
import com.spenditure.logic.TimeBaseReportHandler;
import com.spenditure.logic.TransactionHandler;
import com.spenditure.logic.UserHandler;
import com.spenditure.object.CategoryReport;
import com.spenditure.object.DateTime;
import com.spenditure.object.MainCategory;
import com.spenditure.object.Report;
import com.spenditure.presentation.BottomNavigationHandler;
import com.spenditure.presentation.UIChartUtility;
import com.spenditure.presentation.UIUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.time.LocalDate;


/**
 * Report activity (Also main activity)
 * @author Bao Ngo
 * @version 04 Mar 2024
 */
public class ViewReportActivity extends AppCompatActivity {

    private static String dbName="SC1";

    private ITimeBaseReportHandler reportManager;
    private IGeneralReportHandler generalReportHandler;

    private ITransactionHandler transactionHandler;
    public final String[] custom_option = {"Report by average","Report by total","Report by percentage"}; //Drop down menu option
    public static final String[] TIME_BASE_OPTION = {"Report by year breaking into month","Report by month breaking into weeks"};//Drop down menu option
    private ICategoryHandler categoryHandler;

    private DateTime fromDate;
    private DateTime toDate;
    private LocalDate currTime =  LocalDate.now();
    private int userID;
    private DateTime currDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        transactionHandler = new TransactionHandler(Services.DEVELOPING_STATUS);
        reportManager = new TimeBaseReportHandler(Services.DEVELOPING_STATUS);
        categoryHandler = new CategoryHandler(Services.DEVELOPING_STATUS);
        generalReportHandler = new GeneralReportHandler(Services.DEVELOPING_STATUS);
        this.userID = UserHandler.getUserID();
        this.currDate = TimeBaseReportHandler.getCurrentDate();

        handleGeneralReport();
        handleCustomCategoryReport();
        handleCategoriesReport();
        handleLastYearReport();
        handleTimebaseReport();
        handleCustomDateReport();
        navBarHandling();


    }

    private void handleLineChart(List<Report> reportList,List<String>timeLabels){
        LineChart lineChart = findViewById(R.id.lineChart_spending);

        List<Entry>entries = new ArrayList<>();
        for(int i = 0 ; i <reportList.size(); i++){
            entries.add(new Entry((float) i, (float) reportList.get(i).getTotal()));
        }
        UIChartUtility.configLineChar(lineChart,entries,timeLabels);


        lineChart.invalidate();
    }

    private void handleCategoriesReport(){
        List<CategoryReport> categoryStatisticsList = generalReportHandler.getAllCategoryReport(UserHandler.getUserID());

        handleStatsCategories(categoryStatisticsList);
        handleChartCategory(categoryStatisticsList);
    }

    private void handleStatsCategories(List<CategoryReport> categoryStatisticsList){
        ViewPager viewPagerCategory = findViewById(R.id.viewpager_report);

        if(categoryStatisticsList.size() >0 ){
            ConstraintLayout nonReport = findViewById(R.id.report_no_category_report);
            nonReport.setVisibility(View.INVISIBLE);
            SliderAdapterCatGeneral adapter = new SliderAdapterCatGeneral(this,categoryStatisticsList);
            viewPagerCategory.setAdapter(adapter);
        }else{
            ConstraintLayout nonReport = findViewById(R.id.report_no_category_report);
            nonReport.setVisibility(View.VISIBLE);
        }
    }

    private void handleChartCategory(List<CategoryReport> categoryStatisticsList){
        ArrayList<PieEntry> entries = new ArrayList<>();

        PieChart catChart = findViewById(R.id.piechart_category);

        for(CategoryReport categoryReport : categoryStatisticsList){
            if(categoryReport.getPercentage() > 0){
                entries.add(new PieEntry((float) categoryReport.getPercentage(),categoryReport.getCategory().getName()));
            }
        }

        UIChartUtility.configPieChar(catChart,entries);
        catChart.invalidate();
    }

    private void handleLastYearReport(){
        Report lastYearReport = reportManager.reportBackOneYear(userID, this.currDate);
        TextView numTransactions = findViewById(R.id.textview_lastYear_transactionsCount);
        TextView totalTransactions = findViewById(R.id.textview_lastYear_total);
        TextView average = findViewById(R.id.textview_lastYear_average);
        TextView percentage = findViewById(R.id.textview_lastYear_percentage);

        numTransactions.setText(UIUtility.cleanTransactionNumberString(lastYearReport.getNumTrans()));
        totalTransactions.setText(UIUtility.cleanTotalString(lastYearReport.getTotal()));
        average.setText(UIUtility.cleanAverageString(lastYearReport.getAvgTransSize()));
        percentage.setText(UIUtility.cleanPercentageString(lastYearReport.getPercent()));
    }

    private void handleCustomDateReport(){
        Button fromButton = findViewById(R.id.button_report_from);
        Button toButton = findViewById(R.id.button_report_to);
        Button getReport = findViewById(R.id.button_get_report);
        fromDate = currDate;
        toDate = currDate;
        String currTimeString = currDate.getMonth() + "/" + currDate.getDay() + "/" +  currDate.getYear() ;
        fromButton.setText(currTimeString);
        toButton.setText(currTimeString);
        fromButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseTimeDialog(fromButton,true);
            }
        });

        toButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseTimeDialog(toButton,false);
            }
        });

        getReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fromDate == null || toDate == null){
                    Toast.makeText(ViewReportActivity.this,"Please choose 2 dates",Toast.LENGTH_SHORT).show();
                }else {
                    Report timeCustomReport = reportManager.reportOnUserProvidedDates(userID, fromDate, toDate);

                    TextView transactionNum = findViewById(R.id.textview_customTime_totalTrans);
                    TextView  total = findViewById(R.id.textview_customTime_totalAmount);
                    TextView average = findViewById(R.id.textview_customTime_average);
                    TextView percentage = findViewById(R.id.textview_customTime_percentage);


                    transactionNum.setText(UIUtility.cleanTransactionNumberString(timeCustomReport.getNumTrans()));
                    total.setText(UIUtility.cleanTotalString(timeCustomReport.getTotal()));
                    average.setText(UIUtility.cleanAverageString(timeCustomReport.getAvgTransSize()));
                    percentage.setText(UIUtility.cleanPercentageString(timeCustomReport.getPercent()));

                }
            }
        });


    }

    private void handleTimebaseReport(){
        List<Report> reportList = reportManager.reportBackOnLastYearByMonth(userID,currDate);
        List<String> timeLabels = Arrays.asList(DateTime.MONTHS);

        ShimmerFrameLayout shimmerFrameLayout = findViewById(R.id.shimmer_timebase_report);
        ViewPager viewPagerCustom = findViewById(R.id.gridlayout_timebase_report);
        Spinner spinner = findViewById(R.id.spinner_timebase_report);

        SliderAdapterTimeBase adapterCustom= new SliderAdapterTimeBase(this,reportList,timeLabels);
        viewPagerCustom.setAdapter(adapterCustom);
        handleLineChart(reportList, timeLabels);


        ArrayAdapter<String>adapter = new ArrayAdapter<>(this, R.layout.custom_snipper_report, TIME_BASE_OPTION);
        adapter.setDropDownViewResource( android.R.layout.select_dialog_singlechoice);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewPagerCustom.setVisibility(View.INVISIBLE);
                shimmerFrameLayout.setVisibility(View.VISIBLE);
                shimmerFrameLayout.startShimmerAnimation();
                List<Report>selectReport;
                List<String>selectTime;

                if (position == 0){
                    selectReport = reportManager.reportBackOnLastYearByMonth(userID,currDate);
                    selectTime = Arrays.asList(DateTime.MONTHS);

                }else {
                    selectReport = reportManager.reportBackOneMonthByWeek(userID,currDate);
                    selectTime = Arrays.asList(DateTime.WEEKS);

                }
                SliderAdapterTimeBase adapterCustom= new SliderAdapterTimeBase(getApplicationContext(),selectReport,selectTime);
                viewPagerCustom.setAdapter(adapterCustom);
                handleLineChart(selectReport,selectTime);



                Handler handler = new Handler();
                handler.postDelayed(()->{
                    viewPagerCustom.setVisibility(View.VISIBLE);
                    shimmerFrameLayout.stopShimmerAnimation();
                    shimmerFrameLayout.setVisibility(View.INVISIBLE);
                },3000);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //We did nothing in purpose, but we still have to have this function because of it need to be overrided.
            }
        });
    }

    // Handle the bottom navigation bar
    private void navBarHandling(){
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.navigation_home);
        BottomNavigationHandler navigationHandler = new BottomNavigationHandler();

        navView.setOnItemSelectedListener((item -> {
            if (item.getItemId() == R.id.navigation_home){
                return false;
            }
            Class<? extends AppCompatActivity> newActivity = navigationHandler.select(item.getItemId());
            if(newActivity != null){
                startActivity(new Intent(getApplicationContext(), newActivity));
                return true;
            }
            return false;
        }));
    }

    private void handleGeneralReport(){
        String spendString = "You've spent " + UIUtility.cleanTotalString(generalReportHandler.totalSpending(userID));
        String makeTransactions = "You've made " + UIUtility.cleanTransactionNumberString(generalReportHandler.numTransactions(userID));
        TextView totalSpending = findViewById(R.id.textview_general_total_spending);
        TextView numTrans = findViewById(R.id.textview_summary_num_trans);

        totalSpending.setText(spendString);
        numTrans.setText(makeTransactions);

    }

    private void handleCustomCategoryReport(){
        Spinner spinner = findViewById(R.id.spinner_report);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(ViewReportActivity.this,item,Toast.LENGTH_SHORT).show();

                String spendMostString = "Mostly spending on ";
                String spendLeastString = "Least spending on ";
                List<MainCategory>categories;

                switch (position){
                    case 2:
                        categories = generalReportHandler.sortByPercent(userID,true);
                        break;
                    case 1:
                        categories = generalReportHandler.sortByTotal(userID,true);
                        break;
                    default:
                        categories = generalReportHandler.sortByAverage(userID,true);
                        break;
                }

                if(categories.size() == 0){
                    findViewById(R.id.imageView_check_custom).setVisibility(View.INVISIBLE);
                    findViewById(R.id.textview_custom_report_most).setVisibility(View.INVISIBLE);
                    ((TextView)findViewById(R.id.textview_custom_report_least)).setText("Group transactions to categories to see the report.");
                }else{
                    findViewById(R.id.textview_custom_report_most).setVisibility(View.VISIBLE);
                    spendMostString += categories.get(0).getName();
                    spendLeastString += categories.get(categories.size()-1).getName();

                    ((TextView)findViewById(R.id.textview_custom_report_most)).setText(spendMostString);
                    ((TextView)findViewById(R.id.textview_custom_report_least)).setText(spendLeastString);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //We did nothing in purpose, but we still have to have this function because of it need to be overrided.
            }
        });

        ArrayAdapter<String>adapter = new ArrayAdapter<>(this, R.layout.custom_snipper_report,custom_option);
        adapter.setDropDownViewResource( android.R.layout.select_dialog_singlechoice);
        spinner.setAdapter(adapter);
    }



    private void chooseTimeDialog(Button button,boolean from){

        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String displayDay = String.valueOf(month+ 1 ) + "/"  + String.valueOf(dayOfMonth) + "/" + String.valueOf(year);
                DateTime newDate = new DateTime(year, month + 1,  dayOfMonth);

                if(from){
                    ViewReportActivity.this.fromDate = newDate;
                }else {
                    ViewReportActivity.this.toDate = newDate;
                }

                button.setText(displayDay);

            }
        }, currTime.getYear(), currTime.getMonthValue() -1, currTime.getDayOfMonth());
        dialog.show();

    }



    /**
     * DB set up in Main activity
     */

    public static void setDBPathName(final String name) {
        try {
            Class.forName("org.hsqldb.jdbcDriver").newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        dbName = name;
    }

    public static String getDBPathName() {
        return dbName;
    }
}