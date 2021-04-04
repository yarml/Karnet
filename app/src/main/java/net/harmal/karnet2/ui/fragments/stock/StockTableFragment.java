package net.harmal.karnet2.ui.fragments.stock;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.IngredientBundle;
import net.harmal.karnet2.core.ProductIngredient;
import net.harmal.karnet2.core.registers.IngredientRegister;
import net.harmal.karnet2.ui.adapters.StockTableAdapter;
import net.harmal.karnet2.ui.dialogs.StockItemCountModifyDialog;
import net.harmal.karnet2.ui.fragments.KarnetFragment;
import net.harmal.karnet2.ui.listeners.OnItemInputListener;

import java.util.List;

public class StockTableFragment extends KarnetFragment
{
    RecyclerView               stockTableRecycler     ;
    RecyclerView.LayoutManager stockTableLayoutManager;
    StockTableAdapter          stockTableAdapter      ;

    Spinner                    baseSpinner            ;
    Spinner                    fatSpinner             ;

    List<ProductIngredient> bases ;
    List<ProductIngredient> fats  ;
    List<ProductIngredient> shapes;
    List<ProductIngredient> tastes;
    List<ProductIngredient> extras;

    int selectedBasePos = 0;
    int selectedFatPos  = 0;

    public StockTableFragment()
    {
        super(R.layout.fragment_stock_table);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        stockTableRecycler = view.findViewById(R.id.recycler_stock_table);

        bases  = IngredientRegister.onlyType(ProductIngredient.Type.BASE );
        fats   = IngredientRegister.onlyType(ProductIngredient.Type.FAT  );
        shapes = IngredientRegister.onlyType(ProductIngredient.Type.SHAPE);
        tastes = IngredientRegister.onlyType(ProductIngredient.Type.TASTE);
        extras = IngredientRegister.onlyType(ProductIngredient.Type.EXTRA);

        baseSpinner = view.findViewById(R.id.spinner_stock_table_ingredient_base);
        fatSpinner  = view.findViewById(R.id.spinner_stock_table_ingredient_fat );
        ArrayAdapter<String> baseAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item,
                IngredientRegister.toStringList(bases));
        baseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> fatAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item,
                IngredientRegister.toStringList(fats));
        fatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        baseSpinner.setAdapter(baseAdapter);
        fatSpinner.setAdapter(fatAdapter);

        int columnsCount = shapes.size() + 1;
        int rowCount     = tastes.size() * (extras.size() + 1) + 1;

        stockTableLayoutManager = new GridLayoutManager(requireContext(), columnsCount);

        stockTableAdapter = new StockTableAdapter(columnsCount, rowCount,
                bases.get(0), fats.get(0), shapes, tastes, extras);

        stockTableAdapter.setOnItemInputListener(new OnItemInputListener.Builder(this::onItemClick, null));

        stockTableRecycler.setLayoutManager(stockTableLayoutManager);
        stockTableRecycler.setAdapter(stockTableAdapter);

        baseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                stockTableAdapter.setBaseIngredient(bases.get(position));
                selectedBasePos = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        fatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                stockTableAdapter.setFatIngredient(fats.get(position));
                selectedFatPos = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void onItemClick(View view, int pos)
    {
        int column = stockTableAdapter.positionColumn(pos);
        int row    = stockTableAdapter.positionRow(pos);
        if(column != 0 && row != 0)
        {
            IngredientBundle bundle = new IngredientBundle(bases.get(selectedBasePos).piid(),
                                                           fats.get(selectedFatPos).piid(),
                                                           stockTableAdapter.shapeAt(column),
                                                           stockTableAdapter.tasteAt(row),
                                                           stockTableAdapter.extrasAt(row));
            Toast.makeText(requireContext(), bundle.name(), Toast.LENGTH_SHORT).show();
            StockItemCountModifyDialog dialog = new StockItemCountModifyDialog(R.string.enter_value,
                    bundle, requireView().getWindowToken());
            dialog.addOnDismissEvent(dialog1 -> stockTableAdapter.update());
            dialog.show(getChildFragmentManager(), "");
        }
    }

}
