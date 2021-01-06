package net.harmal.karnet2.ui.dialogs;

import android.app.AlertDialog;
import android.os.IBinder;
import android.view.View;
import android.widget.NumberPicker;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.IngredientBundle;
import net.harmal.karnet2.core.Item;
import net.harmal.karnet2.core.registers.Stock;

import org.jetbrains.annotations.NotNull;

public class StockItemCountModifyDialog extends KarnetDialogFragment
{
    private final IngredientBundle bundle;

    private NumberPicker         numberPicker;
    private FloatingActionButton setBtn      ;
    private FloatingActionButton addBtn      ;
    private FloatingActionButton removeBtn   ;

    public StockItemCountModifyDialog(int title, IngredientBundle bundle, IBinder windowToken)
    {
        super(title, R.layout.dialog_modify_stock_item_count, windowToken);
        this.bundle = bundle;
    }

    @Override
    protected void onCreatingDialog(@NotNull View v, AlertDialog.Builder builder)
    {
        numberPicker = v.findViewById(R.id.number_picker_stock_number_input);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(200);
        setBtn       = v.findViewById(R.id.floating_btn_set_quantity       );
        addBtn       = v.findViewById(R.id.floating_btn_add_quantity       );
        removeBtn    = v.findViewById(R.id.floating_btn_sub_quantity       );

        setBtn.setOnClickListener(v1 -> {
            int num = numberPicker.getValue();
            Stock.set(bundle, num);
            dismiss();
        });

        addBtn.setOnClickListener(v1 -> {
            int num = numberPicker.getValue();
            Stock.add(bundle, num);
            dismiss();
        });
        removeBtn.setOnClickListener(v1 -> {
            int num = numberPicker.getValue();
            Stock.remove(bundle, num);
            dismiss();
        });
    }
}
