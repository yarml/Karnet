package net.harmal.karnet2.core;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Order
{
    private int         oid          ;
    private int         cid          ;
    private int         deliveryPrice;
    private List<Stack> stacks       ;
    private Date        dueDate      ;

    public Order(int oid, int cid, int deliveryPrice, @NotNull List<Stack> stacks, @NotNull Date dueDate)
    {
        this.oid           = oid          ;
        this.cid           = cid          ;
        this.deliveryPrice = deliveryPrice;
        this.stacks        = stacks       ;
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
    public List<Stack> stacks()
    {
        return stacks;
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
    public void stacks(@NotNull List<Stack> nstacks)
    {
        stacks = nstacks;
    }
    public void dueDate(@NotNull Date ndate)
    {
        this.dueDate = ndate;
    }

    public void add(int pid, int count)
    {
        boolean added = false;
        for(Stack s : stacks)
            if(s.pid() == pid)
            {
                s.add(count);
                added = true;
            }
        if(!added)
            stacks.add(new Stack(pid, count));
    }

    public void remove(int pid, int count)
    {
        for(Stack s : stacks)
            if(s.pid() == pid)
            {
                s.remove(count);
                if(s.count() == 0)
                    stacks.remove(s);
            }
    }
}
