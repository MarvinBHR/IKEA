package com.example.IKEA;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.IKEA.db.Member;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class ManagerMember extends BaseActivity {
    private List<Member> memberList = new ArrayList<>();
    private Button back;
    private RecyclerView members;
    private MemberAdapter memberAdapter;
    private SwipeRefreshLayout refreshMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_member);
        back = (Button)findViewById(R.id.back_button_on_manager_member);
        members = (RecyclerView) findViewById(R.id.list_of_manager_member);
        refreshMember = (SwipeRefreshLayout)findViewById(R.id.refresh_member);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        shoeMember();
        refreshMember.setColorSchemeResources(R.color.colorPrimary);
        refreshMember.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                shoeMember();
                refreshMember.setRefreshing(false);
            }
        });
    }

    private boolean initMember(){
        memberList = DataSupport.where("memberName != ?","Manager").find(Member.class);
        if(memberList.size()>0){
            return true;
        }else{
            return false;
        }
    }

    private void shoeMember(){
        memberList.clear();
        if(initMember()){
            GridLayoutManager layoutManager = new GridLayoutManager(this,1);
            members.setLayoutManager(layoutManager);
            memberAdapter = new MemberAdapter(memberList);
            members.setAdapter(memberAdapter);
        }
    }
}
