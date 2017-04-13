package com.example.IKEA.db;

/**
 * Created by Marvin on 2017/4/13.
 */

public class MemberOrder {//订单
    private int furnitureId;//家具ID
    private int memberId;//用户ID
    private double totalPrice;//总价

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
