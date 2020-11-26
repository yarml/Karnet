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
import net.harmal.karnet2.ui.Animations;
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

        noCustomerText = view.findViewById(R.id.text_fragment_customer_no_customer);


        customerList              = view.findViewById(R.id.recycler_customer_list);
        customerListLayoutManager = new LinearLayoutManager(getContext());
        customerListAdapter       = new CustomerListAdapter(CustomerRegister.get());

        customerListAdapter.setOnItemInputListener(new OnItemInputListener.Builder(this::onItemClick, this::onItemLongClick));
        customerList.setLayoutManager(customerListLayoutManager);
        customerList.setAdapter(customerListAdapter);


        if(CustomerRegister.size() == 0)
        {
            noCustomerText.setVisibility(View.VISIBLE);
            customerList.setVisibility(View.GONE);
        }
        else
        {
            noCustomerText.setVisibility(View.GONE);
            customerList.setVisibility(View.VISIBLE);
        }
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
    private void onItemClick(@NotNull View view, int position)
    {
        if(view.getId() == R.id.btn_customer_delete)
        {
            Customer c = customerListAdapter.getVisibleCustomerList().get(position);
            CustomerRegister.remove(c.cid());
            customerListAdapter.update();
            // TODO: experiment with this, try to understand it first please,
            //  don't just delete this line thinking "I changed my mind"
            // Later
            // I actually forgot what it was about
            // customerListAdapter.notifyItemRemoved(position);
            assert getView() != null;
            Snackbar undo = Snackbar.make(getView(), R.string.customer_removed, Snackbar.LENGTH_LONG);
            undo.setAction(R.string.undo, this::onUndoCustomerDeletion);
            undo.show();
        }
        else
        {
            View v = customerListLayoutManager.findViewByPosition(position);
            assert v != null;
            ImageButton deleteButton = v.findViewById(R.id.btn_customer_delete);
            if(deleteButton.getVisibility() == View.VISIBLE)
            {
                Animations.popOut(deleteButton);
                return;
            }
            Customer c = customerListAdapter.getVisibleCustomerList().get(position);
            NavDirections action = CustomerFragmentDirections
                    .actionCustomerFragmentToCustomerDetailsFragment(c.cid(), c.name());
            NavHostFragment.findNavController(this).navigate(action);
        }
    }

    /**
     * Handles long clicks on the recycler view items
     */
    private void onItemLongClick(View view, int position)
    {
        View v = customerListLayoutManager.findViewByPosition(position);
        assert v != null;
        ImageButton deleteButton = v.findViewById(R.id.btn_customer_delete);
        if(deleteButton.getVisibility() == View.GONE) // should appear
            Animations.popIn(deleteButton);
        else
            Animations.popOut(deleteButton);
    }

    private void onUndoCustomerDeletion(View v)
    {
        int cid = CustomerRegister.add(Trash.popCustomer());
        Customer c = CustomerRegister.getCustomer(cid);
        customerListAdapter.update();
        if(customerListAdapter.getVisibleCustomerList().contains(c))
            customerListAdapter.notifyItemInserted(customerListAdapter.getVisibleCustomerList().size() - 1);
    }

    @Override
    @MenuRes
    public int getOptionsMenu()
{
        return R.menu.options_menu_customer;
    }
}
