package com.you.mm;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;

import com.litesuits.orm.db.assit.QueryBuilder;
import com.you.mm.bean.Meizhi;
import com.you.mm.bean.data.MeizhiData;
import com.you.mm.bean.data.休息视频data;
import com.you.mm.bean.entity.Gank;
import com.you.mm.page.adapter.MeizhiListAdapter;
import com.you.mm.page.base.SwipeRefreshBaseActivity;
import com.you.mm.util.Dates;
import com.you.mm.util.Once;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Flowable;


/**
 * Created by ou on 2017/1/20.
 */

public class MainActivity extends SwipeRefreshBaseActivity
{
    private static final int PRELOAD_SIZE = 6;
    private boolean mIsFirstTimeTouchBottom = true;
    private int mPage = 1;
    private boolean mMeizhiBeTouched;
    private int mLastVideoIndex = 0;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.rv_meizhi)
    RecyclerView mRecyclerView;

    private List<Meizhi> mMeizhiList;
    private MeizhiListAdapter meizhiListAdapter;

    @Override
    protected int provideContentViewId()
    {
        return R.layout.activity_main;
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState)
    {
        super.onCreate(savedInstanceState, persistentState);
        mMeizhiList = new ArrayList<>();

        QueryBuilder query = new QueryBuilder(Meizhi.class);
        query.appendOrderDescBy("publishedAt");
        query.limit(0, 10);

        mMeizhiList.addAll(App.sDb.query(query));
        setUpRecycleView();
    }

    private void setUpRecycleView()
    {
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        meizhiListAdapter = new MeizhiListAdapter(this, mMeizhiList);
        mRecyclerView.setAdapter(meizhiListAdapter);

        Once.show(this, "tip_guide_6", () ->
                Snackbar.make(mRecyclerView, getString(R.string.tip_guide), Snackbar.LENGTH_SHORT).setAction(R.string.i_know, view ->
                {} ).show());

        mRecyclerView.addOnScrollListener(getOnBottomListener(layoutManager));
    }

    public RecyclerView.OnScrollListener getOnBottomListener(StaggeredGridLayoutManager layoutManager)
    {
        return new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                boolean isBottom = layoutManager.findLastCompletelyVisibleItemPositions(new int[2])[1] >= meizhiListAdapter.getItemCount() - PRELOAD_SIZE;
                if (! mSwipeRefreshLayout.isRefreshing() && isBottom)
                {
                    if (! mIsFirstTimeTouchBottom)
                    {
                        mSwipeRefreshLayout.setRefreshing(true);
                        mPage +=1;
                        loadData(false);
                    } else
                    {
                        mIsFirstTimeTouchBottom = false;
                    }
                }
            }
        };
    }

    /**
     * 获取服务数据
     *
     * @param clean 清除来自数据库缓存或者已有数据。
     */
    private void loadData(boolean clean)
    {
        mLastVideoIndex = 0;
        Flowable.zip(sGankIO.getMeizhiData(mPage),
                sGankIO.get休息视频Data(mPage),
                ).subscribe();
    }

    private MeizhiData createMeizhiDataWith休息视频Desc(MeizhiData data, 休息视频data love)
    {
        for (Meizhi meizhi : data.results)
        {
            meizhi.desc = meizhi.desc + " " +
                    getFirstVideoDesc(meizhi.publishedAt, love.results);
        }
        return data;
    }

    private String getFirstVideoDesc(Date publishedAt, List<Gank> results)
    {
        String videoDesc = "";

        for (int i= mLastVideoIndex; i < results.size(); i++)
        {
            Gank mGank = results.get(i);
            if (mGank.publishedAt == null)
            {
                mGank.publishedAt = mGank.createdAt;
            }

            if (Dates.isTheSameDay(publishedAt, mGank.publishedAt))
            {
                videoDesc = mGank.desc;
                mLastVideoIndex = i;
                break;
            }
        }

        return videoDesc;
    }
}
