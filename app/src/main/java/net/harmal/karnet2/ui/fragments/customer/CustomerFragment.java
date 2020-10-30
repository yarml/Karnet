package net.harmal.karnet2.ui.fragments.customer;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.harmal.karnet2.NavGraphDirections;
import net.harmal.karnet2.R;
import net.harmal.karnet2.core.Customer;
import net.harmal.karnet2.core.Date;
import net.harmal.karnet2.core.Stack;
import net.harmal.karnet2.core.registers.CustomerRegister;
import net.harmal.karnet2.core.registers.OrderRegister;
import net.harmal.karnet2.ui.adapters.CustomerListAdapter;
import net.harmal.karnet2.ui.fragments.KarnetFragment;
import net.harmal.karnet2.ui.listeners.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerFragment extends KarnetFragment
{

    private TextView                   noCustomerText           ;
    private RecyclerView               customerList             ;
    private RecyclerView.LayoutManager customerListLayoutManager;
    private CustomerListAdapter        customerListAdapter      ;

    public CustomerFragment()
    {
        super(R.layout.fragment_customer);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        noCustomerText = view.findViewById(R.id.str_fragment_customer_no_customer);
        if(CustomerRegister.size() == 0)
            noCustomerText.setVisibility(View.VISIBLE);
        else
            noCustomerText.setVisibility(View.GONE);

        customerList = view.findViewById(R.id.recycler_customer_list);
        customerListLayoutManager = new LinearLayoutManager(getContext());
        customerListAdapter = new CustomerListAdapter(CustomerRegister.get());
        customerListAdapter.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(int position)
            {
                Customer c = CustomerRegister.get().get(position);
                NavDirections action = CustomerFragmentDirections.actionCustomerFragmentToCustomerDetailsFragment(c.cid(), c.name());
                NavHostFragment.findNavController(CustomerFragment.this).navigate(action);
            }
        });

        customerList.setLayoutManager(customerListLayoutManager);
        customerList.setAdapter(customerListAdapter);

    }

    @Override
    public void onMenuOptionsSelected(MenuItem item, NavController navController) {
        if(item.getItemId() == R.id.option_add_customer)
        {
            Date today = Date.today();
            CustomerFragmentDirections.ActionCustomerFragmentToCustomerAddModifyFragment action = CustomerFragmentDirections
                    .actionCustomerFragmentToCustomerAddModifyFragment(
                            -1, getString(R.string.fragment_add_customer_label),
                            "", "", "Casablanca",
                            today.day(), today.month(), today.year());
            navController.navigate(action);
        }
    }
}
