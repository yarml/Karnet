package net.harmal.karnet2.core;

import net.harmal.karnet2.savefile.Savable;

import org.jetbrains.annotations.NotNull;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Stack implements Savable
{
    private int pid  ;
    private int count;

    public Stack(int pid, int count)
    {
        this.pid = pid    ;
        this.count = count;
    }

    public Stack()
    {
        pid   = 0;
        count = 0;
    }

    public int pid()
    {
        return pid;
    }
    public int count()
    {
        return count;
    }

    public void pid(int npid)
    {
        pid = npid;
    }
    public void count(int ncount)
    {
        count = ncount;
    }

    public void add(int a)
    {
        count += a;
    }

    public void remove(int r)
    {
        count -= r;
        if(count < 0)
            count = 0;
    }

    @Override
    public void writeData(@NotNull DataOutputStream stream) throws IOException
    {
        stream.writeInt(pid);
        stream.writeInt(count);
    }
    public static class StackBuilder implements BUILDER<Stack>
    {
        @Override
        public Stack readData(int version, ByteBuffer buffer)
        {
            return new Stack(buffer.getInt(), buffer.getInt());
        }
    }
}
