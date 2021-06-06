package net.harmal.karnet2.core;

import net.harmal.karnet2.savefile.Savable;
import net.harmal.karnet2.utils.Logs;

import org.jetbrains.annotations.NotNull;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Order implements Savable
{
    private int         oid          ;
    private int         cid          ;
    private int         deliveryPrice;
    private List<Item>  items        ;
    private Date        dueDate      ;
    private int         reduction    ;

    public Order(int oid, int cid, int deliveryPrice, @NotNull List<Item> items,
                 @NotNull Date dueDate, int reduction)
    {
        this.oid           = oid          ;
        this.cid           = cid          ;
        this.deliveryPrice = deliveryPrice;
        this.items         = items        ;
        this.dueDate       = dueDate      ;
        this.reduction     = reduction    ;
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

    public int reduction()
    {
        return reduction;
    }
    public void reduction(int reduction)
    {
        this.reduction = reduction;
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
        return price - reduction;
    }

    public void add(Item item)
    {
        for(Item i : items)
            if(i.bundle().equals(item.bundle()))
            {
                i.add(item.count());
                return;
            }
        items.add(item);
    }

    public void remove(Item item)
    {
        List<Item> itemsToRemove = new ArrayList<>();
        for(Item i : items)
            if(i.bundle().equals(item.bundle()))
            {
                i.remove(item.count());
                if(i.count() <= 0)
                {
                    itemsToRemove.add(i);
                }
            }
        for(Item i : itemsToRemove)
            items.remove(i);
    }

    @Override
    public void writeData(@NotNull DataOutputStream stream) throws IOException
    {
        stream.writeInt(oid);
        stream.writeInt(cid);
        stream.writeInt(deliveryPrice);
        stream.writeInt(items.size());
        dueDate.writeData(stream);
        stream.writeInt(reduction);
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
            int reduction = 0;
            if(version >= 0x00000202)
                reduction = buffer.getInt();
            for(int i = 0; i < itemCount; i++)
            {
                Item.ItemBuilder builder = new Item.ItemBuilder();
                Item it = builder.readData(version, buffer);
                if(it != null)
                    items.add(it);
            }
            return new Order(oid, cid, deliveryPrice, items, date, reduction);
        }
    }
    public static class OrderCIDComparator implements Comparator<Order>
    {
        @Override
        public int compare(@NotNull Order o1, @NotNull Order o2)
        {
            return o1.cid - o2.cid;
        }
    }
}
