package net.harmal.karnet2.savefile;

import android.annotation.SuppressLint;
import android.os.Build;

import net.harmal.karnet2.core.Customer;
import net.harmal.karnet2.core.Item;
import net.harmal.karnet2.core.Order;
import net.harmal.karnet2.core.ProductIngredient;
import net.harmal.karnet2.core.registers.CustomerRegister;
import net.harmal.karnet2.core.registers.IngredientRegister;
import net.harmal.karnet2.core.registers.OrderRegister;
import net.harmal.karnet2.core.registers.Stock;
import net.harmal.karnet2.utils.Logs;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class SaveFileRW
{
    private static final int SAVE_FILE_SIG = 0x02A41E72;
    /**
     * The file's version determines mainly how the header should be interpreted
     * but other parts of the body might be interpreted differently
     */
    private static final int SAVE_FILE_VER = 0x00000201;

    private static final String SAVE_FILE_NAME = "save.bin";
    private static final String LOGS_FILE_NAME = "logs.txt";

    public static void read(@NotNull String path) throws Exception
    {
        ByteBuffer buf;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
        {
            Logs.debug("Reading save file the old way");
            File input = new File(path + "/" + SAVE_FILE_NAME);
            if(input.exists())
            {
                FileInputStream stream = new FileInputStream(input);
                Logs.debug("Allocating " + input.length() + " byte for the buffer");
                buf = ByteBuffer.allocate((int) input.length());
                for(int i = 0, b = stream.read(); i < input.length(); i++, b = stream.read())
                    buf.put((byte) b);
                stream.close();
                buf.flip();
            }
            else
                return;
        }
        else
            if(Files.exists(Paths.get(path + "/" + SAVE_FILE_NAME)))
                buf = ByteBuffer.wrap(Files.readAllBytes(
                        Paths.get(
                                path + "/" + SAVE_FILE_NAME
                                )));
            else
                return;
        Logs.debug("Reading new format save file");

        // Read pre head
        if(buf.getInt() != SAVE_FILE_SIG)
        {
            Logs.debug("Save file not signed!");
            return;
        }
        int version = buf.getInt();
        // Read head
        int customerCount  ;
        int ingredientCount;
        int orderCount     ;
        int stockCount     ;

        switch(version)
        {
            case 0x00000200: // First version
                customerCount = buf.getInt();
                ingredientCount = buf.getInt();
                orderCount = buf.getInt();
                stockCount = buf.getInt();

                CustomerRegister.customerIdCount = buf.getInt();
                buf.getInt(); // product register id count
                buf.getInt();
                break;
            case SAVE_FILE_VER:
                // head
                customerCount   = buf.getInt();
                ingredientCount = buf.getInt();
                orderCount      = buf.getInt();
                stockCount      = buf.getInt();

                // id counts
                CustomerRegister.customerIdCount     = buf.getInt();
                IngredientRegister.ingredientIdCount = buf.getInt();
                OrderRegister.orderIdCount           = buf.getInt();
                break;
            default:
                Logs.error("Unrecognized save file version");
                throw new Exception("Unrecognized save file version");
        }

        // Read customers data
        for(int i = 0; i < customerCount; i++)
        {
            Customer.CustomerBuilder builder = new Customer.CustomerBuilder();
            Customer c = builder.readData(version, buf);
            CustomerRegister.add(c);
        }

        if(version == 0x00000200)
            return;

        // Read ingredient data
        for(int i = 0; i < ingredientCount; i++)
        {
            ProductIngredient.ProductIngredientBuilder builder = new
                    ProductIngredient.ProductIngredientBuilder();
            IngredientRegister.add(builder.readData(version, buf));
        }

        // Read orders
        for(int i = 0; i < orderCount; i++)
        {
            Order.OrderBuilder builder = new Order.OrderBuilder();
            Order o = builder.readData(version,buf);
            OrderRegister.add(o);
        }

        // Read stock
        for(int i = 0; i < stockCount; i++)
        {
            Item.ItemBuilder builder = new Item.ItemBuilder();
            Stock.add(builder.readData(version, buf));
        }

        Logs.debug("Data read");
    }

    @SuppressLint("DefaultLocale")
    public static void save(@NotNull String path) throws IOException
    {

        // Save logs
        {
            File logFile = new File(path, LOGS_FILE_NAME);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            {
                if(!logFile.exists())
                    logFile.createNewFile();
                FileOutputStream out = new FileOutputStream(logFile, true);
                for(byte b : Logs.getLogs().getBytes(StandardCharsets.ISO_8859_1))
                    out.write(b);
                out.close();
            }
            else
            {
                if(!Files.exists(Paths.get(path + "/" + LOGS_FILE_NAME)))
                    Files.createFile(Paths.get(path + "/" + LOGS_FILE_NAME));
                Files.write(Paths.get(path + "/" + LOGS_FILE_NAME),
                        Logs.getLogs().getBytes(StandardCharsets.ISO_8859_1),
                        StandardOpenOption.APPEND);
            }
        }

        // Save data
        ByteArrayOutputStream rawBytesStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(rawBytesStream);

        // Pre head
        dataStream.writeInt(SAVE_FILE_SIG);
        dataStream.writeInt(SAVE_FILE_VER);
        // head
        Logs.debug("There are: " + CustomerRegister.size() + " customers");
        dataStream.writeInt(CustomerRegister.size(  ));
        dataStream.writeInt(IngredientRegister.size());
        dataStream.writeInt(OrderRegister.size(     ));
        dataStream.writeInt(Stock.size(             ));

        // id counts
        dataStream.writeInt(CustomerRegister.customerIdCount    );
        dataStream.writeInt(IngredientRegister.ingredientIdCount);
        dataStream.writeInt(OrderRegister.orderIdCount          );


        // Write customers data
        for(Customer c : CustomerRegister.get())
            c.writeData(dataStream);

        // Write products
        for(ProductIngredient p : IngredientRegister.get())
            p.writeData(dataStream);

        // Write orders
        for(Order o : OrderRegister.get())
            o.writeData(dataStream);

        // Write stock
        for(Item s : Stock.get())
            s.writeData(dataStream);

        {
            File dataFile = new File(path, SAVE_FILE_NAME);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            {
                FileOutputStream outputStream = new FileOutputStream(dataFile, false);
                for(byte b : rawBytesStream.toByteArray())
                    outputStream.write(b);
                outputStream.close();
            }
            else
                Files.write(Paths.get(path + "/" + SAVE_FILE_NAME),
                        rawBytesStream.toByteArray(),
                        StandardOpenOption.CREATE, StandardOpenOption.WRITE);

        }

        dataStream.close();
        rawBytesStream.close();

        Logs.debug("Data Saved");
    }
}
