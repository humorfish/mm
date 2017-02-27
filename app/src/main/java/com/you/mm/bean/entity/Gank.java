package com.you.mm.bean.entity;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.Table;
import com.you.mm.bean.Soul;

import java.util.Date;

/**
 * Created by Administrator on 2017/2/27.
 */

@Table("ganks") public class Gank extends Soul
{

    @Column("url") public String url;
    @Column("type") public String type;
    @Column("desc") public String desc;
    @Column("who") public String who;
    @Column("used") public boolean used;
    @Column("createdAt") public Date createdAt;
    @Column("updatedAt") public Date updatedAt;
    @Column("publishedAt") public Date publishedAt;
}
