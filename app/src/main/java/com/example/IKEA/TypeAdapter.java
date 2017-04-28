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
import com.example.IKEA.db.Type;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Marvin on 2017/4/28.
 */

public class TypeAdapter extends RecyclerView.Adapter<TypeAdapter.ViewHolder> {
    private Context mContext;
    private List<Type> mType;
    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        EditText typeName;
        Button deleteType;
        Button changeType;
        public ViewHolder(View view){
            super(view);
            cardView = (CardView)view;
            typeName = (EditText) view.findViewById(R.id.type_name);
            deleteType = (Button) view.findViewById(R.id.delete_type);
            changeType = (Button) view.findViewById(R.id.change_type);
        }
    }

    public TypeAdapter(List<Type> typeList){
        this.mType = typeList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.type_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Type type = mType.get(position);
        holder.typeName.setText(type.getTypeName());
        holder.deleteType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setTitle("提示");
                dialog.setMessage("是否删除该类别？删除类别会将该范围下所有产品删除！");
                dialog.setCancelable(true);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataSupport.deleteAll(Furniture.class,"furnitureType = ?",type.getTypeName());
                        DataSupport.delete(Type.class,type.getId());
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
        holder.changeType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setTitle("提示");
                dialog.setMessage("是否修改该类别？修改类别会将该类别下所有产品更新！");
                dialog.setCancelable(true);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newName = holder.typeName.getText().toString();
                        List<Type> oldType = DataSupport.findAll(Type.class);
                        for(Type t:oldType){
                            if(t.getId() != type.getId() && t.getTypeName().equals(newName)){
                                Alert("修改错误","该类别名称已存在");
                                return;
                            }
                        }
                        List<Furniture> furnitureList = DataSupport.where("furnitureType = ?",type.getTypeName()).find(Furniture.class);
                        for(Furniture furniture:furnitureList){
                            long id = furniture.getId();
                            furniture.setFurnitureType(newName);
                            furniture.update(id);
                        }
                        long typeId = type.getId();
                        type.setTypeName(newName);
                        type.update(typeId);
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
        return mType.size();
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
