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
import com.spenditure.object.Report;
import com.spenditure.presentation.UIUtility;

import java.util.ArrayList;
import java.util.List;


/**
 * Slider for Report on each category
 * @author Bao Ngo
 * @version 04 Mar 2024
 */
public class SliderAdapterTimeBase extends PagerAdapter {
    Context context;
    LayoutInflater inflater;
   private List<Report> reportList;

    private List<String> timeLabels;

    public SliderAdapterTimeBase(Context context, List<Report> reportList, List<String> timeLabels){
        this.context = context;
        this.reportList = reportList;
        this.timeLabels = timeLabels;
    }


    @Override
    public int getCount() {
        return reportList.size();
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
        Report currReport = reportList.get(position);

        //Get data from report manager
        String title = timeLabels.get(position);
//
        //query UI components
        TextView tittle = view.findViewById(R.id.slide_tittle);
        TextView countTransactions = view.findViewById(R.id.textview_catReport_transactionsCount);
        TextView totalTransactions = view.findViewById(R.id.textview_catReport_total);
        TextView average = view.findViewById(R.id.textview_catReport_average);
        TextView percentage = view.findViewById(R.id.textview_catReport_percentage);

//        //Fill UI component with information
        tittle.setText(title );
        countTransactions.setText(UIUtility.cleanTransactionNumberString( currReport.getNumTrans()));
        totalTransactions.setText(UIUtility.cleanTotalString(currReport.getTotal()));
        average.setText(UIUtility.cleanAverageString(currReport.getAvgTransSize()));
        percentage.setText(UIUtility.cleanPercentageString(currReport.getPercent()));

        linearLayout.setBackgroundResource(R.drawable.background_light_green);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }


}
