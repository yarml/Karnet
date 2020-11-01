package net.harmal.karnet2.ui.fragments.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.Date;
import net.harmal.karnet2.core.Stack;
import net.harmal.karnet2.core.registers.CustomerRegister;
import net.harmal.karnet2.core.registers.OrderRegister;
import net.harmal.karnet2.ui.adapters.OrderListAdapter;
import net.harmal.karnet2.ui.fragments.KarnetFragment;
import net.harmal.karnet2.ui.listeners.OnItemInputListener;
import net.harmal.karnet2.utils.Logs;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends KarnetFragment
{

    private TextView                   noOrderText           ;
    private RecyclerView               orderList             ;
    private RecyclerView.LayoutManager orderListLayoutManager;
    private OrderListAdapter           orderListAdapter      ;

    public OrderFragment()
    {
        super(R.layout.fragment_order);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        Logs.debug("View Created for fragment ORDER");

        orderList = view.findViewById(R.id.recycler_order_list);
        orderListLayoutManager = new LinearLayoutManager(getContext());
        orderListAdapter = new OrderListAdapter(OrderRegister.get());

        orderList.setLayoutManager(orderListLayoutManager);
        orderList.setAdapter(orderListAdapter);

        noOrderText = view.findViewById(R.id.str_fragment_order_no_order);
        if(OrderRegister.size() == 0)
        {
            noOrderText.setVisibility(View.VISIBLE);
            orderList.setVisibility(View.GONE);
        }
        else
        {
            noOrderText.setVisibility(View.GONE);
            orderList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    @MenuRes
    public int getOptionsMenu()
    {
        return R.menu.order_options_menu;
    }
}
