package net.harmal.karnet2.ui.adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.harmal.karnet2.ui.listeners.OnItemInputListener;
import net.harmal.karnet2.utils.Logs;

import org.jetbrains.annotations.NotNull;

public abstract class KarnetRecyclerViewHolder extends RecyclerView.ViewHolder
{
    private final OnItemInputListener l;

    public KarnetRecyclerViewHolder(@NonNull View itemView, @NotNull KarnetRecyclerAdapter<? extends KarnetRecyclerViewHolder> adapter)
    {
        super(itemView);
        this.l = adapter.onItemInputListener;

        Logs.debug("Creating Holder");

        ViewGroup vg = (ViewGroup) itemView;
        addListenerToChildren(vg);
        itemView.setOnClickListener(this::onClick);
        itemView.setOnLongClickListener(this::onLongClick);
    }

    private void addListenerToChildren(@NotNull ViewGroup vg)
    {
        for(int i = 0; i < vg.getChildCount(); i++)
        {
            View v = vg.getChildAt(i);
            v.setOnClickListener(this::onClick);
            v.setOnLongClickListener(this::onLongClick);
            if(v instanceof ViewGroup)
                addListenerToChildren((ViewGroup) v);
        }
    }

    private void onClick(View v)
    {
        Logs.debug("Recycler holder child view clicked");
        int position = getAdapterPosition();
        if (l != null)
            if (position != RecyclerView.NO_POSITION)
                l.onItemClick(v, position);
        Logs.debug("Recycler click event called");
    }

    private boolean onLongClick(View v)
    {
        Logs.debug("Recycler holder child view long clicked");

        int position = getAdapterPosition();
        if (l != null)
            if (position != RecyclerView.NO_POSITION)
                l.onItemLongClick(v, position);
        return true;
    }
}
