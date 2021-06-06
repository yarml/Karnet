package net.harmal.karnet2.ui.fragments.stock;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.registers.IngredientRegister;
import net.harmal.karnet2.core.registers.Stock;
import net.harmal.karnet2.ui.adapters.StockAdapter;
import net.harmal.karnet2.ui.dialogs.SelectIngredientBundleDialog;
import net.harmal.karnet2.ui.dialogs.StockItemCountModifyDialog;
import net.harmal.karnet2.ui.dialogs.StockPasswordDialog;
import net.harmal.karnet2.ui.fragments.KarnetFragment;
import net.harmal.karnet2.ui.listeners.OnActionExpandListenerBuilder;
import net.harmal.karnet2.ui.listeners.OnItemInputListener;
import net.harmal.karnet2.ui.listeners.OnQueryTextListenerBuilder;
import net.harmal.karnet2.utils.Logs;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StockFragment extends KarnetFragment
{
    private TextView                   noItemText            ;
    private RecyclerView               stockList             ;
    private RecyclerView.LayoutManager stockListLayoutManager;
    private StockAdapter               stockListAdapter      ;

    private boolean[] filter;

    public StockFragment()
    {
        super(R.layout.fragment_stock);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        StockPasswordDialog dialog = new StockPasswordDialog();
        dialog.show(getChildFragmentManager(), "");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        noItemText = view.findViewById(R.id.text_stock_no_stock);
        stockList = view.findViewById(R.id.recycler_stock);

        stockListLayoutManager = new LinearLayoutManager(getContext());
        stockListAdapter = new StockAdapter(Stock::get);

        stockListAdapter.setOnItemInputListener(new OnItemInputListener.Builder(this::onItemClick,
                null));

        stockList.setLayoutManager(stockListLayoutManager);
        stockList.setAdapter(stockListAdapter);

        if(stockListAdapter.getItemCount() == 0)
        {
            noItemText.setVisibility(View.VISIBLE);
            stockList.setVisibility(View.GONE);
        }
        else
        {
            noItemText.setVisibility(View.GONE);
            stockList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getOptionsMenu()
    {
        return R.menu.options_menu_stock;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem)
    {
        if(menuItem.getItemId() == R.id.option_add_stock)
        {
            if(IngredientRegister.notEnoughIngredients())
            {
                Toast.makeText(requireContext(), R.string.missing_ingredients,
                        Toast.LENGTH_LONG).show();
                return false;
            }
            SelectIngredientBundleDialog dialog = new SelectIngredientBundleDialog(b -> {
                StockItemCountModifyDialog itemCountModifyDialog =
                        new StockItemCountModifyDialog(R.string.enter_value, b);

                itemCountModifyDialog.addOnDismissEvent(dialog1 -> stockListAdapter.update());
                itemCountModifyDialog.show(getChildFragmentManager(), "");
                stockListAdapter.update();
                if(stockListAdapter.getItemCount() == 0)
                {
                    noItemText.setVisibility(View.VISIBLE);
                    stockList.setVisibility(View.GONE);
                }
            });
            dialog.show(getChildFragmentManager(), "");
        }
        else if(menuItem.getItemId() == R.id.option_stock_filter)
        {
            if(IngredientRegister.notEnoughIngredients())
            {
                Toast.makeText(requireContext(), R.string.missing_ingredients,
                        Toast.LENGTH_LONG).show();
                return false;
            }

            filter = stockListAdapter.filterArray();

            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle(R.string.modify_filter_dialog_title);
            builder.setPositiveButton(android.R.string.ok    , this::onFilterSet               );
            builder.setNeutralButton(R.string.clear_selection, null                    );

            List<String> ingredientList = IngredientRegister.toStringList(IngredientRegister.get());
            String[] ingredientArray = new String[ingredientList.size()];
            for(int i = 0; i < ingredientList.size(); i++)
                ingredientArray[i] = ingredientList.get(i);
            builder.setMultiChoiceItems(ingredientArray, filter,
                    this::onIngredientFilterClicked);

            AlertDialog dialog = builder.create();

            // Makes neutral button not dismissing
            dialog.setOnShowListener(dialog1 -> ((AlertDialog) dialog1).getButton(AlertDialog
                    .BUTTON_NEUTRAL).setOnClickListener(v -> onFilterDialogClearClicked(dialog1)));

            dialog.show();

        }
        else if(menuItem.getItemId() == R.id.options_table_stock)
        {
            NavDirections action = StockFragmentDirections
                    .actionStockFragmentToStockTableFragment();
            NavHostFragment.findNavController(this).navigate(action);
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem searchItem = menu.findItem(R.id.option_stock_search);
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

    private boolean onSearchChange(String text)
    {
        stockListAdapter.search(text);
        return true;
    }

    private boolean onSearchClose(@NotNull MenuItem item)
    {
        Logs.debug("Search closed STATISTICS");
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQuery("", false);
        hideSoftwareKeyboard();
        // Refiltering the list is done automatically when we set query to empty
        return true;
    }

    private void onIngredientFilterClicked(DialogInterface dialogInterface,
                                           int which, boolean checked     )
    {
        filter[which] = checked;
    }

    private void onFilterSet(DialogInterface dialog, int which)
    {
        stockListAdapter.filterArray(filter);
    }

    private void onFilterDialogClearClicked(DialogInterface dialog)
    {
        for (int i = 0; i < filter.length; ++i)
        {
            filter[i] = false;
            ((AlertDialog) dialog).getListView().setItemChecked(i, false);
        }
    }

    private void onItemClick(View view, int i)
    {
        Toast.makeText(requireContext(), stockListAdapter.visibleItemList().get(i).bundle().name(), Toast.LENGTH_SHORT).show();
        StockItemCountModifyDialog dialog = new StockItemCountModifyDialog(R.string.enter_value,
                stockListAdapter.visibleItemList().get(i).bundle());
        dialog.addOnDismissEvent(dialog1 -> stockListAdapter.update());
        dialog.show(getChildFragmentManager(), "");
        // stockListAdapter.update();
        if(stockListAdapter.getItemCount() == 0)
        {
            noItemText.setVisibility(View.VISIBLE);
            stockList.setVisibility(View.GONE);
        }
    }

}
