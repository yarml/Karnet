package net.harmal.karnet2.utils;

import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.ContactData;
import net.harmal.karnet2.savefile.SaveFileRW;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
    @NotNull
    @Contract(" -> new")
    public static File appDataBackupFile()
    {
        File dir = new File(Environment.getExternalStorageDirectory(), "Karnet/");
        dir.mkdirs();
        return new File(dir, "save.bin");
    }

    public static boolean copy(File srcFile, File destFile)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            Path src = Paths.get(srcFile.getAbsolutePath());
            Path dest = Paths.get(destFile.getAbsolutePath());
            try
            {
                Files.copy(src, dest);
            } catch (IOException e)
            {
                e.printStackTrace();
                return false;
            }
        }
        else
        {
            try (FileInputStream src = new FileInputStream(srcFile);
                 FileOutputStream dest = new FileOutputStream(destFile))
            {
                byte[] buffer = new byte[1024];
                int len;
                while((len = src.read(buffer)) > 0)
                    dest.write(buffer, 0, len);
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return false;
            }

        }
        return true;
    }
}
