package com.you.mm;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.litesuits.orm.db.assit.QueryBuilder;
import com.you.mm.bean.Meizhi;
import com.you.mm.model.SwipeRefreshLayer;
import com.you.mm.page.adapter.MmListAdapter;
import com.you.mm.widget.MultiSwipeRefreshLayout;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AutoLayoutActivity implements SwipeRefreshLayer{
    @BindView(R.id.swipe_refresh_layout)
    MultiSwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.rv_meizhi)
    RecyclerView recyclerView;
    MmListAdapter mmListAdapter;
    private List<Meizhi> mMeizhiList;
    private boolean mIsRequestDataRefresh = false;
    private boolean mIsFirstTimeTouchBottom = true;
    private int mPage = 1;
    private boolean mMeizhiBeTouched;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mMeizhiList = new ArrayList<>();
        QueryBuilder query = new QueryBuilder(Meizhi.class);
        query.limit(0, 10);
        mMeizhiList.addAll(App.sDb.query(query));

        initView();
    }

    private void initView()
    {
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        mmListAdapter = new MmListAdapter(this, mMeizhiList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mmListAdapter);
        mmListAdapter.setmOnMeizhiTouchListener(new MmListAdapter.OnMeizhiTouchListener() {
            @Override
            public void onTouch(View v, View meizhiView, View card, Meizhi meizhi) {

            }
        });
    }

    @Override
    public void requestDataRefresh() {

    }

    @Override
    public void setRefresh(boolean refresh) {

    }

    @Override
    public void setProgressViewOffset(boolean scale, int start, int end) {

    }

    @Override
    public void setCanChildScrollUpCallback(MultiSwipeRefreshLayout.CanChildScrollUpCallback callback) {

    }
}
