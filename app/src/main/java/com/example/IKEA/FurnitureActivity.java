package com.example.IKEA;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.ParcelFileDescriptor;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.IKEA.db.Furniture;
import com.example.IKEA.db.Member;
import com.example.IKEA.db.MemberOrder;

import org.litepal.crud.DataSupport;

import java.util.List;

public class FurnitureActivity extends BaseActivity implements View.OnClickListener{
    public static final String FURNITURE_NAME ="furniture_name";
    public static final String FURNITURE_PRICE = "furniture_price";
    public static final String FURNITURE_DESCRIBE = "furniture_describe";
    public static final String FURNITURE_ATTRIBUTE ="furniture_attribute";
    public static final String FURNITURE_PIC = "furniture_pic";
    public static final String FURNITURE_ID = "furniture_id";
    public static final String MEMBER_DATA = "member_data";
    private TextView showPrice;
    private TextView showDescribe;
    private TextView showAttribute;
    private String furnitureName;
    private long furnitureId;
    private  Member member;
    private Double furniturePrice;
    private boolean r = false;
    private long sameId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_furniture);
        Intent intent = getIntent();
        furnitureName = intent.getStringExtra(FURNITURE_NAME);
        furniturePrice = intent.getDoubleExtra(FURNITURE_PRICE,0);
        String furnitureDescribe = intent.getStringExtra(FURNITURE_DESCRIBE);
        String furnitureAttribute = intent.getStringExtra(FURNITURE_ATTRIBUTE);
        furnitureId = intent.getLongExtra(FURNITURE_ID,0);
        String furniturePic = intent.getStringExtra(FURNITURE_PIC);
        member =(Member)getIntent().getParcelableExtra(MEMBER_DATA);
        //显示返回按钮
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_of_furniture);
        CollapsingToolbarLayout collapsingToolbar=(CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        ImageView furnitureImage = (ImageView)findViewById(R.id.furniture_image_on_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbar.setTitle(furnitureName);
        Glide.with(this).load(furniturePic).into(furnitureImage);
        showPrice = (TextView)findViewById(R.id.furniture_show_price);
        showPrice.setText(furniturePrice+"");
        showDescribe = (TextView)findViewById(R.id.furniture_show_describe);
        showDescribe.setText(furnitureDescribe);
        showAttribute = (TextView)findViewById(R.id.furniture_show_attribute);
        showAttribute.setText(furnitureAttribute);
        //处理悬浮按钮
        FloatingActionButton addToCar =(FloatingActionButton)findViewById(R.id.add_to_car_float);
        FloatingActionButton deleteFurniture = (FloatingActionButton)findViewById(R.id.delete_furniture);
        FloatingActionButton changeFurniture = (FloatingActionButton)findViewById(R.id.change_furniture);
        if(member.getMemberName().equals("Manager")){
            addToCar.setVisibility(View.GONE);
            deleteFurniture.setVisibility(View.VISIBLE);
            changeFurniture.setVisibility(View.VISIBLE);
        }
        //注册点击事件
        addToCar.setOnClickListener(this);
        deleteFurniture.setOnClickListener(this);
        changeFurniture.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
//点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_to_car_float://添加至购物车
                addToShoppingCar();
                break;
            case R.id.delete_furniture://删除产品
                deleteFurniture();
                break;
            case R.id.change_furniture://修改产品信息
                changeFurniture();
                break;
            default:
                break;
        }
    }

    //删除产品
    private void deleteFurniture(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(FurnitureActivity.this);
        dialog.setTitle("提示");
        dialog.setMessage("确定删除"+furnitureName+"吗？");
        dialog.setCancelable(true);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DataSupport.delete(Furniture.class,furnitureId);
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

    //修改产品
    private void changeFurniture(){
        Intent intent = new Intent(FurnitureActivity.this,ChangeFurniture.class);
        intent.putExtra("furnitureId",furnitureId);
        startActivity(intent);
    }

    //添加至购物车
    private void addToShoppingCar(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(FurnitureActivity.this);
        dialog.setTitle("提示");
        dialog.setMessage("确定将"+furnitureName+"添加至购物车吗？");
        dialog.setCancelable(true);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Boolean flag = false;
                List<MemberOrder> memberOrderList = DataSupport.findAll(MemberOrder.class);
                for(MemberOrder m:memberOrderList){
                    if(m.getMemberId() == member.getId() && m.getFurnitureId() == furnitureId && !m.isCreate() && !m.isPay()){
                        flag = true;//该用户购物车中有该商品
                        sameId = m.getId();
                    }
                }
                if(flag){//已添加过该商品，直接更新数据库，amount+1
                    AlertDialog.Builder dialog2 = new AlertDialog.Builder(FurnitureActivity.this);
                    dialog2.setTitle("提示");
                    dialog2.setMessage("该商品已经在您购物车中，确定购买多个吗？");
                    dialog2.setCancelable(true);
                    dialog2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MemberOrder memberOrder = new MemberOrder();
                            MemberOrder oldMemberOrder = DataSupport.find(MemberOrder.class,sameId);
                            int amount = oldMemberOrder.getFurnitureAmount();
                            double total = oldMemberOrder.getTotalPrice();
                            memberOrder.setFurnitureAmount(amount+1);
                            memberOrder.setTotalPrice(total+furniturePrice);
                            memberOrder.update(sameId);
                        }
                    });
                    dialog2.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dialog2.show();
                }else{//未购买过，新增
                    MemberOrder memberOrder = new MemberOrder();
                    memberOrder.setFurnitureAmount(1);
                    memberOrder.setMemberId(member.getId());
                    memberOrder.setFurnitureId(furnitureId);
                    memberOrder.setTotalPrice(furniturePrice);
                    memberOrder.setPay(false);
                    memberOrder.setCreate(false);
                    memberOrder.setDeleteFlag(false);
                    memberOrder.save();
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

    //弹出警告
    private void Alert(String title,String message){
        AlertDialog.Builder dialog = new AlertDialog.Builder(FurnitureActivity.this);
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
