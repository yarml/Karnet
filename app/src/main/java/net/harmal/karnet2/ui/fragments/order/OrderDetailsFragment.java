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
import net.harmal.karnet2.core.Item;
import net.harmal.karnet2.core.Order;
import net.harmal.karnet2.core.registers.CustomerRegister;
import net.harmal.karnet2.core.registers.OrderRegister;
import net.harmal.karnet2.ui.adapters.OrderItemAdapter;
import net.harmal.karnet2.ui.fragments.KarnetFragment;

public class OrderDetailsFragment extends KarnetFragment
{

    private int oid;

    private TextView                   dateText             ;
    private RecyclerView               itemList             ;
    private RecyclerView.LayoutManager itemListLayoutManager;
    private OrderItemAdapter           itemListAdapter      ;
    private TextView                   itemCountText        ;
    private TextView                   rawTotalText         ;
    private TextView                   reductionText        ;
    private TextView                   deliveryText         ;
    private TextView                   totalText            ;

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

        dateText               = view.findViewById(R.id.text_order_details_date                     );
        itemList               = view.findViewById(R.id.recycler_order_details_stacks               );
        itemCountText          = view.findViewById(R.id.text_order_details_packet_count             );
        rawTotalText           = view.findViewById(R.id.text_order_details_raw_total                );
        reductionText          = view.findViewById(R.id.text_order_details_reduction                );
        deliveryText           = view.findViewById(R.id.text_order_details_delivery                 );
        totalText              = view.findViewById(R.id.text_order_details_total                    );
        itemListLayoutManager  = new LinearLayoutManager(requireContext(                           ));

        Order o = OrderRegister.getOrder(oid);
        assert o != null;

        dateText.setText(o.dueDate().toString());

        int itemCount = 0;
        for(Item i : o.items())
            itemCount += i.count();
        itemCountText.setText(String.format("%d", itemCount));
        rawTotalText.setText(String.format("%d", o.rawTotalPrice()));
        reductionText.setText(String.format("%d", o.reduction()));
        deliveryText.setText(String.format("%d", o.deliveryPrice()));
        totalText.setText(String.format("%d", o.totalPrice() + o.deliveryPrice()));

        itemListAdapter = new OrderItemAdapter(o.items(), false,
                getString(R.string.order_details_item_description));
        itemList.setLayoutManager(itemListLayoutManager);
        itemList.setAdapter(itemListAdapter);
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
