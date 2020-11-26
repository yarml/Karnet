package net.harmal.karnet2.savefile;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Utils
{

    public static void writeString(@NotNull String s, DataOutputStream stream) throws IOException
    {
        for(byte c : s.getBytes(StandardCharsets.ISO_8859_1))
            stream.writeByte(c);
        stream.writeByte((byte) 0);
    }

    @NotNull
    @Contract("_ -> new")
    public static String readString(@NotNull ByteBuffer buf)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        for(byte b = buf.get(); b != 0; b = buf.get())
            stream.write(b);
        return new String(stream.toByteArray(), StandardCharsets.ISO_8859_1);
    }

    public static int intFromByteArray(@NotNull byte[] arr) {
        if (arr.length == 4) {
            return ByteBuffer.wrap(arr).getInt();
        }
        return 0;
    }

    @NotNull
    @Contract(pure = true)
    public static byte[] getPart(byte[] src, int from, int to) {
        if (from > to) {
            return new byte[0];
        }
        byte[] res = new byte[((to - from) + 1)];
        int i = from;
        int j = 0;
        while (i <= to) {
            res[j] = src[i];
            i++;
            j++;
        }
        return res;
    }

    public static short byteArrayToShort(@NotNull byte[] arr) {
        if (arr.length == 2) {
            return ByteBuffer.wrap(arr).getShort();
        }
        return 0;
    }
}
