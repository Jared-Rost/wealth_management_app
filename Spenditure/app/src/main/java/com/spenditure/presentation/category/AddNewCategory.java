package com.spenditure.presentation.category;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.spenditure.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.spenditure.logic.CategoryHandler;
import com.spenditure.logic.ICategoryHandler;
import com.spenditure.logic.UserHandler;
import com.spenditure.logic.exceptions.InvalidCategoryException;
import com.spenditure.presentation.IOnDialogCloseListener;


/**
 * Pop up for adding Category
 * @author Bao Ngo
 * @version 04 Mar 2024
 */
public class AddNewCategory extends BottomSheetDialogFragment {
    public static final String TAG = "AddNewCategory";
    private EditText mCategoryEdit;
    private Button mSaveBtn;
    private ICategoryHandler categoryHandler;
    private Context context;

    //Return new instance of pop up component
    public static AddNewCategory newInstance(){
        return new AddNewCategory();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_new_category,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCategoryEdit = view.findViewById(R.id.category_edittext);
        mSaveBtn = view.findViewById(R.id.category_save_btn);
        this.categoryHandler = new CategoryHandler(false);

        mCategoryEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //We did nothing in purpose, but we still have to have this function because of it need to be overrided.
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    mSaveBtn.setEnabled(false);
                    mSaveBtn.setBackgroundColor(Color.GRAY);
                }else{
                    mSaveBtn.setEnabled(true);
                    mSaveBtn.setBackgroundColor(getResources().getColor(R.color.green_dark));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //We did nothing in purpose, but we still have to have this function because of it need to be overrided.
            }
        });

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newCategory = mCategoryEdit.getText().toString();
                if(newCategory.isEmpty()){
                    Toast.makeText(context,"Empty Category not Allowed", Toast.LENGTH_SHORT).show();

                }else{
                    try {
                        categoryHandler.addCategory(newCategory, UserHandler.getUserID());
                        ViewCategoryActivity activity = (ViewCategoryActivity) getActivity();
                        activity.refresh();

                    }catch (InvalidCategoryException e){
                        Toast.makeText(context,"Category already exists", Toast.LENGTH_SHORT).show();
                    }

                    Toast.makeText(context,"Category added successfully", Toast.LENGTH_SHORT).show();

                }
                dismiss();
            }
        });


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if(activity instanceof IOnDialogCloseListener){
            ((IOnDialogCloseListener)activity).onDialogClose(dialog);
        }
    }
}
