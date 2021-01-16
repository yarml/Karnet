package net.harmal.karnet2.ui.fragments.order;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.Order;
import net.harmal.karnet2.core.registers.OrderRegister;
import net.harmal.karnet2.ui.adapters.DeliveryAdapter;
import net.harmal.karnet2.ui.fragments.KarnetFragment;

public class DeliveryFragment extends KarnetFragment
{

    private RecyclerView               deliveryList             ;
    private RecyclerView.LayoutManager deliveryListLayoutManager;
    private DeliveryAdapter            deliveryListAdapter      ;

    private TextView                   deliveryTotalText        ;

    public DeliveryFragment()
    {
        super(R.layout.fragment_delivery);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        deliveryTotalText         = view.findViewById(R.id.text_delivery_total      );
        deliveryList              = view.findViewById(R.id.recycler_delivery_list   );
        deliveryListLayoutManager = new LinearLayoutManager(requireContext(        ));
        deliveryListAdapter       = new DeliveryAdapter(OrderRegister.withDelivery());

        deliveryList.setLayoutManager(deliveryListLayoutManager);
        deliveryList.setAdapter(deliveryListAdapter            );
        int total = 0;
        for(Order o : OrderRegister.withDelivery())
            total += o.totalPrice() + o.deliveryPrice();
        deliveryTotalText.setText(String.format(getString(R.string.delivery_total), total));

    }
}
