package com.example.IKEA;

import android.content.DialogInterface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.IKEA.db.Range;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class AddRangeActivity extends BaseActivity implements View.OnClickListener{
    private List<Range> rangeList = new ArrayList<>();
    private RecyclerView addRangeRecyclerView;
    private RangeAdapter rangeAdapter;
    private Button back;
    private SwipeRefreshLayout refreshRange;
    private EditText rangeText;
    private Button addRangeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_range);
        addRangeRecyclerView = (RecyclerView)findViewById(R.id.recycler_of_add_range);
        back = (Button)findViewById(R.id.back_button_on_add_range);
        refreshRange = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_on_add_range);
        rangeText = (EditText)findViewById(R.id.add_range_text);
        addRangeButton = (Button)findViewById(R.id.add_range_button);
        back.setOnClickListener(this);
        addRangeButton.setOnClickListener(this);
        showRange();
        //刷新
        refreshRange.setColorSchemeResources(R.color.colorPrimary);
        refreshRange.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showRange();
                refreshRange.setRefreshing(false);
            }
        });
    }

    private boolean initRange(){
        rangeList = DataSupport.findAll(Range.class);
        if(rangeList.size()>0){
            return true;
        }else{
            return false;
        }
    }

    private void showRange(){
        rangeList.clear();
        if(initRange()){
            GridLayoutManager layoutManager = new GridLayoutManager(this,1);
            addRangeRecyclerView.setLayoutManager(layoutManager);
            rangeAdapter = new RangeAdapter(rangeList);
            addRangeRecyclerView.setAdapter(rangeAdapter);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_button_on_add_range:
                finish();
                break;
            case R.id.add_range_button:
                addRange();
                break;
            default:
                break;
        }
    }

    private void addRange(){
        final String newRange = rangeText.getText().toString();
        AlertDialog.Builder dialog = new AlertDialog.Builder(AddRangeActivity.this);
        dialog.setTitle("提示");
        dialog.setMessage("确定新增范围"+newRange+"吗？");
        dialog.setCancelable(true);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<Range> oldRangeList = DataSupport.findAll(Range.class);
                boolean isEmpty = false;
                boolean isExist = false;
                if(newRange == null || newRange.equals("")){
                    Alert("提示","范围名称为空！");
                    isEmpty = true;
                    return;
                }
                for(Range r:oldRangeList){
                    if(r.getRangeName().equals(newRange)){
                        Alert("提示","该范围已存在！");
                        isExist = true;
                        return;
                    }
                }
                if(!isEmpty && !isExist) {
                    Range range = new Range();
                    range.setRangeName(newRange);
                    range.save();
                    rangeText.setText("");
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
        AlertDialog.Builder dialog = new AlertDialog.Builder(AddRangeActivity.this);
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
