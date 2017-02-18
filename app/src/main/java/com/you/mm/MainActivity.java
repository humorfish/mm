package com.you.mm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ou on 2017/1/20.
 */

public class MainActivity extends AppCompatActivity
{
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide);
        ButterKnife.bind(this);
        initView();
    }

    private void initView()
    {
        mToolbar.setTitle("Rocko");

        /* 菜单的监听可以在toolbar里设置，也可以像ActionBar那样，通过Activity的onOptionsItemSelected回调方法来处理 */
        mToolbar.setOnMenuItemClickListener(item ->
        {
            switch (item.getItemId())
            {
                case R.id.action_settings:
                    Toast.makeText(MainActivity.this, "action_settings", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.action_share:
                    Toast.makeText(MainActivity.this, "action_share", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }

            return true;
        });
    }

    private void setIconsVisible(Menu menu, boolean flag)
    {
        //判断menu是否为空
        if (menu != null)
        {
            try
            {
                //如果不为空,就反射拿到menu的setOptionalIconsVisible方法
                Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                //暴力访问该方法
                method.setAccessible(true);
                //调用该方法显示icon
                method.invoke(menu, flag);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        setIconsVisible(menu, true);

        getMenuInflater().inflate(R.menu.main, menu);

        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menu.findItem(R.id.action_share));

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/*");
        mShareActionProvider.setShareIntent(intent);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        return super.onOptionsItemSelected(item);
    }
}
