package net.harmal.karnet2.ui.fragments.customer;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.Customer;
import net.harmal.karnet2.core.registers.CustomerRegister;
import net.harmal.karnet2.ui.fragments.KarnetFragment;

public class CustomerDetailsFragment extends KarnetFragment
{

    TextView nameText ;
    TextView phoneText;
    TextView cityText ;
    TextView dateText ;

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

        Customer c = CustomerRegister.getCustomer(args.getCid());

        assert c != null;
        nameText.setText(c.name()                   );
        phoneText.setText(c.phoneNum()              );
        cityText.setText(c.city()                   );
        dateText.setText(c.creationDate().toString());

    }
}
