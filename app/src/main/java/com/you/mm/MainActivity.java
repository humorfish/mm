package com.you.mm;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.litesuits.orm.db.assit.QueryBuilder;
import com.you.mm.bean.DrakeetFactory;
import com.you.mm.bean.GankApi;
import com.you.mm.bean.Meizhi;
import com.you.mm.model.SwipeRefreshLayer;
import com.you.mm.page.adapter.MmListAdapter;
import com.you.mm.util.Once;
import com.you.mm.widget.MultiSwipeRefreshLayout;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AutoLayoutActivity implements SwipeRefreshLayer{
    public static final GankApi sGankIO = DrakeetFactory.getsGankIOSingleton();

    @BindView(R.id.swipe_refresh_layout)
    MultiSwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.rv_meizhi)
    RecyclerView mRecyclerView;
    MmListAdapter mmListAdapter;

    private int PRELOAD_SIZE = 6;
    private List<Meizhi> mMeizhiList;
    private boolean mIsRequestDataRefresh = false;
    private boolean mIsFirstTimeTouchBottom = true;
    private int mPage = 1;
    private int mLastVideoIndex = 0;
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

    private void initView() {
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        mmListAdapter = new MmListAdapter(this, mMeizhiList);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mmListAdapter);
        Once.show(this, Once.SHARE_KEY_SUGGEST,
                () -> Snackbar.make(mRecyclerView, getString(R.string.tip_guide), Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.i_know, v -> {})
                .show());
        mRecyclerView.addOnScrollListener(getOnBottomListener(layoutManager));
        mmListAdapter.setmOnMeizhiTouchListener((v, meizhiView, card, meizhi) -> {

        });
    }

    RecyclerView.OnScrollListener getOnBottomListener(StaggeredGridLayoutManager layoutManager) {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView rv, int dx, int dy) {
                boolean isBottom = layoutManager.findLastCompletelyVisibleItemPositions(new int[2])[1] >=
                                mmListAdapter.getItemCount() - PRELOAD_SIZE;
                if (!mSwipeRefreshLayout.isRefreshing() && isBottom) {
                    if (!mIsFirstTimeTouchBottom) {
                        mSwipeRefreshLayout.setRefreshing(true);
                        mPage += 1;
                        //loadData();
                    } else {
                        mIsFirstTimeTouchBottom = false;
                    }
                }
            }
        };
    }

//    /**
//     * 获取服务数据
//     *
//     * @param clean 清除来自数据库缓存或者已有数据。
//     */
//    private void loadData(boolean clean) {
//        mLastVideoIndex = 0;
//        // @formatter:off
//        Subscription s = Observable
//                .zip(sGankIO.getMeizhiData(mPage),
//                        sGankIO.get休息视频Data(mPage),
//                        this::createMeizhiDataWith休息视频Desc)
//                .map(meizhiData -> meizhiData.results)
//                .flatMap(Observable::from)
//                .toSortedList((meizhi1, meizhi2) ->
//                        meizhi2.publishedAt.compareTo(meizhi1.publishedAt))
//                .doOnNext(this::saveMeizhis)
//                .observeOn(AndroidSchedulers.mainThread())
//                .finallyDo(() -> setRefresh(false))
//                .subscribe(meizhis -> {
//                    if (clean) mMeizhiList.clear();
//                    mMeizhiList.addAll(meizhis);
//                    mMeizhiListAdapter.notifyDataSetChanged();
//                    setRefresh(false);
//                }, throwable -> loadError(throwable));
//        // @formatter:on
//        addSubscription(s);
//    }

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
