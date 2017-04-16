package com.example.IKEA;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.IKEA.db.Furniture;
import com.example.IKEA.db.Member;

import java.util.List;

/**
 * Created by Marvin on 2017/4/15.
 */

public class FurnitureAdapter extends RecyclerView.Adapter<FurnitureAdapter.ViewHolder> {
    private static final String TAG = "FurnitureAdapter";
    private Context mContext;
    private List<Furniture> mFurniture;
    private Member member;
    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView furnitureImage;
        TextView furnitureName;
        public ViewHolder(View view){
            super(view);
            cardView = (CardView)view;
            furnitureImage = (ImageView)view.findViewById(R.id.show_furniture_image);
            furnitureName = (TextView)view.findViewById(R.id.show_furniture_name);
        }
    }

    public FurnitureAdapter(List<Furniture> furnitureList){
        mFurniture = furnitureList;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        if(mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.furniture_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Furniture furniture = mFurniture.get(position);
                Intent intent = new Intent(mContext, FurnitureActivity.class);
                intent.putExtra(FurnitureActivity.FURNITURE_NAME,furniture.getFurnitureName());
                intent.putExtra(FurnitureActivity.FURNITURE_PRICE,String.valueOf(furniture.getFurniturePrice()));
                intent.putExtra(FurnitureActivity.FURNITURE_ATTRIBUTE,furniture.getFurnitureAttribute());
                intent.putExtra(FurnitureActivity.FURNITURE_DESCRIBE,furniture.getFurnitureDescribe());
                intent.putExtra(FurnitureActivity.FURNITURE_PIC,furniture.getFurnitureImg());
                intent.putExtra(FurnitureActivity.FURNITURE_ID,furniture.getId());
                intent.putExtra(FurnitureActivity.MEMBER_DATA,member);
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Furniture furniture = mFurniture.get(position);
        holder.furnitureName.setText(furniture.getFurnitureName());
        Glide.with(mContext).load(furniture.getFurnitureImg()).into(holder.furnitureImage);
    }

    @Override
    public int getItemCount() {
        return mFurniture.size();
    }

    public void setMember(Member member){
        this.member = member;
    }
}
