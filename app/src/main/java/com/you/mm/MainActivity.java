package com.you.mm;

import android.os.Bundle;

import com.you.mm.model.MultiSwipeRefreshLayout;
import com.you.mm.model.SwipeRefreshLayer;
import com.zhy.autolayout.AutoLayoutActivity;

import butterknife.ButterKnife;

public class MainActivity extends AutoLayoutActivity implements SwipeRefreshLayer{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        QueryBuilder query = new QueryBuilder(
                .class);
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
