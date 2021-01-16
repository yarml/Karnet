package net.harmal.karnet2.ui.fragments.order;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.harmal.karnet2.R;
import net.harmal.karnet2.ui.adapters.OrderStockAdapter;
import net.harmal.karnet2.ui.fragments.KarnetFragment;

public class OrderStockFragment extends KarnetFragment
{
    private RecyclerView               orderStockList             ;
    private RecyclerView.LayoutManager orderStockListLayoutManager;
    private OrderStockAdapter          orderStockListAdapter      ;

    public OrderStockFragment()
    {
        super(R.layout.fragment_order_stock);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        orderStockList              = view.findViewById(R.id.recycler_order_stock_list);
        orderStockListLayoutManager = new LinearLayoutManager(requireContext(        ));
        orderStockListAdapter       = new OrderStockAdapter(                          );

        orderStockList.setLayoutManager(orderStockListLayoutManager);
        orderStockList.setAdapter(orderStockListAdapter);
    }
}
