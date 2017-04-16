package com.example.IKEA;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.IKEA.db.Member;

import org.litepal.crud.DataSupport;

public class ChangePassword extends BaseActivity implements View.OnClickListener {
    private long memberId;
    private Member memberToChangePassword;
    private EditText oldPassword;
    private EditText newPassword;
    private String oldPasswordToChange;
    private String newPasswordToSave;
    private Button changePasswordOk;
    private Button changePasswordCancel;
    private Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Intent intent = getIntent();
        memberId = intent.getLongExtra("memberId",0);
        memberToChangePassword = DataSupport.find(Member.class,memberId);
        oldPassword = (EditText)findViewById(R.id.old_password);
        newPassword = (EditText)findViewById(R.id.new_password);
        changePasswordOk = (Button)findViewById(R.id.ok_on_change_password);
        changePasswordCancel = (Button)findViewById(R.id.cancel_on_change_password);
        back = (Button)findViewById(R.id.back_button_on_change_password);
        changePasswordOk.setOnClickListener(this);
        changePasswordCancel.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel_on_change_password:
            case R.id.back_button_on_change_password:
                finish();
                break;
            case R.id.ok_on_change_password:
                changePassword();
                break;
            default:
                break;
        }
    }

    private void changePassword(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(ChangePassword.this);
        dialog.setTitle("提示");
        dialog.setMessage("确定修改密码吗？修改成功后需重新登录。");
        dialog.setCancelable(true);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                oldPasswordToChange = oldPassword.getText().toString();
                newPasswordToSave = newPassword.getText().toString();
                if(oldPasswordToChange.equals(memberToChangePassword.getMemberPassword())){
                    Member member = new Member();
                    member.setMemberPassword(newPasswordToSave);
                    member.update(memberId);
                    ActivityCollector.finishAll();
                }else{
                    Alert("提示","旧密码输入错误！");
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
        AlertDialog.Builder dialog = new AlertDialog.Builder(ChangePassword.this);
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
