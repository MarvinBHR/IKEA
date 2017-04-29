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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.IKEA.db.Member;

import java.util.List;

/**
 * Created by Marvin on 2017/4/29.
 */

public class MemberAdapter extends RecyclerView.Adapter <MemberAdapter.ViewHolder>{
    private Context mContext;
    private List<Member> mMember;
    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView memberName;
        Button forbid;
        Button unforbid;
        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            memberName = (TextView)view.findViewById(R.id.member_name);
            forbid = (Button)view.findViewById(R.id.forbid_member);
            unforbid = (Button) view.findViewById(R.id.unforbid_member);
        }
    }

    public MemberAdapter(List<Member> memberList){
        this.mMember = memberList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.member_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Member member = mMember.get(position);
        holder.memberName.setText(member.getMemberName());
        boolean isForbid = member.isForbid();
        //判断用户情况，确定显示那个按钮
        if(isForbid){//被冻结
            holder.unforbid.setVisibility(View.VISIBLE);
        }else if(!isForbid){//未冻结
            holder.forbid.setVisibility(View.VISIBLE);
        }
        holder.forbid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setTitle("提示");
                dialog.setMessage("确定冻结用户"+member.getMemberName()+"吗？");
                dialog.setCancelable(true);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        long id = member.getId();
                        member.setForbid(true);
                        member.update(id);
                        Toast.makeText(mContext,"用户已冻结",Toast.LENGTH_SHORT).show();
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
        holder.unforbid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setTitle("提示");
                dialog.setMessage("确定解禁用户"+member.getMemberName()+"吗？");
                dialog.setCancelable(true);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        long id = member.getId();
                        member.setToDefault("forbid");
                        member.update(id);
                        Toast.makeText(mContext,"解禁成功",Toast.LENGTH_SHORT).show();
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
        return mMember.size();
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
