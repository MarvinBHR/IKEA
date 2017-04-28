package com.example.IKEA.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Marvin on 2017/4/28.
 */

public class Range extends DataSupport {
    private String rangeName;
    private long id;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setRangeName(String rangeName) {
        this.rangeName = rangeName;
    }

    public String getRangeName() {
        return rangeName;
    }
}
