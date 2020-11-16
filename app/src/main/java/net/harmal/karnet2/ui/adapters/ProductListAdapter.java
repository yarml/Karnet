package net.harmal.karnet2.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.Product;
import net.harmal.karnet2.ui.Animations;
import net.harmal.karnet2.ui.listeners.OnItemInputListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ProductListAdapter extends KarnetRecyclerAdapter<ProductListAdapter.ProductListViewHolder>
{
    public class ProductListViewHolder extends KarnetRecyclerViewHolder
    {
        private TextView    productName;
        private TextView    priceStats ;
        private ImageButton deleteBtn  ;

        public ProductListViewHolder(@NonNull View itemView, OnItemInputListener listener)
        {
            super(itemView, listener);

            productName = itemView.findViewById(R.id.text_product_name       );
            priceStats  = itemView.findViewById(R.id.text_product_price_stats);
            deleteBtn   = itemView.findViewById(R.id.btn_product_delete      );
        }
    }

    @NotNull
    private final List<Product> productList;

    public ProductListAdapter(@NotNull List<Product> productList)
    {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_product, parent, false);
        return new ProductListViewHolder(v, onItemInputListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListViewHolder holder, int position)
    {
        Product current = productList.get(position);

        holder.productName.setText(current.name(                          ));
        holder.priceStats.setText(String.format("%s ", current.unitPrice()));
        if(holder.deleteBtn.getVisibility() == View.VISIBLE)
            Animations.popOut(holder.deleteBtn);
    }

    @Override
    public int getItemCount()
    {
        return productList.size();
    }
}
