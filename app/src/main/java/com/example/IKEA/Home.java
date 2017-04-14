package com.example.IKEA;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.IKEA.db.Member;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home extends BaseActivity {
    private DrawerLayout drawer;
    private Button navButton;
    private TextView title;
    private TextView name;
    private TextView email;
    private NavigationView navView;
    private View header;
    private CircleImageView navHeadPic;
    private Member member = new Member();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        navView =(NavigationView)findViewById(R.id.nav_view);
        title = (TextView)findViewById(R.id.title);
        member = (Member) getIntent().getParcelableExtra("member_data");
        Toast.makeText(Home.this,member.getHeadPic(),Toast.LENGTH_SHORT).show();
        if(!member.getMemberName().equals("Manager")){
            navView.inflateMenu(R.menu.nav_menu);
        }else if(member.getMemberName().equals("Manager")){
            navView.inflateMenu(R.menu.nav_menu_m);
        }
        drawer = (DrawerLayout)findViewById(R.id.activity_home);
        navButton = (Button)findViewById(R.id.nav_button);
        header = navView.inflateHeaderView(R.layout.nav_header);
        name = (TextView) header.findViewById(R.id.nav_name);
        email = (TextView) header.findViewById(R.id.nav_email);
        navHeadPic = (CircleImageView) header.findViewById(R.id.nav_head_pic);
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
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nva_quit:
                        ActivityCollector.finishAll();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }


}
