package com.example.IKEA;


import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

public class CheckMemberByAge extends BaseActivity {
    private Button back;
    private TextView childCoast;
    private TextView youngCoast;
    private TextView oldCoast;
    private TextView olderCoast;
    private Double child = 0.0;
    private Double old = 0.0;
    private Double young = 0.0;
    private Double older = 0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_member_by_age);
        back = (Button)findViewById(R.id.back_button_on_check_member_by_age);
        childCoast = (TextView)findViewById(R.id.child_coast);
        youngCoast = (TextView)findViewById(R.id.young_coast);
        oldCoast = (TextView)findViewById(R.id.old_coast);
        olderCoast = (TextView)findViewById(R.id.older_coast);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //0~18
        Cursor childCursor = DataSupport.findBySQL("select * from MemberOrder inner join Member on MemberOrder.memberId = Member.id where Member.age >= ? and Member.age <= ? and MemberOrder.pay = ?","0","18","1");
        if(childCursor.moveToFirst()){
            do{
                child += childCursor.getDouble(childCursor.getColumnIndex("totalprice"));
            }while (childCursor.moveToNext());
        }
        childCursor.close();
        childCoast.setText(child+"");
        //19~30
        Cursor youngCursor = DataSupport.findBySQL("select * from MemberOrder inner join Member on MemberOrder.memberId = Member.id where Member.age >= ? and Member.age <= ? and MemberOrder.pay = ?","19","30","1");
        if(youngCursor.moveToFirst()){
            do{
                young += youngCursor.getDouble(youngCursor.getColumnIndex("totalprice"));
            }while (youngCursor.moveToNext());
        }
        youngCoast.setText(young+"");
        //31~50
        Cursor oldCursor = DataSupport.findBySQL("select * from MemberOrder inner join Member on MemberOrder.memberId = Member.id where Member.age >= ? and Member.age <= ? and MemberOrder.pay = ?","31","50","1");
        if(oldCursor.moveToFirst()){
            do{
                old += oldCursor.getDouble(oldCursor.getColumnIndex("totalprice"));
            }while (oldCursor.moveToNext());
        }
        oldCoast.setText(old+"");
        //51以上
        Cursor olderCursor = DataSupport.findBySQL("select * from MemberOrder inner join Member on MemberOrder.memberId = Member.id where Member.age >= ? and MemberOrder.pay = ?","51","1");
        if(olderCursor.moveToFirst()){
            do{
                older += olderCursor.getDouble(olderCursor.getColumnIndex("totalprice"));
            }while (olderCursor.moveToNext());
        }
        olderCoast.setText(older+"");
    }
}
