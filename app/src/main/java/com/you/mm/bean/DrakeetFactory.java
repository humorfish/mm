package com.you.mm.bean;

/**
 * Created by you on 2016/11/24.
 */
public class DrakeetFactory
{
    protected static final Object monitor = new Object();

    public static final int meizhiSize = 10;
    public static final int gankSize = 5;
    static GankApi sGankIOSingleton = null;
    static DrakeetApi sDrakeetSingleton = null;

    public static boolean isDebug = true;

    public static GankApi getsGankIOSingleton()
    {
        synchronized (monitor)
        {
            if (sGankIOSingleton ==  null)
            {
                sGankIOSingleton = new DrakeetRetrofit().getGankService();
            }

            return sGankIOSingleton;
        }
    }

    public static DrakeetApi getsDrakeetSingleton()
    {
        synchronized (monitor)
        {
            if (sDrakeetSingleton ==  null)
            {
                sDrakeetSingleton = new DrakeetRetrofit().getDrakeetService();
            }

            return sDrakeetSingleton;
        }
    }
}
