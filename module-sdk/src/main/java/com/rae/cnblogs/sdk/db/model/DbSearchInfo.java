package com.rae.cnblogs.sdk.db.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by rae on 2018/7/26.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
@Entity(nameInDb = "search_history")
public class DbSearchInfo {
    /**
     * 自增ID
     */
    @Id(autoincrement = true)
    private Long id;

    private String keyword;

    private String type;

    private Long createAt;

    @Generated(hash = 884501561)
    public DbSearchInfo(Long id, String keyword, String type, Long createAt) {
        this.id = id;
        this.keyword = keyword;
        this.type = type;
        this.createAt = createAt;
    }

    @Generated(hash = 768331496)
    public DbSearchInfo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Long createAt) {
        this.createAt = createAt;
    }
}
