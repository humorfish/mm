package com.you.mm.page;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.you.mm.R;
import com.you.mm.page.adapter.GankPagerAdapter;
import com.you.mm.page.base.ToolbarActivity;
import com.you.mm.util.Dates;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/3/27.
 */

public class GankAcitity extends ToolbarActivity implements ViewPager.OnPageChangeListener
{
    public static final String EXTRA_GANK_DATE = "gank_date";

    @BindView(R.id.pager)
    ViewPager mViewPager;

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;

    Date mGankDate;

    GankPagerAdapter mGankPagerAdapter;

    @Override
    protected int provideContentViewId()
    {
        return R.layout.activity_gank;
    }

    @Override
    public boolean canBack()
    {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mGankDate = (Date)getIntent().getSerializableExtra(EXTRA_GANK_DATE);
        setTitle(Dates.toDate(mGankDate));
        initViewPager();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_gank, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        return super.onOptionsItemSelected(item);
    }

    private void initViewPager()
    {

        mViewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
    {

    }

    @Override
    public void onPageSelected(int position)
    {

    }

    @Override
    public void onPageScrollStateChanged(int state)
    {

    }
}
