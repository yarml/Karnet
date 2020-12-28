package net.harmal.karnet2.ui.fragments.product;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.Product;
import net.harmal.karnet2.core.ProductCategory;
import net.harmal.karnet2.core.registers.ProductRegister;
import net.harmal.karnet2.ui.Animations;
import net.harmal.karnet2.ui.dialogs.CategoryMultiChoiceDialog;
import net.harmal.karnet2.ui.dialogs.CategorySingleChoiceDialog;
import net.harmal.karnet2.ui.dialogs.NumberInputDialog;
import net.harmal.karnet2.ui.dialogs.StringInputDialog;
import net.harmal.karnet2.ui.fragments.KarnetFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ProductAddModifyFragment extends KarnetFragment
{

    private int      pid                       ;
    private EditText nameEdit                  ;
    private EditText unitPriceEdit             ;
    private ImageButton editUnitPriceBtn       ;
    private Button   baseBtn                   ;
    private Button   fatBtn                    ;
    private Button   shapeBtn                  ;
    private Button   typeBtn                   ;
    private Button   extraBtn                  ;

    private ProductCategory       baseCategory ;
    private ProductCategory       fatCategory  ;
    private ProductCategory       shapeCategory;
    private ProductCategory       typeCategory ;
    private List<ProductCategory> extraCategory;

    public ProductAddModifyFragment() { super(R.layout.fragment_add_modify_product); }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        assert getArguments() != null;
        ProductAddModifyFragmentArgs args = ProductAddModifyFragmentArgs.fromBundle(getArguments());

        pid = args.getPid();

        nameEdit          = view.findViewById(R.id.edit_text_add_product_name            );
        unitPriceEdit     = view.findViewById(R.id.edit_text_add_product_unit_price      );
        editUnitPriceBtn  = view.findViewById(R.id.btn_edit_unit_price_add_modify_product);
        baseBtn           = view.findViewById(R.id.btn_select_base                       );
        fatBtn            = view.findViewById(R.id.btn_select_fat                        );
        shapeBtn          = view.findViewById(R.id.btn_select_shape                      );
        typeBtn           = view.findViewById(R.id.btn_select_type                       );
        extraBtn          = view.findViewById(R.id.btn_select_extra                      );

        unitPriceEdit.setInputType(InputType.TYPE_NULL);
        editUnitPriceBtn.setOnClickListener(this::onUnitPriceButtonClick);


        if(pid < 0)
        {
            nameEdit.setText("");
            unitPriceEdit.setText("");
            extraCategory = new ArrayList<>();
        }
        else
        {
            Product p = ProductRegister.getProduct(pid);
            assert p != null;
            nameEdit.setText(p.name());
            unitPriceEdit.setText(String.format("%s", p.unitPrice()));
            baseCategory  = p.baseIngredient().clone();
            fatCategory   = p.fat().clone();
            shapeCategory = p.shape().clone();
            typeCategory  = p.type().clone();
            extraCategory = new ArrayList<>(p.extra());

            baseBtn.setText(baseCategory.displayName());
            fatBtn.setText(fatCategory.displayName());
            shapeBtn.setText(shapeCategory.displayName());
            typeBtn.setText(typeCategory.displayName());
            StringBuilder builder = new StringBuilder();
            for(ProductCategory c : extraCategory)
                builder.append(c.displayName()).append(";");
            builder.setCharAt(builder.length() - 1, ' ');
            if(builder.length() > 12)
            {
                builder.setLength(9);
                builder.append("...");
            }
            extraBtn.setText(builder.toString());
        }

        // Setting buttons behavior
        baseBtn.setOnClickListener (v -> onSelectButtonClick(R.string.select_base_ingredient, ProductRegister.getIngredients(), this::onBaseSet ));
        fatBtn.setOnClickListener  (v -> onSelectButtonClick(R.string.select_fat            , ProductRegister.getFats(       ), this::onFatSet  ));
        shapeBtn.setOnClickListener(v -> onSelectButtonClick(R.string.select_shape          , ProductRegister.getShape(      ), this::onShapeSet));
        typeBtn.setOnClickListener (v -> onSelectButtonClick(R.string.select_type           , ProductRegister.getType(       ), this::onTypeSet ));
        extraBtn.setOnClickListener(this::onExtraSelectButtonClick);
    }

    @Override
    public int getOptionsMenu()
    {
        return R.menu.options_menu_add_product;
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item)
    {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.options_add_product_validate)
        {
            String name = nameEdit.getText().toString();
            String unitPrice = unitPriceEdit.getText().toString();
            if(name.isEmpty())
            {
                Animations.shake(nameEdit);
                Toast.makeText(getContext(), R.string.add_product_invalid_name, Toast.LENGTH_SHORT).show();
                return true;
            }
            if(unitPrice.isEmpty())
            {
                Animations.shake(unitPriceEdit);
                Toast.makeText(getContext(), R.string.add_product_invalid_unit_price, Toast.LENGTH_SHORT).show();
                return true;
            }
            if(baseCategory == null)
            {
                Animations.shake(baseBtn);
                Toast.makeText(getContext(), R.string.add_product_invalid_base, Toast.LENGTH_SHORT).show();
                return true;
            }
            if(fatCategory == null)
            {
                Animations.shake(fatBtn);
                Toast.makeText(getContext(), R.string.add_product_invalid_fat, Toast.LENGTH_SHORT).show();
                return true;
            }
            if(shapeCategory == null)
            {
                Animations.shake(shapeBtn);
                Toast.makeText(getContext(), R.string.add_product_invalid_shape, Toast.LENGTH_SHORT).show();
                return true;
            }
            if(typeCategory == null)
            {
                Animations.shake(typeBtn);
                Toast.makeText(getContext(), R.string.add_product_invalid_type, Toast.LENGTH_SHORT).show();
                return true;
            }
            int unitPriceInt = Integer.parseInt(unitPrice); // Shouldn't throw any error

            if(pid < 0) // Add new product
                ProductRegister.add(unitPriceInt, name, baseCategory,
                        fatCategory, shapeCategory, typeCategory, extraCategory);
            else
            {
                Product p = ProductRegister.getProduct(pid);
                assert p != null;
                p.name(name                     );
                p.unitPrice((short) unitPriceInt);
                p.baseIngredient(baseCategory   );
                p.fat(fatCategory               );
                p.shape(shapeCategory           );
                p.type(typeCategory             );
                p.extra(extraCategory           );
            }

            NavHostFragment.findNavController(this).navigateUp();
        }
        return true;
    }

    private void onSelectButtonClick(@StringRes int title, List<ProductCategory> categories, ProductSetter setter)
    {
        assert getContext() != null;
        CategorySingleChoiceDialog dialog = new CategorySingleChoiceDialog(getContext(),
                title, categories, (group, checkedId) ->
                onCategoryChecked(group, categories, checkedId, setter), requireView().getWindowToken());

        dialog.show(getChildFragmentManager(), "");
    }

    private void onCategoryChecked(@NotNull RadioGroup group, List<ProductCategory> categories, int checkedId, ProductSetter setter)
    {
        int position = (int) group.findViewById(checkedId).getTag();
        if(position == 0)
        {
            assert getView() != null;
            // Create a new product category
            StringInputDialog dialog = new StringInputDialog(R.string.enter_name, true,
                    input -> setter.set(new ProductCategory(input)), getView().getWindowToken());
            dialog.show(getChildFragmentManager(), "");
        }
        else
            setter.set(categories.get(position - 1));
    }

    private void onUnitPriceButtonClick(View v)
    {
        assert getView() != null;
        NumberInputDialog dialog = new NumberInputDialog(R.string.choose_price,
                1, 100, this::onNumberInputReturn, getView().getWindowToken());
        dialog.show(getChildFragmentManager(), "");
    }

    @SuppressLint("DefaultLocale")
    private void onNumberInputReturn(int input)
    {
        unitPriceEdit.setText(String.format("%d", input));
    }

    private void onBaseSet(@NotNull ProductCategory value)
    {
        baseCategory = value;
        baseBtn.setText(value.displayName());
    }

    private void onFatSet(@NotNull ProductCategory value)
    {
        fatCategory = value;
        fatBtn.setText(value.displayName());
    }

    private void onShapeSet(@NotNull ProductCategory value)
    {
        shapeCategory = value;
        shapeBtn.setText(value.displayName());
    }

    private void onTypeSet(@NotNull ProductCategory value)
    {
        typeCategory = value;
        typeBtn.setText(value.displayName());
        baseBtn.setText(baseCategory.displayName());
    }

    private interface ProductSetter
    {
        void set(ProductCategory value);
    }

    private void onExtraSelectButtonClick(View v)
    {
        CategoryMultiChoiceDialog dialog = new CategoryMultiChoiceDialog(R.string.select_extra, ProductRegister.getExtras(),
                this::onExtraItemsSelected, requireView().getWindowToken());
        dialog.show(getChildFragmentManager(), "");
    }

    private void onExtraItemsSelected(List<ProductCategory> selected)
    {
        extraCategory = new ArrayList<>(selected);
        StringBuilder builder = new StringBuilder();
        for(ProductCategory c : extraCategory)
            builder.append(c.displayName()).append(";");
        builder.setCharAt(builder.length() - 1, ' ');
        if(builder.length() > 12)
        {
            builder.setLength(9);
            builder.append("...");
        }
        extraBtn.setText(builder.toString());
    }
}
