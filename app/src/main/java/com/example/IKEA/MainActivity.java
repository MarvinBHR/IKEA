package com.example.IKEA;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuAdapter;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.IKEA.db.Member;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener{
    private Button login;
    private Button register;
    private TextView mainName;
    private TextView mainPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LitePal.getDatabase();//创建数据库
        addManager();//添加管理员
        login = (Button)findViewById(R.id.login);
        register = (Button)findViewById(R.id.register);
        mainName = (TextView)findViewById(R.id.main_name);
        mainPassword = (TextView)findViewById(R.id.main_password);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:
                toLogin();
                break;
            case R.id.register:
                toRegister();
                break;
            default:
                break;
        }
    }

    private void toRegister(){
        Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    String returnData = data.getStringExtra("register");
                    if(returnData.equals("ok")){
                        Toast.makeText(MainActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                    }
                }
        }
    }

    private void toLogin(){
        String name = mainName.getText().toString();
        String password = mainPassword.getText().toString();
        if(name == null || "".equals(name)){
            Alert("登陆错误","请填写用户名!");
            return;
        }else if(password == null || "".equals(password)){
            Alert("登录错误","请填写密码");
            return;
        }
        List<Member> members = DataSupport.where("memberName = ? and memberPassword = ?",name,password).find(Member.class);
        if(members.size() == 0){
            Alert("登陆错误","用户名或密码错误");
        }else if(members.size()>1){
            Alert("数据库错误","请联系客服人员");
        }else{
            Member member = members.get(0);
            Intent intent = new Intent(MainActivity.this,Home.class);
            intent.putExtra("member_data",member);
            startActivity(intent);
        }
    }
   private void Alert(String title,String message){
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
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

    private void addManager(){
        Member member = new Member();
        List<Member> members = DataSupport.findAll(Member.class);
        Boolean same = false;
        for(Member m:members){
            if(m.getMemberName().equals("Manager")){
                same = true;
            }
        }
        if(!same){
            member.setMemberName("Manager");
            member.setEmail("873919116@qq.com");
            member.setMemberPassword("admin");
            member.setHeadPic("none");
            member.save();
        }
    }
}
