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
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.harmal.karnet2.R;
import net.harmal.karnet2.core.Customer;
import net.harmal.karnet2.core.Date;
import net.harmal.karnet2.core.Item;
import net.harmal.karnet2.core.Order;
import net.harmal.karnet2.core.ProductIngredient;
import net.harmal.karnet2.core.registers.CustomerRegister;
import net.harmal.karnet2.core.registers.IngredientRegister;
import net.harmal.karnet2.core.registers.OrderRegister;
import net.harmal.karnet2.ui.Animations;
import net.harmal.karnet2.ui.adapters.OrderItemAdapter;
import net.harmal.karnet2.ui.dialogs.NumberInputDialog;
import net.harmal.karnet2.ui.dialogs.SelectCustomerDialog;
import net.harmal.karnet2.ui.dialogs.SelectIngredientBundleDialog;
import net.harmal.karnet2.ui.fragments.KarnetFragment;
import net.harmal.karnet2.ui.listeners.OnItemInputListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class OrderAddModifyFragment extends KarnetFragment
{

    private Button                     chooseCustomerBtn    ;
    private EditText                   deliveryPriceEdit    ;
    private EditText                   dateEdit             ;
    private EditText                   reductionEdit        ;
    private ImageButton                editDeliveryPriceBtn ;
    private ImageButton                editDateBtn          ;
    private ImageButton                editReductionBtn     ;
    private FloatingActionButton       addItemBtn           ;
    private TextView                   noItemText           ;

    private RecyclerView               itemList             ;
    private RecyclerView.LayoutManager itemListLayoutManager;
    private OrderItemAdapter           itemListAdapter      ;

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

        chooseCustomerBtn     = v.findViewById(R.id.btn_order_choose_customer               );
        deliveryPriceEdit     = v.findViewById(R.id.edit_text_add_order_delivery_price      );
        dateEdit              = v.findViewById(R.id.edit_text_add_order_date                );
        reductionEdit         = v.findViewById(R.id.edit_text_add_order_reduction           );
        editDeliveryPriceBtn  = v.findViewById(R.id.btn_edit_delivery_price_add_modify_order);
        editDateBtn           = v.findViewById(R.id.btn_edit_date_add_modify_order          );
        editReductionBtn      = v.findViewById(R.id.btn_edit_reduction_add_modify_order     );
        addItemBtn            = v.findViewById(R.id.floating_btn_add_order_stack            );
        noItemText            = v.findViewById(R.id.text_order_no_stack                     );
        itemList              = v.findViewById(R.id.recycler_order_stacks                   );
        itemListLayoutManager = new LinearLayoutManager(requireContext(                   ));

        addItemBtn.setOnClickListener(this::onAddStackButtonClick);

        deliveryPriceEdit.setInputType(InputType.TYPE_NULL);
        dateEdit.setInputType(InputType.TYPE_NULL         );
        reductionEdit.setInputType(InputType.TYPE_NULL    );

        chooseCustomerBtn.setOnClickListener(this::onChooseCustomerBtnClick    );
        editDeliveryPriceBtn.setOnClickListener(this::onDeliveryEditButtonClick);
        editDateBtn.setOnClickListener(this::onDateEditButtonClick             );
        editReductionBtn.setOnClickListener(this::onReductionEditButtonClick   );

        if(oid < 0)
        {
            chooseCustomerBtn.setText(getResources().getString(R.string.choose_customer));
            deliveryPriceEdit.setText("0");
            dateEdit.setText(Date.afterDays(3).toString());
            reductionEdit.setText("0");
            itemListAdapter = new OrderItemAdapter(new ArrayList<>());
            Animations.popIn(noItemText);
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
            reductionEdit.setText(String.format("%d", o.reduction()));
            itemListAdapter = new OrderItemAdapter(o.items());
            if(o.items().size() == 0)
                Animations.popIn(noItemText);
        }
        itemListAdapter.setOnItemInputListener(new OnItemInputListener.Builder(
                this::onStackItemClick, null));
        itemList.setLayoutManager(itemListLayoutManager);
        itemList.setAdapter(itemListAdapter);
    }


    private void onReductionEditButtonClick(View view)
    {
        NumberInputDialog dialog = new NumberInputDialog(R.string.select_delivery_price,
                0, 200,
                this::onReductionPriceSet, requireView().getWindowToken());
        dialog.show(getChildFragmentManager(), "");
    }

    @SuppressLint("DefaultLocale")
    private void onReductionPriceSet(int i)
    {
        reductionEdit.setText(String.format("%d", i));
    }

    private void onStackItemClick(@NotNull View view, int i)
    {
        if(view.getId() == R.id.btn_order_stack_delete)
        {
            itemListAdapter.itemList().remove(i);
            itemListAdapter.notifyItemRemoved(i);
            if(itemListAdapter.getItemCount() == 0)
                Animations.popIn(noItemText);
        }
        else
        {
            NumberInputDialog numDialog = new NumberInputDialog(R.string.select_count,
                    1, 1000, n -> {
                itemListAdapter.itemList().get(i).count(n);
                itemListAdapter.notifyDataSetChanged();
            }, requireView().getWindowToken());
            numDialog.show(getChildFragmentManager(), "");
        }
    }

    private void onAddStackButtonClick(View view)
    {
        if(IngredientRegister.notEnoughIngredients())
        {
            Toast.makeText(requireContext(), R.string.missing_ingredients,
                    Toast.LENGTH_LONG).show();
            return;
        }
        SelectIngredientBundleDialog dialog = new SelectIngredientBundleDialog(b -> {
            NumberInputDialog numDialog = new NumberInputDialog(R.string.select_count,
                    1, 1000, n -> {
                Item item = new Item(b, n);
                if(itemListAdapter.getItemCount() == 0)
                    Animations.popOut(noItemText);
                boolean alreadyExist = false;
                for(Item i : itemListAdapter.itemList())
                    if(i.bundle().equals(b))
                    {
                        alreadyExist = true;
                        i.add(n);
                        break;
                    }
                if(!alreadyExist)
                    itemListAdapter.itemList().add(item);
                itemListAdapter.notifyDataSetChanged();
            }, requireView().getWindowToken());
            numDialog.show(getChildFragmentManager(), "");
        }, requireView().getWindowToken());
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
            int reduction     = Integer.parseInt(reductionEdit.getText().toString(    ));
            Date date = new Date(dateEdit.getText().toString());
            if(oid < 0)
                OrderRegister.add(cid, deliveryPrice, itemListAdapter.itemList(), date, reduction);
            else
            {
                Order o = OrderRegister.getOrder(oid);
                assert o != null;
                o.cid(cid);
                o.deliveryPrice(deliveryPrice);
                o.items(itemListAdapter.itemList());
                o.dueDate(date);
                o.reduction(reduction);
            }
            NavHostFragment.findNavController(this).navigateUp();
        }
        return true;
    }

    private void onDateEditButtonClick(View view)
    {
        Date date = new Date(dateEdit.getText().toString());
        DatePickerDialog dialog = new DatePickerDialog(requireContext(), this::onDateSet,
                date.year(), date.month() - 1, date.day());
        dialog.show();
    }

    @SuppressLint("DefaultLocale")
    private void onDateSet(DatePicker datePicker, int year, int month, int day)
    {
        Date d = new Date(day, month + 1, year);
        dateEdit.setText(d.toString());
    }

    private void onDeliveryEditButtonClick(View view)
    {
        NumberInputDialog dialog = new NumberInputDialog(R.string.select_delivery_price,
                0, 200,
                this::onDeliveryPriceSet, requireView().getWindowToken());
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
        SelectCustomerDialog dialog = new SelectCustomerDialog(R.string.select_customer,
                this::onCustomerSelected, requireView().getWindowToken());
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
