package com.example.IKEA.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Marvin on 2017/4/28.
 */

public class Type extends DataSupport {
    private String typeName;
    private long id;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }
}
