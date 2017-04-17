package com.example.IKEA;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
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

public class ShoppingCar extends BaseActivity implements View.OnClickListener {
    private List<MemberOrder> memberOrderList= new ArrayList<>();
    private RecyclerView recyclerOfShoppingCar;
    private ShoppingCarAdapter shoppingCarAdapter;
    private TextView totalPriceInShoppingCar;
    private Button back;
    private Button createOrder;
    private Button clear;
    private Member member;
    private double totalPrice = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_car);
        recyclerOfShoppingCar = (RecyclerView)findViewById(R.id.recycler_of_shopping_car);
        back = (Button) findViewById(R.id.back_button_on_shopping_car);
        createOrder =(Button)findViewById(R.id.create_order);
        clear = (Button)findViewById(R.id.clear_shopping_car);
        totalPriceInShoppingCar = (TextView)findViewById(R.id.total_price_in_shopping_car);
        back.setOnClickListener(this);
        createOrder.setOnClickListener(this);
        clear.setOnClickListener(this);
        Intent intent = getIntent();
        member = (Member) intent.getParcelableExtra("member_data");
        showShoppingCar();
        totalPriceInShoppingCar.setText(totalPrice+"");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_button_on_shopping_car:
                finish();
                break;
            case R.id.create_order:
                break;
            case R.id.clear_shopping_car:
                clearShoppingCar();
                break;
            default:
                break;
        }
    }

    private boolean initShoppingCar(){
        memberOrderList = DataSupport.where("create = ? and pay = ? and memberId = ?","0","0",member.getId()+"").find(MemberOrder.class);
        if(memberOrderList.size()>0){
            for(MemberOrder mo:memberOrderList){
                totalPrice += mo.getTotalPrice();
            }
            return true;
        }else {
            return false;
        }
    }

    private void showShoppingCar(){
        memberOrderList.clear();
        if(initShoppingCar()){
            GridLayoutManager layoutManager = new GridLayoutManager(this,1);
            recyclerOfShoppingCar.setLayoutManager(layoutManager);
            shoppingCarAdapter = new ShoppingCarAdapter(memberOrderList);
            recyclerOfShoppingCar.setAdapter(shoppingCarAdapter);
        }
    }

    private void clearShoppingCar(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(ShoppingCar.this);
        dialog.setTitle("提示");
        dialog.setMessage("确定要清空购物车吗？");
        dialog.setCancelable(true);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DataSupport.deleteAll(MemberOrder.class,"create = ? and pay = ? and memberId = ?","0","0",member.getId()+"");
                finish();
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
        AlertDialog.Builder dialog = new AlertDialog.Builder(ShoppingCar.this);
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
