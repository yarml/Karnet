package net.harmal.karnet2.utils;

public class ErrorStream extends LogStream
{
    @Override
    public void flush()
    {
        Logs.error(builder.toString());
        builder = new StringBuilder();
    }
}
