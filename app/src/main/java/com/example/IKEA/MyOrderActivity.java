package com.example.IKEA;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.IKEA.db.Member;
import com.example.IKEA.db.MemberOrder;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class MyOrderActivity extends BaseActivity implements View.OnClickListener{
    private List<MemberOrder> memberOrderList = new ArrayList<>();
    private MyOrderAdapter myOrderAdapter;
    private RecyclerView recyclerView;
    private Member member;
    private Button back;
    private Button pay;
    private Button clare;
    private TextView total;
    private TextView orderEmpty;
    private double totalPrice = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_of_my_order);
        back = (Button)findViewById(R.id.back_button_on_my_order);
        pay = (Button)findViewById(R.id.pay_order);
        clare = (Button)findViewById(R.id.clear_my_order);
        total = (TextView)findViewById(R.id.total_price_in_my_order);
        orderEmpty = (TextView)findViewById(R.id.order_empty);
        back.setOnClickListener(this);
        pay.setOnClickListener(this);
        clare.setOnClickListener(this);
        Intent intent = getIntent();
        member = (Member) intent.getParcelableExtra("member_data");
        showMyOrder();
        total.setText(totalPrice+"");
    }

    //初始化
    private boolean initMyOrder(){
        memberOrderList = DataSupport.where("create = ? and pay = ? and memberId = ?","1","0",member.getId()+"").find(MemberOrder.class);
        if(memberOrderList.size()>0){
            for(MemberOrder mo:memberOrderList){
                totalPrice += mo.getTotalPrice();
            }
            return true;
        }else {
            return false;
        }
    }

    //显示订单
    private void showMyOrder(){
        memberOrderList.clear();
        if(initMyOrder()){
            GridLayoutManager layoutManager = new GridLayoutManager(this,1);
            recyclerView.setLayoutManager(layoutManager);
            myOrderAdapter = new MyOrderAdapter(memberOrderList);
            recyclerView.setAdapter(myOrderAdapter);
        }else{
            orderEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_button_on_my_order:
                finish();
                break;
            case R.id.pay_order:
                payOrder();
                break;
            case R.id.clear_my_order:
                clearOrder();
                break;
            default:
                break;
        }
    }

    private void clearOrder(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(MyOrderActivity.this);
        dialog.setTitle("提示");
        dialog.setMessage("确定删除所有订单吗？");
        dialog.setCancelable(true);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(memberOrderList.size()>0) {
                    for (int i = 0; i < memberOrderList.size(); i++) {
                        long id = memberOrderList.get(i).getId();
                        DataSupport.delete(MemberOrder.class, id);
                    }
                    Toast.makeText(MyOrderActivity.this, "删除订单成功！", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(MyOrderActivity.this, "订单为空！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.show();
    }

    private void payOrder(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(MyOrderActivity.this);
        dialog.setTitle("提示");
        dialog.setMessage("确定支付吗？");
        dialog.setCancelable(true);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog.Builder dialog2 = new AlertDialog.Builder(MyOrderActivity.this);
                dialog2.setTitle("提示");
                dialog2.setMessage("支付功能尚未实现，点击确定模拟支付成功，点击取消模拟支付失败。");
                dialog2.setCancelable(true);
                dialog2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(memberOrderList.size()>0) {
                            for (MemberOrder mo : memberOrderList) {
                                long id = mo.getId();
                                mo.setPay(true);
                                mo.update(id);
                            }
                            Toast.makeText(MyOrderActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(MyOrderActivity.this, "没有东西可以支付哦！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog2.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Alert("提示","支付失败！");
                    }
                });
                dialog2.show();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.show();
    }

    //弹出警告
    private void Alert(String title,String message){
        AlertDialog.Builder dialog = new AlertDialog.Builder(MyOrderActivity.this);
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
