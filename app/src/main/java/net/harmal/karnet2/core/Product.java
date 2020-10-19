package net.harmal.karnet2.core;

import java.util.ArrayList;
import java.util.List;

public class Product
{
    private int          pid               ;
    private short        unitPrice         ;
    private String       name              ;
    private List<String> categoryIngredient;
    private List<String> categoryTaste     ;
    private List<String> categoryShape     ;
    private List<String> categoryExtra     ;

    public Product(int pid, int unitPrice, String name,
                   List<String> catIng, List<String> catTaste,
                   List<String> catShape, List<String> catExtra)
    {
        this.pid                = pid              ;
        this.unitPrice          = (short) unitPrice;
        this.name               = name             ;
        this.categoryIngredient = catIng           ;
        this.categoryTaste      = catTaste         ;
        this.categoryShape      = catShape         ;
        this.categoryExtra      = catExtra         ;

        if(this.categoryIngredient == null)
            this.categoryIngredient = new ArrayList<String>();
        if(this.categoryTaste == null)
            this.categoryTaste = new ArrayList<String>();
        if(this.categoryShape == null)
            this.categoryShape = new ArrayList<String>();
        if(this.categoryExtra == null)
            this.categoryExtra = new ArrayList<String>();
    }
}
