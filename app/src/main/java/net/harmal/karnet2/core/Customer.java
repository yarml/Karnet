package net.harmal.karnet2.core;

import org.jetbrains.annotations.NotNull;

public class Customer
{
    private int    cid         ;
    private Date   creationDate;
    private String name        ;
    private String phoneNum    ;
    private String city        ;

    public Customer(int cid, @NotNull Date creationDate, @NotNull String name, @NotNull String phoneNum, @NotNull String city)
    {
        this.cid          = cid         ;
        this.creationDate = creationDate;
        this.name         = name        ;
        this.phoneNum     = phoneNum    ;
        this.city         = city        ;
    }

    public int cid()
    {
        return cid;
    }
    @NotNull
    public Date creationDate()
    {
        return creationDate;
    }
    @NotNull
    public String name()
    {
        return name;
    }
    @NotNull
    public String city()
    {
        return city;
    }
    @NotNull
    public String phoneNum()
    {
        return phoneNum;
    }

    public void cid(int ncid)
    {
        cid = ncid;
    }
    public void creationDate(@NotNull Date ndate)
    {
        creationDate = ndate;
    }
    public void name(@NotNull String nname)
    {
        name = nname;
    }
    public void city(@NotNull String ncity)
    {
        city = ncity;
    }
    public void phoneNum(@NotNull String nphoneNum)
    {
        phoneNum = nphoneNum;
    }
}
