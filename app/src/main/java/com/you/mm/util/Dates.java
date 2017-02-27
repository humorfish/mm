package com.you.mm.util;

import java.util.Calendar;
import java.util.Date;


/**
 * Created by Administrator on 2017/2/27.
 */

public class Dates
{
    public static boolean isTheSameDay(Date one, Date another)
    {
        Calendar _one = Calendar.getInstance();
        _one.setTime(one);

        Calendar _another = Calendar.getInstance();
        _another.setTime(another);

        int oneDay = _one.get(Calendar.DAY_OF_YEAR);
        int anotherDay = _another.get(Calendar.DAY_OF_YEAR);

        return oneDay == anotherDay;
    }
}
