package net.harmal.karnet2.utils;

import android.content.res.Resources;

public class Maths
{

    public static int dpToPx(int dp, Resources res)
    {
        return (int) ((int) dp * res.getDisplayMetrics().density);
    }
}
