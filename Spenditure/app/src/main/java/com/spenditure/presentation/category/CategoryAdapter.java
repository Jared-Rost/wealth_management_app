package com.spenditure.presentation.category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.spenditure.R;
import com.spenditure.logic.CategoryHandler;
import com.spenditure.object.MainCategory;


import java.util.List;


/**
 * Adapter for Category pages
 * @author Bao Ngo
 * @version 04 Mar 2024
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private List<MainCategory> categories;
    private ViewCategoryActivity activity;


    public CategoryAdapter(ViewCategoryActivity activity, List<MainCategory> categories){
        this.categories = categories;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.category_card,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MainCategory currCat = categories.get(position);
        holder.categoryName.setText(currCat.getName());

    }

    @Override
    public int getItemCount() {
        return this.categories.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView categoryName;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            categoryName = itemView.findViewById(R.id.category_card_name);

        }
    }

    public void deleteTask(int position){
        MainCategory category = categories.get(position);
        CategoryHandler categoryHandler = new CategoryHandler(true);

        try{
            categoryHandler.deleteCategory(category.getCategoryID());
        }catch (Exception e){
            Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        notifyItemRemoved(position);
    }

    public Context getContext() {
        return activity;
    }
}
