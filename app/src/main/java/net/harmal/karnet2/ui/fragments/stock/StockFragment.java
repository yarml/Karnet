package net.harmal.karnet2.ui.fragments.stock;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.registers.ProductRegister;
import net.harmal.karnet2.core.registers.Stock;
import net.harmal.karnet2.ui.adapters.StockAdapter;
import net.harmal.karnet2.ui.dialogs.KarnetDialogFragment;
import net.harmal.karnet2.ui.dialogs.StockItemCountModifyDialog;
import net.harmal.karnet2.ui.fragments.KarnetFragment;
import net.harmal.karnet2.ui.listeners.OnItemInputListener;

import org.jetbrains.annotations.NotNull;

public class StockFragment extends KarnetFragment
{
    private TextView                   noProductText         ;
    private RecyclerView               stockList             ;
    private RecyclerView.LayoutManager stockListLayoutManager;
    private StockAdapter               stockListAdapter      ;

    public StockFragment()
    {
        super(R.layout.fragment_stock);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        noProductText = view.findViewById(R.id.text_stock_no_product);
        stockList = view.findViewById(R.id.recycler_stock);

        stockListLayoutManager = new LinearLayoutManager(getContext());
        stockListAdapter = new StockAdapter(ProductRegister.get());

        stockListAdapter.setOnItemInputListener(new OnItemInputListener.Builder(this::onItemClick, null));

        stockList.setLayoutManager(stockListLayoutManager);
        stockList.setAdapter(stockListAdapter);

        if(ProductRegister.size() == 0)
        {
            noProductText.setVisibility(View.VISIBLE);
            stockList.setVisibility(View.GONE);
        }
        else
        {
            noProductText.setVisibility(View.GONE);
            stockList.setVisibility(View.VISIBLE);
        }
    }

    private void onItemClick(View view, int i)
    {
        StockItemCountModifyDialog dialog = new StockItemCountModifyDialog(R.string.enter_value, ProductRegister.get()
                .get(i).pid(), getView().getWindowToken());

        dialog.addOnDismissEvent(dialog1 -> {
            stockListAdapter.notifyItemChanged(i);
        });
        dialog.show(getChildFragmentManager(), "");
    }

}
