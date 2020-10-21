package net.harmal.karnet2.core;

import java.util.Objects;

public class ProductCategory
{
    String displayName;

    public ProductCategory(String name)
    {
        this.displayName = name;
        if(name == null)
            this.displayName = "?";
    }

    public boolean equals(ProductCategory o)
    {
        return displayName.equalsIgnoreCase(o.displayName);
    }
}
