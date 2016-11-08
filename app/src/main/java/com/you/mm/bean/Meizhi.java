package com.you.mm.bean;

/**
 * Created by Administrator on 2016/11/7.
 */

import java.util.Date;

/**
 * Created by drakeet on 6/20/15.
 */
@Table("meizhis") public class Meizhi extends Soul {

    @Column("url") public String url;
    @Column("type") public String type;
    @Column("desc") public String desc;
    @Column("who") public String who;
    @Column("used") public boolean used;
    @Column("createdAt") public Date createdAt;
    @Column("updatedAt") public Date updatedAt;
    @Column("publishedAt") public Date publishedAt;
    @Column("imageWidth") public int imageWidth;
    @Column("imageHeight") public int imageHeight;
}
