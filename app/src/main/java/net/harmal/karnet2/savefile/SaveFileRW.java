package net.harmal.karnet2.savefile;

import android.annotation.SuppressLint;
import android.os.Build;

import net.harmal.karnet2.core.Customer;
import net.harmal.karnet2.core.Date;
import net.harmal.karnet2.core.Order;
import net.harmal.karnet2.core.Product;
import net.harmal.karnet2.core.ProductCategory;
import net.harmal.karnet2.core.Stack;
import net.harmal.karnet2.core.registers.CustomerRegister;
import net.harmal.karnet2.core.registers.OrderRegister;
import net.harmal.karnet2.core.registers.ProductRegister;
import net.harmal.karnet2.core.registers.Stock;
import net.harmal.karnet2.utils.Logs;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class SaveFileRW
{
    private static final int SAVE_FILE_SIG = 0x02A41E72;
    /**
     * The file's version determines mainly how the header should be interpreted
     * but other parts of the body might be interpreted differently
     */
    private static final int SAVE_FILE_VER = 0x00000200;

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
        int customerCount;
        int productCount ;
        int orderCount   ;
        int stockCount   ;

        switch(version)
        {
            case SAVE_FILE_VER: // Recent version
                customerCount = buf.getInt();
                productCount  = buf.getInt();
                orderCount    = buf.getInt();
                stockCount    = buf.getInt();

                CustomerRegister.customerIdCount = buf.getInt();
                ProductRegister.productIdCount   = buf.getInt();
                OrderRegister.orderIdCount       = buf.getInt();
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

        // Read products data
        for(int i = 0; i < productCount; i++)
        {
            Product.ProductBuilder builder = new Product.ProductBuilder();
            Product p = builder.readData(version, buf);
            ProductRegister.add(p);
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
            Stack.StackBuilder builder = new Stack.StackBuilder();
            Stack s = builder.readData(version, buf);
            Stock.add(s.pid(), s.count());
        }

        Logs.debug("Data read");
    }

    @SuppressLint("DefaultLocale")
    public static void save(@NotNull String path) throws IOException
    {
        // Save logs
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
        {
            File logFile = new File(path + "/" + LOGS_FILE_NAME);
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

        // Save data
        ByteArrayOutputStream rawBytesStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(rawBytesStream);

        // Pre head
        dataStream.writeInt(SAVE_FILE_SIG);
        dataStream.writeInt(SAVE_FILE_VER);
        // head
        dataStream.writeInt(CustomerRegister.size());
        dataStream.writeInt(ProductRegister.size( ));
        dataStream.writeInt(OrderRegister.size(   ));
        dataStream.writeInt(Stock.size(           ));

        // id counts
        dataStream.writeInt(CustomerRegister.customerIdCount);
        dataStream.writeInt(ProductRegister.productIdCount  );
        dataStream.writeInt(OrderRegister.orderIdCount      );


        // Write customers data
        for(Customer c : CustomerRegister.get())
            c.writeData(dataStream);

        // Write products
        for(Product p : ProductRegister.get())
            p.writeData(dataStream);

        // Write orders
        for(Order o : OrderRegister.get())
            o.writeData(dataStream);

        // Write stock
        for(Stack s : Stock.get())
            s.writeData(dataStream);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
        {
            FileOutputStream outputStream = new FileOutputStream(path + "/" + SAVE_FILE_NAME);
            for(byte b : rawBytesStream.toByteArray())
                outputStream.write(b);
            outputStream.close();
        }
        else
            Files.write(Paths.get(path + "/" + SAVE_FILE_NAME), rawBytesStream.toByteArray(),
                    StandardOpenOption.CREATE, StandardOpenOption.WRITE);

        dataStream.close();
        rawBytesStream.close();

        Logs.debug("Data Saved");
    }
}
