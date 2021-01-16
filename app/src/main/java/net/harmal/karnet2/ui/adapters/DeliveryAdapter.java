package net.harmal.karnet2.ui.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.Customer;
import net.harmal.karnet2.core.Order;
import net.harmal.karnet2.core.registers.CustomerRegister;
import net.harmal.karnet2.ui.listeners.OnItemInputListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DeliveryAdapter extends KarnetRecyclerAdapter<DeliveryAdapter.DeliveryViewHolder>
{
    public static class DeliveryViewHolder extends KarnetRecyclerViewHolder
    {
        TextView idText   ;
        TextView infoText ;
        TextView priceText;
        public DeliveryViewHolder(@NonNull View itemView, OnItemInputListener listener)
        {
            super(itemView, listener);
            idText    = itemView.findViewById(R.id.text_delivery_id   );
            infoText  = itemView.findViewById(R.id.text_delivery_info );
            priceText = itemView.findViewById(R.id.text_delivery_price);
        }
    }

    private final List<Order> orderList;
    private List<Order> visibleOrders;

    public DeliveryAdapter(@NotNull List<Order> orderList)
    {
        this.orderList = orderList;
        visibleOrders = new ArrayList<>(orderList);
    }

    @NonNull
    @Override
    public DeliveryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_delivery, parent, false);
        return new DeliveryViewHolder(v, onItemInputListener);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull DeliveryViewHolder holder, int position)
    {
        Order current = visibleOrders.get(position);
        Customer customer = CustomerRegister.getCustomer(current.cid());
        assert customer != null;
        holder.idText.setText(String.format("%d", customer.cid()));
        holder.infoText.setText(String.format(" - %s-%s", customer.name(),
                customer.phoneNum()));
        holder.priceText.setText(String.format("%d dhs",
                current.totalPrice() + current.deliveryPrice()));
    }

    @Override
    public int getItemCount()
    {
        return visibleOrders.size();
    }
}
