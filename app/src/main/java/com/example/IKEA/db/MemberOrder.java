package com.example.IKEA.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Marvin on 2017/4/13.
 */

public class MemberOrder extends DataSupport{//订单
    private long furnitureId;//家具ID
    private long memberId;//用户ID
    private double totalPrice;//总价
    private boolean create;//是否已生成订单：通过该字段确定在购物车中显示还是在订单中显示
    private boolean pay;//是否支付：通过该字段确定在订单中显示还是在历史中显示
    private int amount;//购买数量
    private long id;


    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setCreate(boolean create) {
        this.create = create;
    }

    public boolean isCreate() {
        return create;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setPay(boolean pay) {
        this.pay = pay;
    }

    public boolean isPay() {
        return pay;
    }

    public void setFurnitureId(long furnitureId) {
        this.furnitureId = furnitureId;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public long getFurnitureId() {
        return furnitureId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }



    public long getMemberId() {
        return memberId;
    }
}
