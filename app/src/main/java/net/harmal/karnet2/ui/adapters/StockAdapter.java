package net.harmal.karnet2.ui.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.IngredientBundle;
import net.harmal.karnet2.core.Item;
import net.harmal.karnet2.core.Order;
import net.harmal.karnet2.core.ProductIngredient;
import net.harmal.karnet2.core.registers.IngredientRegister;
import net.harmal.karnet2.core.registers.OrderRegister;
import net.harmal.karnet2.core.registers.Stock;
import net.harmal.karnet2.ui.listeners.OnItemInputListener;
import net.harmal.karnet2.utils.Logs;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class StockAdapter extends KarnetRecyclerAdapter<StockAdapter.StockItemHolder>
{
    public interface ItemGetterInterface
    {
        List<Item> getItems();
    }
    public static class StockItemHolder extends KarnetRecyclerViewHolder
    {
        private final TextView    productName;
        private final TextView    count;

        public StockItemHolder(@NonNull View itemView, KarnetRecyclerAdapter<? extends KarnetRecyclerViewHolder> adapter)
        {
            super(itemView, adapter);

            productName = itemView.findViewById(R.id.text_stock_product_name);
            count = itemView.findViewById(R.id.text_stock_count);
        }
    }

    private final ItemGetterInterface items       ;
    private       List<Item>          visibleItems;

    private String                  search;
    private List<ProductIngredient> filter; /* Allowed ingredient */

    public StockAdapter(@NonNull ItemGetterInterface items)
    {
        this.items = items;
        this.visibleItems = new ArrayList<>(items.getItems());
        this.filter = new ArrayList<>(IngredientRegister.get());
        this.search = "";
        visibleItems.sort(new Item.ItemBundleNameComparator());
    }

    @NonNull
    @Override
    public StockItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_stock, parent, false);
        return new StockItemHolder(v, this);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull StockItemHolder holder, int position)
    {
        Item current = visibleItems.get(position);

        int stock = Stock.countOf(current.bundle());
        int count = OrderRegister.countOf(current.bundle());
        int sum = stock - count;

        holder.productName.setText(current.bundle().name());
        holder.count.setText(String.format("%d / %d", stock, sum));
    }

    public void update()
    {
        Logs.debug("Updating stock list");
        this.visibleItems = new ArrayList<>();

        if(filter.size() == 0 && search.equals(""))
        {
            Logs.debug("A blank filter");
            visibleItems.addAll(items.getItems());
            notifyDataSetChanged();
            return;
        }
        Logs.debug("Not a blank filter");
        ITEMS_LOOP:
        for(Item i : items.getItems())
        {
            Logs.debug("New iteration");
            Logs.debug("Iterating for item: " + i.bundle().name());
            for (ProductIngredient filterIngredient : filter)
            {
                Logs.debug("Iterating for ingredient: " + filterIngredient.displayName());
                if(i.bundle().contains(filterIngredient)
                        && i.bundle().name().toLowerCase().contains(search.toLowerCase()))
                {
                    Logs.debug("Adding item");
                    visibleItems.add(i);
                    Logs.debug("Added Item");
                    continue ITEMS_LOOP;
                }
            }
        }
        visibleItems.sort(new Item.ItemBundleNameComparator());
        Logs.debug("Notifying change");
        notifyDataSetChanged();
    }

    public List<Item> visibleItemList()
    {
        return visibleItems;
    }

    @Override
    public int getItemCount()
    {
        return visibleItems.size();
    }

    public List<ProductIngredient> filter()
    {
        return filter;
    }

    public void search(@NonNull String search)
    {
        this.search = search;
        update();
    }

    public String search()
    {
        return this.search;
    }

    public void filter(List<ProductIngredient> filter)
    {
        this.filter = filter;
        update();
    }

    public boolean[] filterArray()
    {
        boolean[] filter = new boolean[IngredientRegister.size()];
        for(int i = 0; i < filter.length; i++)
        {
            filter[i] = false;
            ProductIngredient ingredient = IngredientRegister.get().get(i);
            for(ProductIngredient ingredientFilter : this.filter)
            {
                if(ingredientFilter.equals(ingredient))
                {
                    filter[i] = true;
                    break;
                }
            }
        }
        return filter;
    }

    public void filterArray(@NotNull boolean[] newFilter)
    {
        if(newFilter.length != IngredientRegister.size())
            throw new IllegalArgumentException("New filter array length should be the" +
                    " same as the ingredient register size");
        filter = new ArrayList<>();
        for(int i = 0; i < newFilter.length; i++)
            if(newFilter[i])
                filter.add(IngredientRegister.get().get(i));
        update();
    }
}
