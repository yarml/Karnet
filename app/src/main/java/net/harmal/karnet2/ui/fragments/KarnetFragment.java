package net.harmal.karnet2.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import androidx.appcompat.widget.Toolbar;
import androidx.annotation.LayoutRes;
import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.harmal.karnet2.R;
import net.harmal.karnet2.utils.Logs;

public abstract class KarnetFragment extends Fragment
{
    public KarnetFragment(@LayoutRes int layout)
    {
        super(layout);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        Logs.debug("View created");
        requireActivity().invalidateOptionsMenu();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        Logs.debug("Inflating menu");
        inflater.inflate(getOptionsMenu(), menu);
    }

    @MenuRes
    public int getOptionsMenu()
    {
        return R.menu.options_menu_default;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        hideSoftwareKeyboard();
    }

    public void hideSoftwareKeyboard()
    {
        ((InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(requireView().getWindowToken(), 0);
    }
}
