package net.harmal.karnet2.ui.fragments.customer;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.ContactData;
import net.harmal.karnet2.core.Customer;
import net.harmal.karnet2.core.Date;
import net.harmal.karnet2.core.Trash;
import net.harmal.karnet2.core.registers.CustomerRegister;
import net.harmal.karnet2.ui.Animations;
import net.harmal.karnet2.ui.adapters.CustomerListAdapter;
import net.harmal.karnet2.ui.dialogs.WaitDialog;
import net.harmal.karnet2.ui.fragments.KarnetFragment;
import net.harmal.karnet2.ui.listeners.OnActionExpandListenerBuilder;
import net.harmal.karnet2.ui.listeners.OnItemInputListener;
import net.harmal.karnet2.ui.listeners.OnQueryTextListenerBuilder;
import net.harmal.karnet2.utils.ExternalActivityInterface;
import net.harmal.karnet2.utils.Logs;
import net.harmal.karnet2.utils.Utils;

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

        customerListAdapter.setOnItemInputListener(new OnItemInputListener
                .Builder(this::onItemClick, this::onItemLongClick));
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
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem searchItem = menu.findItem(R.id.option_customer_search);
        searchItem.setOnActionExpandListener(new OnActionExpandListenerBuilder(null,
                this::onSearchClose).build());
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.setIconified(false);
        ViewGroup.LayoutParams params = new ViewGroup
                .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT              );
        searchView.setLayoutParams(params);
        searchView.setOnQueryTextListener(new OnQueryTextListenerBuilder(this::onSearchChange,
                                                                         this::onSearchChange)
                                                                         .build());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        Logs.debug("OptionsItemClicked");
        if(item.getItemId() == R.id.option_add_customer)
        {
            Date today = Date.today();
            NavDirections action = CustomerFragmentDirections
                    .actionCustomerFragmentToCustomerAddModifyFragment(
                            -1, getString(R.string.fragment_add_customer_label),
                            "", "",
                            getString(R.string.default_city)  ,
                            today.day(), today.month(), today.year());
            NavHostFragment.findNavController(this).navigate(action);
        }
        else  if(item.getItemId() == R.id.option_add_from_contacts)
        {
            Thread other = new Thread(() -> {
                ContactData contact = ExternalActivityInterface.getContactData(requireActivity());
                if(contact == null)
                    return;
                Date today = Date.today();
                CustomerFragmentDirections.ActionCustomerFragmentToCustomerAddModifyFragment action
                        = CustomerFragmentDirections
                        .actionCustomerFragmentToCustomerAddModifyFragment(
                                -1, getString(R.string.fragment_add_customer_label),
                                Utils.extractCustomerName(contact),
                                Utils.extractCustomerNum(contact) ,
                                getString(R.string.default_city)  ,
                                today.day(), today.month(), today.year());
                requireActivity().runOnUiThread(() ->
                        NavHostFragment.findNavController(this).navigate(action));
            });
            other.start();
        }
        else if(item.getItemId() == R.id.option_customers_sync)
        {
            WaitDialog waitDialog = new WaitDialog(() -> {
                ExternalActivityInterface.syncCustomers(requireContext());
                requireActivity().runOnUiThread(() -> customerListAdapter.update());
            }, "Synchronization en cours", "Clients synchronizé",
                    requireView().getWindowToken());
            waitDialog.show(getChildFragmentManager(), "");
        }
        return true;
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
            // I should have described how it worked.
            // customerListAdapter.notifyItemRemoved(position);
            Snackbar undo = Snackbar.make(requireView(), R.string.customer_removed, Snackbar.LENGTH_LONG);
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
            customerListAdapter.notifyItemInserted(customerListAdapter.getItemCount() - 1);
    }

    @Override
    @MenuRes
    public int getOptionsMenu()
{
        return R.menu.options_menu_customer;
    }

    private boolean onSearchChange(String text)
    {
        customerListAdapter.filter(text);
        return true;
    }

    private boolean onSearchClose(@NotNull MenuItem item)
    {
        Logs.debug("Search closed");
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQuery("", false);
        hideSoftwareKeyboard();
        // Refiltering the list is done automatically when we set query to empty
        return true;
    }
}
