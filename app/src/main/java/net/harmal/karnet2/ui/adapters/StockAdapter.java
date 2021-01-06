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
import net.harmal.karnet2.core.ProductIngredient;
import net.harmal.karnet2.core.registers.IngredientRegister;
import net.harmal.karnet2.core.registers.Stock;
import net.harmal.karnet2.ui.listeners.OnItemInputListener;

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

        holder.productName.setText(current.bundle().name());
        holder.count.setText(String.format("%d", Stock.countOf(current.bundle())));
    }

    public void update()
    {
        this.visibleItems = new ArrayList<>();

        MAIN_LOOP:
        for(Item i : items)
        {
            IngredientBundle bundle = i.bundle();
            if(filterAllow(bundle.basePiid(  ))
            || filterAllow(bundle.fatPiid(   ))
            || filterAllow(bundle.shapePiid( ))
            || filterAllow(bundle.tastePiid()))
            {
                visibleItems.add(i);
                continue;
            }
            for(int extraPiid : bundle.extrasPiid())
                if(filterAllow(extraPiid))
                {
                    visibleItems.add(i);
                    continue MAIN_LOOP;
                }
        }
        notifyDataSetChanged();
    }

    private boolean filterAllow(int piid)
    {
        ProductIngredient p = IngredientRegister.getIngredient(piid);
        assert p != null;
        return filter.contains(p);
    }

    public List<Item> visibleItemList()
    {
        return items;
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
}
