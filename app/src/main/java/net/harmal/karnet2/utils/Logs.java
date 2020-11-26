package net.harmal.karnet2.utils;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

public class Logs
{
    private static final StringBuilder logBuilder = new StringBuilder();

    public static void debug(@NotNull String m)
    {
        Log.e("DEBUG", m);
        logBuilder.append("DEBUG: ").append(m).append('\n');
    }
    public static void error(@NotNull String m)
    {
        Log.e("ERROR", m);
        logBuilder.append("ERROR: ").append(m).append('\n');
    }

    public static void info(@NotNull String m)
    {
        Log.i("INFO", m);
        logBuilder.append("INFO: ").append(m).append('\n');
    }

    public static String getLogs()
    {
        return logBuilder.toString();
    }
}
