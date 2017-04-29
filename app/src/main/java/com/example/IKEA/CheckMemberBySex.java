package com.example.IKEA;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.IKEA.db.Member;
import com.example.IKEA.db.MemberOrder;

import org.litepal.crud.DataSupport;

import java.util.List;

public class CheckMemberBySex extends BaseActivity {
    private static final String TAG = "CheckMemberBySex";
    private Button back;
    private TextView manCoast;
    private TextView womanCoast;
    private Double man = 0.0;
    private Double woman = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_member_by_sex);
        back = (Button)findViewById(R.id.back_button_on_check_member_by_sex);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        manCoast = (TextView)findViewById(R.id.man_coast);
        womanCoast = (TextView) findViewById(R.id.woman_coast);
        //查询数据
       Cursor manCursor = DataSupport.findBySQL("select * from MemberOrder inner join Member on MemberOrder.memberId = Member.id where Member.sex = ? and MemberOrder.pay = ?","男","1");
        manCursor.moveToFirst();
        if(manCursor.moveToFirst()){
            do{
                man =man+ manCursor.getDouble(manCursor.getColumnIndex("totalprice"));
            }while (manCursor.moveToNext());
        }
        manCursor.close();
        manCoast.setText(man+"");
        Cursor womanCursor = DataSupport.findBySQL("select * from MemberOrder inner join Member on MemberOrder.memberId = Member.id where Member.sex = ? and MemberOrder.pay = ?","女","1");
        if(womanCursor.moveToFirst()){
            do{
                woman =woman+ womanCursor.getDouble(manCursor.getColumnIndex("totalprice"));
            }while (womanCursor.moveToNext());
        }
        womanCursor.close();
        womanCoast.setText(woman+"");
    }
}
