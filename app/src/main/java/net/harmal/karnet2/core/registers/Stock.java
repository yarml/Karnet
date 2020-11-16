package net.harmal.karnet2.core.registers;

import net.harmal.karnet2.core.Stack;

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
}
