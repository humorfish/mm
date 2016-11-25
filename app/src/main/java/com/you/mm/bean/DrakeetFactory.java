package com.you.mm.bean;

/**
 * Created by you on 2016/11/24.
 */
public class DrakeetFactory {
    static GankApi sGankIOSingleton = null;
    static DrakeetApi sDrakeetSingleton = null;
    public static final int meiziSize = 10;
    public static final int gankSize = 5;


    public static GankApi getsGankIOSingleton(){
        return sGankIOSingleton;
    }

    public static DrakeetApi getsDrakeetSingleton() {
        return sDrakeetSingleton;
    }
}
