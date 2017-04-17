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
 * Created by Marvin on 2017/4/17.
 */

public class ShoppingCarAdapter extends RecyclerView.Adapter<ShoppingCarAdapter.ViewHolder> {
    private Context mContext;
    private List<MemberOrder> mMemberOrder;
    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView furniturePicInShoppingCar;
        TextView furnitureNameInShoppingCar;
        TextView furniturePriceInShoppingCar;
        TextView furnitureAmountInShoppingCar;
        public ViewHolder(View view){
            super(view);
            cardView = (CardView)view;
            furniturePicInShoppingCar = (ImageView)view.findViewById(R.id.furniture_pic_in_shopping_car);
            furnitureNameInShoppingCar = (TextView)view.findViewById(R.id.furniture_name_in_shopping_car);
            furniturePriceInShoppingCar = (TextView)view.findViewById(R.id.furniture_price_in_shopping_car);
            furnitureAmountInShoppingCar = (TextView)view.findViewById(R.id.furniture_amount_in_shopping_car);
        }
    }

    public ShoppingCarAdapter(List<MemberOrder> memberOrderList){
        mMemberOrder = memberOrderList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.shopping_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MemberOrder memberOrder = mMemberOrder.get(position);
        long memberId = memberOrder.getMemberId();
        long furnitureId = memberOrder.getFurnitureId();
        Member member = DataSupport.find(Member.class,memberId);
        Furniture furniture = DataSupport.find(Furniture.class,furnitureId);
        Glide.with(mContext).load(furniture.getFurnitureImg()).into(holder.furniturePicInShoppingCar);
        holder.furnitureNameInShoppingCar.setText(furniture.getFurnitureName());
        holder.furniturePriceInShoppingCar.setText(furniture.getFurniturePrice()+"");
        holder.furnitureAmountInShoppingCar.setText("×"+memberOrder.getAmount());
    }

    @Override
    public int getItemCount() {
        return mMemberOrder.size();
    }
}
