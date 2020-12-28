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
}
