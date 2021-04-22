package net.harmal.karnet2.core.registers;

import net.harmal.karnet2.core.IngredientBundle;
import net.harmal.karnet2.core.Item;
import net.harmal.karnet2.core.Order;
import net.harmal.karnet2.core.ProductIngredient;
import net.harmal.karnet2.utils.Logs;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Stock
{
    public static int VALIDATE_IMPOSSIBLE = 0;
    public static int VALIDATE_POSSIBLE   = 1;
    public static int VALIDATE_WARN       = 2;

    private static List<Item> stock;

    public static void add(@Nullable Item i)
    {
        if(i == null)
            return;
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

    @NotNull
    public static List<Item> get()
    {
        if(stock == null)
            stock = new ArrayList<>();
        List<Item> returnedStock = new ArrayList<>();
        for(IngredientBundle bundle : IngredientRegister.allPossibleIngredients())
        {
            boolean exists = false;
            for(Item i : stock)
                if(i.bundle().equals(bundle))
                {
                    exists = true;
                    returnedStock.add(i);
                    break;
                }
            if(!exists)
                returnedStock.add(new Item(bundle, 0));
        }
        for(Item i : stock)
            if(!returnedStock.contains(i))
                returnedStock.add(i);
        return returnedStock;
    }

    public static int size()
    {
        return get().size();
    }

    public static int canValidate(@NotNull Order o)
    {
        for(Item i : o.items())
            if(countOf(i.bundle()) < i.count())
                return VALIDATE_IMPOSSIBLE;
        for(Item i : o.items())

            if (countOf(i.bundle()) - OrderRegister.countOf(i.bundle()) < 0)
                return VALIDATE_WARN;

        return VALIDATE_POSSIBLE;
    }

    public static void validate(Order o)
    {
        if(canValidate(o) != VALIDATE_IMPOSSIBLE)
            for(Item i : o.items())
                remove(i.bundle(), i.count());
    }
    public static List<Item> items()
    {
        if(stock == null)
            stock = new ArrayList<>();
        return stock;
    }
    public static void clear()
    {
        if(stock == null)
            stock = new ArrayList<>();
        stock.clear();
    }
}
