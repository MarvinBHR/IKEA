package com.example.IKEA;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.IKEA.db.Furniture;
import com.example.IKEA.db.Member;
import com.example.IKEA.db.MemberOrder;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Marvin on 2017/4/18.
 */

public class MyHistoryAdapter extends RecyclerView.Adapter<MyHistoryAdapter.ViewHolder> {
    private Context mContext;
    private List<MemberOrder> mMemberOrder;
    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView furniturePicInMyHistory;
        TextView furnitureNameInMyHistory;
        TextView furniturePriceInMyHistory;
        TextView furnitureAmountInMyHistory;
        public ViewHolder(View view){
            super(view);
            cardView = (CardView)view;
            furniturePicInMyHistory = (ImageView)view.findViewById(R.id.furniture_pic_in_my_history);
            furnitureNameInMyHistory = (TextView)view.findViewById(R.id.furniture_name_in_my_history);
            furniturePriceInMyHistory = (TextView)view.findViewById(R.id.furniture_price_in_my_history);
            furnitureAmountInMyHistory = (TextView)view.findViewById(R.id.furniture_amount_in_my_history);
        }
    }

    public MyHistoryAdapter(List<MemberOrder> memberOrderList){
        mMemberOrder = memberOrderList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.history_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MemberOrder memberOrder = mMemberOrder.get(position);
        long memberId = memberOrder.getMemberId();
        long furnitureId = memberOrder.getFurnitureId();
        Member member = DataSupport.find(Member.class,memberId);
        Furniture furniture = DataSupport.find(Furniture.class,furnitureId);
        Glide.with(mContext).load(furniture.getFurnitureImg()).into(holder.furniturePicInMyHistory);
        holder.furnitureNameInMyHistory.setText(furniture.getFurnitureName());
        holder.furniturePriceInMyHistory.setText(furniture.getFurniturePrice()+"");
        holder.furnitureAmountInMyHistory.setText("×"+memberOrder.getFurnitureAmount());
    }

    @Override
    public int getItemCount() {
        return mMemberOrder.size();
    }
}
