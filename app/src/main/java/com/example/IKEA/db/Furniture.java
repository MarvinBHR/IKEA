package com.example.IKEA.db;

/**
 * Created by Marvin on 2017/4/13.
 */

public class Furniture {
    private String pic;
    private int id;
    private String name;
    private String type;
    private String range;
    private double price;
    private String describe;
    private String abs;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setAbs(String abs) {
        this.abs = abs;
    }

    public String getAbs() {
        return abs;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getDescribe() {
        return describe;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPic() {
        return pic;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getRange() {
        return range;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
