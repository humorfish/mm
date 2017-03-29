package com.you.mm.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Created by Administrator on 2017/3/30.
 */

public class VideoImageView extends ImageView implements Animator.AnimatorListener
{
    private boolean scale = false;

    public VideoImageView(Context context)
    {
        this(context, null);
    }

    public VideoImageView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public VideoImageView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        nextAnimation();
    }

    private void nextAnimation()
    {
        AnimatorSet animatorSet = new AnimatorSet();

        if (scale)
        {
            animatorSet.playTogether(ObjectAnimator.ofFloat(this, "scalex", 1.5f, 1f),
                    ObjectAnimator.ofFloat(this, "scaley", 1.5f, 1f));
        }
        else
        {
            animatorSet.playTogether(ObjectAnimator.ofFloat(this, "scaleX", 1, 1.5f),
                    ObjectAnimator.ofFloat(this, "scaleY", 1, 1.5f));
        }

        animatorSet.setDuration(10987);
        animatorSet.addListener(this);
        animatorSet.start();

        scale = !scale;
    }

    @Override
    public void onAnimationStart(Animator animation)
    {

    }

    @Override
    public void onAnimationEnd(Animator animation)
    {
        nextAnimation();
    }

    @Override
    public void onAnimationCancel(Animator animation)
    {

    }

    @Override
    public void onAnimationRepeat(Animator animation)
    {

    }
}
