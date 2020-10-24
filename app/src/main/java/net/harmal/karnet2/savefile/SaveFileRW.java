package net.harmal.karnet2.savefile;

import android.app.Activity;

import net.harmal.karnet2.core.Date;
import net.harmal.karnet2.core.ProductCategory;
import net.harmal.karnet2.core.registers.CustomerRegister;
import net.harmal.karnet2.core.registers.ProductRegister;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SaveFileRW
{
    private static final int SAVE_FILE_VER = 0x00000200;

    public static void readSaveFile(Activity a) throws IOException, Exception
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

                ProductRegister.productIdCount = buf.getInt();
                for(int i = 0; i < productCount; i++)
                {
                    int id = buf.getInt();
                    int unitPrice = buf.getShort();
                    int catIngCount = buf.getInt();
                    int catTasteCount = buf.getInt();
                    int catShapeCount = buf.getInt();
                    int catExtraCount = buf.getInt();
                    String name = readString(buf);
                    List<ProductCategory> catIng = readProductCategory(buf, catIngCount);
                    List<ProductCategory> catTaste = readProductCategory(buf, catTasteCount);
                    List<ProductCategory> catShape = readProductCategory(buf, catShapeCount);
                    List<ProductCategory> catExtra = readProductCategory(buf, catExtraCount);

                    ProductRegister.add(id, unitPrice, name, catIng, catTaste, catShape, catExtra);
                }

                break;
            default:
                throw new Exception("Unrecognized save file version");
        }
    }

    private static String readString(ByteBuffer buf)
    {
        StringBuilder b = new StringBuilder();
        for(char i = buf.getChar(); i != 0; i = buf.getChar())
            b.append(i);
        return b.toString();
    }
    private static List<ProductCategory> readProductCategory(ByteBuffer buf, int count)
    {
        List<ProductCategory> cat = new ArrayList<ProductCategory>();
        for(int i = 0; i < count; i++)
            cat.add(new ProductCategory(readString(buf)));
        return cat;
    }

}
