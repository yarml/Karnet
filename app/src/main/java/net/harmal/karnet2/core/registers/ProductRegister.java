package net.harmal.karnet2.core.registers;

import net.harmal.karnet2.core.Customer;
import net.harmal.karnet2.core.Product;
import net.harmal.karnet2.core.ProductCategory;

import java.util.List;
import java.util.ArrayList;

public class ProductRegister
{

    public static int productIdCount = 0;

    private static List<Product> productRegister;

    public static void add(int unitPrice, String name,
                           List<ProductCategory> catIng  , List<ProductCategory> catTaste,
                           List<ProductCategory> catShape, List<ProductCategory> catExtra)
    {
        add(productIdCount++, unitPrice, name, catIng, catTaste, catShape, catExtra);
    }

    public static void add(int pid, int unitPrice, String name,
                           List<ProductCategory> catIng  , List<ProductCategory> catTaste,
                           List<ProductCategory> catShape, List<ProductCategory> catExtra)
    {
        if(pid < 0) // PID must be positive
        {
            add(0, unitPrice, name, catIng, catTaste, catShape, catExtra);
            return;
        }
        for(Product p : productRegister) // Make sure the CID is unique
            if(p.pid() == pid)
            {
                add(pid + 1, unitPrice, name, catIng, catTaste, catShape, catExtra);
                return;
            }
        if(productRegister == null)
            productRegister = new ArrayList<Product>();
        productRegister.add(new Product(pid,  unitPrice, name, catIng, catTaste, catShape, catExtra));
    }

    public static List<ProductCategory> ingredientCategories()
    {
        List<ProductCategory> ing = new ArrayList<>();
        for(Product p : productRegister)
            for(ProductCategory c : p.categoryIngredient())
            {
                boolean exists = false;
                for(ProductCategory i : ing)
                    if(c.equals(i))
                    {
                        exists = true;
                        break;
                    }
                if(!exists)
                    ing.add(c);
            }
        return ing;
    }


}
