package net.harmal.karnet2.ui.fragments.customer;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;

import net.harmal.karnet2.MainActivity;
import net.harmal.karnet2.R;
import net.harmal.karnet2.core.Customer;
import net.harmal.karnet2.core.Date;
import net.harmal.karnet2.core.registers.CustomerRegister;
import net.harmal.karnet2.ui.AnimationRegister;
import net.harmal.karnet2.ui.fragments.KarnetFragment;
import net.harmal.karnet2.utils.Logs;

import java.time.Duration;

public class CustomerAddModifyFragment extends KarnetFragment
{
    private EditText nameEdit ;
    private EditText phoneEdit;
    private EditText cityEdit ;
    private EditText dateEdit ;

    private int      cid      ;

    public CustomerAddModifyFragment()
    {
        super(R.layout.fragment_add_modify_customer);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        assert getArguments() != null;
        CustomerAddModifyFragmentArgs args = CustomerAddModifyFragmentArgs.fromBundle(getArguments());

        cid = args.getCid();

        nameEdit  = view.findViewById(R.id.edit_text_add_customer_name );
        phoneEdit = view.findViewById(R.id.edit_text_add_customer_phone);
        cityEdit  = view.findViewById(R.id.edit_text_add_customer_city );
        dateEdit  = view.findViewById(R.id.edit_text_add_customer_date );

        if(cid < 0) // Add customer
        {
            nameEdit.setText(args.getDefaultName());
            phoneEdit.setText(args.getDefaultPhoneNum());
            cityEdit.setText(args.getDefaultCity());
            dateEdit.setText("");
        }
        else // Modify existing customer
        {
            Customer c = CustomerRegister.getCustomer(args.getCid());
        }
    }

    @Override
    public void onMenuOptionsSelected(MenuItem item, NavController navController)
    {
        if(item.getItemId() == R.id.options_add_customer_validate)
        {
            String nameStr  = nameEdit.getText().toString() ;
            String phoneStr = phoneEdit.getText().toString();
            String cityStr  = cityEdit.getText().toString() ;
            String dateStr  = dateEdit.getText().toString() ;

            // check name and city
            if(nameStr.length() <= 3)
            {
                nameEdit.startAnimation(AnimationRegister.shakeAnimation());
                Toast.makeText(getContext(), R.string.name_too_short, Toast.LENGTH_SHORT).show();
                return;
            }
            if(cityStr.length() <= 3)
            {
                nameEdit.startAnimation(AnimationRegister.shakeAnimation());
                Toast.makeText(getContext(), R.string.city_too_short, Toast.LENGTH_SHORT).show();
                return;
            }

            // format and check phoneStr num
            phoneStr = phoneStr.replace("+212", "0");
            phoneStr = phoneStr.replace(" ", "");
            phoneStr = phoneStr.replace("(", "");
            phoneStr = phoneStr.replace(")", "");
            phoneStr = phoneStr.replace("/", "");
            phoneStr = phoneStr.replace("N", "");
            phoneStr = phoneStr.replace(",", "");
            phoneStr = phoneStr.replace(";", "");
            phoneStr = phoneStr.replace("*", "");
            phoneStr = phoneStr.replace("#", "");
            phoneStr = phoneStr.replace("+", "");
            phoneStr = phoneStr.replace("-", "");
            phoneStr = phoneStr.replace(".", "");
            phoneStr = phoneStr.trim();

            if(phoneStr.length() != 10 || !phoneStr.startsWith("06")) // Invalid phoneStr num
            {
                phoneEdit.startAnimation(AnimationRegister.shakeAnimation());
                Toast.makeText(getContext(), R.string.toast_invalide_phone_num, Toast.LENGTH_SHORT).show();
                return;
            }

            // format and check date
            Date date;
            try
            {
                date = new Date(dateStr);
            }
            catch(IllegalArgumentException e) // Invalid date
            {
                dateEdit.startAnimation(AnimationRegister.shakeAnimation());
                Toast.makeText(getContext(), R.string.invalid_date, Toast.LENGTH_SHORT).show();
                return;
            }

            // Everything okay
            if(cid < 0)
            {
                CustomerRegister.add(nameStr, cityStr, phoneStr, date);
            }


            navController.navigateUp();
        }
    }
}
