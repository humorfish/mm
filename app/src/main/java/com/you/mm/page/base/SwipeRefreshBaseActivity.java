package com.you.mm.page.base;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.widget.SwipeRefreshLayout;

import com.you.mm.R;
import com.you.mm.widget.MultiSwipeRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/23.
 */

public abstract class SwipeRefreshBaseActivity extends ToolbarActivity implements SwipeRefreshLayer
{

    @BindView(R.id.swipe_refresh_layout)
    public MultiSwipeRefreshLayout mSwipeRefreshLayout;

    private boolean mIsRequestDataRefresh = false;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState)
    {
        super.onCreate(savedInstanceState, persistentState);
        ButterKnife.bind(this);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState)
    {
        super.onPostCreate(savedInstanceState, persistentState);
        trySetupSwipeRefresh();
    }

    void trySetupSwipeRefresh()
    {
        if (mSwipeRefreshLayout != null)
        {
            mSwipeRefreshLayout.setColorSchemeResources(R.color.refresh_progress_3,
                    R.color.refresh_progress_2, R.color.refresh_progress_1);

            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
            {
                @Override
                public void onRefresh()
                {
                    requestDataRefresh();
                }
            });
        }
    }

    @Override
    public void requestDataRefresh()
    {
        mIsRequestDataRefresh = true;
    }

    @Override
    public void setRefresh(boolean refresh)
    {
        if (mSwipeRefreshLayout == null)
        {
            return;
        }

        if (! refresh)
        {
            mIsRequestDataRefresh = false;

            // 防止刷新消失太快，让子弹飞一会儿.
            mSwipeRefreshLayout.postDelayed(() ->
                    mSwipeRefreshLayout.setRefreshing(false), 1000);
        } else
        {
            mSwipeRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void setProgressViewOffset(boolean scale, int start, int end)
    {
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setProgressViewOffset(scale, start, end);
    }

    @Override
    public void setCanChildScrollUpCallback(MultiSwipeRefreshLayout.CanChildScrollUpCallback callBack)
    {
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setCanChildScrollUpCallback(callBack);
    }

    public boolean isRequestDataRefresh()
    {
        return mIsRequestDataRefresh;
    }
}
