package com.example.IKEA;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.IKEA.db.Furniture;
import com.example.IKEA.db.Range;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Marvin on 2017/4/28.
 */

public class RangeAdapter extends RecyclerView.Adapter<RangeAdapter.ViewHolder> {
    private Context mContext;
    private List<Range> mRange;
    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        EditText rangeName;
        Button deleteRange;
        Button changeRange;
        public ViewHolder(View view) {
            super(view);
            cardView = (CardView)view;
            rangeName = (EditText) view.findViewById(R.id.range_name);
            deleteRange = (Button)view.findViewById(R.id.delete_range);
            changeRange = (Button)view.findViewById(R.id.change_range);
        }
    }

    public RangeAdapter(List<Range> rangeList){
        this.mRange = rangeList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.range_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Range range = mRange.get(position);
        holder.rangeName.setText(range.getRangeName());
        holder.deleteRange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setTitle("提示");
                dialog.setMessage("是否删除该范围？删除范围会将该范围下所有产品删除！");
                dialog.setCancelable(true);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataSupport.deleteAll(Furniture.class,"furnitureRange = ?",range.getRangeName());
                        DataSupport.delete(Range.class,range.getId());
                        Toast.makeText(mContext,"删除成功",Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.show();
            }
        });
        holder.changeRange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setTitle("提示");
                dialog.setMessage("是否修改该范围？修改范围会将该范围下所有产品更新！");
                dialog.setCancelable(true);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newName = holder.rangeName.getText().toString();
                        List<Range> oldRange = DataSupport.findAll(Range.class);
                        for(Range r : oldRange){
                            if(r.getId() != range.getId() && r.getRangeName().equals(newName)){
                                Alert("修改错误","范围名称已存在！");
                                return;
                            }
                        }
                        List<Furniture> furnitureList = DataSupport.where("furnitureRange = ?",range.getRangeName()).find(Furniture.class);
                        for(Furniture furniture:furnitureList){//更新产品
                            long id = furniture.getId();
                            furniture.setFurnitureRange(newName);
                            furniture.update(id);
                        }
                        long rangeId = range.getId();
                        range.setRangeName(newName);
                        range.update(rangeId);
                        Toast.makeText(mContext,"更新成功",Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mRange.size();
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
