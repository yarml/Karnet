package net.harmal.karnet2.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.Order;
import net.harmal.karnet2.core.registers.CustomerRegister;
import net.harmal.karnet2.ui.listeners.OnItemClickListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class OrderListAdapter extends KarnetRecyclerAdapter<OrderListAdapter.OrderViewHolder>
{
    public static class OrderViewHolder extends KarnetRecyclerViewHolder
    {
        TextView nameView;
        TextView dateView;
        public OrderViewHolder(@NotNull View itemView, OnItemClickListener l)
        {
            super(itemView, l);
            nameView = itemView.findViewById(R.id.text_order_item_name);
            dateView = itemView.findViewById(R.id.text_order_item_date);
        }
    }

    private List<Order> orderList;

    public OrderListAdapter(@NotNull List<Order> orderList)
    {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_order, parent, false);
        return new OrderViewHolder(v, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position)
    {
        Order current = orderList.get(position);

        holder.nameView.setText(String.format("Pour %s", CustomerRegister.getCustomer(current.cid()).name()));
        holder.dateView.setText(String.format("Pour le: %s", current.dueDate().toString()));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
