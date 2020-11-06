package net.harmal.karnet2.ui;

import android.animation.Animator;
import android.view.View;
import android.view.animation.CycleInterpolator;

import net.harmal.karnet2.utils.Logs;

import org.jetbrains.annotations.NotNull;

public class Animations
{
    public static void shake(@NotNull View v)
    {
        v.animate().setInterpolator(new CycleInterpolator(7));
        v.animate().translationXBy(10.0f);
        v.animate().translationYBy(0.0f);
        v.animate().setDuration(200);
    }
    public static void popIn(@NotNull View v)
    {
        Logs.debug("Popping in");
        v.animate().setDuration(200);
        v.animate().scaleX(1.0f);
        v.animate().scaleY(1.0f);
        v.animate().setListener(new Animator.AnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animation)
            {
                v.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationEnd(Animator animation) {}
            @Override
            public void onAnimationCancel(Animator animation) {}
            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
    }
    public static void popOut(@NotNull View v)
    {
        Logs.debug("Popping out");
        v.animate().setDuration(200);
        v.animate().scaleX(0.0f);
        v.animate().scaleY(0.0f);
        v.animate().setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}
            @Override
            public void onAnimationEnd(Animator animation)
            {
                v.setVisibility(View.GONE);
            }
            @Override
            public void onAnimationCancel(Animator animation) {}
            @Override
            public void onAnimationRepeat(Animator animation) { }
        });
    }
}
