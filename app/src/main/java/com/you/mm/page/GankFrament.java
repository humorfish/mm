package com.you.mm.page;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.you.mm.bean.entity.Gank;
import com.you.mm.page.adapter.GankListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/28.
 */

public class GankFrament extends Fragment
{
    private final String TAG = "GankFragment";

    private static final String ARG_YEAR = "year";
    private static final String ARG_MONTH = "month";
    private static final String ARG_DAY = "day";

    List<Gank> mGankList;
    GankListAdapter mAdapter;

    public void GankFragment()
    {
    }

    public static GankFrament newInstance(int year, int month, int day)
    {
        GankFrament fragment = new GankFrament();
        Bundle args = new Bundle();
        args.putInt(ARG_YEAR, year);
        args.putInt(ARG_MONTH, month);
        args.putInt(ARG_DAY, day);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mGankList = new ArrayList<>();
        mAdapter = new GankListAdapter(mGankList);
    }
}
