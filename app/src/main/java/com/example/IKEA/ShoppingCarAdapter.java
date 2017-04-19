package com.example.IKEA;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
        Button addInShoppingCar;
        Button subInShoppingCar;
        public ViewHolder(View view){
            super(view);
            cardView = (CardView)view;
            furniturePicInShoppingCar = (ImageView)view.findViewById(R.id.furniture_pic_in_shopping_car);
            furnitureNameInShoppingCar = (TextView)view.findViewById(R.id.furniture_name_in_shopping_car);
            furniturePriceInShoppingCar = (TextView)view.findViewById(R.id.furniture_price_in_shopping_car);
            furnitureAmountInShoppingCar = (TextView)view.findViewById(R.id.furniture_amount_in_shopping_car);
            addInShoppingCar = (Button)view.findViewById(R.id.add_in_shopping_car);
            subInShoppingCar = (Button)view.findViewById(R.id.sub_in_shopping_car);
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
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final MemberOrder memberOrder = mMemberOrder.get(position);
        final long memberOrderId = memberOrder.getId();
        final long memberId = memberOrder.getMemberId();
        final long furnitureId = memberOrder.getFurnitureId();
        Member member = DataSupport.find(Member.class,memberId);
        final Furniture furniture = DataSupport.find(Furniture.class,furnitureId);
        Glide.with(mContext).load(furniture.getFurnitureImg()).into(holder.furniturePicInShoppingCar);
        holder.furnitureNameInShoppingCar.setText(furniture.getFurnitureName());
        holder.furniturePriceInShoppingCar.setText(furniture.getFurniturePrice()+"");
        holder.furnitureAmountInShoppingCar.setText(memberOrder.getAmount()+"");
        holder.addInShoppingCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(memberOrder.isDelete()){
                   Alert("提示","该商品已被删除，再去添加吧~！");
                }else {
                    holder.furnitureAmountInShoppingCar.setText(memberOrder.getAmount() + 1 + "");
                    memberOrder.setAmount(memberOrder.getAmount() + 1);
                    memberOrder.setTotalPrice(memberOrder.getTotalPrice() + furniture.getFurniturePrice());
                    memberOrder.update(memberOrderId);
                    LinearLayout linearLayout = (LinearLayout) holder.cardView.getParent().getParent();
                    TextView total = (TextView) linearLayout.findViewById(R.id.total_price_in_shopping_car);
                    double newTotal = Double.valueOf(total.getText().toString()) + furniture.getFurniturePrice();
                    total.setText(newTotal + "");
                }
            }
        });
        holder.subInShoppingCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int m = memberOrder.getAmount() -1;
                if(m > 0) {
                    holder.furnitureAmountInShoppingCar.setText(m + "");
                    memberOrder.setAmount(m);
                    double p = memberOrder.getTotalPrice() - furniture.getFurniturePrice();
                    memberOrder.setTotalPrice(p);
                    memberOrder.update(memberOrderId);
                    LinearLayout linearLayout = (LinearLayout) holder.cardView.getParent().getParent();
                    TextView total = (TextView) linearLayout.findViewById(R.id.total_price_in_shopping_car);
                    double newTotal = Double.valueOf(total.getText().toString()) - furniture.getFurniturePrice();
                    total.setText(newTotal + "");
                }else if(m == 0){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                    dialog.setTitle("提示");
                    dialog.setMessage("确定删除该商品吗？");
                    dialog.setCancelable(true);
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(memberOrder.isDelete()){
                                Alert("提示","该商品已被删除，再去添加吧~！");
                            }else {
                                holder.furnitureAmountInShoppingCar.setText(String.valueOf(0));
                                memberOrder.setDelete(true);
                                memberOrder.update(memberOrderId);
                                LinearLayout linearLayout = (LinearLayout) holder.cardView.getParent().getParent();
                                TextView total = (TextView) linearLayout.findViewById(R.id.total_price_in_shopping_car);
                                double newTotal = Double.valueOf(total.getText().toString()) - furniture.getFurniturePrice();
                                total.setText(newTotal + "");
                                Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dialog.show();
                }else if(m<0){
                    Alert("提示","该商品个数已经是0了哦！");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMemberOrder.size();
    }

    //弹出警告
    private void Alert(String title,String message){
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setCancelable(true);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.show();
    }
}
