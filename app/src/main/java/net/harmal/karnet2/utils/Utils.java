package net.harmal.karnet2.utils;

import android.os.Environment;

import net.harmal.karnet2.core.ContactData;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class Utils
{
    public static final String GITHUB_API_LATEST = "https://api.github.com/repos/TheCoderCrab/Karnet/releases/latest";
    private static final String GITHUB_LATEST_RELEASE = "https://github.com/TheCoderCrab/Karnet/releases/download/%s/karnet.apk";                 ;

    public static final String UPDATE_FILE_NAME  = "karnet.apk";

    @NotNull
    public static String extractCustomerName(@NotNull ContactData contact)
    {
        String name = contact.name;
        name = name.replaceAll("Cliente", "");
        name = name.replaceAll("cliente", "");
        name = name.replaceAll("Client", "");
        name = name.replaceAll("client", "");
        name = name.trim();
        return name;
    }

    @NotNull
    public static String extractCustomerNum(@NotNull ContactData contact)
    {
        String num = contact.num;
        num = num.replaceAll("\\(", "");
        num = num.replaceAll("\\)", "");
        num = num.replaceAll("-", "");
        num = num.replaceAll(" ", "");
        num = num.replace("+212", "0");
        return num;
    }
    @NotNull
    @Contract(" -> new")
    public static File updateFile()
    {
        return new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                UPDATE_FILE_NAME);
    }
    @NotNull
    public static String updateLink(String tag)
    {
        return String.format(GITHUB_LATEST_RELEASE, tag);
    }
}
