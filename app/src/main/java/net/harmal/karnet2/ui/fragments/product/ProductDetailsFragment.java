package net.harmal.karnet2.ui.fragments.product;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.Product;
import net.harmal.karnet2.core.ProductCategory;
import net.harmal.karnet2.core.registers.ProductRegister;
import net.harmal.karnet2.ui.dialogs.ExtraCategoryDetailsDialog;
import net.harmal.karnet2.ui.fragments.KarnetFragment;

import org.jetbrains.annotations.NotNull;

public class ProductDetailsFragment extends KarnetFragment
{

    private TextView nameText     ;
    private TextView unitPriceText;
    private Button   baseBtn      ;
    private Button   fatBtn       ;
    private Button   shapeBtn     ;
    private Button   typeBtn      ;
    private Button   extraBtn     ;

    private int pid;

    public ProductDetailsFragment()
    {
        super(R.layout.fragment_product_details);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        assert getArguments() != null;
        ProductDetailsFragmentArgs args = ProductDetailsFragmentArgs.fromBundle(getArguments());
        pid = args.getPid();
        Product p = ProductRegister.getProduct(pid);

        nameText      = view.findViewById(R.id.text_details_product_name      );
        unitPriceText = view.findViewById(R.id.text_details_product_unit_price);
        baseBtn       = view.findViewById(R.id.btn_details_base               );
        fatBtn        = view.findViewById(R.id.btn_details_fat                );
        shapeBtn      = view.findViewById(R.id.btn_details_shape              );
        typeBtn       = view.findViewById(R.id.btn_details_type               );
        extraBtn      = view.findViewById(R.id.btn_details_extra              );

        assert p != null;
        nameText.setText(p.name(                               ));
        unitPriceText.setText(String.format("%s", p.unitPrice()));
        baseBtn.setText(p.baseIngredient().toString(           ));
        fatBtn.setText(p.fat().toString(                       ));
        shapeBtn.setText(p.shape().toString(                   ));
        typeBtn.setText(p.type().toString(                     ));
        // extraBtn.setText(...                                 );
        StringBuilder builder = new StringBuilder();
        for(ProductCategory c : p.extra())
            builder.append(c.displayName()).append(";");
        builder.setCharAt(builder.length() - 1, ' ');
        if(builder.length() > 12)
        {
            builder.setLength(9);
            builder.append("...");
        }
        extraBtn.setText(builder.toString());
        // extraBtn.setText(...                                 );

        extraBtn.setOnClickListener(this::onExtraButtonClick);
    }

    @Override
    public int getOptionsMenu()
    {
        return R.menu.options_menu_product_details;
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item)
    {
        if(item.getItemId() == R.id.options_edit_product)
        {
            Product p = ProductRegister.getProduct(pid);
            assert p != null;

            NavDirections action = ProductDetailsFragmentDirections
                    .actionProductDetailsFragmentToProductAddModifyFragment(pid, p.name());
            NavHostFragment.findNavController(this).navigate(action);
        }
        return true;
    }

    private void onExtraButtonClick(View v)
    {
        ExtraCategoryDetailsDialog dialog = new ExtraCategoryDetailsDialog(R.string.all_extras,
                ProductRegister.getProduct(pid).extra(), requireView().getWindowToken());
        dialog.show(getChildFragmentManager(), "");
    }
}
