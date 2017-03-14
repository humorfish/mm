package com.you.mm.page.base;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.you.mm.R;
import com.you.mm.bean.DrakeetFactory;
import com.you.mm.bean.GankApi;
import com.you.mm.page.AboutActivity;
import com.you.mm.util.Once;
import com.you.mm.util.Toasts;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


/**
 * Created by you on 2017/2/18.
 */

public class BaseActivity extends AppCompatActivity
{
    public static final GankApi sGankIO = DrakeetFactory.getsGankIOSingleton();
    private CompositeDisposable mCompositeSubscription;

    public CompositeDisposable getCompositeDisposable()
    {
        if (mCompositeSubscription == null)
        {
            mCompositeSubscription = new CompositeDisposable();
        }

        return mCompositeSubscription;
    }

    public void addDisposable(Disposable disposable)
    {
        getCompositeDisposable().add(disposable);
    }

    public void dispose(Disposable disposable)
    {
        if (mCompositeSubscription != null)
        {
            mCompositeSubscription.delete(disposable);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;

            case R.id.action_login:
                loginGitHub();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void loginGitHub()
    {
        Once.show(this, Once.KEY_LOGIN, () ->
                Toasts.showLongX2(getString(R.string.tip_login_github)));

        /*????*/
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (mCompositeSubscription != null)
            mCompositeSubscription.clear();
    }
}
