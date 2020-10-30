package net.harmal.karnet2.ui.adapters;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.harmal.karnet2.ui.listeners.OnItemClickListener;

public abstract class KarnetRecyclerViewHolder extends RecyclerView.ViewHolder
{

    public KarnetRecyclerViewHolder(@NonNull View itemView, OnItemClickListener l)
    {
        super(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(l != null)
                {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION)
                        l.onItemClick(position);
                }
            }
        });
    }
}
