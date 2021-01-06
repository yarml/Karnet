package net.harmal.karnet2.ui.dialogs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.IBinder;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.IngredientBundle;
import net.harmal.karnet2.core.ProductIngredient;
import net.harmal.karnet2.core.registers.IngredientRegister;
import net.harmal.karnet2.utils.Logs;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SelectIngredientBundleDialog extends KarnetDialogFragment
{
    public interface SelectIngredientBundleDialogInterface
    {
        void onIngredientBundleSelected(IngredientBundle b);
    }

    private Spinner baseSpinner ;
    private Spinner fatSpinner  ;
    private Spinner shapeSpinner;
    private Spinner tasteSpinner;
    private Button  extraBtn    ;

    private final List<Integer> selectedExtras;
    private final SelectIngredientBundleDialogInterface dialogInterface;

    public SelectIngredientBundleDialog(SelectIngredientBundleDialogInterface dialogInterface,
                                        IBinder windowToken)
    {
        super(R.string.select_product, R.layout.dialog_ingredient_bundle_select, windowToken);
        positiveListener(this::onOk);
        this.dialogInterface = dialogInterface;
        selectedExtras = new ArrayList<>();
    }

    private void onOk(DialogInterface dialogInterface, int i)
    {
        int base  = getSelectedIngredient(baseSpinner , ProductIngredient.Type.BASE );
        int fat   = getSelectedIngredient(fatSpinner  , ProductIngredient.Type.FAT  );
        int shape = getSelectedIngredient(shapeSpinner, ProductIngredient.Type.SHAPE);
        int taste = getSelectedIngredient(tasteSpinner, ProductIngredient.Type.TASTE);

        this.dialogInterface.onIngredientBundleSelected(new IngredientBundle(
                base, fat, shape,
                taste, selectedExtras));
    }

    private int getSelectedIngredient(@NotNull Spinner spinner,
                                                    ProductIngredient.Type type)
    {
        List<ProductIngredient> ingredients = IngredientRegister.onlyType(type);
        return ingredients.get(spinner.getSelectedItemPosition()).piid();
    }

    @Override
    protected void onCreatingDialog(@NotNull View v, AlertDialog.Builder builder)
    {

        baseSpinner  = v.findViewById(R.id.spinner_ingredient_bundle_base );
        fatSpinner   = v.findViewById(R.id.spinner_ingredient_bundle_fat  );
        shapeSpinner = v.findViewById(R.id.spinner_ingredient_bundle_shape);
        tasteSpinner = v.findViewById(R.id.spinner_ingredient_bundle_taste);
        extraBtn     = v.findViewById(R.id.btn_ingredient_bundle_extras   );

        extraBtn.setOnClickListener(this::onExtraBtnClicked);
        baseSpinner.setAdapter(getAdapterForType(ProductIngredient.Type.BASE));
        fatSpinner.setAdapter(getAdapterForType(ProductIngredient.Type.FAT));
        shapeSpinner.setAdapter(getAdapterForType(ProductIngredient.Type.SHAPE));
        tasteSpinner.setAdapter(getAdapterForType(ProductIngredient.Type.TASTE));
    }

    @NotNull
    private ArrayAdapter<String> getAdapterForType(ProductIngredient.Type type)
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item,
                IngredientRegister.toStringList(IngredientRegister.onlyType(type)));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private void onExtraBtnClicked(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(R.string.select_extra);
        List<String> extrasString = IngredientRegister.toStringList(IngredientRegister
                .onlyType(ProductIngredient.Type.EXTRA));
        String[] extraStringArray = new String[extrasString.size()];
        for(int i = 0; i < extrasString.size(); i++)
            extraStringArray[i] = extrasString.get(i);
        builder.setMultiChoiceItems(extraStringArray, null,
                this::onExtraSelected);
        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void onExtraSelected(DialogInterface dialog, int which, boolean isChecked)
    {
        List<ProductIngredient> extras = IngredientRegister.onlyType(ProductIngredient.Type.EXTRA);
        int piid = extras.get(which).piid();
        if(isChecked)
        {
            if(!selectedExtras.contains(piid))
                selectedExtras.add(piid);
        }
        else
            if(selectedExtras.contains(piid))
                selectedExtras.remove((Integer) piid);
    }

}
