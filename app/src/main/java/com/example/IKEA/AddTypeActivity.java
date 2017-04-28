package com.example.IKEA;

import android.content.DialogInterface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.IKEA.db.Type;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class AddTypeActivity extends BaseActivity implements View.OnClickListener {
    private List<Type> typeList = new ArrayList<>();
    private RecyclerView addTypeRecyclerView;
    private TypeAdapter typeAdapter;
    private Button back;
    private SwipeRefreshLayout refreshType;
    private EditText typeText;
    private Button addTypeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_type);
        addTypeRecyclerView = (RecyclerView)findViewById(R.id.recycler_of_add_type);
        back = (Button) findViewById(R.id.back_button_on_add_type);
        refreshType = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_on_add_type);
        typeText = (EditText)findViewById(R.id.add_type_text);
        addTypeButton = (Button)findViewById(R.id.add_type_button);
        back.setOnClickListener(this);
        addTypeButton.setOnClickListener(this);
        showType();
    }

    private boolean initType(){
        typeList = DataSupport.findAll(Type.class);
        if(typeList.size()>0){
            return true;
        }else {
            return false;
        }
    }

    private void showType(){
        typeList.clear();
        if(initType()){
            GridLayoutManager layoutManager = new GridLayoutManager(this,1);
            addTypeRecyclerView.setLayoutManager(layoutManager);
            typeAdapter = new TypeAdapter(typeList);
            addTypeRecyclerView.setAdapter(typeAdapter);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_button_on_add_type:
                finish();
                break;
            case R.id.add_type_button:
                addType();
                break;
            default:
                break;
        }
    }

    private void addType(){
        final String newType = typeText.getText().toString();
        AlertDialog.Builder dialog = new AlertDialog.Builder(AddTypeActivity.this);
        dialog.setTitle("提示");
        dialog.setMessage("确定添加新类别"+newType+"吗?");
        dialog.setCancelable(true);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<Type> oldTypeList = DataSupport.findAll(Type.class);
                boolean isEmpty = false;
                boolean isExist = false;
                if(newType == null || "".equals(newType)){
                    Alert("提示","类型名称为空！");
                    isEmpty = true;
                    return;
                }
                for(Type t:oldTypeList){
                    if(t.getTypeName().equals(newType)){
                        Alert("提示","该类型名称已存在");
                        isExist = true;
                    }
                }
                if(!isEmpty && !isExist){
                    Type type = new Type();
                    type.setTypeName(newType);
                    type.save();
                    typeText.setText("");
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
        AlertDialog.Builder dialog = new AlertDialog.Builder(AddTypeActivity.this);
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
