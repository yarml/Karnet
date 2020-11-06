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
            ((MainActivity) context).registerFragment(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        assert getActivity() != null;
        getActivity().invalidateOptionsMenu();
    }

    public void onMenuOptionsSelected(MenuItem item, NavController navController) {}

    public InputMethodManager getInputMethodManager()
    {
        assert getActivity() != null;
        return (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @MenuRes
    public int getOptionsMenu()
    {
        return R.menu.default_options_menu;
    }
}
