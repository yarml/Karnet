package net.harmal.karnet2.core;

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

    public Product(int pid, int unitPrice, String name,
                   List<ProductCategory> catIng  , List<ProductCategory> catTaste,
                   List<ProductCategory> catShape, List<ProductCategory> catExtra)
    {
        this.pid                = pid              ;
        this.unitPrice          = (short) unitPrice;
        this.name               = name             ;
        this.categoryIngredient = catIng           ;
        this.categoryTaste      = catTaste         ;
        this.categoryShape      = catShape         ;
        this.categoryExtra      = catExtra         ;

        if (this.categoryIngredient == null)
            this.categoryIngredient =  new ArrayList<ProductCategory>();
        if (this.categoryTaste      == null)
            this.categoryTaste      =  new ArrayList<ProductCategory>();
        if (this.categoryShape      == null)
            this.categoryShape      =  new ArrayList<ProductCategory>();
        if (this.categoryExtra      == null)
            this.categoryExtra      =  new ArrayList<ProductCategory>();
    }

    public int pid()
    {
        return pid;
    }
    public short unitPrice()
    {
        return unitPrice;
    }
    public String name()
    {
        return name;
    }
    public List<ProductCategory> categoryIngredient()
    {
        return categoryIngredient;
    }
    public List<ProductCategory> categoryTaste()
    {
        return categoryTaste;
    }
    public List<ProductCategory> categoryShape()
    {
        return categoryShape;
    }
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
    public void name(String name)
    {
        this.name = name;
    }
    public void categoryIngredient(List<ProductCategory> ncategory)
    {
        this.categoryIngredient = ncategory;
    }
    public void categoryTaste(List<ProductCategory> ncategory)
    {
        this.categoryTaste = ncategory;
    }
    public void categoryShape(List<ProductCategory> ncategory)
    {
        this.categoryShape = ncategory;
    }
    public void categoryExtra(List<ProductCategory> ncategory)
    {
        this.categoryExtra = ncategory;
    }
}