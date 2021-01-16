package net.harmal.karnet2.ui.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.Date;
import net.harmal.karnet2.core.Item;
import net.harmal.karnet2.core.Order;
import net.harmal.karnet2.core.registers.OrderRegister;
import net.harmal.karnet2.core.registers.Stock;
import net.harmal.karnet2.ui.listeners.OnItemInputListener;

import java.util.ArrayList;
import java.util.List;

public class OrderStockAdapter extends KarnetRecyclerAdapter<OrderStockAdapter.OrderStockViewHolder>
{
    public static class OrderStockViewHolder extends KarnetRecyclerViewHolder
    {
        CardView mainCard  ;
        TextView nameText  ;
        TextView stockText ;
        TextView ordersText;
        TextView sumText   ;
        public OrderStockViewHolder(@NonNull View itemView, OnItemInputListener listener)
        {
            super(itemView, listener);
            mainCard   = itemView.findViewById(R.id.card_order_stock              );
            nameText   = itemView.findViewById(R.id.text_order_details_item_name  );
            stockText  = itemView.findViewById(R.id.text_order_details_item_stock );
            ordersText = itemView.findViewById(R.id.text_order_details_item_orders);
            sumText    = itemView.findViewById(R.id.text_order_details_item_sum   );
        }
    }
    
    private Date monitoredDate;

    public OrderStockAdapter()
    {
        this(null);
    }

    public OrderStockAdapter(Date monitoredDate)
    {
        this.monitoredDate = monitoredDate;
    }
    
    @NonNull
    @Override
    public OrderStockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_order_details, parent, false);
        return new OrderStockViewHolder(v, onItemInputListener);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull OrderStockViewHolder holder, int position)
    {
        Item current = visibleItems().get(position);
        int stock = Stock.countOf(current.bundle());
        int count = current.count();
        int sum = stock - count;
        holder.nameText.setText(current.bundle().name());
        holder.stockText.setText(String.format("%d", stock));
        holder.ordersText.setText(String.format("-%d", count));
        holder.sumText.setText(String.format("%d", sum));

        if(sum < 0)
            holder.mainCard.setCardBackgroundColor(holder.mainCard.getResources()
                    .getColor(android.R.color.holo_red_light, null));
        else if(sum == 0)
            holder.mainCard.setCardBackgroundColor(holder.mainCard.getResources()
                    .getColor(android.R.color.holo_orange_light, null));
        else
            holder.mainCard.setCardBackgroundColor(holder.mainCard.getResources()
                    .getColor(android.R.color.holo_green_light, null));
    }

    @Override
    public int getItemCount()
    {
        return visibleItems().size();
    }

    public List<Item> visibleItems()
    {
        List<Item> items = new ArrayList<>();
        for(Order o : OrderRegister.forDate(monitoredDate))
        {
            for(Item i : o.items())
            {
                boolean exists = false;
                for(Item i2 : items)
                {
                    if(i.bundle().equals(i2.bundle()))
                    {
                        exists = true;
                        i2.add(i.count());
                        break;
                    }
                }
                if(!exists)
                    items.add(new Item(i.bundle(), i.count()));
            }
        }
        return items;
    }

    public void update()
    {
        notifyDataSetChanged();
    }

}