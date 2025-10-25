package com.spenditure.presentation.category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.spenditure.R;
import com.spenditure.object.MainCategory;
import com.spenditure.object.SubCategory;
import com.spenditure.object.Transaction;

import java.util.List;

public class CustomCategorySpinnerAdapter extends BaseAdapter {

    // Instance Variables
    private Context context;
    private List<MainCategory> categories;
    private LayoutInflater inflator;

    // Constructor
    public CustomCategorySpinnerAdapter(List<MainCategory> categories, Context context){
        this.context = context;
        this.categories = categories;
        inflator = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public MainCategory getItem(int i) {
        return categories.get(i);
    }

    @Override
    public long getItemId(int i) {
        return categories.get(i).getCategoryID();
    }

    public int getPosition(MainCategory category) {
        return categories.indexOf(category);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup){
        view =inflator.inflate(R.layout.activity_category_spinner, null);

        TextView categoryName = (TextView) view.findViewById(R.id.textview_category_name);
        categoryName.setText(categories.get(i).getName());

        return view;
    }
}
