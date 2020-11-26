package net.harmal.karnet2.ui.dialogs;

import android.app.AlertDialog;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.Product;
import net.harmal.karnet2.core.ProductCategory;
import net.harmal.karnet2.core.registers.ProductRegister;
import net.harmal.karnet2.ui.adapters.ProductListAdapter;
import net.harmal.karnet2.ui.listeners.OnItemInputListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SelectProductDialog extends KarnetDialogFragment
{
    public interface SelectProductDialogInterface
    {
        void onProductSelected(int pid);
    }

    private final SelectProductDialogInterface selectProductDialogInterface;

    private Button baseBtn ;
    private Button fatBtn  ;
    private Button shapeBtn;
    private Button typeBtn ;
    private Button extraBtn;

    private RecyclerView               productList             ;
    private RecyclerView.LayoutManager productListLayoutManager;
    private ProductListAdapter         productListAdapter      ;

    public SelectProductDialog(int title, SelectProductDialogInterface selectProductDialogInterface, IBinder windowToken)
    {
        super(title, R.layout.dialog_select_product, windowToken);
        this.selectProductDialogInterface = selectProductDialogInterface;
    }

    @Override
    protected void onCreatingDialog(View v, AlertDialog.Builder builder)
    {
        baseBtn  = v.findViewById(R.id.btn_filter_base );
        fatBtn   = v.findViewById(R.id.btn_filter_fat  );
        shapeBtn = v.findViewById(R.id.btn_filter_shape);
        typeBtn  = v.findViewById(R.id.btn_filter_type );
        extraBtn = v.findViewById(R.id.btn_filter_extra);

        productList = v.findViewById(R.id.recycler_select_product);
        productListLayoutManager = new LinearLayoutManager(getContext());
        productListAdapter = new ProductListAdapter(ProductRegister.get());
        productListAdapter.setOnItemInputListener(new OnItemInputListener.Builder(this::onItemClick, null));
        productList.setLayoutManager(productListLayoutManager);
        productList.setAdapter(productListAdapter);

        baseBtn.setOnClickListener(v1 -> onCategoryButtonClick(R.string.select_base_ingredient,
                ProductRegister.getIngredients(), category -> productListAdapter.filterBase(category)));
        fatBtn.setOnClickListener(v1 -> onCategoryButtonClick(R.string.select_fat,
                ProductRegister.getFats(), category -> productListAdapter.filterFat(category)));
        shapeBtn.setOnClickListener(v1 -> onCategoryButtonClick(R.string.select_shape,
                ProductRegister.getShape(), category -> productListAdapter.filterShape(category)));
        typeBtn.setOnClickListener(v1 -> onCategoryButtonClick(R.string.select_type,
                ProductRegister.getType(), category -> productListAdapter.filterType(category)));
        extraBtn.setOnClickListener(this::onExtraButtonClick);
    }

    private void onItemClick(View view, int pos)
    {
        Product p = productListAdapter.getVisibleProducts().get(pos);
        selectProductDialogInterface.onProductSelected(p.pid());
        dismiss();
    }

    public void onCategoryButtonClick(@StringRes int title, @NonNull List<ProductCategory> categories,
                                      OnFilterSet onFilterSet)
    {
        CategorySingleChoiceDialog dialog = new CategorySingleChoiceDialog(requireContext(), title, categories,
                (group, checkedId) -> onCategoryChecked(group, categories, checkedId, onFilterSet),
                false, requireView().getWindowToken());
        dialog.show(getChildFragmentManager(), "");
    }

    private void onCategoryChecked(@NotNull RadioGroup group, @NotNull List<ProductCategory> categories,
                                   int checkedId, @NotNull OnFilterSet onFilterSet)
    {
        int position = (int) group.findViewById(checkedId).getTag();
        onFilterSet.onFilterSet(categories.get(position));
    }

    private void onExtraButtonClick(View v)
    {
        CategoryMultiChoiceDialog dialog = new CategoryMultiChoiceDialog(R.string.select_extra,
                ProductRegister.getExtras(),
                selected -> productListAdapter.filterExtras(selected),
                false, requireView().getWindowToken());
        dialog.show(getChildFragmentManager(), "");
    }

    private interface OnFilterSet
    {
        void onFilterSet(ProductCategory category);
    }
}
