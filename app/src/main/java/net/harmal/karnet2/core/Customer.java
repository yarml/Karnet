package net.harmal.karnet2.core;

import net.harmal.karnet2.savefile.Savable;
import net.harmal.karnet2.savefile.Utils;
import net.harmal.karnet2.utils.Logs;

import org.jetbrains.annotations.NotNull;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Customer implements Savable
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

    @Override
    public String toString() {
        return "Customer{" +
                "cid=" + cid +
                ", creationDate=" + creationDate +
                ", name='" + name + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", city='" + city + '\'' +
                '}';
    }

    @Override
    public void writeData(@NotNull DataOutputStream stream) throws IOException
    {
        stream.writeInt(cid);
        creationDate.writeData(stream);
        Utils.writeString(name, stream);
        Utils.writeString(city, stream);
        Utils.writeString(phoneNum, stream);
    }
    public static class CustomerBuilder implements Savable.BUILDER<Customer>
    {
        @Override
        public Customer readData(int version, @NotNull ByteBuffer buffer)
        {
            int cid = buffer.getInt();
            Date.DateBuilder dateBuilder = new Date.DateBuilder();
            Date date = dateBuilder.readData(version, buffer);
            String name = Utils.readString(buffer);
            String city = Utils.readString(buffer);
            String phoneNum = Utils.readString(buffer);
            return new Customer(cid, date, name, phoneNum, city);
        }
    }
}
