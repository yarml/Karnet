package net.harmal.karnet2.core;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Product
{
    private int          pid                        ;
    private short        unitPrice                  ;
    private String       name                       ;
    private List<ProductCategory> categoryIngredient;
    private List<ProductCategory> categoryTaste     ;
    private List<ProductCategory> categoryShape     ;
    private List<ProductCategory> categoryExtra     ;

    public Product(int pid, int unitPrice, @NotNull String name,
                   @NotNull List<ProductCategory> catIng  , @NotNull List<ProductCategory> catTaste,
                   @NotNull List<ProductCategory> catShape, @NotNull List<ProductCategory> catExtra)
    {
        this.pid                = pid              ;
        this.unitPrice          = (short) unitPrice;
        this.name               = name             ;
        this.categoryIngredient = catIng           ;
        this.categoryTaste      = catTaste         ;
        this.categoryShape      = catShape         ;
        this.categoryExtra      = catExtra         ;
    }

    public int pid()
    {
        return pid;
    }
    public short unitPrice()
    {
        return unitPrice;
    }
    @NotNull
    public String name()
    {
        return name;
    }
    @NotNull
    public List<ProductCategory> categoryIngredient()
    {
        return categoryIngredient;
    }
    @NotNull
    public List<ProductCategory> categoryTaste()
    {
        return categoryTaste;
    }
    @NotNull
    public List<ProductCategory> categoryShape()
    {
        return categoryShape;
    }
    @NotNull
    public List<ProductCategory> categoryExtra()
    {
        return categoryExtra;
    }

    public void pid(int pid)
    {
        this.pid = pid;
    }
    public void unitPrice(int unitPrice)
    {
        this.unitPrice = (short) unitPrice;
    }
    public void name(@NotNull String name)
    {
        this.name = name;
    }
    public void categoryIngredient(List<ProductCategory> ncategory)
    {
        this.categoryIngredient = ncategory;
    }
    public void categoryTaste(@NotNull List<ProductCategory> ncategory)
    {
        this.categoryTaste = ncategory;
    }
    public void categoryShape(@NotNull List<ProductCategory> ncategory)
    {
        this.categoryShape = ncategory;
    }
    public void categoryExtra(@NotNull List<ProductCategory> ncategory)
    {
        this.categoryExtra = ncategory;
    }
}