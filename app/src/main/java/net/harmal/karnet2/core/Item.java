package net.harmal.karnet2.core;


import androidx.annotation.Nullable;

import net.harmal.karnet2.savefile.Savable;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Comparator;

public class Item implements Savable
{
    private IngredientBundle bundle;
    private int              count ;

    public Item(IngredientBundle bundle, int count)
    {
        this.bundle = bundle;
        this.count  = count ;
    }

    public IngredientBundle bundle()
    {
        return bundle;
    }
    public int count()
    {
        return count;
    }

    public void bundle(IngredientBundle nbundle)
    {
        bundle = nbundle;
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

    @Override
    public boolean equals(Object o)
    {
        if(o == null)
            return false;
        if(!(o instanceof  Item))
            return false;
        return bundle == ((Item) o).bundle && count == ((Item) o).count;
    }

    @Override
    public void writeData(DataOutputStream stream) throws IOException
    {
        bundle.writeData(stream);
        stream.writeInt(count);
    }

    public static class ItemBuilder implements Savable.BUILDER<Item>
    {
        @Nullable
        @Override
        public Item readData(int version, ByteBuffer buffer)
        {
            IngredientBundle.IngredientBundleBuilder builder = new IngredientBundle
                    .IngredientBundleBuilder();
            IngredientBundle bundle = builder.readData(version, buffer);
            if(bundle == null)
                return null;
            int count = buffer.getInt();
            return new Item(bundle, count);
        }
    }
    public static class ItemBundleNameComparator implements Comparator<Item>
    {
        @Override
        public int compare(Item o1, Item o2)
        {
            return o1.bundle.name().compareToIgnoreCase(o2.bundle.name());
        }
    }
}
