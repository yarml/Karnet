package net.harmal.karnet2.ui.dialogs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.IBinder;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.IngredientBundle;
import net.harmal.karnet2.core.Item;
import net.harmal.karnet2.core.registers.Stock;
import net.harmal.karnet2.ui.Animations;

import org.jetbrains.annotations.NotNull;

public class StockItemCountModifyDialog extends KarnetDialogFragment
{
    private final IngredientBundle bundle;

    private EditText             numberEdit;
    private FloatingActionButton setBtn    ;
    private FloatingActionButton addBtn    ;
    private FloatingActionButton removeBtn ;

    public StockItemCountModifyDialog(int title, IngredientBundle bundle)
    {
        super(title, R.layout.dialog_modify_stock_item_count);
        this.bundle = bundle;
    }

    @Override
    protected void onCreatingDialog(@NotNull View v, AlertDialog.Builder builder)
    {
        numberEdit   = v.findViewById(R.id.edit_stock_number_input         );
        setBtn       = v.findViewById(R.id.floating_btn_set_quantity       );
        addBtn       = v.findViewById(R.id.floating_btn_add_quantity       );
        removeBtn    = v.findViewById(R.id.floating_btn_sub_quantity       );

        setBtn.setOnClickListener(v1 -> {
            try
            {
                int num = Integer.parseInt(numberEdit.getText().toString());
                Stock.set(bundle, num);
                dismiss();
            }catch (Exception e)
            {
                Animations.shake(numberEdit);
                Toast.makeText(requireContext(), R.string.invalid_number, Toast.LENGTH_SHORT).show();
            }
        });

        addBtn.setOnClickListener(v1 -> {
            try
            {
                int num = Integer.parseInt(numberEdit.getText().toString());
                Stock.add(bundle, num);
                dismiss();
            }catch (Exception e)
            {
                Animations.shake(numberEdit);
                Toast.makeText(requireContext(), R.string.invalid_number, Toast.LENGTH_SHORT).show();
            }
        });
        removeBtn.setOnClickListener(v1 -> {
            try
            {
                int num = Integer.parseInt(numberEdit.getText().toString());
                Stock.remove(bundle, num);
                dismiss();
            }catch (Exception e)
            {
                Animations.shake(numberEdit);
                Toast.makeText(requireContext(), R.string.invalid_number, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
