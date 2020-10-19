package net.harmal.karnet2.savefile;

import android.app.Activity;

import net.harmal.karnet2.core.registers.CustomerRegister;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SaveFileRW
{
    private static final int SAVE_FILE_VER = 0x00000200;

    public static void readSaveFile(Activity a) throws IOException, Exceptionst
    {
        ByteBuffer buf = ByteBuffer.wrap(Files.readAllBytes(
                Paths.get(
                        a.getFilesDir().getAbsolutePath() + "/" + "save.bin"
                        )));
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
                
                break;
            default:
                throw new Exception("Unrecognized save file version");
        }
    }

    private static String readString(ByteBuffer buf)
    {

    }

}
