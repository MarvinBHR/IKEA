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
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ScrollingTabContainerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.IKEA.db.Furniture;
import com.example.IKEA.db.Member;
import com.example.IKEA.db.Range;
import com.example.IKEA.db.Type;

import org.litepal.crud.DataSupport;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "Home";
    public static final int CHOOSE_PHOTO=2;
    private ArrayAdapter rangeAdapter;//范围适配器
    private String selectedRange;//要保存的范围
    private ArrayAdapter typeAdapter;//类型适配器
    private String selectedType;//要保存的类别
    //刷新
    private SwipeRefreshLayout swipeRefresh;
    //家具列表
    private Furniture[] furnitures;
    private List<Furniture> furnitureList = new ArrayList<>();
    private FurnitureAdapter furnitureAdapter;
    //基础布局
    private DrawerLayout drawer;
    private Button navButton;
    private TextView title;
    private TextView name;
    private TextView email;
    private NavigationView navView;
    private View header;
    private CircleImageView navHeadPic;
    //主页显示
    private RecyclerView recyclerView;
    //添加商品
    private LinearLayout addFurniture;
    private EditText furnitureName;
    private Spinner furnitureType;
    private Spinner furnitureRange;
    private EditText furniturePrice;
    private EditText furnitureDescribe;
    private EditText furnitureAttribute;
    private ImageView furniturePic;
    private Button addOpenAlbum;
    private Button addFurnitureOk;
    private Button addFurnitureCancel;
    private String addImagePath = null;//相册选择
    //当前登录人员
    private Member member = new Member();
    //查看会员
    private LinearLayout checkMember;
    private Button checkMemberBySex;
    private Button checkMemberByAge;
    //查看管理员
    private LinearLayout showManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerView = (RecyclerView)findViewById(R.id.home_body_recycle);
        navView =(NavigationView)findViewById(R.id.nav_view);
        title = (TextView)findViewById(R.id.title);
        member = (Member) getIntent().getParcelableExtra("member_data");
        if(!member.getMemberName().equals("Manager")){//判断登录身份，提供不同菜单
            navView.inflateMenu(R.menu.nav_menu);
        }else if(member.getMemberName().equals("Manager")){
            navView.inflateMenu(R.menu.nav_menu_m);
        }
        showFurniture();
        swipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        drawer = (DrawerLayout)findViewById(R.id.activity_home);
        navButton = (Button)findViewById(R.id.nav_button);
        header = navView.inflateHeaderView(R.layout.nav_header);
        name = (TextView) header.findViewById(R.id.nav_name);
        email = (TextView) header.findViewById(R.id.nav_email);
        navHeadPic = (CircleImageView) header.findViewById(R.id.nav_head_pic);
        checkMemberBySex = (Button)findViewById(R.id.check_member_by_sex);
        checkMemberByAge = (Button)findViewById(R.id.check_member_by_age);
        //隐藏在主页上的内容
        checkMember = (LinearLayout)findViewById(R.id.check_member);
        addFurniture = (LinearLayout)findViewById(R.id.add_furniture);
        showManager = (LinearLayout) findViewById(R.id.show_manager);
        title.setText(member.getMemberName());
        name.setText(member.getMemberName());
        if(member.getEmail() != null && !"".equals(member.getEmail())){//处理邮箱
            email.setText(member.getEmail());
        }else {
            email.setText("");
        }
        if(member.getHeadPic().equals("none")){//处理头像
            navHeadPic.setImageResource(R.drawable.head_pic_default);
        }else{
            Bitmap bitmap = BitmapFactory.decodeFile(member.getHeadPic());
            navHeadPic.setImageBitmap(bitmap);
        }
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        //点击侧栏菜单
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nva_quit://退出应用
                        ActivityCollector.finishAll();
                        break;
                    case R.id.nav_add://添加产品
                        addFurniture();
                        break;
                    case R.id.nav_show_forms://查看报表
                        checkMember();
                        break;
                    case R.id.nav_manager_member://管理会员
                        Intent intent10 = new Intent(Home.this,ManagerMember.class);
                        startActivity(intent10);
                        break;
                    case R.id.nav_add_range://管理范围
                        Intent intent6 = new Intent(Home.this,AddRangeActivity.class);
                        startActivity(intent6);
                        break;
                    case R.id.nav_add_type://管理种类
                        Intent intent7 = new Intent(Home.this,AddTypeActivity.class);
                        startActivity(intent7);
                        break;
                    case R.id.nav_to_home://返回主页
                        toHome();
                        break;
                    case R.id.nav_manager://管理员资料
                        showManager();
                        break;
                    case R.id.nav_personal://会员资料
                        Intent intent =new Intent(Home.this,ChangeMember.class);
                        intent.putExtra("member_data",member);
                        drawer.closeDrawers();
                        startActivity(intent);
                        break;
                    case R.id.nav_change_password://修改密码
                        Intent intent2 = new Intent(Home.this,ChangePassword.class);
                        intent2.putExtra("memberId",member.getId());
                        drawer.closeDrawers();
                        startActivity(intent2);
                        break;
                    case R.id.nav_shopping_car://购物车
                        Intent intent3 = new Intent(Home.this,ShoppingCar.class);
                        intent3.putExtra("member_data",member);
                        drawer.closeDrawers();
                        startActivity(intent3);
                        break;
                    case R.id.nav_my_order://我的订单
                        Intent intent4 = new Intent(Home.this,MyOrderActivity.class);
                        intent4.putExtra("member_data",member);
                        drawer.closeDrawers();
                        startActivity(intent4);
                        break;
                    case R.id.nav_history://历史记录
                        Intent intent5 = new Intent(Home.this,MyHistoryActivity.class);
                        intent5.putExtra("member_data",member);
                        drawer.closeDrawers();
                        startActivity(intent5);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        //刷新
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFurniture();
                swipeRefresh.setRefreshing(false);
            }
        });
    }

    //展示管理员信息
    private void showManager(){
        recyclerView.setVisibility(View.GONE);
        checkMember.setVisibility(View.GONE);
        addFurniture.setVisibility(View.GONE);
        showManager.setVisibility(View.VISIBLE);
        drawer.closeDrawers();
    }

    //返回主页
    private void toHome(){
        drawer.closeDrawers();
        recyclerView.setVisibility(View.VISIBLE);
        checkMember.setVisibility(View.GONE);
        showManager.setVisibility(View.GONE);
        addFurniture.setVisibility(View.GONE);
    }
    //初始化家具列表
    private boolean initFurniture(){
        furnitureList = DataSupport.findAll(Furniture.class);
        if(furnitureList.size()<=0) {
            return false;
        }else {
            return true;
        }
    }

    //显示家具
    private void showFurniture(){
        furnitureList.clear();
        if(initFurniture()){//显示列表
            GridLayoutManager layoutManager = new GridLayoutManager(this,2);
            recyclerView.setLayoutManager(layoutManager);
            furnitureAdapter = new FurnitureAdapter(furnitureList);
            furnitureAdapter.setMember(member);
            recyclerView.setAdapter(furnitureAdapter);
        }
    }

    //刷新家具
    private void refreshFurniture(){
        showFurniture();
        furnitureAdapter.notifyDataSetChanged();
        swipeRefresh.setRefreshing(false);
    }
    //点击主页按钮
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_open_album://添加产品时打开相册
                applyOpenAlbum();
                break;
            case R.id.add_furniture_cancel://添加产品时取消
                closeAdd();
                break;
            case  R.id.add_furniture_ok://添加商品
                saveAdd();
                break;
            case R.id.check_member_by_sex://按性别查看
                Intent intent8 = new Intent(Home.this,CheckMemberBySex.class);
                startActivity(intent8);
                break;
            case R.id.check_member_by_age://按年龄查看
                Intent intent9 = new Intent(Home.this,CheckMemberByAge.class);
                startActivity(intent9);
                break;
            default:
                break;
        }
    }

    //添加产品
    private void addFurniture(){
        //初始化控件
        furnitureName = (EditText)findViewById(R.id.furniture_name);
        furnitureType = (Spinner) findViewById(R.id.furniture_type);
        //产品类别下拉菜单初始化
        List<Type> typeList = DataSupport.findAll(Type.class);
        StringBuilder typeNameBuffer = new StringBuilder();
        for(Type t:typeList){
            typeNameBuffer.append(t.getTypeName());
            typeNameBuffer.append(",");
        }
        final String[] typeNameList = typeNameBuffer.toString().split(",");
        typeAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,typeNameList);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//下拉菜单风格
        furnitureType.setAdapter(typeAdapter);
        furnitureType.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedType = typeNameList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //产品类别初始化结束
        furnitureRange = (Spinner) findViewById(R.id.furniture_range);
        //产品范围下拉菜单初始化
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
        furnitureRange.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedRange = rangeNameList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //范围初始化结束
        furniturePrice = (EditText)findViewById(R.id.furniture_price);
        furniturePrice.setInputType(InputType.TYPE_CLASS_NUMBER);//价格只能是数字
        furnitureDescribe = (EditText)findViewById(R.id.furniture_describe);
        furnitureAttribute = (EditText)findViewById(R.id.furniture_attribute);
        furniturePic = (ImageView)findViewById(R.id.furniture_pic);
        addOpenAlbum = (Button) findViewById(R.id.add_open_album);
        addFurnitureOk = (Button)findViewById(R.id.add_furniture_ok);
        addFurnitureCancel = (Button)findViewById(R.id.add_furniture_cancel);
        //显示页面
        recyclerView.setVisibility(View.GONE);
        checkMember.setVisibility(View.GONE);
        showManager.setVisibility(View.GONE);
        addFurniture.setVisibility(View.VISIBLE);
        //注册点击事件
        addOpenAlbum.setOnClickListener(this);
        addFurnitureOk.setOnClickListener(this);
        addFurnitureCancel.setOnClickListener(this);
        //关闭侧菜单
        drawer.closeDrawers();
    }

    //申请打开相册
    private void applyOpenAlbum(){
        if(ContextCompat.checkSelfPermission(Home.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Home.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
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
                    Toast.makeText(Home.this,"获取权限失败",Toast.LENGTH_SHORT).show();
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
        addImagePath = null;
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID+"="+id;
                addImagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                addImagePath = getImagePath(contentUri,null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            addImagePath = getImagePath(uri,null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            addImagePath = uri.getPath();
        }
        displayAddImage(addImagePath);
    }

    private void handleImageBeforeKitKat(Intent data){//4.4以下
        Uri uri = data.getData();
        addImagePath = getImagePath(uri,null);
        displayAddImage(addImagePath);
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

    //取消添加产品
    private void closeAdd(){
        //清空
        furnitureName.setText("");
        furnitureDescribe.setText("");
        furnitureAttribute.setText("");
        furniturePrice.setText("");
        furniturePic.setImageResource(R.drawable.furniture_pic_default);
        //隐藏当前页，显示主页
        addFurniture.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    //保存产品
    private void saveAdd(){
        //获取要保存的字段
        String furnitureNameToSave = furnitureName.getText().toString();
        String furnitureTypeToSave = selectedType;
        String furnitureRangeToSave = selectedRange;
        double furniturePriceToSave =Double.valueOf(furniturePrice.getText().toString());
        String furnitureDescribeToSave = furnitureDescribe.getText().toString();
        String furnitureAttributeToSave = furnitureAttribute.getText().toString();
        String furnitureImgToSave = addImagePath;
        boolean same = false;
        //检测
        if(TextUtils.isEmpty(furnitureAttributeToSave)||TextUtils.isEmpty(furnitureNameToSave)||TextUtils.isEmpty(furnitureTypeToSave)||TextUtils.isEmpty(furnitureRangeToSave)||furniturePriceToSave == 0||TextUtils.isEmpty(furnitureDescribeToSave)){
            Alert("添加错误","有必填项未填");
            return;
        }
        List<Furniture>furnitureList = DataSupport.findAll(Furniture.class);
        for(Furniture f:furnitureList){
            if(f.getFurnitureName().equals(furnitureNameToSave)){
                same = true;
            }
        }
        if(same){
            Alert("信息错误","产品名不能重复");
            return;
        }
        //保存字段
        Furniture furniture = new Furniture();
        furniture.setFurnitureName(furnitureNameToSave);
        furniture.setFurnitureType(furnitureTypeToSave);
        furniture.setFurnitureRange(furnitureRangeToSave);
        furniture.setFurniturePrice(furniturePriceToSave);
        furniture.setFurnitureDescribe(furnitureDescribeToSave);
        furniture.setFurnitureAttribute(furnitureAttributeToSave);
        furniture.setFurnitureImg(furnitureImgToSave);
        furniture.save();
        //通知保存成功
        Toast.makeText(Home.this,"保存成功",Toast.LENGTH_SHORT).show();
        //清空并退出
        closeAdd();
    }

    //查看会员
    private void checkMember(){
        addFurniture.setVisibility(View.GONE);
        showManager.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        checkMember.setVisibility(View.VISIBLE);
        //关闭侧菜单
        drawer.closeDrawers();
        //注册点击事件
        checkMemberByAge.setOnClickListener(this);
        checkMemberBySex.setOnClickListener(this);
    }

    //弹出警告
    private void Alert(String title,String message){
        AlertDialog.Builder dialog = new AlertDialog.Builder(Home.this);
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
