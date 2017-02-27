package com.you.mm.page.base;

import com.you.mm.widget.MultiSwipeRefreshLayout;

/**
 * Created by Administrator on 2017/2/23.
 */

public interface SwipeRefreshLayer
{
    void requestDataRefresh();
    void setRefresh(boolean refresh);
    void setProgressViewOffset(boolean scale, int start, int end);
    void setCanChildScrollUpCallback(MultiSwipeRefreshLayout.CanChildScrollUpCallback callBack);
}