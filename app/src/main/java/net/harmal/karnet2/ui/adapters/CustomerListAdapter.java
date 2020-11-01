package net.harmal.karnet2.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.Customer;
import net.harmal.karnet2.ui.AnimationRegister;
import net.harmal.karnet2.ui.listeners.OnItemInputListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CustomerListAdapter extends KarnetRecyclerAdapter<CustomerListAdapter.CustomerViewHolder>
{
    public static class CustomerViewHolder extends KarnetRecyclerViewHolder
    {
        private TextView    customerName;
        private TextView    phoneCity   ;
        private ImageButton deleteBtn;

        public CustomerViewHolder(@NonNull View itemView, OnItemInputListener listener)
        {
            super(itemView, listener);
            customerName = itemView.findViewById(R.id.text_customer_list_name      );
            phoneCity    = itemView.findViewById(R.id.text_customer_list_phone_city);
            deleteBtn    = itemView.findViewById(R.id.btn_customer_delete          );
        }
    }

    private final List<Customer> customerList;

    public CustomerListAdapter(@NotNull List<Customer> customerList)
    {
        this.customerList = customerList;
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_customer, parent, false);
        return new CustomerViewHolder(v, onItemInputListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position)
    {
        Customer current = customerList.get(position);

        holder.customerName.setText(current.name());
        holder.phoneCity.setText(String.format("%s, %s", current.phoneNum(), current.city()));
        holder.deleteBtn.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount()
    {
        return customerList.size();
    }

}
