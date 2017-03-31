package com.you.mm.page;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.you.mm.R;
import com.you.mm.bean.entity.Gank;
import com.you.mm.page.adapter.GankListAdapter;
import com.you.mm.util.Once;
import com.you.mm.util.Shares;
import com.you.mm.util.Toasts;
import com.you.mm.widget.LoveVideoView;
import com.you.mm.widget.VideoImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/3/28.
 */

public class GankFrament extends Fragment
{
    private final String TAG = "GankFragment";

    private static final String ARG_YEAR = "year";
    private static final String ARG_MONTH = "month";
    private static final String ARG_DAY = "day";

    @BindView(R.id.list)
    RecyclerView mRecyclerView;
    @BindView(R.id.stub_empty_view)
    ViewStub mEmptyViewStub;
    @BindView(R.id.stub_video_view)
    ViewStub mVideoViewStub;

    @BindView(R.id.video_image)
    VideoImageView mVideoImageView;

    LoveVideoView mVideoView;

    int mYear, mMonth, mDay;

    List<Gank> mGankList;
    GankListAdapter mAdapter;
    boolean mIsVideoViewInflated = false;
    String mVideoPreviewUrl;
    Disposable mDisposable;

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
        parseArguments();
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    private void parseArguments()
    {
        Bundle mBundle = getArguments();
        mYear = mBundle.getInt(ARG_YEAR);
        mMonth = mBundle.getInt(ARG_MONTH);
        mDay = mBundle.getInt(ARG_DAY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_gank, container, false);
        ButterKnife.bind(rootView);
        initRecyclerView();
        setVideoViewPosition(getResources().getConfiguration());
        return rootView;
    }



    private void setVideoViewPosition(Configuration configuration)
    {
        switch (configuration.orientation)
        {
            case Configuration.ORIENTATION_LANDSCAPE:
                if (mIsVideoViewInflated)
                {
                    mVideoViewStub.setVisibility(View.VISIBLE);
                }
                else
                {
                    mVideoView = (LoveVideoView) mVideoViewStub.inflate();
                    mIsVideoViewInflated = true;
                    String tip = getString(R.string.tip_video_play);
                    // @formatter:off
                    Once.show(getContext(), tip, () ->
                            Snackbar.make(mVideoView, tip, Snackbar.LENGTH_INDEFINITE)
                                    .setAction(R.string.i_know, v -> {})
                                    .show());
                }

                if (mGankList.size() > 0 && mGankList.get(0).type.equals("休息视频"))
                {
                    mVideoView.loadUrl(mGankList.get(0).url);
                }

                break;
            case Configuration.ORIENTATION_PORTRAIT:
            case Configuration.ORIENTATION_UNDEFINED:
            default:
                mVideoViewStub.setVisibility(View.GONE);
                break;
        }
    }

    void closePlayer()
    {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toasts.showShort(getString(R.string.tip_for_no_gank));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        setVideoViewPosition(newConfig);
        super.onConfigurationChanged(newConfig);
    }

    private void initRecyclerView()
    {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.action_share:
                if (mGankList.size() != 0)
                {
                    Gank gank = mGankList.get(0);
                    String shareText = gank.desc + gank.url + getString(R.string.share_from);
                    Shares.share(getActivity(), shareText);
                }
                else
                {
                    Shares.share(getContext(), R.string.share_text);
                }
                return true;

            case R.id.action_subject:
                //openTodaySubject();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    private void openTodaySubject()
//    {
//        String url = getString(R.string.url_gank_io) +
//                String.format("%s/%s/%s", mYear, mMonth, mDay);
//        Intent intent = WebActivity.newIntent(getActivity(), url,
//                getString(R.string.action_subject));
//        startActivity(intent);
//    }
}
