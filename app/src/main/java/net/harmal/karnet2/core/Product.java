package net.harmal.karnet2.core;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Product
{
    private int          pid                    ;
    private short        unitPrice              ;
    private String       name                   ;

    private ProductCategory       baseIngredient;
    private ProductCategory       fat           ;
    private ProductCategory       shape         ;
    private ProductCategory       type          ;
    private List<ProductCategory> extra         ;

    public Product(int pid, int unitPrice, @NotNull String name,
                   @NotNull ProductCategory base, @NotNull ProductCategory fat, @NotNull ProductCategory shape,
                   @NotNull ProductCategory type, @NotNull List<ProductCategory> extra                        )
    {
        this.pid                = pid              ;
        this.unitPrice          = (short) unitPrice;
        this.name               = name             ;
        this.baseIngredient = base ;
        this.fat            = fat  ;
        this.shape          = shape;
        this.type           = type ;
        this.extra          = extra;
    }

    public int pid()
    {
        return pid;
    }

    public void pid(int pid)
    {
        this.pid = pid;
    }

    public short unitPrice()
    {
        return unitPrice;
    }

    public void unitPrice(short unitPrice)
    {
        this.unitPrice = unitPrice;
    }

    public String name()
    {
        return name;
    }

    public void name(String name)
    {
        this.name = name;
    }

    public ProductCategory baseIngredient() {

        return baseIngredient;
    }

    public void baseIngredient(ProductCategory baseIngredient)
    {
        this.baseIngredient = baseIngredient;
    }

    public ProductCategory fat()
    {
        return fat;
    }

    public void fat(ProductCategory fat)
    {
        this.fat = fat;
    }

    public ProductCategory shape()
    {
        return shape;
    }

    public void shape(ProductCategory shape)
    {
        this.shape = shape;
    }

    public ProductCategory type()
    {
        return type;
    }

    public void type(ProductCategory type)
    {
        this.type = type;
    }

    public List<ProductCategory> extra()
    {
        return extra;
    }

    public void extra(List<ProductCategory> extra)
    {
        this.extra = extra;
    }
}
