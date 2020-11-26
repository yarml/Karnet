package net.harmal.karnet2.core.registers;

import net.harmal.karnet2.core.Order;
import net.harmal.karnet2.core.Stack;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Stock
{
    private static List<Stack> stock;

    public static void add(int pid, int count)
    {
        if(stock == null)
            stock = new ArrayList<>();
        for(Stack s : stock)
            if(s.pid() == pid)
            {
                s.add(count);
                return;
            }
        stock.add(new Stack(pid, count));
    }

    public static void remove(int pid, int count)
    {
        if(stock == null)
            stock = new ArrayList<>();
        for(Stack s : stock)
            if(s.pid() == pid)
            {
                s.remove(count);
                if(s.count() <= 0)
                    stock.remove(s);
                return;
            }
    }

    public static void set(int pid, int count)
    {
        if(stock == null)
            stock = new ArrayList<>();
        for(Stack s : stock)
            if(s.pid() == pid)
            {
                s.count(count);
                if(s.count() <= 0)
                    stock.remove(s);
                return;
            }
        if(count >= 0)
            stock.add(new Stack(pid, count));
    }

    public static int countOf(int pid)
    {
        if(stock == null)
            stock = new ArrayList<>();
        for(Stack s : stock)
            if(s.pid() == pid)
                return s.count();
        return 0;
    }

    public static List<Stack> get()
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
        for(Stack s : o.stacks())
            if(countOf(s.pid()) < s.count())
                return false;
        return true;
    }

    public static void validate(Order o)
    {
        if(!canValidate(o))
            throw new IllegalStateException("Inssuficient stock");
        for(Stack s : o.stacks())
        {
            remove(s.pid(), s.count());
        }
    }
}
