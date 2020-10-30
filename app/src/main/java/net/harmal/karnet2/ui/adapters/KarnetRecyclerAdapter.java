package net.harmal.karnet2.ui.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.harmal.karnet2.ui.listeners.OnItemClickListener;

public abstract class KarnetRecyclerAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T>
{
    protected OnItemClickListener itemClickListener;

    public void setOnItemClickListener(@NonNull OnItemClickListener l)
    {
        itemClickListener = l;
    }
}
