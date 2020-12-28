package net.harmal.karnet2.ui.dialogs;

import android.app.AlertDialog;
import android.content.res.Resources;
import android.os.IBinder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.ProductCategory;
import net.harmal.karnet2.utils.Maths;

import java.util.List;

public class ExtraCategoryDetailsDialog extends KarnetDialogFragment
{

    private final List<ProductCategory> extras;

    public ExtraCategoryDetailsDialog(@StringRes int title, @NonNull List<ProductCategory> extras,
                                      IBinder windowToken)
    {
        super(title, R.layout.dialog_category_extra_details, windowToken);
        this.extras = extras;
    }

    @Override
    protected void onCreatingDialog(View v, AlertDialog.Builder builder)
    {
        LinearLayout linearLayout = v.findViewById(R.id.linea_layout_category_extra_details);
        for(ProductCategory c : extras)
        {
            TextView textView = new TextView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            Resources r = getResources();
            params.setMargins(Maths.dpToPx(48, r), Maths.dpToPx(12, r),
                    Maths.dpToPx(48, r), 0);
            textView.setText(String.format("◾ %s", c.displayName()));
            textView.setTextAppearance(R.style.TextAppearance_AppCompat_Large);
            textView.setLayoutParams(params);
            linearLayout.addView(textView);
        }
    }
}
