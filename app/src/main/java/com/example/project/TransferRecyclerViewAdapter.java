package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;

public class TransferRecyclerViewAdapter extends RecyclerView.Adapter<TransferRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "TransferRecyclerViewAda";

    private ArrayList<String> mSender;
    private ArrayList<String> mReceiver;
    private ArrayList<String> mAmount;
    private ArrayList<String> mMessage;
    private ArrayList<Date> mDate;
    private Context mContext;

    public TransferRecyclerViewAdapter(ArrayList<String> mSender, ArrayList<String> mReceiver, ArrayList<String> mAmount, ArrayList<String> mMessage, ArrayList<Date> mDate, Context mContext) {
        this.mSender = mSender;
        this.mReceiver = mReceiver;
        this.mAmount = mAmount;
        this.mMessage = mMessage;
        this.mDate = mDate;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_transferlistitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //setting texts
        holder.textSender.setText(mSender.get(position));
        holder.textReceiver.setText(mReceiver.get(position));
        holder.textMoney.setText(mAmount.get(position));
        holder.textMessage.setText(mMessage.get(position));
        holder.textDate.setText((CharSequence) mDate.get(position));

    }

    @Override
    public int getItemCount() {
        return mSender.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        //vars
        TextView textSender, textReceiver, textMoney, textMessage,textDate;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textSender = itemView.findViewById(R.id.textSender);
            textReceiver = itemView.findViewById(R.id.textReceiver);
            textMoney = itemView.findViewById(R.id.textMoney);
            textMessage = itemView.findViewById(R.id.textMessage);
            textDate = itemView.findViewById(R.id.textDate);
            parentLayout = itemView.findViewById(R.id.account_parrent_layout);
        }
    }
}
