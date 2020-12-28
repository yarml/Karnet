package net.harmal.karnet2.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.Product;
import net.harmal.karnet2.core.ProductCategory;
import net.harmal.karnet2.ui.Animations;
import net.harmal.karnet2.ui.listeners.OnItemInputListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ProductListAdapter extends KarnetRecyclerAdapter<ProductListAdapter.ProductListViewHolder>
{
    public static class ProductListViewHolder extends KarnetRecyclerViewHolder
    {
        private final TextView    productName;
        private final TextView    priceStats ;
        private final ImageButton deleteBtn  ;

        public ProductListViewHolder(@NonNull View itemView, OnItemInputListener listener)
        {
            super(itemView, listener);

            productName = itemView.findViewById(R.id.text_product_name       );
            priceStats  = itemView.findViewById(R.id.text_product_price_stats);
            deleteBtn   = itemView.findViewById(R.id.btn_product_delete      );
        }
    }

    private ProductCategory filterBase;
    private ProductCategory filterFat;
    private ProductCategory filterShape;
    private ProductCategory filterType;
    private List<ProductCategory> filterExtra;

    @NotNull
    private final List<Product> productList;

    private final List<Product> visibleProducts;

    public ProductListAdapter(@NotNull List<Product> productList)
    {
        this.productList = productList;
        visibleProducts = new ArrayList<>();
        visibleProducts.addAll(productList);
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
        Product current = visibleProducts.get(position);

        holder.productName.setText(current.name(                          ));
        holder.priceStats.setText(String.format("%s ", current.unitPrice()));
        if(holder.deleteBtn.getVisibility() == View.VISIBLE)
            Animations.popOut(holder.deleteBtn);
    }

    @Override
    public int getItemCount()
    {
        return visibleProducts.size();
    }

    @NotNull
    public List<Product> getProductList()
    {
        return productList;
    }

    public List<Product> getVisibleProducts()
    {
        return visibleProducts;
    }

    public void update()
    {
        visibleProducts.clear();
        for(Product p : productList)
        {
            if(p.baseIngredient().equals(filterBase) && p.fat().equals(filterFat)
                && p.shape().equals(filterShape) && p.type().equals(filterType))
            {
                if(filterExtra == null)
                    visibleProducts.add(p);
                else
                    for(ProductCategory c : p.extra())
                        if(filterExtra.contains(c))
                        {
                            visibleProducts.add(p);
                            break;
                        }
            }
        }
        notifyDataSetChanged();
    }

    public void filterBase(ProductCategory category)
    {
        filterBase = category;
        update();
    }

    public void filterFat(ProductCategory category)
    {
        filterFat = category;
        update();
    }

    public void filterShape(ProductCategory category)
    {
        filterShape = category;
        update();
    }

    public void filterType(ProductCategory category)
    {
        filterType = category;
        update();
    }

    public void filterExtras(@NonNull List<ProductCategory> categories)
    {
        filterExtra = categories;
        update();
    }
}
