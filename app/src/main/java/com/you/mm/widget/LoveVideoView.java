package com.you.mm.widget;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Base64;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.InputStream;

/**
 * Created by Administrator on 2017/3/31.
 */

public class LoveVideoView extends WebView
{
    private final Context mContext;

    public LoveVideoView(Context context)
    {
        this(context, null);
    }

    public LoveVideoView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public LoveVideoView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init()
    {
        setWebViewClient(new LoveClient());
    }

    private class LoveClient extends WebViewClient
    {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request)
        {
            view.loadUrl(request.getUrl().toString());
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url)
        {
            super.onPageFinished(view, url);
            // 这些视频需要hack CSS才能达到全屏播放的效果
            if (url.contains("www.vmovier.com")) {
                injectCSS("vmovier.css");
            } else if (url.contains("video.weibo.com")) {
                injectCSS("weibo.css");
            } else if (url.contains("m.miaopai.com")) {
                injectCSS("miaopai.css");
            }
        }

        // Inject CSS method: read style.css from assets folder
        // Append stylesheet to document head
        private void injectCSS(String filename)
        {
            try
            {
                InputStream inputStream = mContext.getAssets().open(filename);
                byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);
                inputStream.close();
                String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
                loadUrl("javascript:(function() {" +
                        "var parent = document.getElementsByTagName('head').item(0);" +
                        "var style = document.createElement('style');" +
                        "style.type = 'text/css';" +
                        // Tell the browser to BASE64-decode the string into your script !!!
                        "style.innerHTML = window.atob('" + encoded + "');" +
                        "parent.appendChild(style)" +
                        "})()");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private class Chrome extends WebChromeClient implements MediaPlayer.OnCompletionListener
    {

        @Override public void onCompletion(MediaPlayer player)
        {
            if (player != null)
            {
                if (player.isPlaying())
                    player.stop();

                player.reset();
                player.release();
            }
        }
    }

}
