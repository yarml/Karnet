package net.harmal.karnet2.core.registers;

import net.harmal.karnet2.core.Date;
import net.harmal.karnet2.core.IngredientBundle;
import net.harmal.karnet2.core.Order;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class OrdersLog
{
    private static List<Order> orders;

    public static void registerValidatedOrder(@NotNull Order o)
    {
        o.dueDate(Date.today());
        get().add(o);
    }

    public static int getCountOf(IngredientBundle bundle, Date d)
    {
        int count = 0;
        for(Order o : get())
            if(o.dueDate().before(d))
                count += o.countOf(bundle);
        return count;
    }

    public static List<Order> get()
    {
        if(orders == null)
            orders = new ArrayList<>();
        return orders;
    }

    public static void clear()
    {
        if(orders == null)
            orders = new ArrayList<>();
        orders.clear();
    }


}
