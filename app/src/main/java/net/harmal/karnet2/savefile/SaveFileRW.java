package net.harmal.karnet2.savefile;

import android.app.Activity;
import android.os.Build;

import androidx.annotation.RequiresApi;

import net.harmal.karnet2.core.Date;
import net.harmal.karnet2.core.ProductCategory;
import net.harmal.karnet2.core.registers.CustomerRegister;
import net.harmal.karnet2.core.registers.ProductRegister;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SaveFileRW
{
    private static final int SAVE_FILE_VER = 0x00000200;

    public static void readSaveFile(@NotNull Activity a) throws Exception
    {
        // TODO: Debug Start
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return;
        // Debug End

        ByteBuffer buf;
        if(Files.exists(Paths.get(a.getFilesDir().getAbsolutePath() + "/" + "save.bin")))
            buf = ByteBuffer.wrap(Files.readAllBytes(
                    Paths.get(
                            a.getFilesDir().getAbsolutePath() + "/" + "save.bin"
                            )));
        else
            return;
        switch(buf.getInt()) // switch version
        {
            case SAVE_FILE_VER: // Current version
                // Header
                int clientCount           = buf.getInt ();
                int productCount          = buf.getInt ();
                int orderCount            = buf.getInt ();
                int stockEntriesCount     = buf.getInt ();
                int statisticsFramesCount = buf.getInt ();
                for(int i = 0; i < 40; i++) buf.getChar(); // Reserved

                // Reading customers data
                CustomerRegister.customerIdCount = buf.getInt();
                for(int i = 0; i < clientCount; i++)
                {
                    int id          = buf.getInt()                                          ;
                    Date cdate      = new Date(buf.getChar(), buf.getChar(), buf.getShort());
                    String name     = readString(buf)                                       ;
                    String city     = readString(buf)                                       ;
                    String phoneNum = readString(buf)                                       ;
                    CustomerRegister.add(id, name, city, phoneNum, cdate);
                }

                ProductRegister.productIdCount = buf.getInt();
                for(int i = 0; i < productCount; i++)
                {

                }

                break;
            default:
                throw new Exception("Unrecognized save file version");
        }
    }

    @NotNull
    private static String readString(@NotNull ByteBuffer buf)
    {
        StringBuilder b = new StringBuilder();
        for(char i = buf.getChar(); i != 0; i = buf.getChar())
            b.append(i);
        return b.toString();
    }
    @NotNull
    private static List<ProductCategory> readProductCategory(@NotNull ByteBuffer buf, int count)
    {
        List<ProductCategory> cat = new ArrayList<ProductCategory>();
        for(int i = 0; i < count; i++)
            cat.add(new ProductCategory(readString(buf)));
        return cat;
    }

}
