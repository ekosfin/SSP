package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AccountRecyclerViewAdapter extends RecyclerView.Adapter<AccountRecyclerViewAdapter.ViewHolder>{
    //source: https://github.com/mitchtabian/Recyclerview/blob/master/RecyclerView/app/src/main/java/codingwithmitch/com/recyclerview/RecyclerViewAdapter.java

    private static final String TAG = "AccountRecyclerViewAdap";

    private ArrayList<String> mTpye;
    private ArrayList<String> mName;
    private ArrayList<String> mMoney;
    private ArrayList<String> mNumber;
    private Context mContext;

    public AccountRecyclerViewAdapter(ArrayList<String> mTpye, ArrayList<String> mName, ArrayList<String> mMoney, ArrayList<String> mNumber, Context mContext) {
        this.mTpye = mTpye;
        this.mName = mName;
        this.mMoney = mMoney;
        this.mNumber = mNumber;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_accountlistitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");

        //setting texts
        holder.textName.setText(mName.get(position));
        holder.textType.setText(mTpye.get(position));
        holder.textMoney.setText(mMoney.get(position));
        holder.textNumber.setText(mNumber.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onClick: clicked on: " + mName.get(position));

                Toast.makeText(mContext, mName.get(position),Toast.LENGTH_SHORT).show();
                Intent iToAccount = new Intent(v.getContext(),AccountActivity.class);
                iToAccount.putExtra("AccountID",mNumber.get(position));
                mContext.startActivity(iToAccount);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mName.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        //vars
        TextView textType, textName, textMoney, textNumber;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textType = itemView.findViewById(R.id.textAccountType);
            textName = itemView.findViewById(R.id.textaAccountName);
            textMoney = itemView.findViewById(R.id.textAccountMoney);
            textNumber = itemView.findViewById(R.id.textaAccountNumber);
            parentLayout = itemView.findViewById(R.id.account_parrent_layout);
        }
    }
}
