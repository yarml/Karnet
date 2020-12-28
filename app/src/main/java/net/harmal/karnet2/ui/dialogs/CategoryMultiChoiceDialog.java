package net.harmal.karnet2.ui.dialogs;

import android.app.AlertDialog;
import android.os.IBinder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.StringRes;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.ProductCategory;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CategoryMultiChoiceDialog extends KarnetDialogFragment
{
    public interface CategoryMultiChoiceDialogInterface
    {
        void onItemsSelected(List<ProductCategory> selected);
    }

    private EditText              newCategoriesEdit;
    private List<CheckBox>        checkboxes       ;
    private final List<ProductCategory> categoryList     ;
    private final boolean showNew;

    public CategoryMultiChoiceDialog(@StringRes int title, @NotNull List<ProductCategory> categories,
                                     CategoryMultiChoiceDialogInterface categoryMultiChoiceDialogInterface,
                                     IBinder windowToken)
    {
        this(title, categories, categoryMultiChoiceDialogInterface, true, windowToken);
    }

    public CategoryMultiChoiceDialog(@StringRes int title, @NotNull List<ProductCategory> categories,
                                     CategoryMultiChoiceDialogInterface categoryMultiChoiceDialogInterface,
                                     boolean showNew, IBinder windowToken)
    {
        super(title, R.layout.dialog_multi_category_choice, windowToken);
        this.showNew = showNew;
        this.categoryList = categories;
        positiveListener((dialog, which) -> {
            List<ProductCategory> checkedCategories = new ArrayList<>();
            for(int i = 0; i < checkboxes.size(); i++)
                if(checkboxes.get(i).isChecked())
                    checkedCategories.add(categoryList.get(i));
            if(showNew)
            {
                String newCategories = newCategoriesEdit.getText().toString();
                for(String s : newCategories.split(","))
                {
                    ProductCategory category = new ProductCategory(s.trim());
                    if(!checkedCategories.contains(category))
                        checkedCategories.add(category);
                }
            }
            categoryMultiChoiceDialogInterface.onItemsSelected(checkedCategories);
        });
    }

    @Override
    protected void onCreatingDialog(@NotNull View v, AlertDialog.Builder builder)
    {
        newCategoriesEdit = v.findViewById(R.id.edit_text_new_categories);
        if(!showNew)
            newCategoriesEdit.setVisibility(View.GONE);
        checkboxes = new ArrayList<>();
        LinearLayout linearLayout = v.findViewById(R.id.linear_layout_multi_categories);
        for(ProductCategory c : categoryList)
        {
            CheckBox b = new CheckBox(getContext());
            b.setChecked(false);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.topMargin = 12;
            b.setLayoutParams(layoutParams);
            b.setTextAppearance(R.style.TextAppearance_AppCompat_Large);
            b.setText(c.displayName());
            checkboxes.add(b);
            linearLayout.addView(b);
        }
    }
}
