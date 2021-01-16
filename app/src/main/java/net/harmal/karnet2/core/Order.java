package net.harmal.karnet2.core;

import net.harmal.karnet2.savefile.Savable;
import net.harmal.karnet2.utils.Logs;

import org.jetbrains.annotations.NotNull;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Order implements Savable
{
    private int         oid          ;
    private int         cid          ;
    private int         deliveryPrice;
    private List<Item>  items       ;
    private Date        dueDate      ;

    public Order(int oid, int cid, int deliveryPrice, @NotNull List<Item> items, @NotNull Date dueDate)
    {
        this.oid           = oid          ;
        this.cid           = cid          ;
        this.deliveryPrice = deliveryPrice;
        this.items        = items       ;
        this.dueDate       = dueDate      ;
    }

    public int oid()
    {
        return oid;
    }
    public int cid()
    {
        return cid;
    }
    public int deliveryPrice()
    {
        return deliveryPrice;
    }
    @NotNull
    public List<Item> items()
    {
        return items;
    }
    @NotNull
    public Date dueDate()
    {
        return dueDate;
    }

    public void oid(int noid)
    {
        oid = noid;
    }
    public void cid(int ncid)
    {
        cid = ncid;
    }
    public void deliveryPrice(int nDeliveryPrice)
    {
        deliveryPrice = nDeliveryPrice;
    }
    public void items(@NotNull List<Item> items)
    {
        this.items = items;
    }
    public void dueDate(@NotNull Date ndate)
    {
        this.dueDate = ndate;
    }

    public int countOf(@NotNull IngredientBundle bundle)
    {
        int count = 0;
        for(Item i : items)
            if(i.bundle().equals(bundle))
                count += i.count();
        return count;
    }

    /**
     * @return Total price without delivery into account
     */
    public int totalPrice()
    {
        int price = 0;
        for(Item i : items)
            price += i.count() * i.bundle().price();
        return price;
    }

    public void add(Item item)
    {
        boolean added = false;
        for(Item i : items)
            if(i.bundle().equals(item.bundle()))
            {
                i.add(item.count());
            }
        if(!added)
            items.add(item);
    }

    public void remove(Item item)
    {
        for(Item i : items)
            if(i.bundle().equals(item.bundle()))
            {
                i.remove(item.count());
                if(i.count() <= 0)
                    items.remove(i);
            }
    }

    @Override
    public void writeData(@NotNull DataOutputStream stream) throws IOException
    {
        stream.writeInt(oid);
        stream.writeInt(cid);
        stream.writeInt(deliveryPrice);
        stream.writeInt(items.size());
        dueDate.writeData(stream);
        for(Item i : items)
            i.writeData(stream);
    }
    public static class OrderBuilder implements Savable.BUILDER<Order>
    {
        @Override
        public Order readData(int version, @NotNull ByteBuffer buffer)
        {
            int oid = buffer.getInt();
            int cid = buffer.getInt();
            int deliveryPrice = buffer.getInt();
            int itemCount = buffer.getInt();
            Date.DateBuilder dateBuilder = new Date.DateBuilder();
            Date date = dateBuilder.readData(version, buffer);
            List<Item> items = new ArrayList<>();
            for(int i = 0; i < itemCount; i++)
            {
                Item.ItemBuilder builder = new Item.ItemBuilder();
                items.add(builder.readData(version, buffer));
            }
            return new Order(oid, cid, deliveryPrice, items, date);
        }
    }
}
