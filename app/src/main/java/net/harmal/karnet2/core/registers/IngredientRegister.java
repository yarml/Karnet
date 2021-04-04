package net.harmal.karnet2.core.registers;

import android.os.Bundle;

import net.harmal.karnet2.core.IngredientBundle;
import net.harmal.karnet2.core.Item;
import net.harmal.karnet2.core.Order;
import net.harmal.karnet2.core.ProductIngredient;
import net.harmal.karnet2.core.Trash;
import net.harmal.karnet2.utils.Logs;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class IngredientRegister
{
    public static int ingredientIdCount = 0;

    private static List<ProductIngredient> ingredients;

    public static int add(@NotNull ProductIngredient i)
    {
        return add(i.piid(), i.price(), i.displayName(), i.type());
    }

    public static int add(int price, @NotNull String name, ProductIngredient.Type type)
    {
        return add(ingredientIdCount++, price, name, type);
    }

    public static int add(int piid, int price, @NotNull String name, ProductIngredient.Type type)
    {
        if(ingredients == null)
            ingredients = new ArrayList<>();
        if(piid < 0)
        {
            return add(0, price, name, type);
        }
        for(ProductIngredient p : ingredients)
            if(p.piid() == piid)
            {
                return add(piid + 1, price, name, type);
            }
        ingredients.add(new ProductIngredient(piid,  price, name, type));
        return piid;
    }

    public static void remove(int piid)
    {
        if(ingredients == null)
            ingredients = new ArrayList<>();
        List<IngredientBundle> bundlesToRemoveFromStock = new ArrayList<>();
        for(ProductIngredient p : ingredients)
            if(p.piid() == piid)
            {
                Logs.debug("Removing ingredient with piid: " + piid + ", name: " + p.displayName());
                for(Item i : Stock.items())
                {
                    if(i.bundle().contains(piid))
                        bundlesToRemoveFromStock.add(i.bundle());
                }
                for(IngredientBundle b : bundlesToRemoveFromStock)
                    Stock.set(b, 0);
                for(Order o : OrderRegister.get())
                {
                    List<Item> itemsToRemove = new ArrayList<>();
                    for(Item i : o.items())
                    {
                        if(i.bundle().contains(piid))
                            itemsToRemove.add(i);
                    }
                    for(Item i : itemsToRemove)
                        o.remove(i);
                }
                Trash.pushIngredient(p);
                ingredients.remove(p);
                return;
            }
    }

    public static boolean exists(int piid)
    {
        return getIngredient(piid) != null;
    }

    @Nullable
    public static ProductIngredient getIngredient(int piid)
    {
        if(ingredients == null)
            ingredients = new ArrayList<>();
        for(ProductIngredient p : ingredients)
            if(p.piid() == piid)
                return p;
        Logs.debug("No ingredient with piid: " + piid + " found");
        Logs.debug("Available piids: ");
        for(ProductIngredient p : ingredients)
        {
            Logs.debug(p.displayName() + ": " + p.piid());
        }
        return null;
    }

    @NotNull
    public static List<ProductIngredient> get()
    {
        if(ingredients == null)
            ingredients = new ArrayList<>();
        return ingredients;
    }
    public static int size()
    {
        return get().size();
    }

    @NotNull
    public static List<ProductIngredient> onlyType(ProductIngredient.Type type)
    {
        if(ingredients == null)
            ingredients = new ArrayList<>();
        List<ProductIngredient> ingredientsType = new ArrayList<>();
        for(ProductIngredient p : IngredientRegister.ingredients)
            if(p.type() == type)
                ingredientsType.add(p);
        return ingredientsType;
    }

    @NotNull
    public static List<String> toStringList(@NotNull List<ProductIngredient> ingredientList)
    {
        List<String> list = new ArrayList<>();
        for(ProductIngredient p : ingredientList)
            list.add(p.displayName());
        return list;
    }

    /*
     * Returns all possible ingredients with at most one extra
     */
    @NotNull
    public static List<IngredientBundle> allPossibleIngredients()
    {
        if(notEnoughIngredients())
            return new ArrayList<>();
        List<IngredientBundle> bundles = new ArrayList<>();
        for(ProductIngredient base : onlyType(ProductIngredient.Type.BASE))
            for(ProductIngredient fat : onlyType(ProductIngredient.Type.FAT))
                for(ProductIngredient shape : onlyType(ProductIngredient.Type.SHAPE))
                    for(ProductIngredient taste : onlyType(ProductIngredient.Type.TASTE))
                    {
                        IngredientBundle bundle = new IngredientBundle(base.piid(), fat.piid(),
                                shape.piid(), taste.piid(), new ArrayList<>());
                        if(!bundles.contains(bundle))
                            bundles.add(bundle);
                        for(ProductIngredient extra : onlyType(ProductIngredient.Type.EXTRA))
                        {
                            ArrayList<Integer> extras = new ArrayList<>();
                            extras.add(extra.piid());
                            IngredientBundle bundleWithExtra =  new IngredientBundle(base.piid(),
                                    fat.piid(), shape.piid(), taste.piid(), extras);
                            if(!bundles.contains(bundleWithExtra))
                                bundles.add(bundleWithExtra);
                        }
                    }
        return bundles;
    }

    public static boolean notEnoughIngredients()
    {
        return onlyType(ProductIngredient.Type.BASE).size() == 0
                || onlyType(ProductIngredient.Type.FAT).size() == 0
                || onlyType(ProductIngredient.Type.SHAPE).size() == 0
                || onlyType(ProductIngredient.Type.TASTE).size() == 0;
    }

}
