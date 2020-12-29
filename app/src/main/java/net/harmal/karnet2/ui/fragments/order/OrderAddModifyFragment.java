package net.harmal.karnet2.ui.fragments.order;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.Customer;
import net.harmal.karnet2.core.Date;
import net.harmal.karnet2.core.Order;
import net.harmal.karnet2.core.Stack;
import net.harmal.karnet2.core.registers.CustomerRegister;
import net.harmal.karnet2.core.registers.OrderRegister;
import net.harmal.karnet2.core.registers.ProductRegister;
import net.harmal.karnet2.ui.Animations;
import net.harmal.karnet2.ui.adapters.OrderStacksAdapter;
import net.harmal.karnet2.ui.dialogs.NumberInputDialog;
import net.harmal.karnet2.ui.dialogs.SelectCustomerDialog;
import net.harmal.karnet2.ui.dialogs.SelectProductDialog;
import net.harmal.karnet2.ui.fragments.KarnetFragment;
import net.harmal.karnet2.ui.listeners.OnItemInputListener;
import net.harmal.karnet2.utils.Logs;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class OrderAddModifyFragment extends KarnetFragment
{

    private Button                     chooseCustomerBtn     ;
    private EditText                   deliveryPriceEdit     ;
    private EditText                   dateEdit              ;
    private ImageButton                editDeliveryPriceBtn  ;
    private ImageButton                editDateBtn           ;
    private FloatingActionButton       addStackBtn           ;
    private TextView                   noStackText           ;

    private RecyclerView               stackList             ;
    private RecyclerView.LayoutManager stackListLayoutManager;
    private OrderStacksAdapter         stackListAdapter      ;

    private int oid;
    private int cid = -1;


    public OrderAddModifyFragment()
    {
        super(R.layout.fragment_add_modify_order);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(v, savedInstanceState);

        assert getArguments() != null;
        OrderAddModifyFragmentArgs args = OrderAddModifyFragmentArgs.fromBundle(requireArguments());

        oid = args.getOid();

        chooseCustomerBtn      = v.findViewById(R.id.btn_order_choose_customer               );
        deliveryPriceEdit      = v.findViewById(R.id.edit_text_add_order_delivery_price      );
        dateEdit               = v.findViewById(R.id.edit_text_add_order_date                );
        editDeliveryPriceBtn   = v.findViewById(R.id.btn_edit_delivery_price_add_modify_order);
        editDateBtn            = v.findViewById(R.id.btn_edit_date_add_modify_order          );
        addStackBtn            = v.findViewById(R.id.floating_btn_add_order_stack            );
        noStackText            = v.findViewById(R.id.text_order_no_stack                     );
        stackList              = v.findViewById(R.id.recycler_order_stacks                   );
        stackListLayoutManager = new LinearLayoutManager(requireContext(                    ));

        addStackBtn.setOnClickListener(this::onAddStackButtonClick);

        deliveryPriceEdit.setInputType(InputType.TYPE_NULL);
        dateEdit.setInputType(InputType.TYPE_NULL);

        chooseCustomerBtn.setOnClickListener(this::onChooseCustomerBtnClick);
        editDeliveryPriceBtn.setOnClickListener(this::onDeliveryEditButtonClick);
        editDateBtn.setOnClickListener(this::onDateEditButtonClick);

        if(oid < 0)
        {
            chooseCustomerBtn.setText(getResources().getString(R.string.choose_customer));
            deliveryPriceEdit.setText("0");
            dateEdit.setText(Date.afterDays(3).toString());
            stackListAdapter = new OrderStacksAdapter(new ArrayList<>());
            Animations.popIn(noStackText);
        }
        else
        {
            Order o = OrderRegister.getOrder(oid);
            assert o != null;
            Customer c = CustomerRegister.getCustomer(o.cid());
            assert c != null;
            cid = o.cid();
            chooseCustomerBtn.setText(c.name());
            deliveryPriceEdit.setText(String.format("%d", o.deliveryPrice()));
            dateEdit.setText(o.dueDate().toString());
            stackListAdapter = new OrderStacksAdapter(o.stacks());
            if(o.stacks().size() == 0)
                Animations.popIn(noStackText);
        }
        stackListAdapter.setOnItemInputListener(new OnItemInputListener.Builder(this::onStackItemClick, null));
        stackList.setLayoutManager(stackListLayoutManager);
        stackList.setAdapter(stackListAdapter);
    }

    private void onStackItemClick(@NotNull View view, int i)
    {
        if(view.getId() == R.id.btn_order_stack_delete)
        {
            stackListAdapter.stackList().remove(i);
            stackListAdapter.notifyItemRemoved(i);
            if(stackListAdapter.stackList().size() == 0)
                Animations.popIn(noStackText);
        }
    }

    private void onAddStackButtonClick(View view)
    {
        if(ProductRegister.size() == 0)
        {
            Toast.makeText(requireContext(), R.string.add_products, Toast.LENGTH_LONG).show();
            return;
        }
        SelectProductDialog dialog = new SelectProductDialog(R.string.select_product, pid -> {
            NumberInputDialog inputDialog = new NumberInputDialog(R.string.select_count,
                    1, 4000,
                    input -> {
                        for(int i = 0; i < stackListAdapter.stackList().size(); i++)
                        {
                            Stack s = stackListAdapter.stackList().get(i);
                            if(s.pid() == pid)
                            {
                                s.add(input);
                                stackListAdapter.notifyItemChanged(i);
                                return;
                            }
                        }
                        stackListAdapter.stackList().add(new Stack(pid, input));
                        stackListAdapter.notifyItemInserted(stackListAdapter.getItemCount() - 1);
                        Animations.popOut(noStackText);
                    }, requireView().getWindowToken());
            inputDialog.show(getChildFragmentManager(), "");
        },requireView().getWindowToken());
        dialog.show(getChildFragmentManager(), "");
    }

    @Override
    public int getOptionsMenu()
    {
        return R.menu.options_menu_add_order;
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item)
    {
        if(item.getItemId() == R.id.options_add_order_validate)
        {
            if(cid < 0)
            {
                Animations.shake(chooseCustomerBtn);
                Toast.makeText(getContext(), R.string.invalid_customer, Toast.LENGTH_SHORT).show();
                return true;
            }
            int deliveryPrice = Integer.parseInt(deliveryPriceEdit.getText().toString());
            Date date = new Date(dateEdit.getText().toString());
            if(oid < 0)
                OrderRegister.add(cid, deliveryPrice, stackListAdapter.stackList(), date);
            else
            {
                Order o = OrderRegister.getOrder(oid);
                assert o != null;
                o.cid(cid);
                o.deliveryPrice(deliveryPrice);
                o.stacks(stackListAdapter.stackList());
                o.dueDate(date);
            }
            NavHostFragment.findNavController(this).navigateUp();
        }
        return true;
    }

    private void onDateEditButtonClick(View view)
    {
        Date date = new Date(dateEdit.getText().toString());
        assert getContext() != null;
        DatePickerDialog dialog = new DatePickerDialog(getContext(), this::onDateSet,
                date.year(), date.month() - 1, date.day());
        dialog.show();
    }

    @SuppressLint("DefaultLocale")
    private void onDateSet(DatePicker datePicker, int year, int month, int day)
    {
        dateEdit.setText(String.format("%d/%d/%d", day, month + 1, year));
    }

    private void onDeliveryEditButtonClick(View view)
    {
        assert getView() != null;
        NumberInputDialog dialog = new NumberInputDialog(R.string.select_delivery_price,
                0, 200,
                this::onDeliveryPriceSet, getView().getWindowToken());
        dialog.show(getChildFragmentManager(), "");
    }

    @SuppressLint("DefaultLocale")
    private void onDeliveryPriceSet(int price)
    {
        deliveryPriceEdit.setText(String.format("%d", price));
    }

    private void onChooseCustomerBtnClick(View view)
    {
        if(CustomerRegister.size() == 0)
        {
            Toast.makeText(getContext(), R.string.toast_add_customer, Toast.LENGTH_LONG).show();
            return;
        }
        assert getView() != null;
        SelectCustomerDialog dialog = new SelectCustomerDialog(R.string.select_customer,
                this::onCustomerSelected, getView().getWindowToken());
        dialog.show(getChildFragmentManager(), "");
    }

    private void onCustomerSelected(int cid)
    {
        Customer c = CustomerRegister.getCustomer(cid);
        assert c != null;
        chooseCustomerBtn.setText(c.name());
        this.cid = cid;
    }
}
