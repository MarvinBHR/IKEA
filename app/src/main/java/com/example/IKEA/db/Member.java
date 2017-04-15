package com.example.IKEA.db;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

/**
 * Created by Marvin on 2017/4/13.
 */

public class Member extends DataSupport implements Parcelable{//用户
    private String memberName;//姓名
    private String memberPassword;//密码
    private  String sex;//性别
    private String age;//年龄
    private String email;//邮箱
    private String phone;//电话
    private String address;//地址
    private String headPic;//头像
    private long id;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAge() {
        return age;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberPassword(String memberPassword) {
        this.memberPassword = memberPassword;
    }

    public String getMemberPassword() {
        return memberPassword;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSex() {
        return sex;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(memberName);
        dest.writeString(memberPassword);
        dest.writeString(sex);
        dest.writeString(age);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(address);
        dest.writeString(headPic);
    }
     public static final Parcelable.Creator<Member>CREATOR = new Parcelable.Creator<Member>(){
         @Override
         public Member createFromParcel(Parcel source) {
             Member member = new Member();
             member.memberName = source.readString();
             member.memberPassword = source.readString();
             member.sex = source.readString();
             member.age = source.readString();
             member.email = source.readString();
             member.phone = source.readString();
             member.address = source.readString();
             member.headPic = source.readString();
             return member;
         }

         @Override
         public Member[] newArray(int size) {
             return new Member[size];
         }
     };
}
