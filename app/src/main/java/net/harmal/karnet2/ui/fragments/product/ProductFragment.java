package net.harmal.karnet2.ui.fragments.product;

import android.view.MenuItem;

import androidx.annotation.MenuRes;
import androidx.navigation.NavController;

import net.harmal.karnet2.R;
import net.harmal.karnet2.ui.fragments.KarnetFragment;

public class ProductFragment extends KarnetFragment
{
    public ProductFragment()
    {
        super(R.layout.fragment_product);
    }

    @Override
    public void onMenuOptionsSelected(MenuItem item, NavController navController)
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

    @Override
    @MenuRes
    public int getOptionsMenu()
    {
        return R.menu.options_menu_product;
    }
}
