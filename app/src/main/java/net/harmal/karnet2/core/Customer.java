package net.harmal.karnet2.core;

public class Customer
{
    private int    cid         ;
    private Date   creationDate;
    private String name        ;
    private String phoneNum    ;
    private String city        ;

    public Customer(int cid, Date creationDate, String name, String phoneNum, String city)
    {
        this.cid          = cid         ;
        this.creationDate = creationDate;
        this.name         = name        ;
        this.phoneNum     = phoneNum    ;
        this.city         = city        ;
    }
    public Customer()
    {
        cid = 0;
        creationDate = Date.today();
        name         = ""          ;
        phoneNum     = "0600000000";
        city         = ""          ;
    }

    public int cid()
    {
        return cid;
    }
    public Date creationDate()
    {
        return creationDate;
    }
    public String name()
    {
        return name;
    }
    public String city()
    {
        return city;
    }
    public String phoneNum()
    {
        return phoneNum;
    }

    public void cid(int ncid)
    {
        cid = ncid;
    }
    public void creationDate(Date ndate)
    {
        creationDate = ndate;
    }
    public void name(String nname)
    {
        name = nname;
    }
    public void city(String ncity)
    {
        city = ncity;
    }
    public void phoneNum(String nphoneNum)
    {
        phoneNum = nphoneNum;
    }
}
