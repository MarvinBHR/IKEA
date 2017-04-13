package com.example.IKEA.db;

/**
 * Created by Marvin on 2017/4/13.
 */

public class Order {
    private int id;
    private int furnitureId;
    private int memberId;
    private double sum;

    public void setFurnitureId(int furnitureId) {
        this.furnitureId = furnitureId;
    }

    public double getSum() {
        return sum;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFurnitureId() {
        return furnitureId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getId() {
        return id;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public int getMemberId() {
        return memberId;
    }
}
