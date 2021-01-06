package net.harmal.karnet2.core.registers;

import net.harmal.karnet2.core.IngredientBundle;
import net.harmal.karnet2.core.Item;
import net.harmal.karnet2.core.Order;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Stock
{
    private static List<Item> stock;

    public static void add(Item i)
    {
        add(i.bundle(), i.count());
    }

    public static void add(IngredientBundle bundle, int count)
    {
        if(stock == null)
            stock = new ArrayList<>();
        for(Item i : stock)
            if(i.bundle().equals(bundle))
            {
                i.add(count);
                return;
            }
        stock.add(new Item(bundle, count));
    }

    public static void remove(IngredientBundle bundle, int count)
    {
        if(stock == null)
            stock = new ArrayList<>();
        for(Item i : stock)
            if(i.bundle().equals(bundle))
            {
                i.remove(count);
                if(i.count() <= 0)
                    stock.remove(i);
                return;
            }
    }

    public static void set(IngredientBundle bundle, int count)
    {
        if(stock == null)
            stock = new ArrayList<>();
        for(Item i : stock)
            if(i.bundle().equals(bundle))
            {
                i.count(count);
                if(i.count() <= 0)
                    stock.remove(i);
                return;
            }
        if(count >= 0)
            stock.add(new Item(bundle, count));
    }

    public static int countOf(IngredientBundle bundle)
    {
        if(stock == null)
            stock = new ArrayList<>();
        for(Item i : stock)
            if(i.bundle().equals(bundle))
                return i.count();
        return 0;
    }

    public static List<Item> get()
    {
        if(stock == null)
            stock = new ArrayList<>();
        return stock;
    }

    public static int size()
    {
        return get().size();
    }

    public static boolean canValidate(@NotNull Order o)
    {
        for(Item i : o.items())
            if(countOf(i.bundle()) < i.count())
                return false;
        return true;
    }

    public static void validate(Order o)
    {
        if(canValidate(o))
        {
            for(Item i : o.items())
                remove(i.bundle(), i.count());
        }
    }
}
