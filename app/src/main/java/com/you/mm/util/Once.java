package com.you.mm.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by you on 2016/11/23.
 */
public class Once
{
    private final static String SHARE_NAME = "once";
    public final static String SHARE_KEY_SUGGEST = "suggest";
    public final static String KEY_LOGIN = "login";

    public static void show(Context context, String type, OnceCallback dosomething)
    {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
        if (!mSharedPreferences.getBoolean(type, false))
        {
            dosomething.onOnce();
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean(type, true);
            editor.apply();
        }
    }

    public interface OnceCallback {
        void onOnce();
    }
}
