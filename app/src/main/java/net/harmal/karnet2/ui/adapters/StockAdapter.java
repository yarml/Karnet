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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class StockAdapter extends KarnetRecyclerAdapter<StockAdapter.StockItemHolder>
{
    public static class StockItemHolder extends KarnetRecyclerViewHolder
    {
        private final TextView    productName;
        private final TextView    count;

        public StockItemHolder(@NonNull View itemView, OnItemInputListener listener)
        {
            super(itemView, listener);

            productName = itemView.findViewById(R.id.text_stock_product_name);
            count = itemView.findViewById(R.id.text_stock_count);
        }
    }

    private final List<Item> items        ;
    private       List<Item> visibleItems ;

    private List<ProductIngredient> filter; /* Allowed ingredient */

    public StockAdapter(@NonNull List<Item> products)
    {
        this.items = products;
        this.visibleItems = new ArrayList<>(products);
        this.filter = new ArrayList<>(IngredientRegister.get());
    }

    @NonNull
    @Override
    public StockItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_stock, parent, false);
        return new StockItemHolder(v, onItemInputListener);
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
        this.visibleItems = new ArrayList<>();

        if(filter.size() == 0)
        {
            visibleItems.addAll(items);
            notifyDataSetChanged();
            return;
        }

        for(Item i : items)
            for(ProductIngredient filterIngredient : filter)
                if(i.bundle().contains(filterIngredient))
                {
                    visibleItems.add(i);
                    break;
                }
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
