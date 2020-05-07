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

public class CardsRecyclerViewAdapeter extends RecyclerView.Adapter<CardsRecyclerViewAdapeter.ViewHolder> {

    private ArrayList<String> mText;
    private ArrayList<String> mType;
    private ArrayList<String> mUlimit;
    private Context mContext;

    public CardsRecyclerViewAdapeter(ArrayList<String> mText, ArrayList<String> mType, ArrayList<String> mUlimit, Context mContext) {
        this.mText = mText;
        this.mType = mType;
        this.mUlimit = mUlimit;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cardslistitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //setting texts
        holder.textName.setText(mText.get(position));
        holder.textType.setText(mType.get(position));
        holder.texUlimit.setText(mUlimit.get(position));
    }

    @Override
    public int getItemCount() {
        return mText.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        //vars
        TextView textName, textType, texUlimit;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textCardName);
            textType = itemView.findViewById(R.id.textCardType);
            texUlimit = itemView.findViewById(R.id.textUlimit);
            parentLayout = itemView.findViewById(R.id.account_parrent_layout);
        }
    }
}
