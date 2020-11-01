package net.harmal.karnet2.ui.adapters;

import androidx.recyclerview.widget.RecyclerView;

import net.harmal.karnet2.ui.listeners.OnItemInputListener;
import net.harmal.karnet2.utils.Logs;

import org.jetbrains.annotations.NotNull;

public abstract class KarnetRecyclerAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T>
{
    protected OnItemInputListener onItemInputListener;

    public void setOnItemInputListener(OnItemInputListener l)
    {
        Logs.debug("Setting on click listener");
        onItemInputListener = l;
    }
    public void setOnItemInputListener(@NotNull OnItemInputListener.Builder builder)
    {
        setOnItemInputListener(builder.build());
    }
}
