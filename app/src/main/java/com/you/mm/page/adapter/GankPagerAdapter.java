package com.you.mm.page.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017/3/28.
 */

public class GankPagerAdapter extends FragmentPagerAdapter
{
    Date mDate;

    public GankPagerAdapter(FragmentManager fm, Date mDate)
    {
        super(fm);
        this.mDate = mDate;
    }

    @Override
    public Fragment getItem(int position)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        calendar.add(Calendar.DATE, -position);

        return GankFrament;
    }

    @Override
    public int getCount()
    {
        return 0;
    }
}
