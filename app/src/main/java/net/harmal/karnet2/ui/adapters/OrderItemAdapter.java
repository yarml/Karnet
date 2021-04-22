package net.harmal.karnet2.ui.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.Item;
import net.harmal.karnet2.core.registers.OrderRegister;
import net.harmal.karnet2.core.registers.Stock;
import net.harmal.karnet2.ui.listeners.OnItemInputListener;

import java.util.List;

public class OrderItemAdapter extends KarnetRecyclerAdapter<OrderItemAdapter.OrderItemViewHolder>
{
    public static class OrderItemViewHolder extends KarnetRecyclerViewHolder
    {
        private final TextView    stackItemName ;
        private final TextView    stackItemCount;
        private final ImageButton stackDeleteBtn;
        private final CardView    background    ;
        public OrderItemViewHolder(@NonNull View itemView, KarnetRecyclerAdapter<? extends KarnetRecyclerViewHolder> adapter)
        {
            super(itemView, adapter);

            stackItemName  = itemView.findViewById(R.id.text_order_stack_item_name );
            stackItemCount = itemView.findViewById(R.id.text_order_stack_item_count);
            stackDeleteBtn = itemView.findViewById(R.id.btn_order_stack_delete     );
            background     = itemView.findViewById(R.id.order_item_view            );
        }
    }

    @NonNull
    private final List<Item> itemList;
    private final boolean     showDelete  ;
    private final String      itemSubtitle;

    public OrderItemAdapter(@NonNull List<Item> itemList)
    {
        this(itemList, true,
                "%d boîtes x %d dhs");
    }

    public OrderItemAdapter(@NonNull List<Item> itemList, boolean showDelete, String itemSubtitle)
    {
        this.itemList = itemList;
        this.showDelete   = showDelete  ;
        this.itemSubtitle = itemSubtitle;
    }

    @NonNull
    public List<Item> itemList()
    {
        return itemList;
    }


    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_order_item, parent, false);
        OrderItemViewHolder vh = new OrderItemViewHolder(v, this);
        if(showDelete)
            vh.stackDeleteBtn.setVisibility(View.VISIBLE);
        else
            vh.stackDeleteBtn.setVisibility(View.GONE);
        return vh;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position)
    {
        Item current = itemList.get(position);
        holder.stackItemName.setText(current.bundle().name());
        holder.stackItemCount.setText(String.format(itemSubtitle,
                current.count(), current.bundle().price()));
        if(showDelete)
        {
            if(Stock.countOf(current.bundle()) < current.count())
                holder.background.setBackgroundColor(holder.background.getResources()
                        .getColor(android.R.color.holo_red_light, null));
            else if (Stock.countOf(current.bundle()) - OrderRegister.countOf(current.bundle()) < 0)
                holder.background.setBackgroundColor(holder.background.getResources()
                        .getColor(android.R.color.holo_orange_light, null));
            else
                holder.background.setBackgroundColor(holder.background.getResources()
                        .getColor(android.R.color.white, null));
        }
    }

    @Override
    public int getItemCount()
    {
        return itemList.size();
    }
}
