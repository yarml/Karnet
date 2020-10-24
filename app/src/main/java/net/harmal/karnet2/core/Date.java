package net.harmal.karnet2.core;

import java.time.LocalDate;

public class Date
{
    private byte  day  ;
    private byte  month;
    private short year ;

    public Date(LocalDate date)
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

    public static Date today()
    {
        return new Date(LocalDate.now());
    }
    public static Date tomorrow()
    {
        return new Date(LocalDate.now().plusDays(1));
    }
    public static Date afterDays(int days)
    {
        return new Date(LocalDate.now().plusDays(days));
    }

}
