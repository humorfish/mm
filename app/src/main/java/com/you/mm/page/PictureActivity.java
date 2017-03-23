package com.you.mm.page;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.you.mm.R;
import com.you.mm.page.base.ToolbarActivity;
import com.you.mm.util.RxMeizhi;
import com.you.mm.util.Toasts;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Administrator on 2017/3/22.
 */

public class PictureActivity extends ToolbarActivity
{
    public static final String EXTRA_IMAGE_URL = "image_url";
    public static final String EXTRA_IMAGE_TITLE = "image_title";
    public static final String TRANSIT_PIC = "picture";

    String mImageUrl, mImageTitle;
    PhotoViewAttacher mPhotoViewAttacher;

    @BindView(R.id.picture)
    ImageView mImageView;

    @Override
    protected int provideContentViewId()
    {
        return R.layout.activity_picture;
    }

    @Override
    public boolean canBack()
    {
        return true;
    }

    private void parseIntent()
    {
        mImageUrl = getIntent().getStringExtra(PictureActivity.EXTRA_IMAGE_URL);
        mImageTitle = getIntent().getStringExtra(PictureActivity.EXTRA_IMAGE_TITLE);
    }

    public static Intent newIntent(Context context, String url, String desc)
    {
        Intent intent = new Intent(context, PictureActivity.class);
        intent.putExtra(PictureActivity.EXTRA_IMAGE_URL, url);
        intent.putExtra(PictureActivity.EXTRA_IMAGE_TITLE, desc);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        parseIntent();
        ViewCompat.setTransitionName(mImageView, TRANSIT_PIC);
        Picasso.with(this).load(mImageUrl).into(mImageView);
        setAppBarAlpha(0.7f);
        setTitle(mImageTitle);
        setupPhotoAttacher();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_picture, menu);
        // TODO: 把图片的一些信息，比如 who，加载到 Overflow 当中
        return true;
    }


    @Override public void onResume()
    {
        super.onResume();
    }


    @Override public void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mPhotoViewAttacher.cleanup();
    }

    private void setupPhotoAttacher()
    {
        mPhotoViewAttacher = new PhotoViewAttacher(mImageView);
        mPhotoViewAttacher.setOnViewTapListener((view, x, y) -> hideOrShowToolbar());
        mPhotoViewAttacher.setOnLongClickListener(view ->
        {
            new AlertDialog.Builder(PictureActivity.this)
                    .setMessage(getString(R.string.ask_saving_picture))
                    .setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss())
                    .setPositiveButton(android.R.string.ok, (dialogInterface, i) ->
                    {
                        saveImageToGallery();
                        dialogInterface.dismiss();
                    })
                    .show();
            return true;
        });
    }

    private void saveImageToGallery()
    {
        // @formatter:off
        Disposable mDisposable = RxMeizhi.saveImageAndGetPathObservable(PictureActivity.this, mImageUrl, mImageTitle)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(uri ->
                {
                    File appDir = new File(Environment.getExternalStorageDirectory(), "Meizhi");
                    String msg = String.format(getString(R.string.picture_has_save_to), appDir.getAbsolutePath());
                    Toasts.showShort(msg);
                }, err ->
                {
                    Toasts.showLong(err.getMessage() + "\n再试试...");
                });

        addDisposable(mDisposable);
        // @formatter:on
    }


}
