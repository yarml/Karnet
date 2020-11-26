package net.harmal.karnet2.ui.fragments.product;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.Product;
import net.harmal.karnet2.core.Trash;
import net.harmal.karnet2.core.registers.ProductRegister;
import net.harmal.karnet2.ui.Animations;
import net.harmal.karnet2.ui.adapters.ProductListAdapter;
import net.harmal.karnet2.ui.fragments.KarnetFragment;
import net.harmal.karnet2.ui.listeners.OnItemInputListener;

import org.jetbrains.annotations.NotNull;

public class ProductFragment extends KarnetFragment
{

    private TextView                   noProductView           ;
    private RecyclerView               productList             ;
    private RecyclerView.LayoutManager productListLayoutManager;
    private ProductListAdapter         productListAdapter      ;

    public ProductFragment()
    {
        super(R.layout.fragment_product);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        noProductView = view.findViewById(R.id.text_no_product);


        productList = view.findViewById(R.id.recycler_product_list);
        productListLayoutManager = new LinearLayoutManager(getContext());
        productListAdapter = new ProductListAdapter(ProductRegister.get());
        productListAdapter.setOnItemInputListener(new OnItemInputListener
                .Builder(this::onItemClick, this::onItemLongClick));

        productList.setLayoutManager(productListLayoutManager);
        productList.setAdapter(productListAdapter            );

        if(ProductRegister.size() == 0)
        {
            noProductView.setVisibility(View.VISIBLE);
            productList.setVisibility(View.GONE);
        }
        else
        {
            noProductView.setVisibility(View.GONE);
            productList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onMenuOptionsSelected(@NotNull MenuItem item, NavController navController)
    {
        if(item.getItemId() == R.id.options_add_product)
        {
            ProductFragmentDirections.ActionProductFragmentToProductAddModifyFragment action =
                    ProductFragmentDirections.actionProductFragmentToProductAddModifyFragment(
                            -1, getString(R.string.add_product)
                    );
            navController.navigate(action);
        }
    }

    private void onItemClick(@NotNull View view, int position)
    {
        if(view.getId() == R.id.btn_product_delete)
        {
            Product p = productListAdapter.getVisibleProducts().get(position);
            ProductRegister.remove(p.pid());
            productListAdapter.update();
            assert getView() != null;
            Snackbar undo = Snackbar.make(getView(), R.string.product_removed, Snackbar.LENGTH_LONG);
            undo.setAction(R.string.undo, this::onUndoProductDeletion);
            undo.show();
        }
        else
        {
            View v = productListLayoutManager.findViewByPosition(position);
            assert v != null;
            ImageButton deleteButton = v.findViewById(R.id.btn_product_delete);
            if(deleteButton.getVisibility() == View.VISIBLE)
                Animations.popOut(deleteButton);
            else
            {
                Product p = ProductRegister.get().get(position);
                NavDirections action = ProductFragmentDirections
                        .actionProductFragmentToProductDetailsFragment(p.pid(), p.name());
                NavHostFragment.findNavController(this).navigate(action);
            }
        }
    }

    private void onItemLongClick(View view, int position)
    {
        View v = productListLayoutManager.findViewByPosition(position);
        assert v != null;
        ImageButton deleteButton = v.findViewById(R.id.btn_product_delete);
        if(deleteButton.getVisibility() == View.GONE) // should appear
            Animations.popIn(deleteButton);
        else
            Animations.popOut(deleteButton);
    }

    private void onUndoProductDeletion(View v)
    {
        int pid = ProductRegister.add(Trash.popProduct());
        Product p = ProductRegister.getProduct(pid);
        productListAdapter.update();
        if(productListAdapter.getVisibleProducts().contains(p))
            productListAdapter.notifyItemInserted(productListAdapter.getVisibleProducts().size() - 1);
    }
    @Override
    @MenuRes
    public int getOptionsMenu()
    {
        return R.menu.options_menu_product;
    }
}
