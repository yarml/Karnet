package net.harmal.karnet2.ui.fragments.order;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.Customer;
import net.harmal.karnet2.core.Date;
import net.harmal.karnet2.core.Order;
import net.harmal.karnet2.core.Trash;
import net.harmal.karnet2.core.registers.CustomerRegister;
import net.harmal.karnet2.core.registers.OrderRegister;
import net.harmal.karnet2.core.registers.OrdersLog;
import net.harmal.karnet2.core.registers.Stock;
import net.harmal.karnet2.ui.Animations;
import net.harmal.karnet2.ui.adapters.OrderListAdapter;
import net.harmal.karnet2.ui.dialogs.ConfirmationDialog;
import net.harmal.karnet2.ui.dialogs.KarnetDialogs;
import net.harmal.karnet2.ui.fragments.KarnetFragment;
import net.harmal.karnet2.ui.listeners.OnActionExpandListenerBuilder;
import net.harmal.karnet2.ui.listeners.OnItemInputListener;
import net.harmal.karnet2.ui.listeners.OnQueryTextListenerBuilder;
import net.harmal.karnet2.utils.Logs;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends KarnetFragment
{

    private TextView                   noOrderText           ;
    private RecyclerView               orderList             ;
    private RecyclerView.LayoutManager orderListLayoutManager;
    private OrderListAdapter           orderListAdapter      ;

    private String[]  filterCities       ;
    private boolean[] filterCitiesChecked;

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
        orderListAdapter.setOnItemInputListener(new OnItemInputListener.Builder(this::onItemClick, this::onItemLongClick));

        orderList.setLayoutManager(orderListLayoutManager);
        orderList.setAdapter(orderListAdapter);

        noOrderText = view.findViewById(R.id.str_fragment_order_no_order);
        if(orderListAdapter.getItemCount() == 0)
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

    private void onItemClick(@NotNull View view, int i)
    {
        if(view.getId() == R.id.btn_order_delete)
        {
            Order o = orderListAdapter.visibleOrderList().get(i);
            OrderRegister.remove(o.oid());
            orderListAdapter.update();
            orderListAdapter.notifyItemRemoved(i);
            if(orderListAdapter.getItemCount() == 0)
            {
                Animations.popIn(noOrderText);
                orderList.setVisibility(View.GONE);
            }
            assert getView() != null;
            Snackbar undo = Snackbar.make(getView(), R.string.order_removed, Snackbar.LENGTH_LONG);
            undo.setAction(R.string.undo, this::onUndoOrderDeletion);
            undo.show();
        }
        else if(view.getId() == R.id.btn_order_done)
        {
            Order o = orderListAdapter.visibleOrderList().get(i);
            if(Stock.canValidate(o) == Stock.VALIDATE_IMPOSSIBLE)
            {
                Toast.makeText(getContext(), R.string.insufficient_stock, Toast.LENGTH_LONG).show();
                return;
            }
            Customer c = CustomerRegister.getCustomer(o.cid());
            assert c != null;
            ConfirmationDialog dialog = new ConfirmationDialog(R.string.confirmation,
                    String.format(getString(R.string.confirm_order_delete), c.name()),
                    (dialog1, which) -> validateOrder(i));
            dialog.show(getChildFragmentManager(), "");
        }
        else
        {
            View v = orderListLayoutManager.findViewByPosition(i);
            assert v != null;
            ImageButton deleteButton = v.findViewById(R.id.btn_order_delete);
            ImageButton doneBtn      = v.findViewById(R.id.btn_order_done  );
            TextView    price        = v.findViewById(R.id.text_order_total);
            if(deleteButton.getVisibility() == View.VISIBLE)
            {
                Animations.popOut(deleteButton);
                Animations.popOut(doneBtn     );
                Animations.popIn(price        );
                return;
            }
            Order o = orderListAdapter.visibleOrderList().get(i);
            Customer c = CustomerRegister.getCustomer(o.cid());
            assert c != null;
            NavDirections action = OrderFragmentDirections
                    .actionOrderFragmentToOrderDetailsFragment(o.oid(), c.name());

            Logs.debug("Navigating to order details");
            NavHostFragment.findNavController(this).navigate(action);
        }
    }

    /**
     * @param pos position in the adapter which was validated
     */
    private void validateOrder(int pos)
    {
        Order o = orderListAdapter.visibleOrderList().get(pos);
        Stock.validate(o);
        OrderRegister.get().remove(o);
        OrdersLog.registerValidatedOrder(o);
        orderListAdapter.update();
        orderListAdapter.notifyItemRemoved(pos);
        if(orderListAdapter.getItemCount() == 0)
        {
            Animations.popIn(noOrderText);
            orderList.setVisibility(View.GONE);
        }
    }

    private void onItemLongClick(View view, int i)
    {
        View v = orderListLayoutManager.findViewByPosition(i);
        assert v != null;
        ImageButton deleteButton = v.findViewById(R.id.btn_order_delete);
        ImageButton doneBtn      = v.findViewById(R.id.btn_order_done  );
        TextView    price        = v.findViewById(R.id.text_order_total);
        if(deleteButton.getVisibility() == View.GONE) // should appear
        {
            Animations.popIn(deleteButton);
            Animations.popIn(doneBtn     );
            Animations.popOut(price      );
        }
        else
        {
            Animations.popOut(deleteButton);
            Animations.popOut(doneBtn     );
            Animations.popIn(price        );
        }
    }

    private void onUndoOrderDeletion(View view)
    {
        if(orderListAdapter.getItemCount() == 0)
        {
            Animations.popOut(noOrderText);
            orderList.setVisibility(View.VISIBLE);
        }
        OrderRegister.add(Trash.popOrder());
        orderListAdapter.update();
    }

    @Override
    @MenuRes
    public int getOptionsMenu()
    {
        return R.menu.options_menu_order;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem searchItem = menu.findItem(R.id.option_order_search);
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
    public boolean onOptionsItemSelected(@NotNull MenuItem item)
    {
        if(item.getItemId() == R.id.option_add_order)
        {
            NavDirections action = OrderFragmentDirections
                    .actionOrderFragmentToOrderAddModifyFragment(-1, getString(R.string.add_order));
            NavHostFragment.findNavController(this).navigate(action);
            return true;
        }
        else if(item.getItemId() == R.id.option_delivery)
        {
            NavDirections action = OrderFragmentDirections
                    .actionOrderFragmentToDeliveryFragment();
            NavHostFragment.findNavController(this).navigate(action);
            return true;
        }
        else if(item.getItemId() == R.id.option_only_delivery)
        {
            switch (orderListAdapter.viewMode())
            {
                case ALL: // -> DELIVERY
                    item.setIcon(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.ic_delivery_white, null));
                    item.setTitle(R.string.show_orders_no_delivery);
                    orderListAdapter.viewMode(OrderListAdapter.ViewMode.DELIVERY);
                    Toast.makeText(requireContext(), R.string.show_only_delivery_orders_text,
                            Toast.LENGTH_SHORT).show();
                    break;
                case DELIVERY: // -> NO_DELIVERY
                    item.setIcon(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.ic_delivery, null));
                    item.setTitle(R.string.show_orders_all);
                    orderListAdapter.viewMode(OrderListAdapter.ViewMode.NO_DELIVERY);
                    Toast.makeText(requireContext(), R.string.show_not_delivery,
                            Toast.LENGTH_SHORT).show();
                    break;
                case NO_DELIVERY: // -> ALL
                    item.setIcon(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.ic_negative, null));
                    item.setTitle(R.string.show_orders_delivery);
                    orderListAdapter.viewMode(OrderListAdapter.ViewMode.ALL);
                    Toast.makeText(requireContext(), R.string.show_all_orders_text,
                            Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        }
        else if(item.getItemId() == R.id.option_order_details)
        {
            NavDirections action = OrderFragmentDirections
                    .actionOrderFragmentToOrderStockFragment();
            NavHostFragment.findNavController(this).navigate(action);
            return true;
        }
        else if(item.getItemId() == R.id.option_order_city)
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
                    this::onFilterSelectChange,
                    this::onFilterSelect,
                    this::onFilterClear);
            dialog.show();
            return true;
        }
        else if(item.getItemId() == R.id.option_order_select_date)
        {
            Date date = Date.today();
            DatePickerDialog dialog = new DatePickerDialog(requireContext(), this::onSelectDate,
                    date.year(), date.month() - 1, date.day());
            dialog.show();
            return true;
        }
        else if(item.getItemId() == R.id.option_order_unselect_date)
        {
            orderListAdapter.monitoredDate(null);
            return true;
        }
        return false;
    }

    private void onSelectDate(DatePicker datePicker, int year, int month, int day)
    {
        Date d = new Date(day, month + 1, year);
        orderListAdapter.monitoredDate(d);
    }

    private void onFilterClear(DialogInterface dialog)
    {
        for(int i = 0; i < filterCitiesChecked.length; i++)
        {
            filterCitiesChecked[i] = false;
            ((AlertDialog) dialog).getListView().setItemChecked(i, false);
        }
    }

    private void onFilterSelectChange(DialogInterface dialogInterface, int i, boolean b)
    {
        filterCitiesChecked[i] = b;
    }

    private void onFilterSelect(DialogInterface dialogInterface, int button)
    {
        List<String> filterCitiesList = new ArrayList<>();
        for(int i = 0; i < filterCities.length; i++)
        {
            if(filterCitiesChecked[i])
                filterCitiesList.add(filterCities[i]);
        }
        orderListAdapter.filterCities(filterCitiesList);
    }

    private boolean onSearchChange(String text)
    {
        orderListAdapter.filterText(text);
        return true;
    }

    private boolean onSearchClose(@NotNull MenuItem item)
    {
        Logs.debug("Order Search closed");
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQuery("", false);
        hideSoftwareKeyboard();
        // Refiltering the list is done automatically when we set query to empty
        return true;
    }
}
