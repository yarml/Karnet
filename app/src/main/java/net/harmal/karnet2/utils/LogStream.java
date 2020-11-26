package net.harmal.karnet2.utils;

import java.io.IOException;
import java.io.OutputStream;

public class LogStream extends OutputStream
{
    protected StringBuilder builder = new StringBuilder();
    @Override
    public void write(int b) throws IOException
    {
        if(b == '\n')
            flush();
        else
            builder.append((char) b);
    }

    @Override
    public void flush() throws IOException
    {
        super.flush();
        Logs.info(builder.toString());
        builder = new StringBuilder();
    }
}
