package net.harmal.karnet2.ui.fragments.customer;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.Customer;
import net.harmal.karnet2.core.Date;
import net.harmal.karnet2.core.registers.CustomerRegister;
import net.harmal.karnet2.ui.Animations;
import net.harmal.karnet2.ui.fragments.KarnetFragment;
import net.harmal.karnet2.utils.Logs;

import org.jetbrains.annotations.NotNull;

public class CustomerAddModifyFragment extends KarnetFragment
{
    private LinearLayout         layout     ;
    private EditText             nameEdit   ;
    private EditText             phoneEdit  ;
    private AutoCompleteTextView cityEdit   ;
    private EditText             dateEdit   ;
    private ImageButton          editDateBtn;

    private int                  cid        ;

    public CustomerAddModifyFragment()
    {
        super(R.layout.fragment_add_modify_customer);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        CustomerAddModifyFragmentArgs args = CustomerAddModifyFragmentArgs.fromBundle(requireArguments());

        cid = args.getCid();

        layout      = view.findViewById(R.id.fragment_add_modify_customer_layout  );
        nameEdit    = view.findViewById(R.id.edit_text_add_customer_name          );
        phoneEdit   = view.findViewById(R.id.edit_text_add_customer_phone         );
        cityEdit    = view.findViewById(R.id.edit_text_add_customer_city          );
        dateEdit    = view.findViewById(R.id.edit_text_add_customer_date          );
        editDateBtn = view.findViewById(R.id.btn_edit_date_add_modify_customer    );

        // Setting city suggestions
        assert getContext() != null;
        ArrayAdapter<String> citySuggestions = new ArrayAdapter<String>(getContext(),
                R.layout.support_simple_spinner_dropdown_item
                , getResources().getStringArray(R.array.suggestions_cities));
        cityEdit.setAdapter(citySuggestions);
        cityEdit.setOnFocusChangeListener(this::onCityEditFocusChanged);

        // Setting date button behavior
        dateEdit.setInputType(InputType.TYPE_NULL);
        editDateBtn.setOnClickListener(this::onDateEditButtonClicked);

        if(cid < 0) // Add customer
        {
            nameEdit.setText(args.getDefaultName());
            phoneEdit.setText(args.getDefaultPhoneNum());
            cityEdit.setText(args.getDefaultCity());
            dateEdit.setText(Date.today().toString());
        }
        else // Modify existing customer
        {
            Customer c = CustomerRegister.getCustomer(args.getCid());
            assert c != null;
            nameEdit.setText (c.name        (           ));
            phoneEdit.setText(c.phoneNum    (           ));
            cityEdit.setText (c.city        (           ));
            dateEdit.setText (c.creationDate().toString());
        }
    }

    @Override
    public void onMenuOptionsSelected(@NotNull MenuItem item, NavController navController)
    {
        if(item.getItemId() == R.id.options_add_customer_validate)
        {
            String nameStr  = nameEdit.getText().toString() ;
            String phoneStr = phoneEdit.getText().toString();
            String cityStr  = cityEdit.getText().toString() ;
            String dateStr  = dateEdit.getText().toString() ;

            // check name and city
            if(nameStr.length() == 0)
            {
                Animations.shake(nameEdit);
                Toast.makeText(getContext(), R.string.name_too_short, Toast.LENGTH_SHORT).show();
                return;
            }
            if(cityStr.length() == 0)
            {
                Animations.shake(cityEdit);
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

            if(phoneStr.length() != 10 || !(phoneStr.startsWith("06") || phoneStr.startsWith("07"))) // Invalid phoneStr num
            {
                Animations.shake(phoneEdit);
                Toast.makeText(getContext(), R.string.toast_invalid_phone_num, Toast.LENGTH_SHORT).show();
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
                Animations.shake(dateEdit);
                Toast.makeText(getContext(), R.string.invalid_date, Toast.LENGTH_SHORT).show();
                return;
            }

            // Everything okay
            if(cid < 0)
                CustomerRegister.add(nameStr, cityStr, phoneStr, date);
            else
            {
                Customer c = CustomerRegister.getCustomer(cid);
                c.name(nameStr     );
                c.phoneNum(phoneStr);
                c.city(cityStr     );
                c.creationDate(date);
            }

            navController.navigateUp();
        }
    }

    /**
     * Shows the city suggestion menu
     * when focused
     */
    private void onCityEditFocusChanged(View v, boolean hasFocus)
    {
        if (hasFocus)
        {
            Logs.debug("Showing city suggestions");
            cityEdit.showDropDown();
        }
    }

    /**
     * Shows DatePickerDialog
     */
    private void onDateEditButtonClicked(View v)
    {
        Date defaultDate = new Date(dateEdit.getText().toString());
        DatePickerDialog dialog = new DatePickerDialog(getContext(), 0, this::onDatePickerDateSet,
                defaultDate.year(), defaultDate.month() - 1, defaultDate.day());
        dialog.show();
    }

    /**
     * Handles data returned from DatePickerDialog
     */
    @SuppressLint("DefaultLocale")
    private void onDatePickerDateSet(DatePicker view, int year, int month, int dayOfMonth)
    {
        dateEdit.setText(String.format("%d/%d/%d", dayOfMonth, month + 1, year));
    }

    @Override
    @MenuRes
    public int getOptionsMenu()
    {
        return R.menu.options_menu_add_customer;
    }
}
