package net.harmal.karnet2.ui.fragments.stock;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.ProductIngredient;
import net.harmal.karnet2.core.registers.IngredientRegister;
import net.harmal.karnet2.core.registers.Stock;
import net.harmal.karnet2.ui.Animations;
import net.harmal.karnet2.ui.adapters.StockAdapter;
import net.harmal.karnet2.ui.dialogs.NumberInputDialog;
import net.harmal.karnet2.ui.dialogs.SelectIngredientBundleDialog;
import net.harmal.karnet2.ui.dialogs.StockItemCountModifyDialog;
import net.harmal.karnet2.ui.fragments.KarnetFragment;
import net.harmal.karnet2.ui.listeners.OnItemInputListener;

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        noItemText = view.findViewById(R.id.text_stock_no_product);
        stockList = view.findViewById(R.id.recycler_stock);

        stockListLayoutManager = new LinearLayoutManager(getContext());
        stockListAdapter = new StockAdapter(Stock.get());

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
            if(!IngredientRegister.hasEnoughIngredients())
            {
                Toast.makeText(requireContext(), R.string.missing_ingredients,
                        Toast.LENGTH_LONG).show();
                return false;
            }
            SelectIngredientBundleDialog dialog = new SelectIngredientBundleDialog(b -> {
                NumberInputDialog numDialog = new NumberInputDialog(R.string.select_count,
                        1, 1000, n -> {
                    if(stockListAdapter.getItemCount() == 0)
                    {
                        Animations.popOut(noItemText);
                        Animations.popIn(stockList);
                    }
                    Stock.add(b, n);
                    stockListAdapter.update();
                }, requireView().getWindowToken());
                numDialog.show(getChildFragmentManager(), "");
            }, requireView().getWindowToken());
            dialog.show(getChildFragmentManager(), "");
        }
        else if(menuItem.getItemId() == R.id.option_stock_filter)
        {
            if(!IngredientRegister.hasEnoughIngredients())
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
        StockItemCountModifyDialog dialog = new StockItemCountModifyDialog(R.string.enter_value,
                stockListAdapter.visibleItemList().get(i).bundle(), requireView().getWindowToken());

        dialog.addOnDismissEvent(dialog1 -> stockListAdapter.update());
        dialog.show(getChildFragmentManager(), "");
        stockListAdapter.update();
        if(stockListAdapter.getItemCount() == 0)
        {
            noItemText.setVisibility(View.VISIBLE);
            stockList.setVisibility(View.GONE);
        }
    }

}
