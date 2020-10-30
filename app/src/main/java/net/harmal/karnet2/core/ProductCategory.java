package net.harmal.karnet2.core;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ProductCategory
{
    private String displayName;

    public ProductCategory(@NotNull String name)
    {
        this.displayName = name;
    }

    public boolean equals(@NotNull ProductCategory o)
    {
        return displayName.equalsIgnoreCase(o.displayName);
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
}
