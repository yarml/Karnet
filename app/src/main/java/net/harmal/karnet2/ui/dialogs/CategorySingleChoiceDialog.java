package net.harmal.karnet2.ui.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.ProductCategory;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CategorySingleChoiceDialog extends KarnetDialogFragment
{
    private RadioGroup radioGroup;

    private final RadioGroup.OnCheckedChangeListener checkedChangeListener;

    private final List<ProductCategory> categories;

    public CategorySingleChoiceDialog(Context context, @StringRes int title, @NonNull List<ProductCategory> categories,
                                      RadioGroup.OnCheckedChangeListener checkedChangeListener,
                                      IBinder windowToken)
    {
        this(context, title, categories,
                checkedChangeListener, true,
                windowToken);
    }

    public CategorySingleChoiceDialog(Context context, @StringRes int title, @NonNull List<ProductCategory> categories,
                                      RadioGroup.OnCheckedChangeListener checkedChangeListener,
                                      boolean newOption, IBinder windowToken)
    {
        super(title, R.layout.dialog_single_category_choice, windowToken);
        this.categories = new ArrayList<>();
        if(newOption)
            this.categories.add(new ProductCategory(context.getResources().getString(R.string.new_categ)));
        this.categories.addAll(categories);
        this.checkedChangeListener = checkedChangeListener;
    }

    @Override
    protected void onCreatingDialog(@NotNull View v, AlertDialog.Builder builder)
    {
        radioGroup = v.findViewById(R.id.radio_group_single_category_choice);
        for(int i = 0; i < categories.size(); i++)
        {
            RadioButton button = new RadioButton(getContext());
            button.setText(categories.get(i).displayName());
            button.setSelected(false);
            button.setTextAppearance(R.style.TextAppearance_AppCompat_Large);
            button.setTag(i);
            radioGroup.addView(button, i);
        }
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            CategorySingleChoiceDialog.this.dismiss();
            checkedChangeListener.onCheckedChanged(group, checkedId);
        });
    }
}
