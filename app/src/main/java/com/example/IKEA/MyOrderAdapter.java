package com.example.IKEA;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {
    private Context mContext;
    private List<MemberOrder> mMemberOrderList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView furniturePicInMyOrder;
        TextView furnitureNameInMyOrder;
        TextView furniturePriceInMyOrder;
        TextView furnitureAmountInMyOrder;
        public ViewHolder(View view){
            super(view);
            cardView = (CardView)view;
            furniturePicInMyOrder = (ImageView)view.findViewById(R.id.furniture_pic_in_my_order);
            furnitureNameInMyOrder = (TextView)view.findViewById(R.id.furniture_name_in_my_order);
            furniturePriceInMyOrder = (TextView)view.findViewById(R.id.furniture_price_in_my_order);
            furnitureAmountInMyOrder = (TextView)view.findViewById(R.id.furniture_amount_in_my_order);
        }
    }
    public MyOrderAdapter(List<MemberOrder> memberOrderList){
        mMemberOrderList = memberOrderList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.my_order_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MemberOrder memberOrder = mMemberOrderList.get(position);
        long memberId = memberOrder.getMemberId();
        long furnitureId = memberOrder.getFurnitureId();
        Member member = DataSupport.find(Member.class,memberId);
        Furniture furniture = DataSupport.find(Furniture.class,furnitureId);
        Glide.with(mContext).load(furniture.getFurnitureImg()).into(holder.furniturePicInMyOrder);
        holder.furnitureNameInMyOrder.setText(furniture.getFurnitureName());
        holder.furniturePriceInMyOrder.setText(furniture.getFurniturePrice()+"");
        holder.furnitureAmountInMyOrder.setText("×"+memberOrder.getAmount());
    }

    @Override
    public int getItemCount() {
        return mMemberOrderList.size();
    }
}
