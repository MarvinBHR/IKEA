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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.IKEA.db.Furniture;
import com.example.IKEA.db.Range;
import com.example.IKEA.db.Type;

import org.litepal.crud.DataSupport;

import java.util.List;

public class ChangeFurniture extends AppCompatActivity implements View.OnClickListener{
    public static final int CHOOSE_PHOTO=2;
    private ArrayAdapter typeAdapter;//类别适配器
    private String selectedType;//要保存的类别
    private ArrayAdapter rangeAdapter;//范围适配器
    private String selectedRange;//要保存的范围
    private long furnitureId;
    private LinearLayout changeFurniture;
    private EditText furnitureName;
    private Spinner furnitureType;
    private Spinner furnitureRange;
    private EditText furniturePrice;
    private EditText furnitureDescribe;
    private EditText furnitureAttribute;
    private ImageView furniturePic;
    private Button changeOpenAlbum;
    private Button changeFurnitureOk;
    private Button changeFurnitureCancel;
    private String changeImagePath = null;
    //要修改的产品
    private Furniture furnitureToChange;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_furniture);
        //获取要更改的产品
        Intent intent = getIntent();
        furnitureId = intent.getLongExtra("furnitureId",0);
        furnitureToChange = DataSupport.find(Furniture.class,furnitureId);
        //初始化控件
        changeFurniture = (LinearLayout)findViewById(R.id.to_change_furniture);
        furnitureName = (EditText)findViewById(R.id.furniture_name_to_change);
        furnitureType = (Spinner) findViewById(R.id.furniture_type_to_change);
        //初始化类别下拉框
        List<Type> typeList = DataSupport.findAll(Type.class);//所有范围
        StringBuilder typeNameBuffer = new StringBuilder();
        for(Type t:typeList){
            typeNameBuffer.append(t.getTypeName());
            typeNameBuffer.append(",");
        }
        final String[] typeNameList = typeNameBuffer.toString().split(",");
        typeAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,typeNameList);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        furnitureType.setAdapter(typeAdapter);
        //设置默认值
        int typePosition = getPosition(typeNameList,furnitureToChange.getFurnitureType());
        furnitureType.setSelection(typePosition,true);
        furnitureType.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedType = typeNameList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //类别下拉框初始化结束

        furnitureRange = (Spinner) findViewById(R.id.furniture_range_to_change);
        //初始化范围下拉框
        List<Range> rangeList = DataSupport.findAll(Range.class);//所有范围
        StringBuffer rangeNameBuffer = new StringBuffer();
        for(Range r : rangeList){
            rangeNameBuffer.append(r.getRangeName());
            rangeNameBuffer.append(",");
        }
        final String[] rangeNameList = rangeNameBuffer.toString().split(",");
        rangeAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,rangeNameList);//适配下拉菜单
        rangeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//下拉菜单风格
        furnitureRange.setAdapter(rangeAdapter);
        //设置默认值
        int rangePosition = getPosition(rangeNameList,furnitureToChange.getFurnitureRange());
        furnitureRange.setSelection(rangePosition,true);
        furnitureRange.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRange = rangeNameList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //范围下拉框初始化结束

        //填入原始资料
        furniturePrice = (EditText)findViewById(R.id.furniture_price_to_change);
        furniturePrice.setInputType(InputType.TYPE_CLASS_NUMBER);
        furnitureDescribe = (EditText)findViewById(R.id.furniture_describe_to_change);
        furnitureAttribute = (EditText)findViewById(R.id.furniture_attribute_to_change);
        furniturePic = (ImageView)findViewById(R.id.furniture_pic_to_change);
        changeOpenAlbum = (Button)findViewById(R.id.change_open_album);
        changeFurnitureOk = (Button)findViewById(R.id.change_furniture_ok);
        changeFurnitureCancel = (Button)findViewById(R.id.change_furniture_cancel);
        furnitureName.setText(furnitureToChange.getFurnitureName());
        furniturePrice.setText(furnitureToChange.getFurniturePrice()+"");
        furnitureDescribe.setText(furnitureToChange.getFurnitureDescribe());
        furnitureAttribute.setText(furnitureToChange.getFurnitureAttribute());
        Bitmap bitmap = BitmapFactory.decodeFile(furnitureToChange.getFurnitureImg());
        furniturePic.setImageBitmap(bitmap);
        Toolbar toolbar = (Toolbar)findViewById(R.id.change_furniture_toolbar);
        changeOpenAlbum.setOnClickListener(this);
        changeFurnitureOk.setOnClickListener(this);
        changeFurnitureCancel.setOnClickListener(this);
        //显示返回按钮
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    //获取位置
    private int getPosition(String[] nameList,String name){
        int i = 0;
        for(;i<nameList.length;i++){
            if(nameList[i].equals(name)){
                return i;
            }
        }
        return i;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    //点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.change_furniture_cancel:
                finish();
                break;
            case R.id.change_furniture_ok:
                saveFurniture();
                break;
            case R.id.change_open_album:
                applyOpenAlbum();
                break;
            default:
                break;
        }
    }

    //保存产品
    private void saveFurniture(){
        Furniture furniture = new Furniture();
        List<Furniture> furnitureList = DataSupport.findAll(Furniture.class);
        for(Furniture f:furnitureList){
            if(f.getId() != furnitureId && f.getFurnitureName().equals(furnitureName.getText().toString())){
                Alert("修改错误","产品名称重复！");
                return;
            }
        }
        furniture.setFurnitureName(furnitureName.getText().toString());
        furniture.setFurnitureType(selectedType);
        furniture.setFurniturePrice(Double.valueOf(furniturePrice.getText().toString()));
        furniture.setFurnitureRange(selectedRange);
        furniture.setFurnitureDescribe(furnitureDescribe.getText().toString());
        furniture.setFurnitureAttribute(furnitureAttribute.getText().toString());
        furniture.setFurnitureImg(changeImagePath);
        furniture.update(furnitureId);
        Toast.makeText(ChangeFurniture.this,"修改成功",Toast.LENGTH_SHORT).show();
        finish();
    }

    //弹出警告
    private void Alert(String title,String message){
        AlertDialog.Builder dialog = new AlertDialog.Builder(ChangeFurniture.this);
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
        if(ContextCompat.checkSelfPermission(ChangeFurniture.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ChangeFurniture.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }else {
            openAlbumToUpload();
        }
    }

    //打开相册
    private void openAlbumToUpload(){
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
                    openAlbumToUpload();
                }else{
                    Toast.makeText(ChangeFurniture.this,"获取权限失败",Toast.LENGTH_SHORT).show();
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
        changeImagePath = null;
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID+"="+id;
                changeImagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                changeImagePath = getImagePath(contentUri,null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            changeImagePath = getImagePath(uri,null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            changeImagePath = uri.getPath();
        }
        displayAddImage(changeImagePath);
    }

    private void handleImageBeforeKitKat(Intent data){//4.4以下
        Uri uri = data.getData();
        changeImagePath = getImagePath(uri,null);
        displayAddImage(changeImagePath);
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
            furniturePic.setImageBitmap(bitmap);
        }else {
            Toast.makeText(this,"failed to get image",Toast.LENGTH_SHORT).show();
        }
    }
}
