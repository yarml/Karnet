package net.harmal.karnet2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import net.harmal.karnet2.core.Product;

public class HomeFragment extends Fragment
{
    public HomeFragment()
    {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        ((Button) view.findViewById(R.id.button_fragment_home_login)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                NavDirections action = HomeFragmentDirections.actionHomeFragmentToLoginFragment();
                Navigation.findNavController(view).navigate(action);
            }
        });
        Product d;

    }
}
