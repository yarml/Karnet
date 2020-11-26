package net.harmal.karnet2.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.Order;
import net.harmal.karnet2.core.registers.CustomerRegister;
import net.harmal.karnet2.ui.listeners.OnItemInputListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OrderListAdapter extends KarnetRecyclerAdapter<OrderListAdapter.OrderViewHolder>
{
    public static class OrderViewHolder extends KarnetRecyclerViewHolder
    {
        TextView nameView    ;
        TextView dateView    ;
        ImageButton deleteBtn;
        ImageButton doneBtn  ;
        public OrderViewHolder(@NotNull View itemView, OnItemInputListener l)
        {
            super(itemView, l);
            nameView  = itemView.findViewById(R.id.text_order_item_name);
            dateView  = itemView.findViewById(R.id.text_order_item_date);
            deleteBtn = itemView.findViewById(R.id.btn_order_delete    );
            doneBtn   = itemView.findViewById(R.id.btn_order_done      );
        }
    }

    private final List<Order> orderList;

    public List<Order> getOrderList()
    {
        return orderList;
    }

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
        return new OrderViewHolder(v, onItemInputListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position)
    {
        Order current = orderList.get(position);

        holder.nameView.setText(String.format("Pour %s", CustomerRegister.getCustomer(current.cid()).name()));
        holder.dateView.setText(String.format("Pour le: %s", current.dueDate().toString()));
        holder.deleteBtn.setVisibility(View.GONE);
        holder.doneBtn.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
