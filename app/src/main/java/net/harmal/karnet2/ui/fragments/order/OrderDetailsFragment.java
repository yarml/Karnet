package net.harmal.karnet2.ui.fragments.order;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.Customer;
import net.harmal.karnet2.core.Order;
import net.harmal.karnet2.core.registers.CustomerRegister;
import net.harmal.karnet2.core.registers.OrderRegister;
import net.harmal.karnet2.ui.Animations;
import net.harmal.karnet2.ui.adapters.OrderStacksAdapter;
import net.harmal.karnet2.ui.fragments.KarnetFragment;

import org.w3c.dom.Text;

public class OrderDetailsFragment extends KarnetFragment
{

    private int oid;

    private LinearLayout               deliveryPriceLayout   ;
    private TextView                   deliveryPriceText     ;
    private TextView                   dateText              ;
    private TextView                   totalPriceText        ;
    private TextView                   totalWithDeliveryText ;
    private RecyclerView               stackList             ;
    private RecyclerView.LayoutManager stackListLayoutManager;
    private OrderStacksAdapter         stackListAdapter      ;

    public OrderDetailsFragment()
    {
        super(R.layout.fragment_details_order);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        OrderDetailsFragmentArgs args = OrderDetailsFragmentArgs.fromBundle(requireArguments());
        oid = args.getOid();

        deliveryPriceLayout    = view.findViewById(R.id.layout_delivery_price                       );
        deliveryPriceText      = view.findViewById(R.id.text_order_details_delivery_price           );
        dateText               = view.findViewById(R.id.text_order_details_date                     );
        totalPriceText         = view.findViewById(R.id.text_order_details_total_price              );
        totalWithDeliveryText  = view.findViewById(R.id.text_order_details_total_with_delivery_price);
        stackList              = view.findViewById(R.id.recycler_order_details_stacks               );
        stackListLayoutManager = new LinearLayoutManager(requireContext(                           ));


        Order o = OrderRegister.getOrder(oid);
        assert o != null;
        if(o.deliveryPrice() == 0)
        {
            deliveryPriceLayout.setVisibility(View.GONE  );
            totalWithDeliveryText.setVisibility(View.GONE);
        }
        else {
            deliveryPriceText.setText(String.format(getString(R.string.currency),
                    o.deliveryPrice()));
            totalWithDeliveryText.setText(String.format(getString(
                    R.string.total_with_delivery_price),
                    o.totalPrice() + o.deliveryPrice()));
        }
        dateText.setText(o.dueDate().toString());
        totalPriceText.setText(String.format(getString(R.string.total_price), o.totalPrice()));

        stackListAdapter = new OrderStacksAdapter(o.stacks(), false,
                getString(R.string.order_details_item_description));
        stackList.setLayoutManager(stackListLayoutManager);
        stackList.setAdapter(stackListAdapter);
    }

    @Override
    public int getOptionsMenu()
    {
        return R.menu.options_menu_details_order;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(item.getItemId() == R.id.options_edit_order)
        {
            Order o = OrderRegister.getOrder(oid);
            assert o != null;
            Customer c = CustomerRegister.getCustomer(o.cid());
            assert c != null;
            NavDirections action = OrderDetailsFragmentDirections
                    .actionOrderDetailsFragmentToOrderAddModifyFragment(oid,
                            String.format(getString(R.string.modify_order_label), c.name()));
            NavHostFragment.findNavController(this).navigate(action);
        }
        return true;
    }
}
