package net.harmal.karnet2.ui.fragments.customer;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.Customer;
import net.harmal.karnet2.core.Date;
import net.harmal.karnet2.core.registers.CustomerRegister;
import net.harmal.karnet2.ui.fragments.KarnetFragment;

public class CustomerDetailsFragment extends KarnetFragment
{

    private TextView nameText ;
    private TextView phoneText;
    private TextView cityText ;
    private TextView dateText ;

    private int      cid      ;

    public CustomerDetailsFragment() { super(R.layout.fragment_details_customer); }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        assert getArguments() != null;
        CustomerDetailsFragmentArgs args = CustomerDetailsFragmentArgs.fromBundle(getArguments());

        nameText  = view.findViewById(R.id.text_details_customer_name         );
        phoneText = view.findViewById(R.id.text_details_customer_phone        );
        cityText  = view.findViewById(R.id.text_details_customer_city         );
        dateText  = view.findViewById(R.id.text_details_customer_creation_date);

        cid = args.getCid();

        Customer c = CustomerRegister.getCustomer(cid);


        assert c != null;
        nameText.setText(c.name()                   );
        phoneText.setText(c.phoneNum()              );
        cityText.setText(c.city()                   );
        dateText.setText(c.creationDate().toString());

    }

    @Override
    public void onMenuOptionsSelected(MenuItem item, NavController navController) {
        if(item.getItemId() == R.id.options_edit_customer)
        {
            Customer c = CustomerRegister.getCustomer(cid);
            Date date  = c.creationDate();

            CustomerDetailsFragmentDirections.ActionCustomerDetailsFragmentToCustomerAddModifyFragment action =
                    CustomerDetailsFragmentDirections.actionCustomerDetailsFragmentToCustomerAddModifyFragment(cid,
                            "Modifier " + c.name(), c.name(), c.phoneNum(), c.city(),
                            date.day(), date.month(), date.year());

            navController.navigate(action);
        }
    }

    @Override
    @MenuRes
    public int getOptionsMenu()
    {
        return R.menu.options_menu_details_customer;
    }
}