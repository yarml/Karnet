package net.harmal.karnet2.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.ProductIngredient;
import net.harmal.karnet2.ui.listeners.OnItemInputListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class IngredientListAdapter extends KarnetRecyclerAdapter<IngredientListAdapter.IngredientViewHolder>
{
    public static class IngredientViewHolder extends KarnetRecyclerViewHolder
    {
        TextView    ingredientNameText;
        ImageButton deleteBtn         ;
        public IngredientViewHolder(@NonNull View itemView, OnItemInputListener listener)
        {
            super(itemView, listener);
            ingredientNameText = itemView.findViewById(R.id.text_ingredient_name );
            deleteBtn          = itemView.findViewById(R.id.btn_ingredient_delete);
        }
    }

    @NotNull
    private final List<ProductIngredient> list         ;
    @NotNull
    private final List<ProductIngredient> displayedList;

    public IngredientListAdapter(@NotNull List<ProductIngredient> list)
    {
        this.list     = list             ;
        displayedList = new ArrayList<>();
        displayedList.addAll(list);
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_ingredient, parent, false);
        return new IngredientViewHolder(v, onItemInputListener);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position)
    {
        ProductIngredient i = displayedList.get(position);
        holder.ingredientNameText.setText(i.displayName());
    }

    @Override
    public int getItemCount()
    {
        return displayedList.size();
    }

    public void update()
    {
        displayedList.clear();
        displayedList.addAll(list);
        notifyDataSetChanged();
    }

    @NotNull
    public List<ProductIngredient> getVisibleItems()
    {
        return displayedList;
    }
}
