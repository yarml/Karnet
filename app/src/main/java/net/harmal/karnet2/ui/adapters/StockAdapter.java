package net.harmal.karnet2.ui.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.Product;
import net.harmal.karnet2.core.Stack;
import net.harmal.karnet2.core.registers.ProductRegister;
import net.harmal.karnet2.core.registers.Stock;
import net.harmal.karnet2.ui.listeners.OnItemInputListener;

import java.util.List;

public class StockAdapter extends KarnetRecyclerAdapter<StockAdapter.StockItemHolder>
{
    public class StockItemHolder extends KarnetRecyclerViewHolder
    {
        private TextView    productName;
        private TextView    count;

        public StockItemHolder(@NonNull View itemView, OnItemInputListener listener)
        {
            super(itemView, listener);

            productName = itemView.findViewById(R.id.text_stock_product_name);
            count = itemView.findViewById(R.id.text_stock_count);
        }
    }

    private final List<Product> products;

    public StockAdapter(@NonNull List<Product> products)
    {
        this.products = products;
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
        Product current = products.get(position);

        holder.productName.setText(current.name());
        holder.count.setText(String.format("%d", Stock.countOf(current.pid())));
    }

    @Override
    public int getItemCount()
    {
        return products.size();
    }
}
