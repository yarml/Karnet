package net.harmal.karnet2.core.registers;

import net.harmal.karnet2.core.IngredientBundle;
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
        return add(o.oid(), o.cid(), o.deliveryPrice(), o.items(), o.dueDate(), o.reduction());
    }
    // Returns OID
    public static int add(int cid, int deliveryPrice, List<Item> stacks,
                          Date dueDate, int reduction)
    {
        return add(orderIdCount++, cid, deliveryPrice, stacks, dueDate, reduction);
    }
    // Returns OID
    public static int add(int oid, int cid, int deliveryPrice,
                          List<Item> stacks, Date dueDate, int reduction)
    {
        if(orderRegister == null)
            orderRegister = new ArrayList<>();
        if(cid < 0) // CID must be positive
        {
            return add(0, cid, deliveryPrice, stacks, dueDate, reduction);
        }
        for(Order o : orderRegister) // Make sure the CID is unique
            if(o.oid() == oid)
            {
                return add(oid + 1, cid, deliveryPrice, stacks, dueDate, reduction);
            }
        orderRegister.add(new Order(oid, cid, deliveryPrice, stacks, dueDate, reduction));
        return oid;
    }
    @NotNull
    public static List<Order> beforeDate(Date d)
    {
        if(d == null)
            return get();
        List<Order> orders = new ArrayList<>();
        for(Order o : get())
            if(o.dueDate().before(d))
                orders.add(o);
        return orders;
    }
    @NotNull
    public static List<Order> forDate(Date d)
    {
        if(d == null)
            return get();
        List<Order> orders = new ArrayList<>();
        for(Order o : get())
            if(o.dueDate().equals(d))
                orders.add(o);
        return orders;
    }

    @NotNull
    public static List<Order> withDelivery()
    {
        List<Order> result = new ArrayList<>();
        for(Order o : get())
            if(o.deliveryPrice() != 0)
                result.add(o);
        return result;
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

    public static int countOf(@NotNull IngredientBundle bundle)
    {
        int count = 0;
        for(Order o : orderRegister)
            count += o.countOf(bundle);
        return count;
    }
    public static int countOf(@NotNull IngredientBundle bundle, Date monitoredDate)
    {
        int count = 0;
        for(Order o : forDate(monitoredDate))
            count += o.countOf(bundle);
        return count;
    }
    public static int countOfBefore(@NotNull IngredientBundle bundle, Date monitoredDate)
    {
        int count = 0;
        for(Order o : beforeDate(monitoredDate))
            count += o.countOf(bundle);
        return count;
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
    public static void clear()
    {
        if(orderRegister == null)
            orderRegister = new ArrayList<>();
        orderRegister.clear();
    }
}
