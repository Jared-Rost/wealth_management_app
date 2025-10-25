package com.spenditure.presentation.report;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.spenditure.R;
import com.spenditure.application.Services;
import com.spenditure.logic.CategoryHandler;
import com.spenditure.logic.GeneralReportHandler;
import com.spenditure.logic.IGeneralReportHandler;
import com.spenditure.logic.ITimeBaseReportHandler;
import com.spenditure.logic.TimeBaseReportHandler;
import com.spenditure.logic.UserHandler;
import com.spenditure.object.CategoryReport;
import com.spenditure.presentation.UIUtility;

import java.util.List;

/**
 * Slider for Report on each category
 * @author Bao Ngo
 * @version 04 Mar 2024
 */

public class SliderAdapterCatGeneral extends PagerAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ITimeBaseReportHandler reportManager;
    private IGeneralReportHandler generalReportHandler;
    private CategoryHandler categoryHandler;
    List<CategoryReport> categoryStatisticsList;


    public SliderAdapterCatGeneral(Context context, List<CategoryReport> categoryStatisticsList){
        this.context = context;
        this.reportManager = new TimeBaseReportHandler(Services.DEVELOPING_STATUS);
        this.generalReportHandler = new GeneralReportHandler(Services.DEVELOPING_STATUS);
        this.categoryHandler = new CategoryHandler(Services.DEVELOPING_STATUS);
        this.categoryStatisticsList = categoryStatisticsList;
    }


    @Override
    public int getCount() {
        return categoryHandler.getAllCategory(UserHandler.getUserID()).size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (LinearLayout)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.report_slide,container,false);
        LinearLayout linearLayout = view.findViewById(R.id.report_slider_layout);
        CategoryReport currCategoryReport = categoryStatisticsList.get(position);

        //Get data from report manager
        String countTransactionsString =  UIUtility.cleanTransactionNumberString(currCategoryReport.getNumTransactions());
        String totalTransactionsString= UIUtility.cleanTotalString(currCategoryReport.getTotalSpending());
        String averageString=  UIUtility.cleanAverageString(currCategoryReport.getAverageTransactions());
        String percentageString = UIUtility.cleanPercentageString(currCategoryReport.getPercentage());

        //query UI components
        TextView tittle = view.findViewById(R.id.slide_tittle);
        TextView countTransactions = view.findViewById(R.id.textview_catReport_transactionsCount);
        TextView totalTransactions = view.findViewById(R.id.textview_catReport_total);
        TextView average = view.findViewById(R.id.textview_catReport_average);
        TextView percentage = view.findViewById(R.id.textview_catReport_percentage);

        //Fill UI component with information
        tittle.setText(currCategoryReport.getCategory().getName());
        countTransactions.setText(countTransactionsString);
        totalTransactions.setText(totalTransactionsString);
        average.setText(averageString);
        percentage.setText(percentageString);

        linearLayout.setBackgroundResource(R.drawable.background_light_green);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }

}
