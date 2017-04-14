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
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuAdapter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.IKEA.db.Member;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RegisterActivity extends BaseActivity implements View.OnClickListener{
    public static final int TAKE_PHOTO=1;
    public static final int CHOOSE_PHOTO=2;
    private Button backButton;
    private Button ok;
    private Button cancel;
    private EditText name;
    private EditText password;
    private EditText age;
    private EditText email;
    private EditText phone;
    private EditText address;
    private RadioGroup radioGroup;
    private RadioButton man;
    private RadioButton women;
    private ImageView headPic;
    private Button upLoadPic;
    private Button openAlbum;
    private String sex;
    private Uri imageUri = null;//拍照上传
    private String imagePath = null;//相册选择
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        backButton = (Button)findViewById(R.id.back_button);
        ok = (Button)findViewById(R.id.ok);
        cancel = (Button)findViewById(R.id.cancel);
        name = (EditText)findViewById(R.id.register_name);
        password = (EditText)findViewById(R.id.register_password);
        age = (EditText)findViewById(R.id.register_age);
        age.setInputType(InputType.TYPE_CLASS_NUMBER);
        email = (EditText)findViewById(R.id.register_email);
        email.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        phone = (EditText)findViewById(R.id.register_phone);
        phone.setInputType(InputType.TYPE_CLASS_PHONE);
        address = (EditText)findViewById(R.id.register_address);
        radioGroup = (RadioGroup)findViewById(R.id.register_radio);
        man = (RadioButton)findViewById(R.id.radio_man);
        women = (RadioButton)findViewById(R.id.radio_woman);
        headPic = (ImageView)findViewById(R.id.register_head_pic);
        upLoadPic = (Button)findViewById(R.id.register_upload_pic);
        openAlbum = (Button)findViewById(R.id.register_open_album);
        backButton.setOnClickListener(this);
        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
        upLoadPic.setOnClickListener(this);
        openAlbum.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == man.getId()){
                    sex = "男";
                }else if(checkedId == women.getId()){
                    sex = "女";
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
            case R.id.back_button://退出&取消
                finish();
                break;
            case R.id.register_upload_pic://拍照上传
                imagePath = null;//清空相册选择的
                uploadHeadPic();
                break;
            case R.id.register_open_album://相册选择
                imageUri = null;//清空拍照
                upLoadPicFromAlbum();
                break;
            case R.id.ok:
                registerOk();
                break;
            default:
                break;
        }
    }

    private void uploadHeadPic(){
        Date date = new Date();
        String picName = date.toString()+"_head_pic.jpg";
        picName.replaceAll(" ","_");
        File outputImage = new File(getExternalCacheDir(),picName);
        try{
            if(outputImage.exists()){
                outputImage.delete();
            }
            outputImage.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }
        if(Build.VERSION.SDK_INT >= 24){
            imageUri = FileProvider.getUriForFile(RegisterActivity.this,"com.example.IKEA.fileprovider",outputImage);
        }else {
            imageUri = Uri.fromFile(outputImage);
        }
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,TAKE_PHOTO);
    }

    private void upLoadPicFromAlbum(){
        if(ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(RegisterActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }else {
            openAlbumToUpload();
        }
    }

    private void openAlbumToUpload(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {//申请权限结果
        switch (requestCode){
            case 1:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openAlbumToUpload();
                }else{
                    Toast.makeText(RegisterActivity.this,"获取权限失败",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case TAKE_PHOTO:
                if(resultCode == RESULT_OK){
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        headPic.setImageBitmap(bitmap);
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
                break;
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
    private void handleImageOnKitKat(Intent data){
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
        displayImage(imagePath);
    }

    private void handleImageBeforeKitKat(Intent data){
        Uri uri = data.getData();
        imagePath = getImagePath(uri,null);
        displayImage(imagePath);
    }

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

    private void displayImage(String imagePath){
        if(imagePath != null){
            Bitmap bitmap =BitmapFactory.decodeFile(imagePath);
            headPic.setImageBitmap(bitmap);
        }else {
            Toast.makeText(this,"failed to get image",Toast.LENGTH_SHORT).show();
        }
    }

    private void registerOk(){
        Member member = new Member();
        String nameToSave = name.getText().toString();
        String passwordToSave = password.getText().toString();
        String ageToSave = age.getText().toString();
        String emailToSave = email.getText().toString();
        String phoneToSave = phone.getText().toString();
        String addressToSave = address.getText().toString();
        String headPicToSave = new String();
        Boolean same = false;
        if(imagePath != null){
            headPicToSave = imagePath;
        }else if(imageUri != null ){
            headPicToSave = imageUri.toString();
        }else {
            headPicToSave = "none";
        }
        if(nameToSave == null || "".equals(nameToSave) || passwordToSave == null || "".equals(passwordToSave) ||addressToSave == null || "".equals(addressToSave)||sex ==null ||"".equals(sex)){
            ifNull();
            return;
        }
        List<Member> members = DataSupport.findAll(Member.class);
        for(Member m:members){
            if(m.getMemberName().equals(nameToSave)){
                same = true;
            }
        }
        if(same){
            AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
            dialog.setTitle("注册错误");
            dialog.setMessage("用户名已存在！");
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
            return;
        }
        member.setMemberName(nameToSave);
        member.setMemberPassword(passwordToSave);
        member.setSex(sex);
        member.setAge(ageToSave);
        member.setEmail(emailToSave);
        member.setPhone(phoneToSave);
        member.setAddress(addressToSave);
        member.setHeadPic(headPicToSave);
        member.save();
        Intent intent = new Intent();
        intent.putExtra("register","ok");
        setResult(RESULT_OK,intent);
        finish();
    }

    private void ifNull(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
        dialog.setTitle("注册错误");
        dialog.setMessage("有必填项未填！");
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
