package net.harmal.karnet2.core;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ProductCategory
{
    private String displayName;

    public ProductCategory(@NotNull String name)
    {
        this.displayName = name;
    }

    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(Object o)
    {
        if(o == null)
            return false;
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
}
