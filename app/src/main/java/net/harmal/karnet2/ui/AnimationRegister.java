package net.harmal.karnet2.ui;

import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;

public class AnimationRegister
{
    public static TranslateAnimation shakeAnimation()
    {
        TranslateAnimation anim = new TranslateAnimation(0.0f, 10.0f, 0.0f, 0.0f);
        anim.setDuration(200);
        anim.setInterpolator(new CycleInterpolator(7));
        return anim;
    }
}
