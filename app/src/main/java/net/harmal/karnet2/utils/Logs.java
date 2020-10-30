package net.harmal.karnet2.utils;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

public class Logs
{
    private static boolean logToConsole = true;

    private static final StringBuilder logBuilder = new StringBuilder();

    public static void logToConsole(boolean l)
    {
        logToConsole = l;
    }

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
}
