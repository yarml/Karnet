package net.harmal.karnet2.core.registers;

import net.harmal.karnet2.core.Item;
import net.harmal.karnet2.core.Order;
import net.harmal.karnet2.core.Date;
import net.harmal.karnet2.core.Trash;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class OrderRegister
{
    public static int orderIdCount = 0;

    private static List<Order> orderRegister;


    public static int add(@NotNull Order o)
    {
        return add(o.oid(), o.cid(), o.deliveryPrice(), o.items(), o.dueDate());
    }
    // Returns OID
    public static int add(int cid, int deliveryPrice, List<Item> stacks, Date dueDate)
    {
        return add(orderIdCount++, cid, deliveryPrice, stacks, dueDate);
    }
    // Returns OID
    public static int add(int oid, int cid, int deliveryPrice, List<Item> stacks, Date dueDate)
    {
        if(orderRegister == null)
            orderRegister = new ArrayList<>();
        if(cid < 0) // CID must be positive
        {
            return add(0, cid, deliveryPrice, stacks, dueDate);
        }
        for(Order o : orderRegister) // Make sure the CID is unique
            if(o.oid() == oid)
            {
                return add(oid + 1, cid, deliveryPrice, stacks, dueDate);
            }
        orderRegister.add(new Order(oid, cid, deliveryPrice, stacks, dueDate));
        return oid;
    }

    public static void remove(int oid)
    {
        if(orderRegister == null)
            orderRegister = new ArrayList<>();
        for(Order o : orderRegister)
        {
            if(o.oid() == oid)
            {
                Trash.pushOrder(o);
                orderRegister.remove(o);
                break;
            }
        }
    }

    @Nullable
    public static Order getOrder(int oid)
    {
        if(orderRegister == null)
            orderRegister = new ArrayList<>();
        for(Order o : orderRegister)
            if(o.oid() == oid)
                return o;
        return null;
    }

    @NotNull
    public static List<Order> get()
    {
        if(orderRegister == null)
            orderRegister = new ArrayList<>();
        return orderRegister;
    }

    public static int size()
    {
        return get().size();
    }

}
