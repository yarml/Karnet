package net.harmal.karnet2.savefile;

import android.os.Build;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import net.harmal.karnet2.savefile.Utils;
import net.harmal.karnet2.core.Customer;
import net.harmal.karnet2.core.registers.CustomerRegister;
import net.harmal.karnet2.core.Date;
import net.harmal.karnet2.utils.Logs;

public class OldSaveFormat {
    private static List<Integer> loadedClientsIds = new ArrayList();

    public static void load(String path) {
        try {
            Logs.debug("Reading the old format file");
            loadAllClientData(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadAllClientData(String PATH) throws IOException {
        loadedClientsIds.clear();
        StringBuilder sb = new StringBuilder();
        sb.append(PATH);
        sb.append("/clients.dat");
        byte[] fileData;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
        {
            File input = new File(sb.toString());
            if(input.exists())
            {
                FileInputStream stream = new FileInputStream(input);
                Logs.debug("Allocating " + input.length() + " byte for the buffer");
                ByteBuffer buf = ByteBuffer.allocate((int) input.length());
                for(int i = 0, b = stream.read(); i < input.length(); i++, b = stream.read())
                    buf.put((byte) b);
                stream.close();
                fileData = buf.array();
            }
            else
            {
                Logs.debug("The file doesn't exist");
                return;
            }
        }
        else
        {
            Path filePath = Paths.get(sb.toString());
            if(Files.exists(filePath))
                fileData = Files.readAllBytes(filePath);
            else
                return;
        }
        if (fileData.length != 0) {
            CustomerRegister.customerIdCount = Utils.intFromByteArray(Utils.getPart(fileData, 0, 3));
            byte[] fileData2 = Utils.getPart(fileData, 4, fileData.length - 1);
            if (fileData2.length != 0) {
                while (fileData2.length != 0 && fileData2.length != 1) {
                    byte nameLength = fileData2[4];
                    byte cityNameLength = fileData2[5];
                    short statisticsStoredDays = Utils.byteArrayToShort(Utils.getPart(fileData2, 6, 7));
                    Customer c = loadClient(Utils.getPart(fileData2, 0, nameLength + 97 + cityNameLength + (statisticsStoredDays * 75)));
                    Logs.debug("Loaded customer: " + c.cid() + ": " + c.name() + " -> " + c.phoneNum() + ", " + c.city());
                    CustomerRegister.add(c);
                    fileData2 = Utils.getPart(fileData2, nameLength + 98 + cityNameLength + (statisticsStoredDays * 75), fileData2.length - 1);
                }
                return;
            }
            return;
        }
        CustomerRegister.customerIdCount = 0;
    }

    private static Customer loadClient(byte[] rawData) {
        byte[] bArr = rawData;
        int id = Utils.intFromByteArray(Utils.getPart(bArr, 0, 3));
        byte nameLength = bArr[4];
        byte cityNameLength = bArr[5];
        short statisticsStoredDays = Utils.byteArrayToShort(Utils.getPart(bArr, 6, 7));
        StringBuilder sb = new StringBuilder();
        sb.append("0");
        sb.append(new String(Utils.getPart(bArr, 8, 16), StandardCharsets.ISO_8859_1));
        String phoneNo = sb.toString();
        Date date = new Date(bArr[17], bArr[18], bArr[19] + 2000);
        byte orderUnitPrice = bArr[20];
        short deliveryPrice = Utils.byteArrayToShort(new byte[]{bArr[21], bArr[22]});
      
        String name = new String(Utils.getPart(bArr, 98, nameLength + 97), StandardCharsets.ISO_8859_1);
        String cityName = new String(Utils.getPart(bArr, nameLength + 98, nameLength + 97 + cityNameLength));
        Utils.getPart(bArr, nameLength + 98 + cityNameLength, nameLength + 96 + cityNameLength + (statisticsStoredDays * 75));
        
        loadedClientsIds.add(id);
        return new Customer(id, date, name, phoneNo, cityName);
    }
}
