package com.example.IKEA.db;

/**
 * Created by Marvin on 2017/4/13.
 */

public class Furniture {//家具
    private String furnitureImg;//图片
    private String furnitureName;//名称
    private String furnitureType;//类别
    private String furnitureRange;//范围
    private double furniturePrice;//价格
    private String furnitureDescribe;//描述
    private String furnitureAttribute;//属性

    public void setFurnitureAttribute(String furnitureAttribute) {
        this.furnitureAttribute = furnitureAttribute;
    }

    public double getFurniturePrice() {
        return furniturePrice;
    }

    public void setFurnitureDescribe(String furnitureDescribe) {
        this.furnitureDescribe = furnitureDescribe;
    }

    public String getFurnitureAttribute() {
        return furnitureAttribute;
    }

    public void setFurnitureImg(String furnitureImg) {
        this.furnitureImg = furnitureImg;
    }

    public String getFurnitureDescribe() {
        return furnitureDescribe;
    }

    public void setFurnitureName(String furnitureName) {
        this.furnitureName = furnitureName;
    }

    public String getFurnitureImg() {
        return furnitureImg;
    }

    public void setFurniturePrice(double furniturePrice) {
        this.furniturePrice = furniturePrice;
    }

    public String getFurnitureName() {
        return furnitureName;
    }

    public void setFurnitureRange(String furnitureRange) {
        this.furnitureRange = furnitureRange;
    }

    public String getFurnitureRange() {
        return furnitureRange;
    }

    public void setFurnitureType(String furnitureType) {
        this.furnitureType = furnitureType;
    }

    public String getFurnitureType() {
        return furnitureType;
    }
}
