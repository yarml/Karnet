package net.harmal.karnet2.core;

public class Stack
{
    private int pid  ;
    private int count;

    public Stack(int pid, int count)
    {
        this.pid = pid    ;
        this.count = count;
    }

    public Stack()
    {
        pid   = 0;
        count = 0;
    }

    public int pid()
    {
        return pid;
    }
    public int count()
    {
        return count;
    }

    public void pid(int npid)
    {
        pid = npid;
    }
    public void count(int ncount)
    {
        count = ncount;
    }

    public void add(int a)
    {
        count += a;
    }

    public void remove(int r)
    {
        count -= r;
        if(count < 0)
            count = 0;
    }
}
