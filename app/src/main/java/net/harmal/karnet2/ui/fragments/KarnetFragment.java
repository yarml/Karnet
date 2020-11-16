package net.harmal.karnet2.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import net.harmal.karnet2.MainActivity;
import net.harmal.karnet2.R;
import net.harmal.karnet2.utils.EventHandler;

public abstract class KarnetFragment extends Fragment
{
    public KarnetFragment(@LayoutRes int layout)
    {
        super(layout);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof MainActivity)
            ((MainActivity) context).onRegisterFragment(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().invalidateOptionsMenu();
    }

    @EventHandler
    public void onMenuOptionsSelected(MenuItem item, NavController navController) {}

    @MenuRes
    public int getOptionsMenu()
    {
        return R.menu.options_menu_default;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();

        ((InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }
}
