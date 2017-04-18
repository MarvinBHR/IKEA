package com.example.IKEA;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.IKEA.db.Member;
import com.example.IKEA.db.MemberOrder;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class MyHistoryActivity extends BaseActivity{
    private List<MemberOrder> memberOrderList= new ArrayList<>();
    private MyHistoryAdapter myHistoryAdapter;
    private RecyclerView recyclerView;
    private Member member;
    private Button back;
    private TextView total;
    private TextView historyEmpty;
    private double totalPrice = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_history);
        historyEmpty = (TextView)findViewById(R.id.history_empty);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_of_my_history);
        back = (Button)findViewById(R.id.back_button_on_my_history);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        member = (Member) intent.getParcelableExtra("member_data");
        total = (TextView)findViewById(R.id.total_price_in_my_history);
        showHistory();
        total.setText(totalPrice+"");
    }

    private boolean initHistory(){
        memberOrderList = DataSupport.where("create = ? and pay = ? and memberId = ?","1","1",member.getId()+"").find(MemberOrder.class);
        if(memberOrderList.size()>0){
            for(MemberOrder mo:memberOrderList){
                totalPrice += mo.getTotalPrice();
            }
            return true;
        }else {
            return false;
        }
    }

    private void showHistory(){
        memberOrderList.clear();
        if(initHistory()){
            GridLayoutManager layoutManager = new GridLayoutManager(this,1);
            recyclerView.setLayoutManager(layoutManager);
            myHistoryAdapter = new MyHistoryAdapter(memberOrderList);
            recyclerView.setAdapter(myHistoryAdapter);
        }else{
            historyEmpty.setVisibility(View.VISIBLE);
        }
    }
}
