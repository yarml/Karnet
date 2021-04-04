package net.harmal.karnet2.ui.fragments.ingredients;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.ProductIngredient;
import net.harmal.karnet2.core.registers.IngredientRegister;
import net.harmal.karnet2.ui.dialogs.NumberInputDialog;
import net.harmal.karnet2.ui.fragments.KarnetFragment;

public class IngredientAddModifyFragment extends KarnetFragment
{

    private Spinner     typeSpinner;
    private EditText    nameEdit   ;
    private EditText    priceEdit  ;
    private ImageButton priceBtn   ;

    private int piid;

    public IngredientAddModifyFragment()
    {
        super(R.layout.fragment_ingredient_add_modify);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        IngredientAddModifyFragmentArgs args = IngredientAddModifyFragmentArgs
                .fromBundle(getArguments());

        piid = args.getPiid();

        nameEdit    = view.findViewById(R.id.edit_text_ingredient_name );
        priceEdit   = view.findViewById(R.id.edit_text_ingredient_price);
        priceBtn    = view.findViewById(R.id.btn_edit_ingredient_price );
        typeSpinner = view.findViewById(R.id.spinner_ingredient_type   );

        priceEdit.setInputType(InputType.TYPE_NULL);
        priceBtn.setOnClickListener(this::onBtnEditPriceClicked);

        ArrayAdapter<ProductIngredient.Type> typeSpinnerAdapter
                = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item,
                ProductIngredient.Type.values());
        typeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeSpinnerAdapter);

        if(piid < 0)
        {
            nameEdit.setText("");
            priceEdit.setText("0");
            typeSpinner.setSelection(0);
        }
        else
        {
            ProductIngredient p = IngredientRegister.getIngredient(piid);
            nameEdit.setText(p.displayName());
            priceEdit.setText(Integer.toString(p.price()));
            typeSpinner.setSelection(p.type().ordinal());
        }
    }

    private void onBtnEditPriceClicked(View view)
    {
        NumberInputDialog dialog = new NumberInputDialog(R.string.choose_price,
                this::onPriceSet, requireView().getWindowToken());
        dialog.show(getChildFragmentManager(), "");
    }

    @SuppressLint("SetTextI18n")
    private void onPriceSet(int price)
    {
        priceEdit.setText(Integer.toString(price));
    }

    @Override
    public int getOptionsMenu()
    {
        return R.menu.options_menu_add_ingredient;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(item.getItemId() == R.id.options_add_ingredient_validate)
        {
            String name = nameEdit.getText().toString();
            if(name.isEmpty())
            {
                Toast.makeText(getContext(), R.string.name_too_short, Toast.LENGTH_SHORT).show();
                return true;
            }
            int price = Integer.parseInt(priceEdit.getText().toString());
            if(piid < 0)
                IngredientRegister.add(price, name,
                        ProductIngredient.Type.values()[typeSpinner.getSelectedItemPosition()]);
            else
            {
                ProductIngredient p = IngredientRegister.getIngredient(piid);
                p.displayName(name);
                p.price(price);
                p.type(ProductIngredient.Type.values()[typeSpinner.getSelectedItemPosition()]);
            }
            NavHostFragment.findNavController(this).navigateUp();
        }
        return true;
    }
}
