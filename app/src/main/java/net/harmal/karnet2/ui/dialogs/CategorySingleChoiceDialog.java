package net.harmal.karnet2.ui.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatDialogFragment;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.ProductCategory;

import java.util.ArrayList;
import java.util.List;

public class CategorySingleChoiceDialog extends KarnetDialogFragment
{
    private RadioGroup radioGroup;

    private final List<ProductCategory> categories;

    public CategorySingleChoiceDialog(Context context, @StringRes int title, @NonNull List<ProductCategory> categories)
    {
        super(title, R.layout.dialog_single_category_choice);
        this.categories = new ArrayList<>();
        this.categories.add(new ProductCategory(context.getResources().getString(R.string.new_categ)));
        this.categories.addAll(categories);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    protected void onCreatingDialog(View v, AlertDialog.Builder builder)
    {
        radioGroup = v.findViewById(R.id.radio_group_single_category_choice);

        for(ProductCategory p : categories)
        {
            RadioButton button = new RadioButton(getContext());
            button.setText(p.displayName());
            button.setSelected(false);
            button.setTextAppearance(R.style.TextAppearance_AppCompat_Large);
            radioGroup.addView(button);
        }
    }
}
