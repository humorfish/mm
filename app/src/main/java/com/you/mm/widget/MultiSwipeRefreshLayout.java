package com.you.mm.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

import com.you.mm.R;

/**
 * Created by Administrator on 2016/11/11.
 */
public class MultiSwipeRefreshLayout extends SwipeRefreshLayout {
    private Drawable mForegroundDrawable;
    CanChildScrollUpCallback mCallback;

    public MultiSwipeRefreshLayout(Context context) {
        this(context, null);
    }

    public MultiSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MultiSwipeRefreshLayout, 0, 0);
        mForegroundDrawable = typedArray.getDrawable(R.styleable.MultiSwipeRefreshLayout_foreground);
        if (mForegroundDrawable != null) {
            mForegroundDrawable.setCallback(this);
            setWillNotDraw(false);
        }
        typedArray.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mForegroundDrawable != null)
            mForegroundDrawable.setBounds(w, h, oldw, oldh);
    }

    public interface CanChildScrollUpCallback{
        boolean canSwipeRefreshChildScrollUp();
    }

    public void setmCallback(CanChildScrollUpCallback mCallback) {
        this.mCallback = mCallback;
    }

    @Override public boolean canChildScrollUp() {
        if (mCallback != null) {
            return mCallback.canSwipeRefreshChildScrollUp();
        }
        return super.canChildScrollUp();
    }
}
