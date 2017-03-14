package com.you.mm;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.model.ConflictAlgorithm;
import com.umeng.update.UmengUpdateAgent;
import com.you.mm.bean.Meizhi;
import com.you.mm.bean.data.MeizhiData;
import com.you.mm.bean.data.休息视频data;
import com.you.mm.bean.entity.Gank;
import com.you.mm.page.adapter.MeizhiListAdapter;
import com.you.mm.page.base.SwipeRefreshBaseActivity;
import com.you.mm.util.Dates;
import com.you.mm.util.Once;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;


/**
 * Created by ou on 2017/1/20.
 */

public class MainActivity extends SwipeRefreshBaseActivity
{
    private static final int PRELOAD_SIZE = 6;
    private boolean mIsFirstTimeTouchBottom = true;
    private int mPage = 1;
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
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mMeizhiList = new ArrayList<>();
        QueryBuilder query = new QueryBuilder(Meizhi.class);
        query.appendOrderDescBy("publishedAt");
        query.limit(0, 10);
        mMeizhiList.addAll(App.sDb.query(query));

        setUpRecycleView();
        setupUmeng();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        new Handler().postDelayed(() -> setRefresh(true), 358);
        loadData(true);
    }


    @Override
    public void requestDataRefresh()
    {
        super.requestDataRefresh();
        mPage = 1;
        loadData(true);
    }

    private void setupUmeng()
    {
        UmengUpdateAgent.update(this);
        UmengUpdateAgent.setDeltaUpdate(false);
        UmengUpdateAgent.setUpdateOnlyWifi(false);
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

        // @formatter:off
        Disposable disposable = Observable
                .zip(sGankIO.getMeizhiData(mPage),
                        sGankIO.get休息视频Data(mPage),
                        this::createMeizhiDataWith休息视频Desc)
                .map(meizhiData -> meizhiData.results)
                .doOnNext(unsortMeizhis ->
                {
                    Log.i("", "unsortMeizhis.size:" + unsortMeizhis.size());
                    List<Meizhi> sortedList = new ArrayList<>(unsortMeizhis.size());
                    Collections.copy(sortedList, unsortMeizhis);
                    Collections.sort(sortedList, (meizhi1, meizhi2) -> meizhi1.publishedAt.compareTo(meizhi2.publishedAt));
                    saveMeizhis(sortedList);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> setRefresh(false))
                .subscribe(meizhis -> {
                    if (clean) mMeizhiList.clear();
                    mMeizhiList.addAll(meizhis);
                    meizhiListAdapter.notifyDataSetChanged();
                    setRefresh(false);
                }, throwable -> loadError(throwable));

        // @formatter:on
        addDisposable(disposable);
    }

    private void loadError(Throwable throwable)
    {
        throwable.printStackTrace();
        Snackbar.make(mRecyclerView, R.string.snap_load_fail, Snackbar.LENGTH_LONG)
                .setAction(R.string.retry, view -> requestDataRefresh())
                .show();
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

    private void saveMeizhis(List<Meizhi> meizhis)
    {
        App.sDb.insert(meizhis, ConflictAlgorithm.Replace);
    }
}
