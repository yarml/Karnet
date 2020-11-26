package net.harmal.karnet2.savefile;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public interface Savable
{
    void writeData(DataOutputStream stream) throws IOException;

    interface BUILDER<T>
    {
        T readData(int version, ByteBuffer buffer);
    }
}
