package com.example.IKEA.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Marvin on 2017/4/13.
 */

public class MemberOrder extends DataSupport{//订单
    private int furnitureId;//家具ID
    private int memberId;//用户ID
    private double totalPrice;//总价
    private boolean pay;//是否支付

    public void setPay(boolean pay) {
        this.pay = pay;
    }

    public boolean isPay() {
        return pay;
    }

    public void setFurnitureId(int furnitureId) {
        this.furnitureId = furnitureId;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public int getFurnitureId() {
        return furnitureId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }



    public int getMemberId() {
        return memberId;
    }
}
