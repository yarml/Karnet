package net.harmal.karnet2.ui.fragments.customer;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.Customer;
import net.harmal.karnet2.core.Date;
import net.harmal.karnet2.core.Trash;
import net.harmal.karnet2.core.registers.CustomerRegister;
import net.harmal.karnet2.ui.AnimationRegister;
import net.harmal.karnet2.ui.adapters.CustomerListAdapter;
import net.harmal.karnet2.ui.fragments.KarnetFragment;
import net.harmal.karnet2.ui.listeners.OnItemInputListener;
import net.harmal.karnet2.utils.Logs;

import org.jetbrains.annotations.NotNull;

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

        Logs.debug("Creating CUSTOMER fragment");

        noCustomerText = view.findViewById(R.id.str_fragment_customer_no_customer);
        if(CustomerRegister.size() == 0)
            noCustomerText.setVisibility(View.VISIBLE);
        else
            noCustomerText.setVisibility(View.GONE);

        customerList = view.findViewById(R.id.recycler_customer_list);
        customerListLayoutManager = new LinearLayoutManager(getContext());
        customerListAdapter = new CustomerListAdapter(CustomerRegister.get());
        customerListAdapter.setOnItemInputListener(new OnItemInputListener.Builder(this::onItemClick, this::onItemLongClick));

        customerList.setLayoutManager(customerListLayoutManager);
        customerList.setAdapter(customerListAdapter);

    }

    @Override
    public void onMenuOptionsSelected(@NotNull MenuItem item, NavController navController) {
        if(item.getItemId() == R.id.option_add_customer)
        {
            Date today = Date.today();
            CustomerFragmentDirections.ActionCustomerFragmentToCustomerAddModifyFragment action = CustomerFragmentDirections
                    .actionCustomerFragmentToCustomerAddModifyFragment(
                            -1, getString(R.string.fragment_add_customer_label),
                            "", "", "",
                            today.day(), today.month(), today.year());
            navController.navigate(action);
        }
    }

    /**
     * Handles clicks on the recycler view items
     */
    private void onItemClick(View view, int position)
    {
        if(view.getId() == R.id.btn_customer_delete)
        {
            Customer c = CustomerRegister.get().get(position);
            CustomerRegister.remove(c.cid());
            customerListAdapter.notifyItemRemoved(position);
            Snackbar undo = Snackbar.make(getView(), R.string.customer_removed, Snackbar.LENGTH_LONG);
            undo.setAction(R.string.undo, this::undoCustomerDeletion);
            undo.show();
        }
        else
        {
            View v = customerListLayoutManager.findViewByPosition(position);
            ImageButton deleteButton = v.findViewById(R.id.btn_customer_delete);
            if(deleteButton.getVisibility() == View.VISIBLE)
            {
                AnimationRegister.popOut(deleteButton);
                return;
            }
            Customer c = CustomerRegister.get().get(position);
            NavDirections action = CustomerFragmentDirections.actionCustomerFragmentToCustomerDetailsFragment(c.cid(), c.name());
            NavHostFragment.findNavController(CustomerFragment.this).navigate(action);
        }
    }

    /**
     * Handles long clicks on the recycler view items
     */
    private void onItemLongClick(View view, int position)
    {
        View v = customerListLayoutManager.findViewByPosition(position);
        ImageButton deleteButton = v.findViewById(R.id.btn_customer_delete);
        if(deleteButton.getVisibility() == View.GONE) // should appear
            AnimationRegister.popIn(deleteButton);
        else
            AnimationRegister.popOut(deleteButton);
    }


    private void undoCustomerDeletion(View v)
    {
        // TODO: must change item insertion position to enable list sorting
        CustomerRegister.add(Trash.popCustomer());
        customerListAdapter.notifyItemInserted(CustomerRegister.size() - 1);
    }

    @Override
    @MenuRes
    public int getOptionsMenu()
{
        return R.menu.customer_options_menu;
    }
}
