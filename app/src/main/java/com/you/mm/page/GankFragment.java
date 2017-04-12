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

import com.bumptech.glide.Glide;
import com.you.mm.R;
import com.you.mm.bean.DrakeetFactory;
import com.you.mm.bean.entity.Gank;
import com.you.mm.bean.entity.GankData;
import com.you.mm.page.adapter.GankListAdapter;
import com.you.mm.page.base.BaseActivity;
import com.you.mm.util.LoveStrings;
import com.you.mm.util.Once;
import com.you.mm.util.Shares;
import com.you.mm.util.Toasts;
import com.you.mm.widget.LoveVideoView;
import com.you.mm.widget.VideoImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/3/28.
 */

public class GankFragment extends Fragment
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

    public static GankFragment newInstance(int year, int month, int day)
    {
        GankFragment fragment = new GankFragment();
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
        ButterKnife.bind(this, rootView);
        initRecyclerView();
        setVideoViewPosition(getResources().getConfiguration());
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        if (mGankList.size() == 0)
            loadData();

        if (mVideoPreviewUrl != null)
            Glide.with(this).load(mVideoPreviewUrl).into(mVideoImageView);
    }

    private void loadData()
    {
        loadVideoPreview();
        mDisposable = BaseActivity.sGankIO
                .getGankData(mYear, mMonth, mDay)
                .map(data -> data.results)
                .map(this::addAllResults)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(gankList ->
                {
                    if (gankList.isEmpty())
                    {
                        showEmptyView();
                    }
                    else
                    {
                        mAdapter.notifyDataSetChanged();
                    }
                }, Throwable::printStackTrace);
    }

    private List<Gank> addAllResults(GankData.Result results) {
        if (results.androidList != null) mGankList.addAll(results.androidList);
        if (results.iOSList != null) mGankList.addAll(results.iOSList);
        if (results.appList != null) mGankList.addAll(results.appList);
        if (results.拓展资源List != null) mGankList.addAll(results.拓展资源List);
        if (results.瞎推荐List != null) mGankList.addAll(results.瞎推荐List);
        if (results.休息视频List != null) mGankList.addAll(0, results.休息视频List);
        return mGankList;
    }

    private void loadVideoPreview()
    {
        String where  = String.format("{\"tag\":\"%d-%d-%d\"}", mYear, mMonth, mDay);

        DrakeetFactory.getDrakeetSingleton()
                .getDGankData(where)
                .map(dGankData -> dGankData.results)
                .filter(dGanks -> dGanks.size() > 0)
                .map(dGanks -> dGanks.get(0))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dGank -> startPreview(dGank.preview),
                        throwable -> getOldVideoPreview(new OkHttpClient()));
    }

    private void startPreview(String preview)
    {
        mVideoPreviewUrl = preview;
        if(preview != null && mVideoImageView != null)
        {
            mVideoView.post(() ->
                    Glide.with(mVideoImageView.getContext()).load(preview).into(mVideoImageView));
        }
    }

    private void getOldVideoPreview(OkHttpClient client)
    {
        String httpUrl = "http://gank.io/" + String.format("%d%d%d", mYear, mMonth, mDay);
        Request mRequest = new Request.Builder().url(httpUrl).build();
        client.newCall(mRequest).enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                String body = response.body().string();
                mVideoPreviewUrl = LoveStrings.getVideoPreviewImageUrl(body);
                startPreview(mVideoPreviewUrl);
            }
        });
    }

    private void showEmptyView() {mEmptyViewStub.inflate();}

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
