package net.harmal.karnet2.core;


import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.RequiresApi;

import net.harmal.karnet2.savefile.Savable;
import net.harmal.karnet2.utils.Logs;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Date implements Savable
{
    private byte  day  ;
    private byte  month;
    private short year ;

    @RequiresApi(Build.VERSION_CODES.O)
    public Date(@NotNull LocalDate date)
    {
        this.day    = (byte) date.getDayOfMonth();
        this.month  = (byte) date.getMonthValue();
        this.year   = (short) date.getYear     ();
    }

    public Date(int day, int month, int year)
    {
        this.day    = (byte)  day  ;
        this.month  = (byte)  month;
        this.year   = (short) year ;
    }

    /**
     * Creates a Date object using the input
     * Format: ded/MM/yyyy
     * @throws IllegalArgumentException if the input can not be transformed to a date object or isn't valid
     */
    public Date(String raw) throws IllegalArgumentException
    {
        try
        {
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            {
                String[] split = raw.split("/");
                if(split.length != 3)
                    throw new Exception("There should be 3 date entries");
                int day   = Integer.parseInt(split[0]);
                int month = Integer.parseInt(split[1]);
                int year  = Integer.parseInt(split[2]);

                Logs.debug("Day: " + day);
                Logs.debug("Month: " + month);
                Logs.debug("Year: " + year);

                if(day < 1 || day > 31)
                    throw new Exception("Day should be between 1 and 31");
                if(month < 1 || month > 12)
                    throw new Exception("Month should be between 1 and 12");
                this.day   = (byte ) day  ;
                this.month = (byte ) month;
                this.year  = (short) year ;
                return;
            }

            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            Logs.debug("Parsing date");
            LocalDate date = LocalDate.parse(raw, format);
            Logs.debug("Date parsed");
            this.day   = (byte ) date.getDayOfMonth();
            this.month = (byte ) date.getMonthValue();
            this.year  = (short) date.getYear(      );
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }

    }

    public int day()
    {
        return day;
    }
    public int month()
    {
        return month;
    }
    public int year()
    {
        return year;
    }

    public void day(int day)
    {
        this.day = (byte) day;
    }
    public void month(int month)
    {
        this.month = (byte) month;
    }
    public void year(int year)
    {
        this.year = (short) year;
    }

    @NotNull
    @Contract(" -> new")
    public static Date today()
    {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return new Date(1, 1, 2019);
        return new Date(LocalDate.now());
    }
    @NotNull
    @Contract("_ -> new")
    public static Date afterDays(int days)
    {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return new Date(1 + days, 1, 2019);
        return new Date(LocalDate.now().plusDays(days));
    }

    @NotNull
    @Override
    public String toString()
    {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return day + "/" + month + "/" + year;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.of(year, month, day).format(formatter);
    }

    @Override
    public void writeData(@NotNull DataOutputStream stream) throws IOException
    {
        stream.writeByte(day);
        stream.writeByte(month);
        stream.writeShort(year);
    }

    public static class DateBuilder implements BUILDER<Date>
    {
        @Override
        public Date readData(int version, ByteBuffer buffer)
        {
            return new Date(buffer.get(), buffer.get(), buffer.getShort());
        }
    }
}
