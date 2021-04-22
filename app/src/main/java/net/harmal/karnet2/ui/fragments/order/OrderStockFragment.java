package net.harmal.karnet2.ui.fragments.order;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.Date;
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

    @Override
    public int getOptionsMenu() {
        return R.menu.options_menu_order_stock;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(item.getItemId() == R.id.option_order_stock_limit_date)
        {
            Date date = Date.today();
            DatePickerDialog dialog = new DatePickerDialog(requireContext(), this::onLimitDateSet,
                    date.year(), date.month() - 1, date.day());
            dialog.show();
            return true;
        }
        else if(item.getItemId() == R.id.option_order_stock_select_date)
        {
            Date date = Date.today();
            DatePickerDialog dialog = new DatePickerDialog(requireContext(), this::onSelectDate,
                    date.year(), date.month() - 1, date.day());
            dialog.show();
            return true;
        }
        else if(item.getItemId() == R.id.option_order_stock_no_date)
        {
            orderStockListAdapter.limitDate(null);
            return true;
        }
        return false;
    }

    private void onSelectDate(DatePicker datePicker, int year, int month, int day)
    {
        Date d = new Date(day, month + 1, year);
        orderStockListAdapter.monitoredDate(d);
    }

    private void onLimitDateSet(DatePicker datePicker, int year, int month, int day)
    {
        Date d = new Date(day, month + 1, year);
        orderStockListAdapter.limitDate(d);
    }

}
