package net.harmal.karnet2.core;

import net.harmal.karnet2.savefile.Savable;
import net.harmal.karnet2.savefile.Utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ProductCategory implements Savable
{
    private String displayName;

    public ProductCategory(@NotNull String name)
    {
        this.displayName = name;
    }

    @Contract(value = "null -> true", pure = true)
    @Override
    public boolean equals(Object o)
    {
        if(o == null)
            return true;
        if(o.getClass() == getClass())
            return displayName.equalsIgnoreCase(((ProductCategory)o).displayName);
        return  false;
    }

    @Override
    public String toString() {
        return displayName();
    }

    @NotNull
    public String displayName()
    {
        return displayName;
    }

    public void displayName(@NotNull String nname)
    {
        displayName = nname;
    }

    public ProductCategory clone()
    {
        return new ProductCategory(displayName);
    }

    @Override
    public void writeData(DataOutputStream stream) throws IOException
    {
        Utils.writeString(displayName, stream);
    }

    public static class ProductCategoryBuilder implements BUILDER<ProductCategory>
    {
        @Override
        public ProductCategory readData(int version, ByteBuffer buffer)
        {
            return new ProductCategory(Utils.readString(buffer));
        }
    }
}
