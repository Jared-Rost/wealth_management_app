/**
 * CustomTransactionAdapter.java
 *
 * COMP3350 SECTION A02
 *
 * @author Jillian Friesen, 7889402
 * @date Tuesday, February 7, 2024
 *
 * PURPOSE:
 *  This file contains an adapter that takes in a list of Transaction objects and generates
 *  a custom UI list of transactions for the user to view.
 **/

package com.spenditure.presentation.transaction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.spenditure.R;
import com.spenditure.object.Transaction;

import java.util.List;

public class CustomTransactionAdapter extends BaseAdapter {
    private Context context;
    private List<Transaction> transactions;
    private LayoutInflater inflator;

    public CustomTransactionAdapter(List<Transaction> transactions, Context context){
        this.context = context;
        this.transactions = transactions;
        inflator = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return transactions.size();
    }

    @Override
    public Transaction getItem(int i) {
        return transactions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return transactions.get(i).getTransactionID();
    }

    // Set up the UI with the information about the Transaction
    @Override
    public View getView(int i, View view, ViewGroup viewGroup){
        view =inflator.inflate(R.layout.activity_transactions_list_view, null);

        // Set the transaction 'what the heck'
        TextView whatTheHeck = (TextView) view.findViewById(R.id.textview_list_what_the_heck);
        whatTheHeck.setText(transactions.get(i).getName());

        // Set the transaction amount
        TextView amount = (TextView) view.findViewById(R.id.textview_list_amount);
        amount.setText(String.format("$%.2f", transactions.get(i).getAmount()));

        return view;
    }

}
