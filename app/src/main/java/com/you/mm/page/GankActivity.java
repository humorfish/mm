package com.you.mm.page;

import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.you.mm.R;
import com.you.mm.event.OnKeyBackClickEvent;
import com.you.mm.page.adapter.GankPagerAdapter;
import com.you.mm.page.base.ToolbarActivity;
import com.you.mm.util.Dates;
import com.you.mm.util.RxBus;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/3/27.
 */

public class GankActivity extends ToolbarActivity implements ViewPager.OnPageChangeListener
{
    private final String TAG  = GankActivity.class.getSimpleName();
    private Disposable mDisposable;

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
        initTabLayout();

        mDisposable = RxBus.getInstance().doSubscribe(OnKeyBackClickEvent.class, keyBackClickEvent ->
                Log.d(TAG, "onCreate: " + keyBackClickEvent.getName()), Throwable::printStackTrace);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        RxBus.getInstance().addSubscription(this, mDisposable);
    }

    @Override
    protected void onDestroy()
    {
        if (! RxBus.getInstance().hasObservers())
            RxBus.getInstance().unSubscribe(this);

        super.onDestroy();
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
        mGankPagerAdapter = new GankPagerAdapter(getSupportFragmentManager(), mGankDate);
        mViewPager.setAdapter(mGankPagerAdapter);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.addOnPageChangeListener(this);
    }

    private void initTabLayout()
    {
        for (int i=0; i < mGankPagerAdapter.getCount(); i++)
        {
            mTabLayout.addTab(mTabLayout.newTab());
        }

        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
            hideOrShowToolbar();
        else
            hideOrShowToolbar();
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void hideOrShowToolbar()
    {
        View toolbar = findViewById(R.id.toolbar_with_indicator);
        toolbar.animate()
                .translationY(mIsHiden ? 0 : -mToolbar.getHeight())
                .setInterpolator(new DecelerateInterpolator(2))
                .start();

        mIsHiden = !mIsHiden;
        if (mIsHiden)
        {
            mViewPager.setTag(mViewPager.getPaddingTop());
            mViewPager.setPadding(0, 0, 0, 0);
        }
        else
        {
            mViewPager.setPadding(0, (int) mViewPager.getTag(), 0, 0);
            mViewPager.setTag(null);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            {
                RxBus.getInstance().post(new OnKeyBackClickEvent());
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
    {

    }

    @Override
    public void onPageSelected(int position)
    {
        setTitle(Dates.toDate(mGankDate, -position));
    }

    @Override
    public void onPageScrollStateChanged(int state)
    {

    }
}
