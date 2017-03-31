package com.you.mm.util;

import android.content.Context;
import android.content.Intent;

import com.you.mm.R;

/**
 * Created by Administrator on 2017/3/31.
 */

public class Shares
{

    public static void share(Context context, int stringRes)
    {
        share(context, context.getString(stringRes));
    }

    public static void share(Context context, String shareText)
    {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.action_share));
        intent.putExtra(Intent.EXTRA_TEXT, shareText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(
                Intent.createChooser(intent, context.getString(R.string.action_share)));
    }
}
