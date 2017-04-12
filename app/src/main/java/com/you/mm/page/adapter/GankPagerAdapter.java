package com.you.mm.page.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.you.mm.bean.DrakeetFactory;
import com.you.mm.page.GankFragment;

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

        return GankFragment.newInstance(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public int getCount()
    {
        return DrakeetFactory.gankSize;
    }
}
