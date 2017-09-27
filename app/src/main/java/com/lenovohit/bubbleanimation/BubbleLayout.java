package com.lenovohit.bubbleanimation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Random;

/**
 * 气泡效果
 * Created by yuzhijun on 2017/9/26.
 */
public class BubbleLayout extends RelativeLayout {
    private Drawable redBubble,yellowBubble,blueBubble;
    private Drawable[] mDrawables;
    private Interpolator[] mInterpolators;
    private LayoutParams mParams;
    private int bubbleWidth,bubbleHeight;
    private int width,height;
    private Random mRandom = new Random();

    public BubbleLayout(Context context) {
        super(context);
        init();
    }

    public BubbleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BubbleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initDrawable();
        initInterpolator();

        mParams = new LayoutParams(bubbleWidth,bubbleHeight);
        mParams.addRule(CENTER_HORIZONTAL,TRUE);
        mParams.addRule(ALIGN_PARENT_BOTTOM,TRUE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    private void initDrawable() {
        redBubble = getResources().getDrawable(R.mipmap.red);
        blueBubble = getResources().getDrawable(R.mipmap.blue);
        yellowBubble = getResources().getDrawable(R.mipmap.yellow);

        mDrawables = new Drawable[3];
        mDrawables[0] = redBubble;
        mDrawables[1] = blueBubble;
        mDrawables[2] = yellowBubble;

        bubbleWidth = redBubble.getIntrinsicWidth();
        bubbleHeight = redBubble.getIntrinsicHeight();
    }

    private void initInterpolator() {
        mInterpolators = new Interpolator[4];
        mInterpolators[0] = new AccelerateDecelerateInterpolator();
        mInterpolators[1] = new AccelerateInterpolator();
        mInterpolators[2] = new DecelerateInterpolator();
        mInterpolators[3] = new LinearInterpolator();
    }

    public void addBubbles() {
        ImageView bubble = new ImageView(getContext());
        bubble.setImageDrawable(mDrawables[mRandom.nextInt(mDrawables.length)]);
        bubble.setLayoutParams(mParams);
        addView(bubble);

        startAnimation(bubble);
    }

    private void startAnimation(final ImageView bubble) {
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(bubble,"alpha",0.2f,1f);
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(bubble,"scaleX",0.2f,1f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(bubble,"scaleY",0.2f,1f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                startBezierAnimation(bubble);
            }
        });
        animatorSet.setDuration(300);
        animatorSet.playTogether(alphaAnimator,scaleXAnimator,scaleYAnimator);
        animatorSet.start();
    }

    private void startBezierAnimation(final ImageView bubble) {
        //两个控制点随意生成
        PointF B = new PointF(mRandom.nextInt(width/2) + width/4,mRandom.nextInt(height/2) + height/2);
        PointF C = new PointF(mRandom.nextInt(width/2) + width/4,mRandom.nextInt(height/2));
        //起始点在这个布局的顶部中心点,结束节点在顶部x为任意，y这里设置0高度
        PointF A = new PointF((width - bubbleWidth)/2,height - bubbleHeight);
        PointF D = new PointF(mRandom.nextInt(width),0);

        BezierEvaluator bezierEvaluator = new BezierEvaluator(B,C);
        ValueAnimator valueAnimator = ValueAnimator.ofObject(bezierEvaluator,A,D);
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                removeView(bubble);
            }
        });
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                PointF currentPoint = (PointF) valueAnimator.getAnimatedValue();
                bubble.setX(currentPoint.x);
                bubble.setY(currentPoint.y);
                bubble.setAlpha(1 - valueAnimator.getAnimatedFraction());
            }
        });
        valueAnimator.setTarget(bubble);
        valueAnimator.setInterpolator(mInterpolators[mRandom.nextInt(mInterpolators.length)]);
        valueAnimator.setDuration(2000);
        valueAnimator.start();
    }
}
