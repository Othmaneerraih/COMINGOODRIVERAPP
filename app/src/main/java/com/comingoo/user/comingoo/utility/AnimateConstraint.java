package com.comingoo.user.comingoo.utility;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;

import android.graphics.Color;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

public class AnimateConstraint {

    public static void animate(final Context context, final View constraintLayout, final float reachedHeigth, final float currentHeight, int duration, final View... opt) {

        constraintLayout.setVisibility(View.VISIBLE);

        final int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int) reachedHeigth, context.getResources().getDisplayMetrics());
        ValueAnimator anim = ValueAnimator.ofInt(constraintLayout.getMeasuredHeight(), height);


        if (opt.length != 0) {
            opt[0].setVisibility(View.GONE);
            fadeIn(context, opt[1], 500, 10);
        }


        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = constraintLayout.getLayoutParams();
                layoutParams.height = val;
                constraintLayout.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(duration);
        anim.start();

    }

    public static void animateWidth(final Context context, final View constraintLayout, final float reachedHeigth, final float currentHeight, int duration, final View... opt) {

        constraintLayout.setVisibility(View.VISIBLE);

        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int) reachedHeigth, context.getResources().getDisplayMetrics());

        ValueAnimator anim = ValueAnimator.ofInt(constraintLayout.getMeasuredHeight(), height);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = constraintLayout.getLayoutParams();
                layoutParams.width = val;
                constraintLayout.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(duration);
        anim.start();

    }

    public static void fadeIn(Context context, final View constraintLayout, final int duration, final int howSmooth) {
        constraintLayout.setVisibility(View.VISIBLE);

        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(constraintLayout, "alpha", 0f, 1f);
        fadeIn.setDuration(duration);

        final AnimatorSet mAnimationSet = new AnimatorSet();
        mAnimationSet.play(fadeIn);
        mAnimationSet.start();
    }

    public static void fadeOut(Context context, final View constraintLayout, final int duration, final int howSmooth) {
        constraintLayout.setVisibility(View.VISIBLE);

        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(constraintLayout, "alpha", 1f, 0f);
        fadeIn.setDuration(duration);

        final AnimatorSet mAnimationSet = new AnimatorSet();
        mAnimationSet.play(fadeIn);
        mAnimationSet.start();
    }

    public static void animateCollapse(final Context context, final View constraintLayout, final float reachedHeigth, final float currentHeight, int duration) {

        constraintLayout.setVisibility(View.VISIBLE);

        final int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int) reachedHeigth, context.getResources().getDisplayMetrics());
        ValueAnimator anim = ValueAnimator.ofInt(constraintLayout.getMeasuredHeight(), height);


        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = constraintLayout.getLayoutParams();
                layoutParams.height = val;
                constraintLayout.setLayoutParams(layoutParams);
            }
        });

        anim.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                constraintLayout.setBackgroundColor(Color.TRANSPARENT);
            }
        });

        anim.setDuration(duration);
        anim.start();
    }

    public static void resideAnimation(final Context context, final View constraintLayout,
                                       final View contentBlocker, final int screenWidth, final int screenHeight, final int duration) {

        final int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int) screenHeight, context.getResources().getDisplayMetrics());
        final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (int) screenWidth, context.getResources().getDisplayMetrics());
        final float xTranslation = 0.6f * width;
        final float yTranslation = 0.25f * height;


        final double percentageScale = (1 - 0.6) / (duration / 20);
        final double percentageTranslation = (xTranslation) / (duration / 20);

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int i = 1;

            @Override
            public void run() {
                double value = 1 - (percentageScale * i);

                if ((percentageTranslation * i) <= xTranslation) {
                    constraintLayout.setTranslationX((int) (percentageTranslation * i));
                    contentBlocker.setTranslationX((int) (percentageTranslation * i));
                }
                if (value >= 0.6) {
                    constraintLayout.setScaleY((float) value);
                    constraintLayout.setScaleX((float) value);
                }
                i++;
                if (value > 0.6 || (percentageTranslation * i) < xTranslation) {
                    handler.postDelayed(this, 20);
                } else {
                    contentBlocker.setScaleY((float) (value + 0.12));
                    contentBlocker.setScaleX((float) (value + 0.215));

                    contentBlocker.setVisibility(View.VISIBLE);
                    contentBlocker.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            contentBlocker.setVisibility(View.GONE);
                            contentBlocker.setOnClickListener(null);
                            final Handler handler = new Handler();
                            Runnable runnable = new Runnable() {
                                int i = 1;

                                @Override
                                public void run() {
                                    double value = 0.6 + (percentageScale * i);

                                    if ((xTranslation - (percentageTranslation * i)) >= 0) {
                                        constraintLayout.setTranslationX((int) (xTranslation - (percentageTranslation * i)));
                                        contentBlocker.setTranslationX((int) (xTranslation - (percentageTranslation * i)));
                                    }
                                    if (value <= 1) {
                                        constraintLayout.setScaleY((float) value);
                                        constraintLayout.setScaleX((float) value);
                                        contentBlocker.setScaleY((float) value);
                                        contentBlocker.setScaleX((float) value);
                                    }


                                    i++;
                                    if (value < 1 || (xTranslation - (percentageTranslation * i)) >= 0) {
                                        handler.postDelayed(this, 20);
                                    } else {
                                        constraintLayout.setTranslationX(0);
                                        contentBlocker.setTranslationX(0);
                                    }
                                }
                            };
                            runnable.run();
                        }
                    });

                }
            }
        };
        runnable.run();

    }

    static void expandCircleAnimation(final Context context, final View constraintLayout, float maxHeight, float maxWidth) {
        double Height = maxHeight + (maxHeight * 0.5) + maxHeight;
        double Width = maxWidth + (maxWidth * 0.5);
        animate(context, constraintLayout, (float) Height, 1, 650);
        animateWidth(context, constraintLayout, (float) Width, 1, 350);
    }

}
