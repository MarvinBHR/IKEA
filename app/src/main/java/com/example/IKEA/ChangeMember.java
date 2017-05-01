package com.example.IKEA;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.IKEA.db.Member;

import org.litepal.crud.DataSupport;

import java.util.List;

public class ChangeMember extends BaseActivity implements View.OnClickListener {
    public static final int CHOOSE_PHOTO=2;
    private long memberId;
    private Member memberToChange;
    private EditText memberName;
    private EditText memberAge;
    private RadioGroup radioGroup;
    private RadioButton man;
    private RadioButton women;
    private EditText memberEmail;
    private EditText memberPhone;
    private EditText memberAddress;
    private ImageView memberHedPic;
    private Button openAlbumToChange;
    private Button okOnChange;
    private Button cancelOnChange;
    private Button back;
    private String imagePath = null;
    private String sexToChange;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_member);
        Intent intent = getIntent();
        //初始化控件
        memberToChange = (Member) intent.getParcelableExtra("member_data");
        memberId  = memberToChange.getId();
        memberName = (EditText)findViewById(R.id.change_name);
        memberAge = (EditText)findViewById(R.id.change_age);
        memberAge.setInputType(InputType.TYPE_CLASS_NUMBER);
        radioGroup = (RadioGroup)findViewById(R.id.change_radio);
        man = (RadioButton)findViewById(R.id.radio_man_on_change);
        women = (RadioButton)findViewById(R.id.radio_woman_on_change);
        memberEmail = (EditText)findViewById(R.id.change_email);
        memberPhone = (EditText)findViewById(R.id.change_phone);
        memberAddress = (EditText)findViewById(R.id.change_address);
        memberHedPic = (ImageView)findViewById(R.id.change_head_pic);
        openAlbumToChange = (Button)findViewById(R.id.change_open_album);
        okOnChange = (Button)findViewById(R.id.ok_on_change);
        cancelOnChange = (Button)findViewById(R.id.cancel_on_change);
        back = (Button)findViewById(R.id.back_button_on_change);
        //注册点击事件
        openAlbumToChange.setOnClickListener(this);
        okOnChange.setOnClickListener(this);
        cancelOnChange.setOnClickListener(this);
        back.setOnClickListener(this);
        memberName.setText(memberToChange.getMemberName());
        memberAge.setText(memberToChange.getAge());
        String sex = memberToChange.getSex();
        if(sex.equals("男")){
            man.setChecked(true);
        }else if(sex.equals("女")){
            women.setChecked(true);
        }
        memberEmail.setText(memberToChange.getEmail());
        memberPhone.setText(memberToChange.getPhone());
        memberAddress.setText(memberToChange.getAddress());
        Bitmap bitmap = BitmapFactory.decodeFile(memberToChange.getHeadPic());
        sexToChange  = memberToChange.getSex();
        memberHedPic.setImageBitmap(bitmap);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == man.getId()){
                    sexToChange = "男";
                }else if(checkedId == women.getId()){
                    sexToChange = "女";
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel_on_change:
            case R.id.back_button_on_change:
                finish();
                break;
            case R.id.ok_on_change:
                saveMember();
                break;
            case R.id.change_open_album:
                applyOpenAlbum();
            default:
                break;
        }
    }

    //保存资料
    private void saveMember(){
        List<Member> memberList = DataSupport.findAll(Member.class);
        for (Member m:memberList){
            if(m.getId() != memberId && m.getMemberName().equals(memberName.getText().toString())){
                Alert("修改错误","该用户名已存在");
                return;
            }
        }
        if(TextUtils.isEmpty(memberName.getText().toString()) || TextUtils.isEmpty(memberAge.getText().toString()) ||TextUtils.isEmpty(sexToChange)||TextUtils.isEmpty(memberAddress.getText().toString())){
            Alert("修改错误","有必填项未填");
            return;
        }
        memberToChange.setMemberName(memberName.getText().toString());
        memberToChange.setAge(memberAge.getText().toString());
        memberToChange.setSex(sexToChange);
        memberToChange.setEmail(memberEmail.getText().toString());
        memberToChange.setPhone(memberPhone.getText().toString());
        memberToChange.setAddress(memberAddress.getText().toString());
        if(imagePath != null){
            memberToChange.setHeadPic(imagePath);
        }else{
            memberToChange.setHeadPic(memberToChange.getHeadPic());
        }
        memberToChange.update(memberId);
        Toast.makeText(ChangeMember.this,"修改成功",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ChangeMember.this,Home.class);
        intent.putExtra("member_data",memberToChange);
        startActivity(intent);
    }

    //弹出警告
    private void Alert(String title,String message){
        AlertDialog.Builder dialog = new AlertDialog.Builder(ChangeMember.this);
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

    //申请打开相册
    private void applyOpenAlbum(){
        if(ContextCompat.checkSelfPermission(ChangeMember.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ChangeMember.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }else {
            openAlbum();
        }
    }

    //打开相册
    private void openAlbum(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }

    //申请权限结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else{
                    Toast.makeText(ChangeMember.this,"获取权限失败",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    //根据手机版本保存图片
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case CHOOSE_PHOTO:
                if(resultCode == RESULT_OK){
                    if(Build.VERSION.SDK_INT >= 19){
                        //4.4及以上
                        handleImageOnKitKat(data);
                    }else{
                        //4.4以下
                        handleImageBeforeKitKat(data);
                    }
                }
            default:
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){//4.4及以上
        imagePath = null;
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID+"="+id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            imagePath = getImagePath(uri,null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            imagePath = uri.getPath();
        }
        displayAddImage(imagePath);
    }

    private void handleImageBeforeKitKat(Intent data){//4.4以下
        Uri uri = data.getData();
        imagePath = getImagePath(uri,null);
        displayAddImage(imagePath);
    }

    //获取图片路径
    private String getImagePath(Uri uri,String selection){
        String path = null;
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    //展示添加的产品图片
    private void displayAddImage(String imagePath){
        if(imagePath != null){
            Bitmap bitmap =BitmapFactory.decodeFile(imagePath);
            memberHedPic.setImageBitmap(bitmap);
        }else {
            Toast.makeText(this,"failed to get image",Toast.LENGTH_SHORT).show();
        }
    }
}
