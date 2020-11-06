package net.harmal.karnet2.core.registers;

import net.harmal.karnet2.core.Order;
import net.harmal.karnet2.core.Product;
import net.harmal.karnet2.core.ProductCategory;
import net.harmal.karnet2.core.Stack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.ArrayList;

public class ProductRegister
{

    public static int productIdCount = 0;

    private static List<Product> productRegister;

    // Returns PID
    public static int add(int unitPrice, @NotNull String name,
                          @NotNull ProductCategory base  , @NotNull ProductCategory fat, ProductCategory shape,
                          @NotNull ProductCategory type, @NotNull List<ProductCategory> extra                 )
    {
        return add(productIdCount++, unitPrice, name, base, fat, shape, type, extra);
    }
    // Returns PID
    public static int add(int pid, int unitPrice, @NotNull String name,
                          @NotNull ProductCategory base  , @NotNull ProductCategory fat, ProductCategory shape,
                          @NotNull ProductCategory type, @NotNull List<ProductCategory> extra                 )
    {
        if(productRegister == null)
            productRegister = new ArrayList<Product>();
        if(pid < 0) // PID must be positive
        {
            return add(0, unitPrice, name, base, fat, shape, type, extra);
        }
        for(Product p : productRegister) // Make sure the CID is unique
            if(p.pid() == pid)
            {
                return add(pid + 1, unitPrice, name, base, fat, shape, type, extra);
            }
        productRegister.add(new Product(pid,  unitPrice, name, base, fat, shape, type, extra));
        return pid;
    }

    public static void remove(int pid)
    {
        if(productRegister == null)
            productRegister = new ArrayList<>();

        for(Product p : productRegister)
            if(p.pid() == pid)
            {
                productRegister.remove(p);
                break;
            }
        for(Order o : OrderRegister.get())
        {
            for(Stack s : o.stacks())
                if(s.pid() == pid)
                {
                    o.stacks().remove(s);
                }
            if(o.stacks().size() == 0)
                OrderRegister.remove(o.oid());
        }
    }

    @Nullable
    public static Product getProduct(int pid)
    {
        if(productRegister == null)
            productRegister = new ArrayList<>();
        for(Product p : productRegister)
            if(p.pid() == pid)
                return p;
        return null;
    }

    @NotNull
    public static List<Product> get()
    {
        if(productRegister == null)
            productRegister = new ArrayList<>();
        return productRegister;
    }

    @NotNull
    public static List<ProductCategory> getIngredients()
    {
        if(productRegister == null)
            productRegister = new ArrayList<>();
        List<ProductCategory> ingredients = new ArrayList<>();
        for(Product p : productRegister)
            if(!ingredients.contains(p.baseIngredient()))
                ingredients.add(p.baseIngredient());
        return ingredients;
    }
    @NotNull
    public static List<ProductCategory> getFats()
    {
        if(productRegister == null)
            productRegister = new ArrayList<>();
        List<ProductCategory> fats = new ArrayList<>();
        for(Product p : productRegister)
            if(!fats.contains(p.fat()))
                fats.add(p.fat());
        return fats;
    }
    @NotNull
    public static List<ProductCategory> getShape()
    {
        if(productRegister == null)
            productRegister = new ArrayList<>();
        List<ProductCategory> shapes = new ArrayList<>();
        for(Product p : productRegister)
            if(!shapes.contains(p.shape()))
                shapes.add(p.shape());
        return shapes;
    }
    @NotNull
    public static List<ProductCategory> getType()
    {
        if(productRegister == null)
            productRegister = new ArrayList<>();
        List<ProductCategory> types = new ArrayList<>();
        for(Product p : productRegister)
            if(!types.contains(p.type()))
                types.add(p.type());
        return types;
    }
    @NotNull
    public static List<ProductCategory> getExtras()
    {
        if(productRegister == null)
            productRegister = new ArrayList<>();
        List<ProductCategory> extras = new ArrayList<>();
        for(Product p : productRegister)
            for(ProductCategory extra : p.extra())
                if(!extras.contains(extra))
                    extras.add(extra);
        return extras;
    }
}
