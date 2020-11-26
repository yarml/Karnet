package net.harmal.karnet2.ui.dialogs;

import android.app.AlertDialog;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.Customer;
import net.harmal.karnet2.core.registers.CustomerRegister;
import net.harmal.karnet2.ui.adapters.CustomerListAdapter;
import net.harmal.karnet2.ui.listeners.OnItemInputListener;

import org.jetbrains.annotations.NotNull;

public class SelectCustomerDialog extends KarnetDialogFragment
{
    public interface SelectCustomerDialogInterface
    {
        void onCustomerSelected(int cid);
    }

    private EditText                   filterEdit               ;
    private RecyclerView               customerList             ;
    private RecyclerView.LayoutManager customerListLayoutManager;
    private CustomerListAdapter        customerListAdapter      ;

    private final SelectCustomerDialogInterface selectCustomerDialogInterface;

    public SelectCustomerDialog(int title, SelectCustomerDialogInterface selectCustomerDialogInterface, IBinder windowToken)
    {
        super(title, R.layout.dialog_select_customer, windowToken);
        this.selectCustomerDialogInterface = selectCustomerDialogInterface;
    }

    @Override
    protected void onCreatingDialog(@NotNull View v, AlertDialog.Builder builder)
    {
        filterEdit   = v.findViewById(R.id.edit_text_select_customer_filter);
        filterEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                customerListAdapter.filter(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        customerList = v.findViewById(R.id.recycler_select_customer        );

        customerListLayoutManager = new LinearLayoutManager(getContext());
        customerListAdapter = new CustomerListAdapter(CustomerRegister.get());
        customerListAdapter.setOnItemInputListener(new OnItemInputListener.Builder(this::onItemClick, null));
        customerList.setLayoutManager(customerListLayoutManager);
        customerList.setAdapter(customerListAdapter);
    }

    private void onItemClick(View v, int position)
    {
        Customer clicked = customerListAdapter.getVisibleCustomerList().get(position);

        selectCustomerDialogInterface.onCustomerSelected(clicked.cid());
        dismiss();
    }
}
