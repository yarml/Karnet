package net.harmal.karnet2.ui.fragments.order;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.Date;
import net.harmal.karnet2.core.Order;
import net.harmal.karnet2.core.registers.CustomerRegister;
import net.harmal.karnet2.core.registers.OrderRegister;
import net.harmal.karnet2.ui.adapters.DeliveryAdapter;
import net.harmal.karnet2.ui.dialogs.KarnetDialogs;
import net.harmal.karnet2.ui.fragments.KarnetFragment;

import java.util.ArrayList;
import java.util.List;

public class DeliveryFragment extends KarnetFragment
{

    private RecyclerView               deliveryList             ;
    private RecyclerView.LayoutManager deliveryListLayoutManager;
    private DeliveryAdapter            deliveryListAdapter      ;

    private TextView                   deliveryTotalText        ;

    private String[]  filterCities       ;
    private boolean[] filterCitiesChecked;

    public DeliveryFragment()
    {
        super(R.layout.fragment_delivery);
    }

    @Override
    public int getOptionsMenu()
    {
        return R.menu.options_menu_delivery;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(item.getItemId() == R.id.option_delivery_city)
        {
            List<String> cities = CustomerRegister.allCities();
            filterCities = new String[cities.size()];
            filterCitiesChecked = new boolean[cities.size()];
            for(int i = 0; i < cities.size(); i++)
            {
                filterCities[i] = cities.get(i);
                filterCitiesChecked[i] = true;
            }
            AlertDialog dialog = KarnetDialogs.multiCityChoiceDialog(requireContext(),
                                        filterCities,
                                        filterCitiesChecked,
                                        this::onCityFilterSelectChange,
                                        this::onCityFilterSelect,
                                        this::onCityFilterClear);
            dialog.show();
            return true;
        }
        else if(item.getItemId() == R.id.option_delivery_date)
        {
            Date today = Date.today();
            DatePickerDialog dialog = new DatePickerDialog(
                    requireContext(),
                    this::onDateFilterSet,
                    today.year(),
                    today.month() - 1,
                    today.day());
            dialog.show();
        }
        else if(item.getItemId() == R.id.option_delivery_remove_date_filter)
        {
            deliveryListAdapter.filterDate(Date.always());
            updateTotalText();
            updateTitle();
        }
        return false;
    }

    private void onDateFilterSet(DatePicker datePicker, int year, int month, int dayOfMonth)
    {
        deliveryListAdapter.filterDate(new Date(dayOfMonth, month + 1, year));
        updateTotalText();
        updateTitle();
    }

    private void onCityFilterClear(DialogInterface dialog)
    {
        for(int i = 0; i < filterCitiesChecked.length; i++)
        {
            filterCitiesChecked[i] = false;
            ((AlertDialog) dialog).getListView().setItemChecked(i, false);
        }
    }

    private void onCityFilterSelectChange(DialogInterface dialogInterface, int i, boolean b)
    {
        filterCitiesChecked[i] = b;
    }

    private void onCityFilterSelect(DialogInterface dialogInterface, int button)
    {
        List<String> filterCitiesList = new ArrayList<>();
        for(int i = 0; i < filterCities.length; i++)
        {
            if(filterCitiesChecked[i])
                filterCitiesList.add(filterCities[i]);
        }
        deliveryListAdapter.filterCities(filterCitiesList);
        updateTotalText();
        updateTitle();
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

        updateTotalText();
        updateTitle();
    }

    private void updateTotalText()
    {
        int total = 0;
        for(Order o : deliveryListAdapter.visibleOrders())
            total += o.totalPrice() + o.deliveryPrice();
        deliveryTotalText.setText(String.format(getString(R.string.delivery_total), total));
    }

    private void updateTitle()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(getString(R.string.delivery_fragment));
        if(deliveryListAdapter.filterCities().size() == 1)
            builder.append(String.format(" - %s", deliveryListAdapter.filterCities().get(0)));
        if(!deliveryListAdapter.filterDate().equals(Date.always()))
            builder.append(String.format(" - %s", deliveryListAdapter.filterDate().toString()));
        setTitle(builder.toString());
    }
}
